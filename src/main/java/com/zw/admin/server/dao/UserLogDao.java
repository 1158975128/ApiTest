package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.dto.MachineNumDto;
import org.apache.ibatis.annotations.*;
import com.zw.admin.server.model.UserLog;

@Mapper
public interface UserLogDao {

    @Select("select * from user_log t where t.id = #{id}")
    UserLog getById(Long id);

    @Delete("delete from user_log where id = #{id}")
    int delete(Long id);

    int update(UserLog userLog);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user_log(user_id, machine_id, machine_type, ip, update_time, create_time, rups_ver, time,log_id) values(#{userId}, #{machineId}, #{machineType}, #{ip}, #{updateTime}, now(), #{rupsVer}, #{time},#{logId})")
    int save(UserLog userLog);

    int count(@Param("params") Map<String, Object> params);

    List<UserLog> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

        /**
         *功能描述:根据用户id者上传时间机器ID查询是否存在
         * @author larry
         * @Date 2020/10/29 14:13
         * @param userLog
         * @return com.zw.admin.server.model.UserLog
         */
    UserLog checkExists(UserLog userLog);

    /**
     * 功能描述:设备使用总时长
     *
     * @param params
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/29 17:02
     */
    Long selectTotalMachineUseTime(Map<String, Object> params);

    /**
     * 功能描述:各设备使用总时长
     *
     * @param list
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/10/29 17:09
     */
    List<MachineNumDto> selectMachineUseTimeByType(List<String> list);

    /**
     * 功能描述:设备使用总时长趋势
     *
     * @param params
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/10/29 17:09
     */
    @MapKey("date")
    Map<String,DateNumDto> selectMachineUseTimeTrendChart(Map<String,Object> params);

    Integer countUserLogByLogId(String logId);

}