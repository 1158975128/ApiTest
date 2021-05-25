package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.DeviceTypeDao;
import com.zw.admin.server.model.DeviceType;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/deviceTypes")
public class DeviceTypeController {

	@Autowired
	private DeviceTypeDao deviceTypeDao;

	/**
	 * 列表
	 */
	@ApiOperation("DeviceType配置列表")
	@GetMapping("/list")
	public ResultVO<?> list(String params, Integer page, Integer num) {

		Map<String, Object> tmap = new HashMap<>();
		try {
			tmap = JSONObject.parseObject(params, Map.class);
			System.err.println(tmap);
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}

		Integer total = deviceTypeDao.count(tmap);
		PageObject page1 = PageUtils.pageClass(page, total, num);

		List<DeviceType> list = deviceTypeDao.list(tmap, page1.getStartIndex(), num);

		for (DeviceType mc : list) {
			DeviceType mc2 = deviceTypeDao.getById(mc.getParentId());
			if (mc2 != null) {
				mc.setParentName(mc2.getName());
			}
		}
		return R.success(list);
	}

	/**
	 * DeviceType父级列表
	 */
	@ApiOperation("DeviceType父级下拉")
	@GetMapping("/parentlist")
	public ResultVO<?> Parentlist(Integer parentId) {

		Map<String, Object> map = new HashMap<>();
		map.put("parent_id", parentId);
		List<DeviceType> list = deviceTypeDao.list(map, null, null);

		return R.success(list);
	}

	/**
	 * 信息
	 */
	@ApiOperation("DeviceType信息")
	@GetMapping("/{id}")
	public ResultVO<?> info(@PathVariable("id") Long id) {
		DeviceType deviceType = deviceTypeDao.getById(id);
		return R.success(deviceType);
	}

	/**
	 * 保存
	 */
	@ApiOperation("DeviceType保存")
	@PostMapping("/save")
	public ResultVO<?> save(DeviceType deviceType) {
		deviceTypeDao.save(deviceType);

		return R.success();
	}

	/**
	 * 修改
	 */
	@ApiOperation("DeviceType修改")
	@PostMapping("/update")
	public ResultVO<?> update(DeviceType deviceType) {

		deviceTypeDao.update(deviceType);
		return R.success();
	}

	/**
	 * 删除
	 */
	@ApiOperation("DeviceType删除")
	@PostMapping("/delete/{id}")
	public ResultVO<?> delete(@PathVariable("id") Long id) {
		DeviceType deviceType = deviceTypeDao.getById(id);
		if (deviceType.getParentId() == 0) {

			Map<String, Object> map = new HashMap<>();
			map.put("parent_id", id);
			List<DeviceType> list = deviceTypeDao.list(map, null, null);

			if (list.size() > 0) {
				return R.error(1, "请先删除子内容");
			}
		}
		deviceTypeDao.delete(id);

		return R.success();
	}

}
