package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.DateNumDto;
import com.zw.admin.server.model.DataCollectionBo;
import com.zw.admin.server.model.TrainDataSum;
import org.apache.ibatis.annotations.*;

import com.zw.admin.server.model.DataCollection;

@Mapper
public interface DataCollectionDao {

    @Select("select t.id from data_collection t where t.uid = #{uid} and t.eid=#{eid} and t.rid=#{rid} ")
    Long check(DataCollection dataCollection);

    List<DataCollection> listRF(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                  @Param("limit") Integer limit);

    @Select("select * from data_collection t where t.id = #{id}")
    DataCollection getById(Long id);

    @Delete("delete from data_collection where id = #{id}")
    int delete(Long id);

    int update(DataCollection dataCollection);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into data_collection(uid, eid, etype, rid, val, create_time, update_time,start_time,end_time,range_index) values(#{uid}, #{eid}, #{etype}, #{rid}, #{val}, now(), now(),#{startTime},#{endTime},#{rangeIndex})")
    int save(DataCollection dataCollection);

    int count(@Param("params") Map<String, Object> params);

    List<DataCollection> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                              @Param("limit") Integer limit);

    /**
     * 功能描述:设备类型下拉框
     *
     * @param params
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/6/19 17:01
     */
    List<String> getEtypes(Map<String,Object> params);

    /**
     * 功能描述:用户名下拉框
     *
     * @param params
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/6/19 17:01
     */
    List<String> getNames(Map<String,Object> params);

    /**
     * 功能描述:sn下拉框
     *
     * @param params
     * @return java.util.List<java.lang.String>
     * @author larry
     * @Date 2020/6/19 17:01
     */
    List<String> getEids(Map<String,Object> params);

    List<TrainDataSum> countEtype(@Param("params") Map<String, Object> params);

    /**
     * 功能描述:训练总次数
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/21 13:59
     */
    Long selectTrainNum();

    /**
     * 功能描述:
     *
     * @param
     * @return java.lang.Long
     * @author larry
     * @Date 2020/10/21 14:01
     */
    Long selectTrainTimeSum();

    /**
     * 功能描述:根据给定时间范围，查询每天的训练次数
     *
     * @param param
     * @return java.util.Map<java.lang.String, com.zw.admin.server.dto.DateNumDto>
     * @author larry
     * @Date 2020/10/21 17:39
     */
    @MapKey("date")
    Map<String, DateNumDto> selectTrainNumByDay(Map<String, Object> param);


}
