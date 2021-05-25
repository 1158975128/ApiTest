package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.UserTrajectoryDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserTrajectory;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/userTrajectorys")
public class UserTrajectoryController {

	@Autowired
	private UserTrajectoryDao userTrajectoryDao;

	/**
	 * 
	 * @param trajectoryid
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(UserTrajectory userTrajectory) {
		List<UserTrajectory> listRf = userTrajectoryDao.apiList(userTrajectory);
		return R.success(listRf);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> save(UserTrajectory userTrajectory) {
		int i = 0;
		if (StringUtils.isBlank(userTrajectory.getTrajectoryid())) {
			return R.error(1, "trajectoryid为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("trajectoryid", userTrajectory.getTrajectoryid());
		int count = userTrajectoryDao.count(map);
		if (count > 0) {
			i = userTrajectoryDao.update(userTrajectory);
		} else {
			i = userTrajectoryDao.save(userTrajectory);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success();
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public UserTrajectory get(@PathVariable Long id) {
		return userTrajectoryDao.getById(id);
	}

//	@PutMapping
//	@ApiOperation(value = "修改")
//	public UserTrajectory update(@RequestBody UserTrajectory userTrajectory) {
//		userTrajectoryDao.update(userTrajectory);
//
//		return userTrajectory;
//	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return userTrajectoryDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<UserTrajectory> list(PageTableRequest request) {
				return userTrajectoryDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

//	@DeleteMapping("/{id}")
//	@ApiOperation(value = "删除")
//	public void delete(@PathVariable Long id) {
//		userTrajectoryDao.delete(id);
//	}
}
