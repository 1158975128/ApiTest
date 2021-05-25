package com.zw.admin.server.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.model.DeviceConfig;
import com.zw.admin.server.model.Game;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zw.admin.server.dao.DeviceDao;
import com.zw.admin.server.model.Device;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/devices")
public class DeviceController {

	@Autowired
	private DeviceDao deviceDao;

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(Device device) {
		int i = 0;
		Long id = deviceDao.check(device);
		if (id != null) {
			return R.error(2, "数据重复", id);
		}
		i = deviceDao.save(device);
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(device.getId());
	}

	/*
	 * @PostMapping
	 * 
	 * @ApiOperation(value = "保存") public Device save(@RequestBody Device device) {
	 * deviceDao.save(device);
	 * 
	 * return device; }
	 */

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Device get(@PathVariable Long id) {
		return deviceDao.getById(id);
	}

	/**
	 * 
	 * @param id 0查询所有
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(Device device) {
		List<Device> listRf = deviceDao.listRF(device);
		return R.success(listRf);
	}

	/*
	 * @PutMapping
	 * 
	 * @ApiOperation(value = "修改") public Device update(@RequestBody Device device)
	 * { deviceDao.update(device);
	 * 
	 * return device; }
	 */

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return deviceDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Device> list(PageTableRequest request) {
				return deviceDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	/*
	 * @DeleteMapping("/{id}")
	 * 
	 * @ApiOperation(value = "删除") public void delete(@PathVariable Long id) {
	 * deviceDao.delete(id); }
	 */
	@GetMapping("/webList")
	@ApiOperation(value = "设备列表")
	public ResultVO<?> list(String param, Integer page, Integer num) {
		Map<String, Object> params = new HashMap<>();
		try {
			Map<String, Object> map = JSONObject.parseObject(param, Map.class);
			params = map == null ? params : map;
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}
		int total = deviceDao.count(params);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		//返回信息
		Map<Object, Object> map = new HashMap<>();
		List<Device> list = deviceDao.webList(params, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	/**
	 * 功能描述:修改(添加)设备配置
	 *
	 * @param deviceConfig
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/9/14 11:13
	 */
	@LogAnnotation(module = "修改(添加)设备配置")
	@PostMapping("/saveOrUpdateConfig")
	@ApiOperation(value = "修改(添加)设备配置")
	public ResultVO<?> configDevice(@RequestBody DeviceConfig deviceConfig) {
		//先查询是否有配置，如果有配置则修改配置，没有配置新增配置
		DeviceConfig config = deviceDao.selectDeviceConfig(deviceConfig.getSn());
		if (config == null) {
			//添加
			deviceConfig.setCreateTime(new Date());
			deviceDao.insertDeviceConfig(deviceConfig);
		} else {
			//更新
			deviceDao.updateDeviceConfig(deviceConfig);
		}
		return R.success();
	}
}
