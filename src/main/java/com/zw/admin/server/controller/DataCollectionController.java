package com.zw.admin.server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.model.*;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.UserUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.DataCollectionDao;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@Api(tags = "数据收集")
@Slf4j
@RestController
@RequestMapping("/dataCollections")
public class DataCollectionController {

	@Autowired
	private DataCollectionDao dataCollectionDao;
	@Resource
	private UserDao userDao;

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public DataCollection get(@PathVariable Long id) {
		return dataCollectionDao.getById(id);
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(DataCollection dataCollection) {
		log.info("dataCollection保存:"+dataCollection.toString());
		//非空校验
		if (StringUtils.isBlank(dataCollection.getEid()) || StringUtils.isBlank(dataCollection.getUid()) || StringUtils.isBlank(dataCollection.getRid())) {
			//无效数据直接返回
			return R.error(3, "必填参数为空");
		}
		int i = 0;
		if (dataCollection.getId() != null && dataCollection.getId() != 0) {
			i = dataCollectionDao.update(dataCollection);
		} else {
			Long id = dataCollectionDao.check(dataCollection);
			if (id != null) {
				return R.error(2, "数据重复", id);
			}
			i = dataCollectionDao.save(dataCollection);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(dataCollection.getId());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(@RequestBody Map<String, Object> params) {
		//查询用户的角色
		User currentUser = UserUtil.getCurrentUser();
		String id = currentUser.getId();
		User byId = userDao.getById(id);
		Long identity = byId.getIdentity();
		//如果是医师查询属于自己患者的数据和自己的数据
		if(identity== Identity.THERAPIST.getIdentity()){
			//查询属于自己的患者id
			List<String> uids = userDao.selectChildrenUserId(id);
			if(CollectionUtils.isNotEmpty(uids)){
				uids.add(id);
			}else {
				uids = new ArrayList<String>();
				uids.add(id);
			}
			params.put("uids",uids);
		}
		//如果是患者查看自己的数据
		if(identity==Identity.PATIENT.getIdentity()){
			params.put("uid",id);
		}
		//if(identity==)
		Integer total = dataCollectionDao.count(params);
		Integer page = (Integer) params.get("page");
		Integer num = (Integer) params.get("num");
		PageObject page1 = PageUtils.pageClass(page, total, num);

		List<DataCollection> listRf = dataCollectionDao.listRF(params, page1.getStartIndex(), num);
		Map<Object, Object> map = new HashMap<>();
		map.put("list", listRf);
		map.put("total", total);
		return R.success(map);
	}

	/**
	 * 功能描述:训练数据汇总
	 *
	 * @param params
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/8/19 15:12
	 */
	/**
	 *
	 * @return
	 */
	@PostMapping("/selectTrainData")
	@ApiOperation(value = "训练数据汇总")
	public ResultVO<?> selectTrainData(@RequestBody Map<String, Object> params) {
		//训练数据的汇总
		List<TrainDataSum> trainDataSums = dataCollectionDao.countEtype(params);
		Map<Object, Object> map = new HashMap<>();
		map.put("trainData", trainDataSums);
		return R.success(map);
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return dataCollectionDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<DataCollection> list(PageTableRequest request) {
				return dataCollectionDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		dataCollectionDao.delete(id);
	}

	/**
	 * 功能描述:下拉框查询
	 *
	 * @param params
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/6/19 16:50
	 */
	@PostMapping("/selectComboBox")
	@ApiOperation(value = "下拉框信息")
	public ResultVO<?> selectComboBox(Map<String, Object> params) {
		User currentUser = UserUtil.getCurrentUser();
		String id = currentUser.getId();
		User byId = userDao.getById(id);
		Long identity = byId.getIdentity();
		//设备类型下拉
		List<String> etypes;
		//设备eids
		List<String> eids;
		List<String> names;
		//ids
		List<String> ids;
		//如果是医师查询属于自己患者的数据和自己的数据
		if(identity== Identity.THERAPIST.getIdentity()){
			//查询属于自己的患者id
			List<String> strings = userDao.selectChildrenUserId(id);
			strings.add(id);
			params.put("ids",strings);
		}else if(identity == Identity.PATIENT.getIdentity()){
			//患者
			ids = new ArrayList<String>();
			ids.add(id);
			params.put("ids",ids);
		}
		names = dataCollectionDao.getNames(params);
		eids = dataCollectionDao.getEids(params);
		etypes = dataCollectionDao.getEtypes(params);
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		map.put("names",names);
		map.put("etypes",etypes);
		map.put("eids",eids);
		return R.success(map);
	}
}
