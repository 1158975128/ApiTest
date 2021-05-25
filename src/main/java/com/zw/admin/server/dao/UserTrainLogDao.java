package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.*;
import com.zw.admin.server.model.MachineInfo;
import com.zw.admin.server.model.TrainData;
import com.zw.admin.server.model.UserTrainLogBo;
import org.apache.ibatis.annotations.*;
import com.zw.admin.server.model.UserTrainLog;

/**
 *功能描述:用户训练日志同步
 * @author larry
 * @Date 2020/10/22 19:04
 */
@Mapper
public interface UserTrainLogDao {
    @Select("select t.*,g.gamename game_name from user_train_log t left join game g on t.game_id = g.id where t.id = #{id}")
    UserTrainLog getById(Long id);

    @Delete("delete from user_train_log where id = #{id}")
    int delete(Long id);

    int update(UserTrainLog userTrainLog);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("replace into user_train_log(train_log_id, user_id, machine_id, game_id, machine_data, start_time, end_time, game_data, time,machine_type,create_time,update_time,user_name) values(#{trainLogId}, #{userId}, #{machineId}, #{gameId}, #{machineData}, #{startTime}, #{endTime}, #{gameData}, #{time},#{machineType},#{createTime},#{updateTime},#{userName})")
    int save(UserTrainLog userTrainLog);

    long count(@Param("params") Map<String, Object> params);

    List<UserTrainLog> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 功能描述:最喜欢的三个游戏
     *
     * @param
     * @return java.util.List<com.zw.admin.server.dto.GameNumDto>
     * @author larry
     * @Date 2020/10/27 10:59
     */
    List<GameNumDto> selectMostPopularGames();

    /**
     * 功能描述:过去一个月设备
     *
     * @param
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/10/27 12:04
     */
    List<GameNumDto> selectMostPopularGameList(Map<String,Object> params);

    /**
     * 功能描述:设备使用次数
     *
     * @param params
     * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.DateNumDto>
     * @author larry
     * @Date 2020/11/3 11:39
     */
    @MapKey("date")
    Map<String,DateNumDto> getMachineUseTimes(Map<String,Object> params);

    /**
     * 功能描述:设备使用次数
     *
     * @param params
     * @return java.lang.Long
     * @author larry
     * @Date 2020/11/3 13:39
     */
    Long getTotalMachineUseTimes(Map<String,Object> params);

    /**
     * 功能描述:活跃人数查询
     *
     * @param params
     * @return java.lang.Long
     * @author larry
     * @Date 2020/11/3 15:39
     */
    List<DateNumDto> selectActiveUser(Map<String,Object> params);

    /**
     * 功能描述:设备使用总时长趋势
     *
     * @param params
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/10/29 17:09
     */
    @MapKey("date")
    Map<String,DateNumDto> selectMachineTrainTimeTrendChart(Map<String,Object> params);

    /**
     * 功能描述:本周，月，年各设备使用明细
     *
     * @param params
     * @return java.util.List<com.zw.admin.server.dto.MachineNumDto>
     * @author larry
     * @Date 2020/11/4 13:47
     */
    List<MachineNumDto> getMachineUseDetailByDateType(Map<String, Object> params);

    /**
     * 功能描述:分页查询
     *
     * @param userTrainLog
     * @return java.util.List<com.zw.admin.server.model.UserTrainLogBo>
     * @author larry
     * @Date 2020/11/16 10:59
     */
    List<UserTrainLogBo> selectTrainsLogPage(UserTrainLogBo userTrainLog);

    /**
     * 功能描述:姓名下拉框查询
     *
     * @param userTrainLog
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/16 11:00
     */
    List<String> selectUserNameComboBox(UserTrainLogBo userTrainLog);

    /**
     * 功能描述:机器型号下拉框查询
     *
     * @param userTrainLogBo
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/16 11:00
     */
    List<String> selectMachineTypeComboBox(UserTrainLogBo userTrainLogBo);

    /**
     * 功能描述:机器型号下拉框查询
     *
     * @param userTrainLogBo
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/16 11:00
     */
    List<String> selectPlaceComboBox(UserTrainLogBo userTrainLogBo);

    /**
     * 功能描述:游戏名称下拉
     *
     * @param userTrainLogBo
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/16 11:00
     */
    List<GameDto> selectGameComboBox(UserTrainLogBo userTrainLogBo);

    /**
     * 功能描述:训练数据统计
     *
     * @param userTrainLogBo
     * @return
     * @author larry
     * @Date 2020/11/16 11:00
     */
    TrainData selectTrainData(UserTrainLogBo userTrainLogBo);

    /**
     * 功能描述:游戏数据柱状图查询
     *
     * @param userTrainLogBo
     * @return com.zw.admin.server.dto.DateNumDto
     * @author larry
     * @Date 2020/11/17 10:37
     */
    List<GameDto> selectGameTrainData(UserTrainLogBo userTrainLogBo);

}