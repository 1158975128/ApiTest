package com.zw.admin.server.controller;

import com.zw.admin.server.annotation.PermissionAccess;
import com.zw.admin.server.dao.*;
import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.dto.GameNumDto;
import com.zw.admin.server.dto.MachineNumDto;
import com.zw.admin.server.model.*;
import com.zw.admin.server.utils.Constant;
import com.zw.admin.server.utils.DateUtil;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author larry
 * @Description:数据驾驶舱控制器
 * @date 2020/10/21 11:29
 */
@Api(tags = "数据驾驶舱")
@Slf4j
@RestController
@RequestMapping("/dataControlCabin")
public class DataControlCabinController {

    @Resource
    private DeviceDao deviceDao;
    @Resource
    private DataCollectionDao dataCollectionDao;
    @Resource
    private UserDao userDao;
    @Resource
    private UserTrainLogDao userTrainLogDao;
    @Resource
    private MachineInfoDao machineInfoDao;
    @Resource
    private TrainStatsDao trainStatsDao;
    @Resource
    private GameDao gameDao;
    @Resource
    private UserPlaceRelDao userPlaceRelDao;
    @Resource
    private UserLogDao userLogDao;
    private String year = "year";
    private String month = "month";
    private String week = "week";
    private String day = "day";

    /**
     * 功能描述:总数据展示
     *
     * @param params
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/21 11:38
     */
    @PostMapping("/selectDataSum")
    @ApiOperation(value = "列表")
    public ResultVO<?> selectDataSum(@RequestBody Map<String, Object> params) {
        Map<String,Object> result = new HashMap<String,Object>();
        //总设备,总场所
        Map<String,Long> deviceAndPlaceNum = deviceDao.selectDeviceNum(null);
        Long deviceNum = deviceAndPlaceNum.get("deviceNum");
        //总场所
        Long placeNum = deviceAndPlaceNum.get("placeNum");
        //总患者数
        Long userNum = userDao.selectUserNum();
        //日训练数
        String  startTime= (String) params.get("startTime");
        String  endTime= (String) params.get("endTime");

        //日新增患者数量
        List<DateNumDto> increaseNumList = new ArrayList<>();
        //日训练数量
        List<DateNumDto> trainTimeByDay = new ArrayList<>();
        try {
            //判断开始时间和结束时间是否合理
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DATE_PATTERN);
            Date startDate = simpleDateFormat.parse(startTime);
            Date endDate = simpleDateFormat.parse(endTime);
            if (startDate.getTime()>endDate.getTime()){
                return R.error(1,"开始时间和结束时间范围不合理");
            }
            List<String> betweenDateStr =DateUtil.getBetweenDays(startTime,endTime);
            //查询日新增数据
            Map<String, DateNumDto> map = userDao.selectIncreaseNumByDay(params);
            Map<String, DateNumDto> stringDateNumDtoMap = dataCollectionDao.selectTrainNumByDay(params);
            //如果没有新增的设置为0
            for (String dateStr :betweenDateStr){
                //日增数量
                DateNumDto dateNumDto = new DateNumDto();
                //日训练数量
                DateNumDto trainTimeDto = new DateNumDto();
                dateNumDto.setDate(dateStr);
                trainTimeDto.setDate(dateStr);
                if(map.get(dateStr)!=null){
                    //如果当天有新增则设置为新增数量
                    dateNumDto.setNum(map.get(dateStr).getNum());
                }else {
                    //当天没有数据设置为0
                    dateNumDto.setNum(0);
                }
                //设置日训练次数
                if(stringDateNumDtoMap.get(dateStr)!=null){
                    trainTimeDto.setNum(stringDateNumDtoMap.get(dateStr).getNum());
                }else {
                    trainTimeDto.setNum(0);
                }
                //将结果添加到list
                increaseNumList.add(dateNumDto);
                //将每天训练次数添加到list
                trainTimeByDay.add(trainTimeDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //总训练数,训练数据更新后统计方式变更
        //Long trainNum = dataCollectionDao.selectTrainNum();
        Long trainNum = userTrainLogDao.count(new HashMap<String, Object>());
        //总训练时长
        Long trainTimeSum = dataCollectionDao.selectTrainTimeSum();
        //过去各月份设备增加的数量
        //result.put("machineIncreaseByMonth",machineIncreaseByMonth);

        //result.put("userByMachineType",list);
        //总设备
        result.put("deviceNum",deviceNum);
        //总场所
        result.put("placeNum",placeNum);
        //日新增患者
        result.put("increaseNumByDay",increaseNumList);
        //总患者
        result.put("userNum",userNum);
        //总训练数
        result.put("trainNum",trainNum);
        //总训练时长
        result.put("trainTimeSum",trainTimeSum);
        //日训练次数
        result.put("trainTimeByDay",trainTimeByDay);
        return R.success(result);
    }

    /**
     * 功能描述:根据开始时间和结束时间获取设备月增量
     *
     * @param params
     * @return java.util.List<com.zw.admin.server.dto.DateNumDto>
     * @author larry
     * @Date 2020/10/26 11:48
     */
    @PostMapping("/getMachineIncreaseByMonth")
    @ApiOperation(value = "列表")
    private ResultVO<?> getMachineIncreaseByMonth(@RequestBody Map<String, Object> params) {
        String  startTime= (String) params.get("startTime");
        String  endTime= (String) params.get("endTime");
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return R.error(1,"开始时间或结束时间为空");
        }
        List<DateNumDto> list = new ArrayList<DateNumDto>();
        try {
            List<String> monthBetween = DateUtil.getMonthBetween(startTime, endTime);
            Map<String,DateNumDto> increaseByMonth = machineInfoDao.getMachineIncreaseByMonth();
            for (int i=0;i<monthBetween.size();i++){
                DateNumDto dateNumDto = new DateNumDto();
                dateNumDto.setDate(monthBetween.get(i));
                if(increaseByMonth.get(monthBetween.get(i))!=null){
                    dateNumDto.setNum(increaseByMonth.get(monthBetween.get(i)).getNum());
                }else {
                    dateNumDto.setNum(0);
                }
                list.add(dateNumDto);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return R.success(list);
    }

    /**
     * 功能描述:最受欢迎的游戏
     *
     * @param
     * @return java.util.List<com.zw.admin.server.dto.GameNumDto>
     * @author larry
     * @Date 2020/10/27 11:11
     */
    @PostMapping("/selectMostPopularGames")
    @ApiOperation(value = "最受欢迎的游戏")
    @PermissionAccess
    public ResultVO<?> selectMostPopularGames(@RequestBody Map<String, Object> params) {
        //要查询的游戏数量
        Object objNum = params.get("gameNum");
        //机器型号
        Object objType = params.get("machineType");
        if(!(objNum instanceof Integer) || !(objType instanceof String)){
            return R.error(1,"参数错误");
        }
        Integer gameNum = (Integer)objNum;
        String machineType = (String)objType;
        if(gameNum<=0){
            return R.error(1,"参数错误");
        }
        if("all".equals(machineType)){
            //如果是全部游戏
            params.put("machineType",null);
        }
        Map<String,Object> result = new HashMap<>();
        List<String> machineIds = selectMachineIds();
        List<GameNumDto> mostPopularGameList = new ArrayList<>();
        params.put("list",machineIds);
        mostPopularGameList = userTrainLogDao.selectMostPopularGameList(params);
        /*if(!CollectionUtils.isEmpty(mostPopularGameList)){
            for(GameNumDto gameNumDto:mostPopularGameList){
                String gameNameById = gameDao.getGameNameById(gameNumDto.getGame());
                gameNumDto.setGame(gameNameById);
            }
        }*/
        //循环查询
        result.put("mostPopularGameList",mostPopularGameList);
        return R.success(result);
    }

    /**
     * 功能描述:设备使用时间统计
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/28 18:37
     */
    @PostMapping("/getMachineUseTime")
    @ApiOperation(value = "设备使用时间统计")
    @PermissionAccess
    public ResultVO<?> getMachineUseTime(@RequestBody Map<String, Object> params) {
        //日期条件校验，日，月，年
        ResultVO<?> resultVO = this.checkDateType(params);
        if(resultVO.getCode()!=0){
            return resultVO;
        }
        //如果登录的用户为机构，查询配置的机器Id
        List<String> machineIds = this.selectMachineIds();
        params.put("list",machineIds);
        //所有设备使用总长
        Long totalMachineUseTime = userLogDao.selectTotalMachineUseTime(params);;
        //各设备使用总时长
        List<MachineNumDto> machineUseTimeByType = new ArrayList<>();
        Map<String,Object> result = new HashMap<String,Object>();
        machineUseTimeByType = userLogDao.selectMachineUseTimeByType(machineIds);
        String dateType = (String)params.get("dateType");
        List<String> dates = getDates(dateType);
        //所有设备使用时间趋势
        Map<String,DateNumDto> useTimeTrendChartMap = userLogDao.selectMachineUseTimeTrendChart(params);
        //查询使用时间趋势
        List<DateNumDto> useTimeTrendChart = getDateNumList(dates, useTimeTrendChartMap);
        //查询设备训练时间趋势
        Map<String,DateNumDto> trainTimeTrendChartMap = userTrainLogDao.selectMachineTrainTimeTrendChart(params);
        //设备训练时间趋势
        List<DateNumDto> trainTimeTrendChart = getDateNumList(dates, trainTimeTrendChartMap);
        //各设备使用时间趋势
        result.put("totalMachineUseTime",totalMachineUseTime);
        //result.put("machineUseTimeByType",machineUseTimeByType);
        result.put("useTimeTrendChart",useTimeTrendChart);
        result.put("trainTimeTrendChart",trainTimeTrendChart);
        return R.success(result);
    }

    /**
     * 功能描述:设备数量统计
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/28 20:13
     */
    @PostMapping("/getMachineNum")
    @ApiOperation(value = "设备数量统计")
    public ResultVO<?> getMachineNum(@RequestBody Map<String, Object> params) {
        List<String> machineIds = null;
        //总设备
        Long totalMachineNum = 0l;
        //本月新增
        Long monthTotalMachineIncreaseNum = 0l;
        //本月设备新增明细
        Map<String,Object> result = new HashMap<>();
        List<MachineNumDto> monthMachineIncreaseDetail= new ArrayList<>();
        if(this.isOrganization()){
            //机构查询
            machineIds = this.getMachineIds();
            if(CollectionUtils.isEmpty(machineIds)){
                result.put("totalMachineNum",totalMachineNum);
                result.put("monthTotalMachineIncreaseNum",monthTotalMachineIncreaseNum);
                result.put("monthMachineIncreaseDetail",monthMachineIncreaseDetail);
                return R.success(result);
            }
        }
        //总设备,总场所
        totalMachineNum = machineInfoDao.selectTotalMachineNum(machineIds);
        monthTotalMachineIncreaseNum = machineInfoDao.selectMonthTotalMachineIncreaseNum(machineIds);
        monthMachineIncreaseDetail = machineInfoDao.selectMonthMachineIncreaseDetail(machineIds);
        result.put("totalMachineNum",totalMachineNum);
        result.put("monthTotalMachineIncreaseNum",monthTotalMachineIncreaseNum);
        result.put("monthMachineIncreaseDetail",monthMachineIncreaseDetail);
        return R.success(result);
    }

    /**
     * 功能描述:活跃人数查询
     *
     * @param params
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/30 16:42
     */
    @PermissionAccess
    @PostMapping("/selectActiveUser")
    @ApiOperation(value = "活跃人数查询")
    public ResultVO<?> selectActiveUser(@RequestBody Map<String, Object> params) {
        String dateType;
        //日期条件校验，日，月，年
        ResultVO<?> resultVO = this.checkDateType(params);
        if(resultVO.getCode()!=0){
            return resultVO;
        }
        String pale ;
        //范围，医院，省份
        if (params.get("pale") instanceof String){
            pale = (String)params.get("pale");
            if(!"province".equals(pale)&&!"hospital".equals(pale)){
                return R.error(1,"参数错误");
            }
        }else {
            return R.error(1,"参数错误");
        }
        Integer limit ;
        //展示数量
        if(params.get("limit") instanceof Integer){
            limit = (Integer)params.get("limit");
        }else {
            return R.error(1,"参数错误");
        }
        List<String> machineIds = selectMachineIds();
        //用户配置的机器序列号
        params.put("list",machineIds);
        //day,month,year,日月年
        List<DateNumDto> activeUsers = userTrainLogDao.selectActiveUser(params);
        Map<String,Object> result = new HashMap<String,Object>();
        if(isOrganization() && CollectionUtils.isEmpty(machineIds)){
            //用户没有配置设备
            result.put("activeUsers",null);
            return R.success();
        }
        if("province".equals(pale) && !CollectionUtils.isEmpty(activeUsers)){
            //如果查询的是省份
            for (DateNumDto dateNumDto:activeUsers){
                dateNumDto.setCoordinate(machineInfoDao.selectCoordinateByProvince(dateNumDto));
            }
        }
        result.put("activeUsers",activeUsers);
        return R.success(result);
    }


    /**
     * 功能描述:判断是否为医院等机构
     *
     * @param
     * @return java.lang.Boolean
     * @author larry
     * @Date 2020/10/29 16:34
     */
    public Boolean isOrganization() {
        User currentUser = UserUtil.getCurrentUser();
        if(currentUser==null){
            return false;
        }
        //如果是医院管理人员
        Long identity = currentUser.getIdentity();
        List<Long> roleIds = userDao.selectRoleIdByUserId(currentUser.getId());
        if(!CollectionUtils.isEmpty(roleIds)){
            if(roleIds.contains(Constant.ORGANIZATION_ADMIN)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 功能描述:根据用户id获取关联机器
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/29 16:37
     */
    public List<String> getMachineIds() {
        User currentUser = UserUtil.getCurrentUser();
        return userPlaceRelDao.selectMachineIdByUserId(currentUser.getId());
    }

    /**
     * 功能描述:患者新增
     *
     * @param params
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/31 11:47
     */
    @PostMapping("/selectIncreaseUser")
    @ApiOperation(value = "查询新增患者")
    @PermissionAccess
    public ResultVO<?> selectIncreaseUser(@RequestBody Map<String, Object> params) {
        //校验时间范围是否正确
        ResultVO<?> resultVO = this.checkDateType(params);
        if(resultVO.getCode()!=0){
            return resultVO;
        }
        String dateType = (String)params.get("dateType");
        //可以查询的机器列表,如果时管理员集合为空
        List<String> machineIds = selectMachineIds();
        params.put("list",machineIds);
        //日期集合
        List<String> dates = getDates(dateType);
        //获取时间集合
        Map<String,DateNumDto> dateNumDtoMaps = userDao.selectIncreaseUser(params);
        List<DateNumDto> dateNumList = getDateNumList(dates, dateNumDtoMaps);
        Map<String,Object> result = new HashMap<String,Object>();
        //如果该账户没有配置设备
        if(isOrganization() && CollectionUtils.isEmpty(getMachineIds())){
            result.put("increaseUser",null);
            result.put("totalNum",0);
            result.put("dayNum",0);
            result.put("monthNum",0);
            return R.success(result);
        }
        result.put("increaseUser",dateNumList);
        //查询总患者数
        params.put("dateType",null);
        Long totalNum = userDao.selectTotalRecoveredNum(params);
        //查询当天新增患者数
        params.put("dateType","day");
        Long dayNum = userDao.selectTotalRecoveredNum(params);
        //查询当月新增患者数
        params.put("dateType","month");
        Long monthNum = userDao.selectTotalRecoveredNum(params);
        result.put("totalNum",totalNum);
        result.put("dayNum",dayNum);
        result.put("monthNum",monthNum);
        return R.success(result);
    }

    /**
     * 功能描述:根据日期和查询结果拼接趋势
     *
     * @param list
     * @param map
     * @return java.util.List<com.zw.admin.server.dto.DateNumDto>
     * @author larry
     * @Date 2020/10/31 17:20
     */
    public List<DateNumDto> getDateNumList(List<String> list, Map<String, DateNumDto> map) {
        List<DateNumDto> dateNumDtos = new ArrayList<>();
        if (map == null) {
            //查询结果为空所有数据都为0
            for (String date : list) {
                DateNumDto dateNumDto = new DateNumDto();
                dateNumDto.setDate(date);
                dateNumDto.setNum(0);
                dateNumDtos.add(dateNumDto);
            }
            return dateNumDtos;
        }
        for (String date : list) {
            DateNumDto dateNumDto = new DateNumDto();
            dateNumDto.setDate(date);
            if (map.get(date) != null) {
                dateNumDto.setNum(map.get(date).getNum());
            } else {
                dateNumDto.setNum(0);
            }
            dateNumDtos.add(dateNumDto);
        }
        return dateNumDtos;
    }

    /**
     * 功能描述:设备使用次数统计
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/31 18:37
     */
    @PostMapping("/getMachineUseTimes")
    @ApiOperation(value = "设备使用次数统计")
    public ResultVO<?> getMachineUseTimes(@RequestBody Map<String, Object> params) {
        //校验时间范围是否正确
        ResultVO<?> resultVO = this.checkDateType(params);
        if(resultVO.getCode()!=0){
            return resultVO;
        }
        if (day.equals((String) params.get("dateType"))) {
            return R.error(1, "参数错误");
        }
        List<String> machineIds = null;
        //所有设备使用总长
        Long totalMachineUseTime = 0l;
        //各设备使用总时长
        List<MachineNumDto> machineUseTimeByType = new ArrayList<>();
        Map<String,Object> result = new HashMap<String,Object>();
        machineIds = this.selectMachineIds();
        String dateType = (String)params.get("dateType");
        //日期集合
        List<String> dates = getDates(dateType);
        //增加机器Id查询条件
        params.put("list",machineIds);
        Map<String,DateNumDto> machineUseTimes = userTrainLogDao.getMachineUseTimes(params);
        Map<String, MachineNumDto> machineTypeNumMap = machineInfoDao.selectMachineTypeNumByPlace(params);
        List<DateNumDto> dateNumList = getDateNumList(dates, machineUseTimes);
        Long totalUserTimes = userTrainLogDao.getTotalMachineUseTimes(params);
        //所有设备使用时间趋势
        result.put("machineUseTimes",dateNumList);
        //所有设备使用总次数
        result.put("totalUserTimes",totalUserTimes);
        //各设备使用率
        List<MachineNumDto> machineUserDetail = userTrainLogDao.getMachineUseDetailByDateType(params);
        //计算百分比
        List<MachineNumDto> machineUserRate = this.setRate(machineUserDetail, getOpenTime(dateType), 2,machineTypeNumMap);
        result.put("machineUserDetail",machineUserDetail);
        result.put("machineUserRate",machineUserRate);
        return R.success(result);
    }

    /**
     * 功能描述:校验统计时间范围参数是否正确
     *
     * @param params
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/31 17:41
     */
    private ResultVO<?> checkDateType(Map<String, Object> params) {
        //年year，月month，周week
        String dateType;
        if(params.get("dateType") instanceof String){
            dateType =(String) params.get("dateType");
        }else {
            return R.error(1, "时间格式不正确");
        }
        if (StringUtils.isEmpty(dateType)) {
            return R.error(1, "时间为空");
        }
        if (!year.equalsIgnoreCase(dateType) && !month.equalsIgnoreCase(dateType) && !week.equalsIgnoreCase(dateType) && !day.equalsIgnoreCase(dateType)) {
            return R.error(1, "时间格式不正确");
        }
        return R.success();
    }


    /**
     * 功能描述:根据时间类型获取时间集合
     *
     * @param dateType
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 18:03
     */
    private List<String> getDates(String dateType) {
        List<String> dates;
        //如果是本周
        if (week.equalsIgnoreCase(dateType)) {
            //查询本周
            dates = DateUtil.getThisWeek();
        } else if (month.equalsIgnoreCase(dateType)) {
            dates = DateUtil.getThisMonth();
        } else {
            dates = DateUtil.getThisYearMonth();
        }
        return dates;
    }

    /**
     * 功能描述:查询管理的sn集合
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/31 18:12
     */
    public List<String> selectMachineIds() {
        //可以查询的机器列表
        List<String> machineIds = new ArrayList<>();
        if (isOrganization()) {
            //如果机构登录
            machineIds = this.getMachineIds();
            if (CollectionUtils.isEmpty(machineIds)) {
                //没有机器sn
                machineIds.add("noMachineId");
                return machineIds;
            }
            return machineIds;
        } else {
            //非机构登录，直接返回
            return machineIds;
        }
    }

    /**
     * 功能描述:根据统计时间类型计算营业时间
     *
     * @param dateType
     * @return java.lang.Long
     * @author larry
     * @Date 2020/11/4 14:17
     */
    public Long getOpenTime(String dateType) {
        int weekMonthYear = DateUtil.getWeekMonthYear(dateType);
        //转成秒数
        Long totalTime = weekMonthYear*8*3600l;
        return totalTime;
    }


    /**
     * 功能描述:计算设备使用的百分比
     *
     * @param list
     * @param openTime
     * @param bit
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/11/4 14:55
     */
    public List<MachineNumDto> setRate(List<MachineNumDto> list, Long openTime, int bit,Map<String,MachineNumDto> map) {
        //根据角色统计每种设备的数量
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(bit);
        if (!CollectionUtils.isEmpty(list)) {
            for (MachineNumDto numDto : list) {
                //判断这个机型的数量
                MachineNumDto machineNumDto = map.get(numDto.getMachineType());
                Long machineNum = 1L;
                if(machineNumDto !=null){
                    machineNum = machineNumDto.getNum();
                }
                //保留两位小数
                String rate = format.format((float) numDto.getTime() / (float) (openTime * machineNum) * 100 ) + "%";
                numDto.setRate(rate);
            }
        }
        return list;
    }

}
