package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.SnDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.Sn;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@RequestMapping("/sns")
public class SnController {

	@Autowired
	private SnDao snDao;

	@PostMapping
	@ApiOperation(value = "保存")
	public Sn save(@RequestBody Sn sn) {
		snDao.save(sn);

		return sn;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Sn get(@PathVariable Long id) {
		return snDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public Sn update(@RequestBody Sn sn) {
		snDao.update(sn);

		return sn;
	}

	@PostMapping("/webList")
	@ApiOperation(value = "web列表")
	public ResultVO<?> getUserList(String params, Integer page, Integer num) {
		//参数封装到map
		Map<String, Object> tmap = new HashMap<>();
		try {
			Map<String, Object> map = JSONObject.parseObject(params, Map.class);
			tmap = map == null ? tmap : map;
			log.info("[方法]:[{}],[params]:[{}]","用户列表",params);
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}
		Integer total = snDao.count(tmap);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<Sn> list = snDao.webList(tmap, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return snDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Sn> list(PageTableRequest request) {
				return snDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		snDao.delete(id);
	}
}
