package com.zw.admin.server.service;

public class CommonService {
	/**
	 * 审核批准
	 * 
	 * @param status 审核批准状态
	 * @param check  通过不通过
	 * @return
	 */
	public static Integer check(Integer status, Boolean check) {
		if (check) {
			if (status == 0 || status == 1) {// 待审核和审核未通过点同意都改成批准中
				status = 2;
			} else if (status == 2 || status == 3) {
				status = 4;
			}
		} else {
			if (status == 0) {
				status = 1;
			} else if (status == 2 || status == 4) {
				status = 3;
			}
		}
		return status;
	}
}
