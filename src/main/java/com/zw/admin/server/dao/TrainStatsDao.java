package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.MachineNumDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.TrainStats;

/**
 *功能描述:用户训练统计
 * @author larry
 * @Date 2020/10/22 19:08
 */
@Mapper
public interface TrainStatsDao {
    @Select("select * from train_stats t where t.id = #{id}")
    TrainStats getById(Long id);

    int update(TrainStats trainStats);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into train_stats(user_id, machine_type, train_time, train_distance, train_times, train_days, force_times, total_score,create_time,update_time) values(#{userId}, #{machineType}, #{trainTime}, #{trainDistance}, #{trainTimes}, #{trainDays}, #{forceTimes}, #{totalScore},now(),#{updateTime})")
    int save(TrainStats trainStats);

    int count(@Param("params") Map<String, Object> params);

    /**
     * 功能描述:用户训练统计
     *
     * @param trainStats
     * @return java.util.List<com.zw.admin.server.model.TrainStats>
     * @author larry
     * @Date 2020/10/23 18:01
     */
    List<TrainStats> selectTrainStats(TrainStats trainStats);

    /**
     * 功能描述:删除用户
     *
     * @param trainStats
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/10/23 19:22
     */
    Integer deleteTrainStats(TrainStats trainStats);

    /**
     * 功能描述:各设备累计患者数量
     *
     * @param
     * @return com.zw.admin.server.dto.MachineNumDto
     * @author larry
     * @Date 2020/10/26 16:31
     */
    List<MachineNumDto> countUserByMachineType();
}