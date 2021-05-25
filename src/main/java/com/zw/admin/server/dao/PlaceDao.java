package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.Place;

/**
 *功能描述:场所数据层
 * @author larry
 * @Date 2020/11/5 16:11
 */
@Mapper
public interface PlaceDao {

    @Select("select * from place t where t.id = #{id}")
    Place getById(Long id);

    @Select("select * from place t where t.place = #{place} limit 1")
    Place getByPlace(String place);

    int update(Place place);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into place(place, create_time, update_time, create_user, update_user) values(#{place}, now(), #{updateTime}, #{createUser}, #{updateUser})")
    int save(Place place);

    int count(@Param("params") Map<String, Object> params);

    List<Place> selectPlacePage(Place place);

    List<Map<Long,String>> selectPlaces();

    /**
     * 功能描述:检查名称是否用重复
     *
     * @param place
     * @return com.zw.admin.server.model.Place
     * @author larry
     * @Date 2020/11/10 15:29
     */
    @Select("select id,place from place where id != #{id} and place = #{place} limit 1")
    Place checkPlaceDouble(Place place);
}