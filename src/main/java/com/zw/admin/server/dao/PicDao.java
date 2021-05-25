package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Dict;
import com.zw.admin.server.model.Pic;

@Mapper
public interface PicDao {

	@Select("select * from pic t where t.id = #{id}")
	Pic getById(Long id);

	@Delete("delete from pic where id = #{id}")
	int delete(Long id);

	int update(Pic pic);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into pic(content,  k, pic) values(#{content}, #{k}, #{pic})")
	int save(Pic pic);

	int count(@Param("params") Map<String, Object> params);

	List<Dict> webList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<Pic> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
