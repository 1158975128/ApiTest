package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.model.DeviceConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Device;

@Mapper
public interface DeviceDao {

	@Select("select t.id from device t where t.sn = #{sn} and t.os=#{os} and t.updatetime=#{updatetime}")
	Long check(Device device);

	List<Device> listRF(@Param("params") Device device);

	@Select("select * from device t where t.id = #{id}")
	Device getById(Long id);

	@Delete("delete from device where id = #{id}")
	int delete(Long id);

	int update(Device device);

	int save(Device device);

	int count(@Param("params") Map<String, Object> params);

	List<Device> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<Device> webList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
						 @Param("limit") Integer limit);

	DeviceConfig selectDeviceConfig(String sn);

	/**
	 * 功能描述:添加设备配置
	 *
	 * @param deviceConfig
	 * @return int
	 * @author larry
	 * @Date 2020/9/14 16:15
	 */
	int insertDeviceConfig(DeviceConfig deviceConfig);

	/**
	 * 功能描述:更新配置
	 *
	 * @param deviceConfig
	 * @return int
	 * @author larry
	 * @Date 2020/9/14 16:16
	 */
	int updateDeviceConfig(DeviceConfig deviceConfig);

	/**
	 * 功能描述:总设备数
	 *
	 * @param
	 * @return int
	 * @author larry
	 * @Date 2020/10/21 11:43
	 */
	Map<String,Long> selectDeviceNum(List<String> list);

	/**
	 * 功能描述:总场所数
	 *
	 * @param
	 * @return int
	 * @author larry
	 * @Date 2020/10/21 11:43
	 */
	Integer selectPlaceNum();

}
