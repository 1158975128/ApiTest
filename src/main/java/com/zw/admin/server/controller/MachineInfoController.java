package com.zw.admin.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.UserTrainLogDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.model.UserTrainLogBo;
import com.zw.admin.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.zw.admin.server.dao.MachineInfoDao;
import com.zw.admin.server.model.MachineInfo;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *功能描述:机器类型控制器
 * @author larry
 * @Date 2020/6/19 10:23
 */
@Slf4j
@RestController
@RequestMapping("/machineInfo")
public class MachineInfoController {

    @Resource
    private MachineInfoDao machineInfoDao;

    @Resource
    private DataControlCabinController dataControlCabinController;

    @Resource
    private UserTrainLogDao userTrainLogDao;

    @Resource
    private UserTrainLogController userTrainLogController;

    /**
     * 功能描述:机器信息保存或者修改
     *
     * @param machineInfo
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/6/19 10:38
     */
    @LogAnnotation(module = "添加机器类型")
    @PostMapping("/save")
    @ApiOperation(value = "保存或这修改信息")
    public ResultVO<?> save(MachineInfo machineInfo) {
        log.info("machineInfo保存或修改" + machineInfo.toString());
        if (machineInfo == null) {
            return R.error(1, "参数错误");
        }
        String sn = machineInfo.getSn();
        if (StringUtils.isBlank(sn)) {
            return R.error(1, "缺少SN");
        }
        String rupsVer = machineInfo.getRupsVer();
        if (StringUtils.isBlank(rupsVer)) {
            return R.success();
        }
        //先查询,如果数据库已经存在则走更新，否则走添加
        MachineInfo bySn = machineInfoDao.getBySn(sn);
        if(bySn!=null){
            //提交更新
            machineInfo.setPlace(null);
            machineInfo.setRoute(null);
            machineInfo.setCardNumber(null);
            machineInfo.setId(bySn.getId());
            machineInfoDao.update(machineInfo);
            return R.success();
        }
        //把场所初始化为机构
        machineInfo.setPlace(machineInfo.getOrganization());
        machineInfoDao.save(machineInfo);
        return R.success();
    }

    @GetMapping("/selectMachineInfoBySn")
    @ApiOperation(value = "根据id获取")
    public MachineInfo get(String sn) {
        return machineInfoDao.getBySn(sn);
    }

    /**
     * 功能描述:修改机器信息
     *
     * @param machineInfo
     * @return com.zw.admin.server.model.MachineInfo
     * @author larry
     * @Date 2020/6/19 10:42
     */
    @LogAnnotation(module = "机器信息修改")
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public ResultVO<?> update(MachineInfo machineInfo) {
        //machineInfo.setUpdateTime(new Date());
        User user = UserUtil.getCurrentUser();
        //更新人
        if(user!=null){
            machineInfo.setUpdateUser(user.getId());
        }
        machineInfoDao.update(machineInfo);
        return R.success();
    }

    /**
     * 功能描述:设备列表
     *
     * @param param
     * @param page
     * @param num
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/24 15:46
     */
    @PostMapping("/webList")
    @ApiOperation(value = "设备列表")
    public ResultVO<?> list(String param, Integer page, Integer num) {
        Map<String, Object> params = new HashMap<>();
        List<String> sns = dataControlCabinController.selectMachineIds();
        try {
            Map<String, Object> map = JSONObject.parseObject(param, Map.class);
            map.put("list",sns);
            params = map == null ? params : map;
        } catch (Exception e) {
            return R.error(1, "params格式错误");
        }
        int total = machineInfoDao.count(params);
        PageObject page1 = PageUtils.pageClass(page, total, num);
        //返回信息
        Map<Object, Object> map = new HashMap<>();
        List<MachineInfo> list = machineInfoDao.list(params, page1.getStartIndex(), num);
        map.put("list", list);
        map.put("total", total);
        return R.success(map);
    }

    /**
     * 功能描述:省份下拉
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/26 10:12
     */
    @PostMapping("/selectProvince")
    @ApiOperation(value = "省份下拉")
    public ResultVO<?> selectProvince() {
        List<String> strings = machineInfoDao.selectProvince();
        return R.success(strings);
    }

    /**
     * 功能描述:设备下拉
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/30 17:26
     */
    @GetMapping("/selectMachineTypes")
    @ApiOperation(value = "设备下拉")
    public ResultVO<?> selectMachineTypes() {
        /*List<String> machineTypes = machineInfoDao.selectMachineTypes();
        return R.success(machineTypes);*/
        Map<String, Object> result = new HashMap<>();
        List<Long> roleIds = UserUtil.getRoleIds();
        //如果登录用户的角色不合理或者没有权限,不是病人，不是治疗师，不是医院管理员，不是管理员
        if (!roleIds.contains(Constant.ROOT) && !roleIds.contains(Constant.ORGANIZATION_ADMIN) && !roleIds.contains(Constant.PATIENT) && !roleIds.contains(Constant.THERAPIST)) {
            return R.success(result);
        }
        UserTrainLogBo userTrainLogBo = userTrainLogController.initUserTrainLogBo(new UserTrainLogBo());
        //机器型号
        List<String> machineTypes = userTrainLogDao.selectMachineTypeComboBox(userTrainLogBo);
        return R.success(machineTypes);
    }

    /**
     * 功能描述:省份坐标查询
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/11 18:20
     */
    @GetMapping("/selectProvinceCoordinate")
    @ApiOperation(value = "省份坐标查询")
    public ResultVO<?> selectProvinceCoordinate() {
        List<Map<String, Object>> coordinate = machineInfoDao.selectProvinceCoordinate();
        return R.success(coordinate);
    }

}