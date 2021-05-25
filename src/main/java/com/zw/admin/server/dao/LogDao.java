package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Log;

@Mapper
public interface LogDao {

	@Select("select * from log t where t.id = #{id}")
	Log getById(Long id);

	@Delete("delete from log where id = #{id}")
	int delete(Long id);

	int update(Log log);

	int save(Log log);

	int count(@Param("params") Map<String, Object> params);

	List<Log> webList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<Log> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
