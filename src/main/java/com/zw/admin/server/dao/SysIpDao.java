package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.SysIp;

@Mapper
public interface SysIpDao {

	@Select("select count(1) from sys_ip t where t.name = #{name} and t.ip=#{ip}")
	int check(SysIp sysIp);

	List<SysIp> listRf(@Param("id") Long id);

	@Select("select * from sys_ip t where t.id = #{id}")
	SysIp getById(Long id);

	@Delete("delete from sys_ip where id = #{id}")
	int delete(Long id);

	int update(SysIp sysIp);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_ip(name, ip,front_ip) values(#{name}, #{ip},#{frontIp})")
	int save(SysIp sysIp);

	int count(@Param("params") Map<String, Object> params);

	List<SysIp> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);
}
