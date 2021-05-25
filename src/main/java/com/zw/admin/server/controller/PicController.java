package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.PicDao;
import com.zw.admin.server.model.Dict;
import com.zw.admin.server.model.Pic;
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
@RequestMapping("/pics")
public class PicController {

	@Autowired
	private PicDao picDao;

	@PostMapping
	@ApiOperation(value = "保存")
	public Pic save(@RequestBody Pic pic) {
		picDao.save(pic);

		return pic;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Pic get(@PathVariable Long id) {
		return picDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public Pic update(@RequestBody Pic pic) throws Exception {
		pic.setVersion(pic.getVersion() + 1);
		picDao.update(pic);
		return pic;
	}

	@PostMapping("/webList")
	@ApiOperation(value = "web列表")
	public ResultVO<?> getUserList(Map<String, Object> params, Integer page, Integer num) {

		Integer total = picDao.count(params);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<Dict> list = picDao.webList(params, page1.getStartIndex(), num);
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
				return picDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Pic> list(PageTableRequest request) {
				return picDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		picDao.delete(id);
	}
}
