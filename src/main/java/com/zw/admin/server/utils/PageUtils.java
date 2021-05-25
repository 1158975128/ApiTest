package com.zw.admin.server.utils;

public abstract class PageUtils {

	/**
	 * 
	 * @param pageCurrent 当前页
	 * @param totalCount  记录总条数
	 * @param pageSize    每页显示条数
	 * @return
	 */
	public static PageObject pageClass(Integer pageCurrent, Integer totalCount, Integer pageSize) {
		if (pageCurrent == null || pageCurrent <= 0) {
			pageCurrent = 1;
		}
		pageSize = pageSize != null ? pageSize : 0;
		// 计算每页开始的位置
		int startIndex = (pageCurrent - 1) * pageSize;
		// 计算获得总页数
		if (pageSize == 0) {
			pageSize = 10;
		}
		int pageCount = totalCount / pageSize;
		if (totalCount % pageSize != 0) {
			pageCount++;
		}
		PageObject page = new PageObject();
		page.setRowCount(totalCount);
		page.setPageSize(pageSize);
		page.setPageCount(pageCount);
		page.setStartIndex(startIndex);
		page.setPageCurrent(pageCurrent);
		return page;
	}
}
