package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.Place;
import com.zw.admin.server.model.UserTrainLogBo;
import org.apache.ibatis.annotations.*;

import com.zw.admin.server.model.User;

@Mapper
public interface UserDao {

	int save(User user);

	int merge(User user);

	@Select("select * from user t where t.id = #{id}")
	User getById(String id);

	//@Select("select * from user t where phone = #{phone} and machine_id=#{machineId} limit 1")
	User getByTemporary(User u);

	@Select("select * from user t where phone = #{phone} and (is_temporary = '0' or is_temporary='' or is_temporary is null) and check_column in (2,3) limit 1")
	User getUserByPhone(String phone);

	@Select("select * from user t where phone = #{phone} and is_checked =1 and check_column in (2,3) limit 1")
	User getUserByPhoneOnline(String phone);

	@Select("select * from user t where email = #{email} and is_checked =1 and check_column in (1,3) limit 1")
	User getUserByEmailOnline(String email);

	@Select("select * from user t where email = #{email} and (is_temporary = '0' or is_temporary='' or is_temporary is null) and check_column in (1,3) limit 1")
	User getUserByEmail(String email);

	@Select("select * from user t where username = #{username}")
	User getUserByUsername(String username);

	@Select("select * from user t where phone = #{username} or email=#{username} or username=#{username}")
	User checkUser(String username);

//	@Select("select * from user t where CONCAT(t.first_name,t.last_name) = #{username}")
//	User getUser(String username);

	@Update("update user t set t.password = #{password} where t.id = #{id}")
	int changePassword(@Param("id") String id, @Param("password") String password);

	Integer count(@Param("params") Map<String, Object> params);

	List<User> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<User> adminList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	@Delete("delete from sys_role_user where userId = #{userId}")
	int deleteUserRole(String userId);

	@Delete("delete from sys_role_user where userId = #{userId} and roleId = 3")
	int deleteUserDev(String userId);

	int saveUserRoles(@Param("userId") String userId, @Param("roleIds") List<Long> roleIds);

	int saveUserRole(@Param("userId") String userId, @Param("roleId") Long roleId);

	int update(User user);

	int updateByTemporary(User user);

	@Delete("delete from user where id = #{id}")
	int deleteUser(String phone);

	List<Map<String, Object>> getRecentLogin();

	List<User> getUserByEmailPhoneAndId(User user);

	@Select("select * from user")
	List<User> getAllUser();

	/**
	 * 功能描述:查询所有的下属患者
	 *
	 * @param id
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/9/15 10:52
	 */
	List<String> selectChildrenUserId(String id);

	/**
	 * 功能描述:查询日新增患者数据
	 *
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/9/15 10:52
	 */
	List<DateNumDto> selectIncreaseNum(Map<String,Object> param);

	/**
	 * 功能描述:查询日新增患者数据
	 *
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/9/15 10:52
	 */
	@MapKey("date")
	Map<String,DateNumDto> selectIncreaseNumByDay(Map<String,Object> param);

	/**
	 * 功能描述:总患者数量
	 *
	 * @param
	 * @return java.lang.Long
	 * @author larry
	 * @Date 2020/10/21 15:01
	 */
	Long selectUserNum();

	/**
	 * 功能描述:查询用户新增
	 *
	 * @param params
	 * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.DateNumDto>
	 * @author larry
	 * @Date 2020/10/31 16:39
	 */
	@MapKey("date")
	Map<String, DateNumDto> selectIncreaseUser(Map<String, Object> params);

	/**
	 * 功能描述:康复患者数，本周，本月新增数
	 *
	 * @param params
	 * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.DateNumDto>
	 * @author larry
	 * @Date 2020/11/04 16:39
	 */
	Long selectTotalRecoveredNum(Map<String, Object> params);

	/**
	 * 功能描述:用户列表查询
	 *
	 * @param user
	 * @return java.util.List<com.zw.admin.server.model.User>
	 * @author larry
	 * @Date 2020/11/5 19:16
	 */
	List<User> selectUserList(UserDto user);

	/**
	 * 功能描述:根据用户id查询角色id
	 *
	 * @param userId
	 * @return java.util.List<com.zw.admin.server.model.User>
	 * @author larry
	 * @Date 2020/11/5 19:16
	 */
	List<Long> selectRoleIdByUserId(String userId);

	/**
	 * 功能描述:根据用户id查询已经配置好的场所
	 *
	 * @param userId
	 * @return java.util.List<com.zw.admin.server.model.Place>
	 * @author larry
	 * @Date 2020/11/9 16:02
	 */
	List<Place> selectUserPlaceByUserId(String userId);

	@Select("select * from user t where t.id = #{id}")
	UserDto selectUserDetail(String id);

	/**
	 * 功能描述:
	 *
	 * @param userTrainLogBo
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/11/14 18:02
	 */
	List<String> selectUserNameByTrainLog(UserTrainLogBo userTrainLogBo);

	/**
	 * 功能描述:
	 *
	 * @param id
	 * @return java.lang.String
	 * @author larry
	 * @Date 2020/11/16 11:02
	 */
	@Select("select name from user where id = #{id}")
	String selectUserNameById(String id);

	/**
	 *功能描述:查询在线用户
	 * @author larry
	 * @Date 2020/12/7 18:04
	 * @param userDto
	 * @return com.zw.admin.server.model.User
	 */
	User selectUserOnline(UserDto userDto);

	/**
	 * 功能描述:根据openid获取用户
	 *
	 * @param openid
	 * @return com.zw.admin.server.model.User
	 * @author larry
	 * @Date 2021/3/24 13:48
	 */
	@Select("select * from user where openid = #{openid} limit 1")
	User getByOpenid(String openid);
}
