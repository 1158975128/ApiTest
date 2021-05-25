package com.zw.admin.server.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mchange.rmi.NotAuthorizedException;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.annotation.PermissionAccess;
import com.zw.admin.server.dao.*;
import com.zw.admin.server.dto.*;
import com.zw.admin.server.model.*;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.Constant;
import com.zw.admin.server.utils.DateUtil;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户训练日志同步
 * @author larry
 * @Date 2020/10/22 19:04
 */
@Slf4j
@RestController
@Api(tags = "用户训练日志同步")
@RequestMapping("/userTrainLogs")
public class UserTrainLogController {

    @Resource
    private UserTrainLogDao userTrainLogDao;
    @Resource
    private MachineInfoDao machineInfoDao;
    @Resource
    private DataControlCabinController dataControlCabinController;
    @Resource
    private UserDao userDao;
    @Resource
    private GameDao gameDao;
    @Resource
    private UserTrainReportDao userTrainReportDao;
    @Resource
    private UserTrainEvaluationDao userTrainEvaluationDao;
    @Resource
    private UserService userService;

    /**
     * 功能描述:用户训练数据添加
     *
     * @param userTrainLog
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/22 20:13
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @LogAnnotation(module = "用户训练日志")
    public ResultDTO<?> save(UserTrainLog userTrainLog) {
        String trainLogId = userTrainLog.getTrainLogId();
        String userId = userTrainLog.getUserId();
        String machineId = userTrainLog.getMachineId();
        if(StringUtils.isBlank(trainLogId)||StringUtils.isBlank(userId)||StringUtils.isBlank(machineId)){
            return R.fail(1,"必填字段为空");
        }
        String machineType = machineInfoDao.getMachineTypeBySn(machineId);
        userTrainLog.setMachineType(machineType);
        //判断是否为json
        if(!JSONUtil.isJson(userTrainLog.getGameData())){
            userTrainLog.setGameData(null);
        }
        if(!JSONUtil.isJson(userTrainLog.getMachineData())){
            userTrainLog.setMachineData(null);
        }
        //根据用户id查询用户名
        userTrainLog.setUserName(userDao.selectUserNameById(userTrainLog.getUserId()));
        userTrainLogDao.save(userTrainLog);
        return R.ok();
    }

    /**
     * 功能描述:分页查询
     *
     * @param pageRequest
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/12 15:04
     */
    @PostMapping("/selectTrainsLogPage")
    @ApiOperation(value = "分页查询")
    //@PermissionAccess
    public ResultVO<?> selectTrainsLogPage(@RequestBody PageRequest<UserTrainLogBo> pageRequest,@RequestHeader(value = "logintoken",required = false) String token,@RequestHeader(value = "source",required = false) String source) {
        Integer num = pageRequest.getNum();
        Integer page = pageRequest.getPage();
        if(num == null || page==null){
            return R.error(1,"分页数据为空");
        }
        UserTrainLogBo userTrainLogBo = pageRequest.getParams();
        if(userTrainLogBo==null){
            userTrainLogBo = new UserTrainLogBo();
        }

        List<Long> roleIds = UserUtil.getRoleIds();
        //是否根据token初始化角色，默认为否
        Boolean tokenFlag = Boolean.FALSE;
        if (StringUtils.isNotEmpty(source)) {
            //根据token方式获取当前登录用户信息
            User userByToken = userService.getUserByToken(token);
            if (userByToken != null) {
                tokenFlag = Boolean.TRUE;
                roleIds = userByToken.getRoleIds();
            }
        }
        if(CollectionUtils.isEmpty(roleIds)){
            throw new UnknownAccountException();
//            PageInfo<UserTrainLogBo> userTrainLogPage = new PageInfo<>(null);
//            return R.success(userTrainLogPage);
        }
        //如果登录用户的角色不合理或者没有权限,不是病人，不是治疗师，不是医院管理员，不是管理员
        if (!roleIds.contains(Constant.ROOT) && !roleIds.contains(Constant.ORGANIZATION_ADMIN) && !roleIds.contains(Constant.PATIENT) && !roleIds.contains(Constant.THERAPIST)) {
            PageInfo<UserTrainLogBo> userTrainLogPage = new PageInfo<>(null);
            return R.success(userTrainLogPage);
        }
        //根据不同角色初始化sn,用户id
        //如果角色根据token查询,start
        if (tokenFlag) {
            this.initUserTrainLogBoByToken(userTrainLogBo);
        } else {
            this.initUserTrainLogBo(userTrainLogBo);
        }
        //如果角色根据token查询,end
        //this.initUserTrainLogBo(userTrainLogBo);
        PageHelper.startPage(page, num);
        List<UserTrainLogBo> userTrainLogBos = userTrainLogDao.selectTrainsLogPage(userTrainLogBo);
        //权限，1角色判断
        PageInfo<UserTrainLogBo> userTrainLogPage = new PageInfo<>(userTrainLogBos);
        return R.success(userTrainLogPage);
    }


    /**
     * 功能描述:根据token组装角色
     *
     * @param roleIds
     * @param tokenFlag
     * @param token
     * @return void
     * @author larry
     * @Date 2021/5/22 19:55
     */
    public void assembleRoleIdByToke(List<Long> roleIds, Boolean tokenFlag, String token) {
        if (CollectionUtils.isEmpty(roleIds)) {
            //根据token方式获取当前登录用户信息
            User userByToken = userService.getUserByToken(token);
            if (userByToken != null) {
                tokenFlag = Boolean.TRUE;
                roleIds = userByToken.getRoleIds();
            }
        }
    }

    /**
     * 功能描述:根据不同角色初始化sn,用户id
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2021/5/22 15:31
     */
    public UserTrainLogBo initUserTrainLogBoByToken(UserTrainLogBo userTrainLogBo) {
        if(userTrainLogBo==null){
            userTrainLogBo = new UserTrainLogBo();
        }
        List<Long> roleIds = UserUtil.getRoleIds();
        userTrainLogBo.setMachineIds(null);
        return userTrainLogBo;
    }

    /**
     * 功能描述:查询训练数据详细信息
     *
     * @param id
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/13 15:05
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "查询训练数据详细信息")
    public ResultVO<?> getById(@PathVariable("id") long id,Integer limit) {
        if(limit==null || limit<0){
            return R.error(1,"参数错误");
        }
        Map<String,Object> result = new HashMap<String,Object>();
        UserTrainLog userTrainLog = userTrainLogDao.getById(id);
        result.put("trainsLog",userTrainLog);
        UserTrainReport userTrainReport = null;
        //机型
        String machineType = userTrainLog.getMachineType();
        //用户
        String userId = userTrainLog.getUserId();
        //查询患者信息
        User byId = userDao.getById(userId);
        //查询游戏信息
        result.put("user",byId);
        List<JSONObject> historyReportData = new ArrayList<>();
        if(StringUtils.isNotBlank(userTrainLog.getTrainLogId())){
            userTrainReport = userTrainReportDao.selectByTrainsLogId(userTrainLog.getTrainLogId());
            if (userTrainReport != null) {
                UserTrainReport report = new UserTrainReport();
                report.setUserId(userId);
                report.setMachineType(userTrainLog.getMachineType());
                report.setLimit(limit);
                report.setUpdateTime(userTrainReport.getUpdateTime());
                //创建接收查询出的结果集合
                List<String> historyReport = new ArrayList<>();
                //user
                //机型
                if ("M2".equals(machineType) || "M2P".equals(machineType) || "M1W".equals(machineType) || "EMU".equals(machineType) || "M1A".equals(machineType) || "M1S".equals(machineType)) {
                    //根据机型，模式，用户查询历史数据
                    Integer type = userTrainReport.getType();
                    Integer mode = userTrainReport.getMode();
                    Integer category = userTrainReport.getCategory();
                    report.setType(type);
                    report.setMode(mode);
                    report.setCategory(category);
                    historyReport = userTrainReportDao.selectReportByMode(report);
                } else if ("PH".equals(machineType)) {
                    //根据游戏id查询
                    report.setGameId(userTrainLog.getGameId());
                    historyReport = userTrainReportDao.selectReportByMode(report);
                }
                //如果查询结果不为空，将原本的倒叙排列，顺序反转成正序排列
                if (!CollectionUtils.isEmpty(historyReport)) {
                    for (String s : historyReport) {
                        JSONObject jsonObject = new JSONObject(s);
                        historyReportData.add(jsonObject);
                    }
                    Collections.reverse(historyReportData);
                }
            }
        }
        result.put("historyReportData",historyReportData);
        result.put("trainReport",userTrainReport);
        return R.success(result);
    }

    /**
     * 功能描述:根据不同角色初始化sn,用户id
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/13 15:31
     */
    public UserTrainLogBo initUserTrainLogBo(UserTrainLogBo userTrainLogBo) {
        if(userTrainLogBo==null){
            userTrainLogBo = new UserTrainLogBo();
        }
        List<Long> roleIds = UserUtil.getRoleIds();
        User currentUser = UserUtil.getCurrentUser();
        //判断是否当前用户session为空
        if(currentUser==null){
            //抛出异常
            throw new UnknownAccountException();
        }
        String userId = UserUtil.getCurrentUser().getId();
        //如果是患者查询自己数据
        if(roleIds.contains(Constant.ROOT)){
            //管理员查询全部
            userTrainLogBo.setMachineIds(null);
        }else if(roleIds.contains(Constant.ORGANIZATION_ADMIN)){
            //机构管理员，查询机构
            List<String> machineIds = dataControlCabinController.selectMachineIds();
            userTrainLogBo.setMachineIds(machineIds);
        }else if(roleIds.contains(Constant.THERAPIST)){
            //查询属于自己的患者id
            List<String> uids = userDao.selectChildrenUserId(userId);
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(uids)){
                uids.add(userId);
            }else {
                uids = new ArrayList<String>();
                uids.add(userId);
            }
            userTrainLogBo.setUserIds(uids);
        }else if(roleIds.contains(Constant.PATIENT)){
            //判断是否是在线用户如果是在线用户
            String onlineId = currentUser.getOnlineId();
            if(StringUtils.isNotBlank(onlineId)){
                userTrainLogBo.setOnlineId(onlineId);
            }else {
                //查看自己
                userTrainLogBo.setUserId(currentUser.getId());
            }
        }
        return userTrainLogBo;
    }
    

    /**
     * 功能描述:下拉框数据显示
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/14 16:15
     */
    @PostMapping("/selectComboBox")
    @ApiOperation(value = "下拉框信息")
    @PermissionAccess
    public ResultVO<?> selectComboBox(@RequestBody UserTrainLogBo userTrainLogBo) {
        Map<String,Object> result = new HashMap<>();
        List<Long> roleIds = UserUtil.getRoleIds();
        //如果登录用户的角色不合理或者没有权限,不是病人，不是治疗师，不是医院管理员，不是管理员
        if (!roleIds.contains(Constant.ROOT) && !roleIds.contains(Constant.ORGANIZATION_ADMIN) && !roleIds.contains(Constant.PATIENT) && !roleIds.contains(Constant.THERAPIST)) {
            return R.success(result);
        }
        userTrainLogBo = this.initUserTrainLogBo(userTrainLogBo);
        //场所下拉
        List<String> places = userTrainLogDao.selectPlaceComboBox(userTrainLogBo);
        //用户名下拉框查询
        List<String> userNames = userTrainLogDao.selectUserNameComboBox(userTrainLogBo);
        //机器型号
        List<String> machineTypes = userTrainLogDao.selectMachineTypeComboBox(userTrainLogBo);
        //游戏下拉
        List<GameDto> games = userTrainLogDao.selectGameComboBox(userTrainLogBo);
        result.put("places",places);
        result.put("userNames",userNames);
        result.put("machineTypes",machineTypes);
        result.put("games",games);
        return R.success(result);
    }

    /**
     * 功能描述:训练数据统计
     *
     * @param userTrainLogBo
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/16 15:15
     */
    @PostMapping("/selectTrainData")
    @ApiOperation(value = "下拉框信息")
    @PermissionAccess
    public ResultVO<?> selectTrainData(@RequestBody UserTrainLogBo userTrainLogBo) {
        Map<String,Object> result = new HashMap<>();
        List<Long> roleIds = UserUtil.getRoleIds();
        //如果登录用户的角色不合理或者没有权限,不是病人，不是治疗师，不是医院管理员，不是管理员
        if (!roleIds.contains(Constant.ROOT) && !roleIds.contains(Constant.ORGANIZATION_ADMIN) && !roleIds.contains(Constant.PATIENT) && !roleIds.contains(Constant.THERAPIST)) {
            return R.success(result);
        }
        userTrainLogBo = this.initUserTrainLogBo(userTrainLogBo);
        TrainData trainData = userTrainLogDao.selectTrainData(userTrainLogBo);
        result.put("trainData",trainData);
        return R.success(result);
    }

    /**
     * 功能描述:游戏训练数据统计
     *
     * @param userTrainLogBo
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/17 10:06
     */
    @PostMapping("/selectGameTrainData")
    @ApiOperation(value = "统计游戏训练数据")
    public ResultVO<?> selectGameTrainData(@RequestBody UserTrainLogBo userTrainLogBo) {
        //时间类型判断
        String dateType = userTrainLogBo.getDateType();
        //查询类型
        String countType = userTrainLogBo.getCountType();
        if (!DateTypeEnum.DAY.getDateType().equals(dateType) && !DateTypeEnum.HOUR.getDateType().equals(dateType) && !DateTypeEnum.WEEK.getDateType().equals(dateType) && !DateTypeEnum.MONTH.getDateType().equals(dateType)) {
            return R.error(1, "时间类型格式不正确");
        }
        //查询类型
        if (!CountTypeEnum.PATIENT.getCountType().equals(countType) && !CountTypeEnum.TIME.getCountType().equals(countType) && !CountTypeEnum.TIMES.getCountType().equals(countType)) {
            return R.error(1, "查询类型格式不正确");
        }
        userTrainLogBo = this.initUserTrainLogBo(userTrainLogBo);
        //如果时周统计
        if(DateTypeEnum.WEEK.getDateType().equals(dateType)){
            List<GameTrainDataDto> gameTrainDatas = new ArrayList<>();
            List<DateWeekly> weekly = DateUtil.getWeekly(userTrainLogBo.getStartTime(), userTrainLogBo.getEndTime());
            for (DateWeekly dateWeekly : weekly
            ) {
                //时间轴节点
                GameTrainDataDto gameTrainDataDto = new GameTrainDataDto();
                //时间坐标
                gameTrainDataDto.setDate(dateWeekly.getSDate());
                userTrainLogBo.setStartTime(dateWeekly.getSDate());
                userTrainLogBo.setEndTime(dateWeekly.getEDate());
                List<GameDto> games = userTrainLogDao.selectGameTrainData(userTrainLogBo);
                //纵坐标，游戏数据
                gameTrainDataDto.setGameDtoList(games);
                gameTrainDatas.add(gameTrainDataDto);
            }
            return R.success(gameTrainDatas);
        }
        List<GameDto> gameDtos = new ArrayList<>();
        try {
            //非周统计
            List<String> dates = this.getDates(dateType, userTrainLogBo);
            gameDtos = userTrainLogDao.selectGameTrainData(userTrainLogBo);
            //游戏趋势数据集合，包含时间轴，游戏数据
            List<GameTrainDataDto> gameTrainDatas = new ArrayList<>();
            for (String date : dates) {
                //生成时间轴，游戏数据
                GameTrainDataDto gameTrainDataDto = new GameTrainDataDto();
                List<GameDto> games = new ArrayList<GameDto>();
                //时间轴初始化到游戏训练类
                gameTrainDataDto.setDate(date);
                //组装时间有游戏数据
                for (GameDto gameDto : gameDtos) {
                    if (date.equals(gameDto.getDate())) {
                        games.add(gameDto);
                    }
                }
                //游戏数据初始化到游戏训练类
                gameTrainDataDto.setGameDtoList(games);
                gameTrainDatas.add(gameTrainDataDto);
            }
            return R.success(gameTrainDatas);
        } catch (Exception e) {
            e.printStackTrace();
            R.error(1, "时间类型有误");
        }
        log.info(JSONUtil.toJsonStr(gameDtos));
        return R.success();
    }

    /**
     * 功能描述:获取日期
     *
     * @param dateType
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/17 15:18
     */
    private List<String> getDates(String dateType, UserTrainLogBo gameTrainReqDto) throws Exception {
        List<String> dates;
        //如果是本周
        if (DateTypeEnum.HOUR.getDateType().equalsIgnoreCase(dateType)) {
            //销售统计
            //dates = DateUtil.getDateByNum(7);
            //线生成天，然后根据天生成小时
            dates = DateUtil.getBetweenDays(gameTrainReqDto.getStartTime(), gameTrainReqDto.getEndTime());
            return this.calDayHour(dates);
        } else if (DateTypeEnum.DAY.getDateType().equalsIgnoreCase(dateType)) {
            //天统计
            dates = DateUtil.getBetweenDays(gameTrainReqDto.getStartTime(), gameTrainReqDto.getEndTime());
        } else if (DateTypeEnum.WEEK.getDateType().equalsIgnoreCase(dateType)) {
            //周统计
            dates = DateUtil.getThisYearMonthAll();
        } else {
            //月统计
            dates = DateUtil.getThisYearMonthAll();
        }
        return dates;
    }

    /**
     * 功能描述:获取周集合
     *
     * @param dateType
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/17 15:18
     */
    /*private List<DateWeekly> getDates(String dateType, UserTrainLogBo gameTrainReqDto) throws Exception {
        List<DateWeekly> weekly = DateUtil.getWeekly(gameTrainReqDto.getStartTime(), gameTrainReqDto.getEndTime());
        return weekly;
    }*/

    /**
     * 功能描述:根据天生成小时
     *
     * @param dates
     * @return java.lang.String
     * @author larry
     * @Date 2020/11/18 11:06
     */
    public List<String> calDayHour(List<String> dates) {
        //创建新的时间轴
        List<String> dateList = new ArrayList<String>();
        for (String dateStr : dates) {
            for (int i = 0; i < 24; i++) {
                dateList.add(dateStr + " " + this.getSerialNumber(i, 2));
            }
        }
        return dateList;
    }

    /**
     * 功能描述:根据数字生成固定长度字符串，不足加前缀0
     *
     * @param num
     * @param len
     * @return java.lang.String
     * @author larry
     * @Date 2020/11/18 11:23
     */
    public String getSerialNumber(int num, int len) {
        //日期时间前缀
        //String format = sdf.format(new Date());
        String numStr = String.valueOf(num);
        int numLen = numStr.length();
        if (numLen >= len) {
            //如果流水号长度大于指定长度，直接返回
            return numStr;
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len - numLen; i++) {
                sb.append("0");
            }
            sb.append(numStr);
            return sb.toString();
        }
    }

    /**
     * 功能描述:评估训练列表展示
     *
     * @param pageRequest
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2021/1/12 14:14
     */
    @PostMapping("/selectTrainEvaluationPage")
    @ApiOperation(value = "分页查询")
    @PermissionAccess
    public ResultVO<?> selectTrainEvaluationPage(@RequestBody PageRequest<UserTrainLogBo> pageRequest,@RequestHeader(value = "logintoken",required = false) String token,@RequestHeader(value = "source",required=false) String source) {
        Integer num = pageRequest.getNum();
        Integer page = pageRequest.getPage();
        if(num == null || page==null){
            return R.error(1,"分页数据为空");
        }
        List<Long> roleIds = UserUtil.getRoleIds();
        //是否根据token初始化角色，默认为否
        Boolean tokenFlag = Boolean.FALSE;
        if (StringUtils.isNotBlank(source)) {
            //根据token方式获取当前登录用户信息
            User userByToken = userService.getUserByToken(token);
            if (userByToken != null) {
                tokenFlag = Boolean.TRUE;
                roleIds = userByToken.getRoleIds();
            }
        }
        if(CollectionUtils.isEmpty(roleIds)){
            throw new UnknownAccountException();
            /*PageInfo<UserTrainEvaluationBo> userTrainLogPage = new PageInfo<>(null);
            return R.success(userTrainLogPage);*/
        }
        //如果登录用户的角色不合理或者没有权限,不是病人，不是治疗师，不是医院管理员，不是管理员
        if (!roleIds.contains(Constant.ROOT) && !roleIds.contains(Constant.ORGANIZATION_ADMIN) && !roleIds.contains(Constant.PATIENT) && !roleIds.contains(Constant.THERAPIST)) {
            PageInfo<UserTrainEvaluationBo> userTrainLogPage = new PageInfo<>(null);
            return R.success(userTrainLogPage);
        }
        UserTrainLogBo userTrainLogBo = pageRequest.getParams();
        if(userTrainLogBo==null){
            userTrainLogBo = new UserTrainLogBo();
        }
        //如果角色根据token查询,start
        if (tokenFlag) {
            this.initUserTrainLogBoByToken(userTrainLogBo);
        } else {
            this.initUserTrainLogBo(userTrainLogBo);
        }
        //如果角色根据token查询,end
        //this.initUserTrainLogBo(userTrainLogBo);
        PageHelper.startPage(page, num);
        List<UserTrainEvaluationBo> list = userTrainEvaluationDao.selectTrainEvaluationPage(userTrainLogBo);
        PageInfo<UserTrainEvaluationBo> userTrainLogPage = new PageInfo<>(list);
        return R.success(userTrainLogPage);
    }

    /**
     * 功能描述:根据评估报告id查询评估报告
     *
     * @param userTrainEvaluation
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2021/1/12 18:29
     */
    @PostMapping("/selectTrainEvaluationDetail")
    @ApiOperation(value = "查询评估明细")
    public ResultVO<?> selectTrainEvaluation(@RequestBody UserTrainEvaluation userTrainEvaluation) {
        Long id = userTrainEvaluation.getId();
        if(id==null){
            return R.error(1,"评估报告Id为空");
        }
        UserTrainEvaluationBo userTrainEvaluationBo = new UserTrainEvaluationBo();
        userTrainEvaluationBo.setId(id);
        List<UserTrainEvaluationBo> userTrainEvaluationBos = userTrainEvaluationDao.selectTrainEvaluationBoList(userTrainEvaluationBo);
        //查询报告为空
        if(CollectionUtils.isEmpty(userTrainEvaluationBos)){
            return R.error(1,"评估报告为空");
        }
        Map<String,Object> result = new HashMap<String,Object>();
        UserTrainEvaluationBo userTrainEvaluationBo1 = userTrainEvaluationBos.get(0);
        //用户
        String userId = userTrainEvaluationBo1.getUserId();
        userTrainEvaluationBo1.setId(null);
        //查询历史评估报告
        List<UserTrainEvaluationBo> userTrainEvaluationBoList = userTrainEvaluationDao.selectTrainEvaluationBoList(userTrainEvaluationBo1);
        //查询患者信息
        User user = userDao.getById(userId);
        //患者数据
        result.put("user",user);
        result.put("historyData",userTrainEvaluationBoList);
        //判断机型是否为EMU，如果是，结构重新处理
        if("EMU".equalsIgnoreCase(userTrainEvaluationBo1.getMachineType()) && !CollectionUtils.isEmpty(userTrainEvaluationBoList)){
            List<Object> jsonObjects = new ArrayList<>();
            for(UserTrainEvaluationBo trainEvaluationBo:userTrainEvaluationBoList){
                JSONObject jsonObject = new JSONObject(trainEvaluationBo);
                if(jsonObject != null){
                    JSONObject data = new JSONObject(jsonObject.get("data"));
                    jsonObjects.add(data);
                }
            }
            result.put("historyData",jsonObjects);
        }
        return R.success(result);
    }

}