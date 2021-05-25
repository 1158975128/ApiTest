package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserTrainReport;

/**
 *功能描述:用户训练报表同步
 * @author larry
 * @Date 2020/10/22 19:13
 */
@Mapper
public interface UserTrainReportDao {

    int update(UserTrainReport userTrainReport);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("replace into user_train_report(report_id, user_id, machine_id, train_log_id, game_id, type, mode, category, data,create_time,update_time,machine_type) values(#{reportId}, #{userId}, #{machineId}, #{trainLogId}, #{gameId}, #{type}, #{mode}, #{category}, #{data},now(),#{updateTime},#{machineType})")
    int save(UserTrainReport userTrainReport);

    UserTrainReport selectByTrainsLogId(String logId);

    /**
     * 功能描述:查询历史数据
     *
     * @param userTrainReport
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author larry
     * @Date 2020/11/23 16:10
     */
    List<String> selectReportByMode(UserTrainReport userTrainReport);

}