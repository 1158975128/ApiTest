package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Dbereport;

@Mapper
public interface DbereportDao {

	@Select("select * from dbereport t where t.id = #{id}")
	Dbereport getById(String string);

	@Delete("delete from dbereport where id = #{id}")
	int delete(String id);

	int update(Dbereport dbereport);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into dbereport(id,content, date, image, mode, result, time, user_id) values(#{id},#{content}, #{date}, #{image}, #{mode}, #{result}, #{time}, #{userId})")
	int save(Dbereport dbereport);

	int count(@Param("params") Map<String, Object> params);

	List<Dbereport> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
