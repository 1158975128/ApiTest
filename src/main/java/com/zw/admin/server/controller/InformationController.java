package com.zw.admin.server.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.InformationDao;
import com.zw.admin.server.model.Information;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.service.InformationService;
import com.zw.admin.server.utils.JDBCUtil;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "信息操作")
@RestController
@RequestMapping("/equipments")
public class InformationController {

	@Autowired
	private InformationDao equipmentDao;

	@Autowired
	private InformationService equipmentService;

	@PostMapping("/doMsg")
	@ApiOperation(value = "保存信息")
	// @RequiresPermissions("equipment:add")
	public ResultVO<?> saveMsg(String name, String value, String id) {

		value = "'" + value + "'";
		int i = 0;
		try {
			if ("0".equals(id)) {
				String create = JDBCUtil.create(name);
				if (!"".equals(create)) {
					return R.error(1, create);
				}
				i = equipmentDao.saveMsg(name, value);
			} else {
				// id = "'" + id + "'";
				i = equipmentDao.updateMsg(name, value, id);
			}
		} catch (Exception e) {
			return R.error(1, "请检查表名是否正确");
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success();
	}

	@PostMapping("/getMsg")
	@ApiOperation(value = "获取信息")
	// @RequiresPermissions("equipment:add")
	public ResultVO<?> getMsg(String name, String id) {
		// id = "'" + id + "'";
		@SuppressWarnings("rawtypes")
		List<Map> msg = null;
		try {
			msg = equipmentDao.getMsg(name, id);
		} catch (Exception e) {
			return R.error(1, "请检查表名是否正确");
		}
		if (msg == null) {
			return R.error(1, "无此id信息");
		}
		return R.success(msg);
	}

	@PostMapping("/deleteMsg")
	@ApiOperation(value = "删除信息")
	// @RequiresPermissions("equipment:add")
	public ResultVO<?> deleteMsg(String name, String id) {

		id = "'" + id + "'";
		int i = 0;
		try {
			i = equipmentDao.deleteMsg(name, id);
		} catch (Exception e) {
			return R.error(1, "请检查表名是否正确");
		}
		if (i <= 0) {
			return R.error(1, "无此id信息");
		}
		return R.success();
	}

//	@PostMapping("/add")
//	@ApiOperation(value = "保存")
//	// @RequiresPermissions("equipment:add")
//	public ResultVO save(Equipment equipment) {
//
//		equipmentDao.save(equipment);
//		return ResultUtil.success();
//	}

	@PostMapping("/getEquipment")
	@ApiOperation(value = "根据id获取")
	// @RequiresPermissions("equipment:query")
	public ResultVO<?> get(Long id) {
		Information equipment = equipmentDao.getById(id);
		return R.success(equipment);
	}

	@PostMapping("/update")
	@ApiOperation(value = "修改")
	// @RequiresPermissions("equipment:add")
	public ResultVO<?> update(Information equipment) {
		equipmentDao.update(equipment);
		return R.success();
	}

	@PostMapping("/equipmentList")
	@ApiOperation(value = "列表")
	// @RequiresPermissions("equipment:query")
	public ResultVO<?> list(String params, Integer page, Integer num) {
		@SuppressWarnings("unchecked")
		Map<String, Object> tmap = JSONObject.parseObject(params, Map.class);
		System.err.println(tmap);
		return equipmentService.list(tmap, page, num);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	// @RequiresPermissions("equipment:del")
	public ResultVO<?> delete(Long id) {
		equipmentDao.delete(id);
		return R.success();
	}
}
