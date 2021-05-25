package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import com.zw.admin.server.model.MachineLog;

@Mapper
public interface MachineLogDao {


    int update(MachineLog machineLog);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("replace into machine_log(log_id, create_time, update_time, machine_id, data, mmu_app_ver, mmu_iap_ver, rups_ver) values(#{logId}, now(), #{updateTime}, #{machineId}, #{data}, #{mmuAppVer}, #{mmuIapVer}, #{rupsVer})")
    int save(MachineLog machineLog);

    int count(@Param("params") Map<String, Object> params);

    List<MachineLog> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}