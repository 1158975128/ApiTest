package com.zw.admin.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zw.admin.server.dao.InformationDao;
import com.zw.admin.server.model.Information;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.service.InformationService;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;

@Service
public class InformationServiceImpl implements InformationService {

	@Autowired
	private InformationDao equipmentDao;

	@Override
	public ResultVO<?> list(Map<String, Object> params, Integer page, Integer num) {
		Integer total = equipmentDao.count(params);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<Information> list = equipmentDao.list(params, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}
}
