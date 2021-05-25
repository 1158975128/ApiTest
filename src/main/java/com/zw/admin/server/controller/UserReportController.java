package com.zw.admin.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.UserReportDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserReport;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/userReports")
public class UserReportController {
	@Autowired
	private UserReportDao userReportDao;

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public UserReport get(@PathVariable Long id) {
		return userReportDao.getById(id);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(UserReport userReport) {
		int i = 0;

		if (userReport.getId() != null && userReport.getId() != 0) {
			i = userReportDao.update(userReport);
		} else {
			Long id = userReportDao.check(userReport);
			if (id != null) {
				return R.error(2, "数据重复", id);
			}
			i = userReportDao.save(userReport);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(userReport.getId());
	}

	/**
	 * 
	 * @param id 0查询所有
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(UserReport userReport) {
		List<UserReport> listRf = userReportDao.listRF(userReport);
		return R.success(listRf);
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {
			@Override
			public int count(PageTableRequest request) {
				return userReportDao.count(request.getParams());
			}
		}, new ListHandler() {
			@Override
			public List<UserReport> list(PageTableRequest request) {
				return userReportDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> deleteRestful(Long id) {
		int i = userReportDao.delete(id);
		if (i <= 0) {
			return R.error(1, "删除失败");
		}
		return R.success();
	}

}