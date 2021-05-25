package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.UserReport;

@Mapper
public interface UserReportDao {

	@Select("select t.id from user_report t where t.uid = #{uid} and t.eid=#{eid} and t.rid=#{rid}")
	Long check(UserReport userReport);

	List<UserReport> listRF(@Param("params") UserReport userReport);

	@Select("select * from user_report t where t.id = #{id}")
	UserReport getById(Long id);

	@Delete("delete from user_report where id = #{id}")
	int delete(Long id);

	int update(UserReport userReport);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into user_report(uid, eid, rid, val) values(#{uid}, #{eid}, #{rid}, #{val})")
	int save(UserReport userReport);

	int count(@Param("params") Map<String, Object> params);

	List<UserReport> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}