package com.zw.admin.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.SportRecordDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.SportRecord;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sportRecords")
public class SportRecordController {
	@Autowired
	private SportRecordDao sportRecordDao;

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public SportRecord get(@PathVariable Long id) {
		return sportRecordDao.getById(id);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(SportRecord sportRecord) {
		int i = 0;

		if (sportRecord.getId() != null && sportRecord.getId() != 0) {
			i = sportRecordDao.update(sportRecord);
		} else {
			Long id = sportRecordDao.check(sportRecord);
			if (id != null) {
				return R.error(2, "数据重复", id);
			}
			i = sportRecordDao.save(sportRecord);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(sportRecord.getId());
	}

	/**
	 * 
	 * @param id 0查询所有
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(SportRecord sportRecord) {
		List<SportRecord> listRf = sportRecordDao.listRF(sportRecord);
		return R.success(listRf);
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {
			@Override
			public int count(PageTableRequest request) {
				return sportRecordDao.count(request.getParams());
			}
		}, new ListHandler() {
			@Override
			public List<SportRecord> list(PageTableRequest request) {
				return sportRecordDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> deleteRestful(Long id) {
		int i = sportRecordDao.delete(id);
		if (i <= 0) {
			return R.error(1, "删除失败");
		}
		return R.success();
	}

}