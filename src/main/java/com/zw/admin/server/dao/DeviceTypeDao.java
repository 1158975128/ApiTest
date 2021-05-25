package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.DeviceType;

@Mapper
public interface DeviceTypeDao {

    @Select("select * from device_type t where t.id = #{id}")
    DeviceType getById(Long id);

    @Delete("delete from device_type where id = #{id}")
    int delete(Long id);

    int update(DeviceType deviceType);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into device_type(name, parent_id, create_time, update_time) values(#{name}, #{parentId}, #{createTime}, #{updateTime})")
    int save(DeviceType deviceType);
    
    int count(@Param("params") Map<String, Object> params);

    List<DeviceType> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
