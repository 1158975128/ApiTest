package com.zw.admin.server.task;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zw.admin.server.api.XiaoShouYiApi;
import com.zw.admin.server.dao.MachineInfoDao;
import com.zw.admin.server.dto.AssetDto;
import com.zw.admin.server.dto.SerialNumberDto;
import com.zw.admin.server.model.MachineInfo;
import com.zw.admin.server.utils.DateUtil;
import com.zw.admin.server.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author larry
 * @Description:从销售易中获取数据
 * @date 2020/11/21 9:59
 */
@Slf4j
@Component
public class InitPlaceByXiaoShouYiTask implements ITask {

    @Resource
    private XiaoShouYiApi xiaoShouYiApi;
    @Resource
    private MachineInfoDao machineInfoDao;

    @Override
    public void run() {
        log.info("**********获取销售易sn号************");
        //获取token
        String strJson = xiaoShouYiApi.getAccessTokenByPassword();
        JSONObject tokenJson = new JSONObject(strJson);
        String accessToken = tokenJson.getStr("access_token");
        accessToken = "Bearer " + accessToken;
        //开始时间，昨天零点
        Date yesterday = DateUtil.getYesterday();
        Long yesterdayStamp = DateUtil.dateToStamp(yesterday);
        //结束时间，今天零点
        Date today = DateUtil.getToday();
        Long todayStamp = today.getTime();
        String queryByTime = " and updatedAt between " + yesterdayStamp + " and " + todayStamp;
        DateUtil.dateToStamp(today);
        String serialNumberSql = "select id,name,customItem2__c machineTypes,customItem11__c sn,createdAt,updatedAt,customItem6__c hardwareVer,customItem5__c mechanismVer,customItem3__c as softwareVersion,customItem4__c as algorithmVersion  from customEntity1__c where customItem11__c is not null and customItem11__c <>'海外版'";
        String queryAssetSql = "select serialNumber ,customItem2__c as place from asset where serialNumber is not null";
        serialNumberSql = serialNumberSql + queryByTime;
        queryAssetSql = queryAssetSql + queryByTime;
        //获取sn的map集合
        Map<String, SerialNumberDto> serialNumberMap = getSerialNumber(accessToken, serialNumberSql, Boolean.FALSE);
        List<MachineInfo> machineInfos = new ArrayList<MachineInfo>();
        if (serialNumberMap != null && serialNumberMap.size() > 0) {
            //获取资产集合
            List<AssetDto> assets = getAssets(accessToken, queryAssetSql, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(assets)) {
                for (AssetDto assetDto : assets) {
                    //如果可以匹配上
                    if (serialNumberMap.containsKey(assetDto.getSerialNumber())) {
                        log.info("*****" + assetDto.getSerialNumber());
                        //SerialNumberDto serialNumberDto = serialNumberMap.get(assetDto.getSerialNumber());
                        serialNumberMap.get(assetDto.getSerialNumber()).setPlace(assetDto.getPlace());
                    }
                }
            }
        }
        Set<Map.Entry<String, SerialNumberDto>> entries = serialNumberMap.entrySet();
        for (Map.Entry<String, SerialNumberDto> entry : entries) {
            SerialNumberDto value = entry.getValue();
            //assets.add(value)
            machineInfos.add(this.getMachineInfo(value));
        }
        //插入数据
        if (!CollectionUtils.isEmpty(machineInfos)) {
            for (MachineInfo machineInfo : machineInfos) {
                MachineInfo bySn = machineInfoDao.getBySn(machineInfo.getSn());
                if (bySn==null) {
                    machineInfoDao.save(machineInfo);
                }else {
                    String place = machineInfo.getPlace();
                    if(!StringUtils.isEmpty(place)){
                        //如果place不为空则更新场所信息
                        //machineInfoDao.selectMachineTotalUseTime()
                        MachineInfo info = new MachineInfo();
                        info.setPlace(place);
                        info.setSn(machineInfo.getSn());
                        info.setUpdateTime(new Date());
                        machineInfoDao.update(info);
                    }
                    log.info(bySn.getSn()+"数据库已经存在");
                }
            }
        }
        log.info("**********任务结束************");


    }

    /**
     * 功能描述:获取编号
     *
     * @param accessToken
     * @param sql
     * @param b
     * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.SerialNumberDto>
     * @author larry
     * @Date 2020/11/21 18:13
     */
    public Map<String, SerialNumberDto> getSerialNumber(String accessToken, String sql, Boolean b) {
        Boolean flag = Boolean.FALSE;
        String result = xiaoShouYiApi.getMsgByXoql(sql, flag, accessToken);
        JSONObject resultJson = new JSONObject(result);
        String code = resultJson.getStr("code");
        if ("200".equals(code)) {
            //请求成功
            JSONObject obj = resultJson.getJSONObject("data");
            if (obj != null) {
                JSONArray jsonArray = obj.getJSONArray("records");
                ArrayList<SerialNumberDto> serialNumberDtos = jsonArray.toList(SerialNumberDto.class);
                if (!CollectionUtils.isEmpty(serialNumberDtos)) {
                    //将集合包装到Map
                    Map<String, SerialNumberDto> map = new HashMap<String, SerialNumberDto>();
                    for (SerialNumberDto serialNumberDto : serialNumberDtos) {
                        //判断机型是否为空,如果为空不添加到map
                        if (!StringUtils.isEmpty(serialNumberDto.getMachineTypes().get(0))) {
                            String mt = serialNumberDto.getMachineTypes().get(0).toUpperCase();
                            //m2p
                            if (mt.contains("M2 PRO")) {
                                serialNumberDto.setMachineType("M2P");
                            } else if (mt.contains("M1A")) {
                                serialNumberDto.setMachineType("M1A");
                            } else if (mt.contains("M1W") ) {
                                serialNumberDto.setMachineType("M1W");
                            } else if (mt.contains("M2K")) {
                                serialNumberDto.setMachineType("M2K");
                            } else if (mt.contains("M2")||mt.contains("M2 P")) {
                                serialNumberDto.setMachineType("M2");
                            } else if (mt.contains("H1")) {
                                serialNumberDto.setMachineType("H1");
                            } else if (mt.contains("PH")) {
                                serialNumberDto.setMachineType("PH");
                            } else if(mt.contains("EMU")){
                                serialNumberDto.setMachineType("EMU");
                            }else {
                                serialNumberDto.setMachineType(mt);
                            }
                            map.put(serialNumberDto.getName(), serialNumberDto);
                        }
                    }
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * 功能描述:获取资产
     *
     * @param accessToken
     * @param sql
     * @param b
     * @return java.util.List<com.zw.admin.server.dto.AssetDto>
     * @author larry
     * @Date 2020/11/21 18:12
     */
    public List<AssetDto> getAssets(String accessToken, String sql, Boolean b) {

        String assetResult = xiaoShouYiApi.getMsgByXoql(sql, b, accessToken);
        JSONObject assetJson = new JSONObject(assetResult);
        String assetCode = assetJson.getStr("code");
        if ("200".equals(assetCode)) {
            JSONObject json = assetJson.getJSONObject("data");
            if (json != null) {
                JSONArray array = json.getJSONArray("records");
                if (array != null) {
                    ArrayList<AssetDto> assetDtos = array.toList(AssetDto.class);
                    ArrayList<AssetDto> assets = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(assetDtos)) {
                        for (AssetDto assetDto : assetDtos) {
                            String serialNumber = assetDto.getSerialNumber();
                            if (!StringUtils.isEmpty(serialNumber)) {
                                String[] strings = {};
                                if (serialNumber.indexOf(" ") > 0) {
                                    strings = serialNumber.split(" ");
                                } else if (serialNumber.indexOf("、") > 0) {
                                    strings = serialNumber.split("、");
                                } else if (serialNumber.indexOf(";") > 0) {
                                    strings = serialNumber.split(";");
                                } else if (serialNumber.indexOf("；") > 0) {
                                    strings = serialNumber.split("；");
                                } else if (serialNumber.indexOf(",") > 0) {
                                    strings = serialNumber.split(",");
                                } else if (serialNumber.indexOf("，") > 0) {
                                    strings = serialNumber.split("，");
                                } else if (serialNumber.indexOf("|") > 0) {
                                    strings = serialNumber.split("|");
                                }
                                if (strings.length > 1) {
                                    for (String s : strings) {
                                        AssetDto asset = new AssetDto();
                                        asset.setPlace(assetDto.getPlace());
                                        asset.setSerialNumber(s);
                                        assets.add(asset);
                                    }
                                } else {
                                    assets.add(assetDto);
                                }

                            }
                        }
                        return assets;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能描述:转换对象
     *
     * @param serialNumberDto
     * @return com.zw.admin.server.model.MachineInfo
     * @author larry
     * @Date 2020/11/21 18:35
     */
    public MachineInfo getMachineInfo(SerialNumberDto serialNumberDto) {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setSn(serialNumberDto.getSn());
        machineInfo.setMachineType(serialNumberDto.getMachineType());
        machineInfo.setSerialNumber(serialNumberDto.getName());
        machineInfo.setCreateTime(DateUtil.stampToDate(serialNumberDto.getCreatedAt()));
        machineInfo.setUpdateTime(DateUtil.stampToDate(serialNumberDto.getUpdatedAt()));
        machineInfo.setPlace(serialNumberDto.getPlace());
        return machineInfo;
    }
}
