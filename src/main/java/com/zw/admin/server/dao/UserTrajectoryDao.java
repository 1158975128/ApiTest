package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.UserTrajectory;

@Mapper
public interface UserTrajectoryDao {

	@Select("select * from user_trajectory t where t.id = #{id}")
	UserTrajectory getById(Long id);

	@Delete("delete from user_trajectory where id = #{id}")
	int delete(Long id);

	int update(UserTrajectory userTrajectory);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into user_trajectory(trajectoryid, creatorid, modifyid, name, machinetype, image, weight, height, point, createdate, updatedate) values(#{trajectoryid}, #{creatorid}, #{modifyid}, #{name}, #{machinetype}, #{image}, #{weight}, #{height}, #{point}, #{createdate}, #{updatedate})")
	int save(UserTrajectory userTrajectory);

	int count(@Param("params") Map<String, Object> params);

	List<UserTrajectory> apiList(@Param("params") UserTrajectory params);

	List<UserTrajectory> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
