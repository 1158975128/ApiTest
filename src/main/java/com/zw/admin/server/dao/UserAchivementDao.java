package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserAchivement;

/**
 *功能描述:用户成就数据层
 * @author larry
 * @Date 2020/7/6 15:49
 */
@Mapper
public interface UserAchivementDao {
    @Select("select * from user_achivement t where t.id = #{id}")
    UserAchivement getById(Long id);

    @Delete("delete from user_achivement where id = #{id}")
    int delete(Long id);

    int update(UserAchivement userAchivement);

    @Insert("insert into user_achivement(user_id, achivement_id, process, state, create_time, update_time) values(#{userId}, #{achivementId}, #{process}, #{state}, now(), #{updateTime})")
    int save(UserAchivement userAchivement);

    int count(@Param("params") Map<String, Object> params);

    List<UserAchivement> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    UserAchivement selectUserAchivement(UserAchivement userAchivement);

    List<UserAchivement> selectUserAchivements(UserAchivement userAchivement);
}