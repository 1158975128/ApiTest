package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Information;

@Mapper
public interface InformationDao {

	int saveMsg(@Param("tableName") String tableName, @Param("value") String value);

	int updateMsg(@Param("tableName") String tableName, @Param("value") String value, @Param("id") String id);

	int deleteMsg(@Param("tableName") String tableName, @Param("id") String id);

	@SuppressWarnings("rawtypes")
	List<Map> getMsg(@Param("tableName") String tableName, @Param("id") String id);

	@Select("select * from equipment t where t.id = #{id}")
	Information getById(Long id);

	@Delete("delete from equipment where id = #{id}")
	int delete(Long id);

	int update(Information equipment);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into equipment(name, value , create_time, update_time) values(#{name}, #{value}, NOW(), NOW())")
	int save(Information equipment);

	int count(@Param("params") Map<String, Object> params);

	List<Information> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
