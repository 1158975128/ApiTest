package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.dto.MachineNumDto;
import com.zw.admin.server.model.Province;
import org.apache.ibatis.annotations.*;
import com.zw.admin.server.model.MachineInfo;

/**
 *功能描述:机器信息数据层
 * @author larry
 * @Date 2020/6/19 10:19
 */
@Mapper
public interface MachineInfoDao {
    @Select("select * from machine_info t where t.sn = #{sn} limit 1")
    MachineInfo getBySn(String sn);

    @Delete("delete from machine_info where sn = #{sn}")
    int deleteBySn(String sn);

    int update(MachineInfo machineInfo);

    int save(MachineInfo machineInfo);

    int count(@Param("params") Map<String, Object> params);

    List<MachineInfo> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<String> selectProvince();

    @MapKey("date")
    Map<String,DateNumDto> getMachineIncreaseByMonth();

    @Select("SELECT machine_type from machine_info where sn= #{sn}")
    String getMachineTypeBySn(String sn);

    /**
     * 功能描述:查询机器型号对应的数量
     *
     * @param
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/10/27 14:44
     */
    List<MachineNumDto> selectMachineTypeNum();

    /**
     * 功能描述:所有设备使用总时长
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/28 19:07
     */
    Long selectMachineTotalUseTime();

    /**
     * 功能描述:查询各设备使用总时长
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/28 19:07
     */
    List<MachineNumDto> selectMachineUseTimeByType();

    /**
     * 功能描述:所有设备使用总次数
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/28 19:07
     */
    Long selectTotalUseNum();

    /**
     * 功能描述:当日训练次数
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/28 19:07
     */
    Long selectTodayUseNum();


    /**
     * 功能描述:总设备数量
     *
     * @param list
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/30 10:18
     */
    Long selectTotalMachineNum(List<String> list);

    /**
     * 功能描述:月新增总设备数量
     *
     * @param list
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/30 10:18
     */
    Long selectMonthTotalMachineIncreaseNum(List list);

    /**
     * 功能描述:月新增设备数量明细
     *
     * @param list
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/30 10:18
     */
    List<MachineNumDto> selectMonthMachineIncreaseDetail(List list);

    /**
     * 功能描述:设备下拉
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/30 10:18
     */
    List<String> selectMachineTypes();

    /**
     * 功能描述:场所下拉框
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/5 11:51
     */
    List<String> selectPlaces();

    Integer initPlaceCoordinate(Province province);

    List<Map<String,Object>> selectProvinceCoordinate();

    String selectCoordinateByProvince(DateNumDto dateNumDto);

    /**
     * 功能描述:查询每个机型的数量
     *
     * @param params
     * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2021/3/25 17:09
     */
    @MapKey("machineType")
    Map<String, MachineNumDto> selectMachineTypeNumByPlace(Map<String,Object> params);

}