package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Test;

@Mapper
public interface TestDao {

	@Select("select max(testId) from test")
	Long maxId();

	@Select("select * from test")
	List<Test> list1();

	@Select("select * from test t where t.id = #{id}")
	Test getById(Long id);

	@Delete("delete from test where id = #{id}")
	int delete(Long id);

	int update(Test test);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into test(username, score, time,testId) values(#{username}, #{score}, #{time},#{testId})")
	int save(Test test);

	int count(@Param("params") Map<String, Object> params);

	List<Test> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
