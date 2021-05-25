package com.zw.admin.server.service;

import com.zw.admin.server.model.SysLogs;

/**
 * 日志service
 * 
 * @author THF
 *
 *         2017年8月19日
 */
public interface SysLogService {

	void save(SysLogs sysLogs);

	void save(String userId, String module, Boolean flag, String remark);

	void deleteLogs();
}
