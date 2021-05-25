package com.zw.admin.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.LanguageDao;
import com.zw.admin.server.model.Language;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/languages")
public class LanguageController {

	@Autowired
	private LanguageDao languageDao;

	@PostMapping("/list")
	@ApiOperation(value = "获取语言列表")
	public ResultVO<?> querylist() {
		return R.success(languageDao.querylist());
	}

	@PostMapping
	@ApiOperation(value = "保存")
	public Language save(@RequestBody Language language) {
		languageDao.save(language);

		return language;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Language get(@PathVariable Long id) {
		return languageDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public Language update(@RequestBody Language language) {
		languageDao.update(language);

		return language;
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return languageDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Language> list(PageTableRequest request) {
				return languageDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		languageDao.delete(id);
	}
}
