package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.FileInfo;

@Mapper
public interface FileInfoDao {

	@Select("select * from file_info order by platform asc,updateTime desc")
	List<FileInfo> getList();

	@Select("select * from file_info t where t.id = #{id}")
	FileInfo getById(String id);

/*	@Select("select * from file_info t where t.name = #{name} and t.platform=#{platform}")
	FileInfo getByName(String name, String platform);*/

	//@Select("select * from file_info t where t.name = #{name} and t.platform=#{platform} and t.type=#{type}")
	//FileInfo getByName(String name, String platform,String type);

	FileInfo getByName(FileInfo fileInfo);

	@Insert("insert into file_info(id, contentType, size, path, url, type,sn, createTime, updateTime,name,version,platform,platform_id) values(#{id}, #{contentType}, #{size}, #{path}, #{url}, #{type}, #{sn}, now(), now(),#{name},#{version},#{platform},#{platformId})")
	int save(FileInfo fileInfo);

	int update(FileInfo fileInfo);

	@Delete("delete from file_info where id = #{id}")
	int delete(String id);

	@Delete("delete from file_info where name = #{name}")
	int deleteByName(String name);

	int count(@Param("params") Map<String, Object> params);

	List<FileInfo> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	int userCount(@Param("params") Map<String, Object> params, @Param("userId") String userId);

	List<FileInfo> userList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("userId") String userId);

	/**
	 * 获取游戏文件（不包含分页信息，需分页可配合pageHelper使用）
	 * @param params id:游戏ID、platform:平台、name:游戏名、userId:用户id、type:机型、userType:1为给develop页面，2为给v4页面
	 * @return 历史文件列表
	 */
	List<FileInfo> fileListWithoutPagination(@Param("params") Map<String, Object> params);
}
