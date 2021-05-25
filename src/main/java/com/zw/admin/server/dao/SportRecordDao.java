package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.SportRecord;

@Mapper
public interface SportRecordDao {

	@Select("select t.id from sport_record t where t.uid = #{uid} and t.eid=#{eid} and t.rid=#{rid}")
	Long check(SportRecord sportRecord);

	List<SportRecord> listRF(@Param("params") SportRecord sportRecord);

	@Select("select * from sport_record t where t.id = #{id}")
	SportRecord getById(Long id);

	@Delete("delete from sport_record where id = #{id}")
	int delete(Long id);

	int update(SportRecord sportRecord);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sport_record(uid, eid, rid, val) values(#{uid}, #{eid}, #{rid}, #{val})")
	int save(SportRecord sportRecord);

	int count(@Param("params") Map<String, Object> params);

	List<SportRecord> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}