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

import com.zw.admin.server.dao.SysIpDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.SysIp;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sysIps")
public class SysIpController {

	@Autowired
	private SysIpDao sysIpDao;

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(SysIp sysIp) {
		int i = 0;
		int b = sysIpDao.check(sysIp);
		if (b > 0) {
			return R.error(1, "服务器名或ip重复");
		}
		if (sysIp.getId() != null && sysIp.getId() != 0) {
			i = sysIpDao.update(sysIp);
		} else {
			i = sysIpDao.save(sysIp);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(sysIp);
	}

	/**
	 * 
	 * @param id 0查询所有
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(Long id) {
		List<SysIp> listRf = sysIpDao.listRf(id);
		return R.success(listRf);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> deleteRestful(Long id) {
		int i = sysIpDao.delete(id);
		if (i <= 0) {
			return R.error(1, "删除失败");
		}
		return R.success();
	}

	@PostMapping
	@ApiOperation(value = "保存")
	public SysIp save(@RequestBody SysIp sysIp) {
		sysIpDao.save(sysIp);

		return sysIp;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public SysIp get(@PathVariable Long id) {
		return sysIpDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public SysIp update(@RequestBody SysIp sysIp) {
		sysIpDao.update(sysIp);

		return sysIp;
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {

		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return sysIpDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<SysIp> list(PageTableRequest request) {
				return sysIpDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		sysIpDao.delete(id);
	}
}
