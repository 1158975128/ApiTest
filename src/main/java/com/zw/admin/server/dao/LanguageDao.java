package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Language;

@Mapper
public interface LanguageDao {

	@Select("select * from language")
	List<Language> querylist();

	@Select("select * from language t where t.id = #{id}")
	Language getById(Long id);

	@Delete("delete from language where id = #{id}")
	int delete(Long id);

	int update(Language language);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into language(createTime, k, updateTime, val) values(NOW(), #{k}, NOW(), #{val})")
	int save(Language language);

	int count(@Param("params") Map<String, Object> params);

	List<Language> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
