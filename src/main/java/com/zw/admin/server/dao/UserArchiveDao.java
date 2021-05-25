package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserArchive;

/**
 *功能描述:用户存档
 * @author larry
 * @Date 2020/10/22 19:02
 */
@Mapper
public interface UserArchiveDao {
    @Select("select * from user_archive t where t.id = #{id}")
    UserArchive getById(Long id);

    int update(UserArchive userArchive);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user_archive(user_id, machine_type, `key`, archive,create_time,update_time) values(#{userId}, #{machineType}, #{key}, #{archive},#{createTime},#{updateTime})")
    int save(UserArchive userArchive);

    /**
     * 功能描述:用户存档查询
     *
     * @param userArchive
     * @return java.util.List<com.zw.admin.server.model.UserArchive>
     * @author larry
     * @Date 2020/10/23 18:07
     */
    List<UserArchive> selectUserArchive(UserArchive userArchive);

    /**
     * 功能描述:用户存档查询
     *
     * @param userArchive
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/10/23 18:07
     */
    Integer deleteUserArchive(UserArchive userArchive);
}