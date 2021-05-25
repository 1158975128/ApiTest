package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Dbereportdata;

@Mapper
public interface DbereportdataDao {

	@Select("select * from dbereportdata t where t.id = #{id}")
	Dbereportdata getById(String id);

	@Delete("delete from dbereportdata where id = #{id}")
	int delete(String id);

	int update(Dbereportdata dbereportdata);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into dbereportdata(id,aromarea, aromfourpoints, arompoints, createtime, image, promarea, promfourpoints, prompoints, remark,  type, user_id, xaromvalue, xpromvalue, yaromvalue, ypromvalue) values(#{id},#{aromarea}, #{aromfourpoints}, #{arompoints}, #{createtime}, #{image}, #{promarea}, #{promfourpoints}, #{prompoints}, #{remark},  #{type}, #{userId}, #{xaromvalue}, #{xpromvalue}, #{yaromvalue}, #{ypromvalue})")
	int save(Dbereportdata dbereportdata);

	int count(@Param("params") Map<String, Object> params);

	List<Dbereportdata> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
