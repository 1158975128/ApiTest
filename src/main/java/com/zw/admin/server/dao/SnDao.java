package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Sn;

@Mapper
public interface SnDao {

	@Select("select * from sn t where t.id = #{id}")
	Sn getById(Long id);

	@Delete("delete from sn where id = #{id}")
	int delete(Long id);

	int update(Sn sn);

	int save(Sn sn);

	int count(@Param("params") Map<String, Object> params);

	List<Sn> webList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<Sn> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
