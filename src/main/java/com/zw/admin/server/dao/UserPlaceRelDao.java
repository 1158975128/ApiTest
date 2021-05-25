package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.UserPlaceDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserPlaceRel;

/**
 *功能描述:用户与场所关系数据层
 * @author larry
 * @Date 2020/10/26 17:53
 */
@Mapper
public interface UserPlaceRelDao {

    int update(UserPlaceRel userPlaceRel);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user_place_rel(user_id, place) values(#{userId}, #{place})")
    int save(UserPlaceRel userPlaceRel);

    Integer deleteByUserId(Long userId);

    /**
     * 功能描述:根据用户id查询场所
     *
     * @param userId
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/10/29 15:39
     */
    List<String> selectMachineIdByUserId(String userId);

    /**
     * 功能描述:批量添加场所和用户关系表
     *
     * @param userPlaceDto
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/11/5 11:44
     */
    Integer saveUserPlaceRelBatch(UserPlaceDto userPlaceDto);

    /**
     * 功能描述:批量添加场所和用户关系表
     *
     * @param userId
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/11/5 11:44
     */
    List<Map<Long,String>> selectPlaceByUserId(@Param("userId") String userId);

    /**
     * 功能描述:根据用户id删除
     *
     * @param userPlaceDto
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/11/5 15:26
     */
    Integer removeRelByUserId(UserPlaceDto userPlaceDto);

}