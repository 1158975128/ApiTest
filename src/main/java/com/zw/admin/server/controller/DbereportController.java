package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.DbereportDao;
import com.zw.admin.server.model.Dbereport;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dbereports")
public class DbereportController {

	@Autowired
	private DbereportDao dbereportDao;

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> save(Dbereport dbereport) {

		Dbereport byId = dbereportDao.getById(dbereport.getId());
		if (byId != null) {
			return R.error(1, "id已存在");
		}
		int i = dbereportDao.save(dbereport);

		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(dbereport);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Dbereport get(@PathVariable String id) {
		return dbereportDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public Dbereport update(@RequestBody Dbereport dbereport) {
		dbereportDao.update(dbereport);

		return dbereport;
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return dbereportDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Dbereport> list(PageTableRequest request) {
				return dbereportDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> list(String params, Integer page, Integer num) {
		Map<String, Object> tmap = new HashMap<>();
		try {
			tmap = JSONObject.parseObject(params, Map.class);
			System.err.println(tmap);
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}

		Integer total = dbereportDao.count(tmap);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<String, Object> map = new HashMap<>();

		List<Dbereport> list = dbereportDao.list(tmap, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(list);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> delete(String id) {
		dbereportDao.delete(id);
		return R.success();
	}
}
