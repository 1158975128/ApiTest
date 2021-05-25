package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.XiaoShouYi;

/**
 *功能描述:销售易参数数据层
 * @author larry
 * @Date 2020/11/20 15:56
 */
@Mapper
public interface XiaoShouYiDao {
    @Select("select * from xiao_shou_yi t where t.id = #{id}")
    XiaoShouYi getById(Long id);

    @Delete("delete from xiao_shou_yi where id = #{id}")
    int delete(Long id);

    int update(XiaoShouYi xiaoShouYi);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into xiao_shou_yi(client_id, client_secret, redirect_uri, username, password, security_token, token_url, time_out, connect_name) values(#{clientId}, #{clientSecret}, #{redirectUri}, #{username}, #{password}, #{securityToken}, #{tokenUrl}, #{timeOut}, #{connectName})")
    int save(XiaoShouYi xiaoShouYi);

    List<XiaoShouYi> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from xiao_shou_yi limit 1")
    XiaoShouYi getOne();
}