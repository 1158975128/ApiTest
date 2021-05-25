package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserTrainState;

/**
 *功能描述:用户统计信息数据层
 * @author larry
 * @Date 2020/7/6 15:49
 */
@Mapper
public interface UserTrainStateDao {
    @Select("select * from user_train_state t where t.id = #{id}")
    UserTrainState getById(Long id);

    @Delete("delete from user_train_state where id = #{id}")
    int delete(Long id);

    int update(UserTrainState userTrainState);

    @Insert("insert into user_train_state(user_id, train_time, train_distance, train_exert, create_time, update_time, user_game_grade, machine_type) values(#{userId}, #{trainTime}, #{trainDistance}, #{trainExert}, now(), #{updateTime}, #{userGameGrade}, #{machineType})")
    int save(UserTrainState userTrainState);

    int count(@Param("params") Map<String, Object> params);

    List<UserTrainState> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    UserTrainState selectTrainStateByUser(UserTrainState userTrainState);
}