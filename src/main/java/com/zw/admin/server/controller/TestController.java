package com.zw.admin.server.controller;

import java.util.HashMap;
import java.util.Map;

import com.zw.admin.server.task.InitPlaceByXiaoShouYiTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.TestDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.Test;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tests")
public class TestController {

	@Autowired
	private TestDao testDao;
	@Resource
	InitPlaceByXiaoShouYiTask initPlaceByXiaoShouYiTask;

	@Autowired
	private UserService userService;

	@PostMapping("/doUser")
	@ApiOperation(value = "doUser")
	public void doUser() {

	}

	@PostMapping("/maxId")
	@ApiOperation(value = "最大id")
	public ResultVO<?> maxId() {
		Map<String, Long> hashMap = new HashMap<String, Long>();
		Long maxId = testDao.maxId();
		hashMap.put("testId", maxId);
		return R.success(hashMap);
	}

	//@LogAnnotation
	@PostMapping("/add")
	@ApiOperation(value = "测试保存")
	public ResultVO<?> add(String test) {

		// testDao.save(test);
		// String score = test.getScore();
		// map接收
		JSONObject jasonObject = JSONObject.parseObject(test);
		Map<?, ?> map = jasonObject;
		map.get("username");

		// 对象接收
		JSONObject jsonObject = JSONObject.parseObject(test);
		Test stu = JSONObject.toJavaObject(jsonObject, Test.class);
		stu.getUsername();
		return R.success();
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	public ResultVO<?> saveRestful(Test sysIp) {
		int i = 0;
//		int b = testDao.check(sysIp);
//		if (b > 0) {
//			return ResultUtil.error(1, "服务器名或ip重复");
//		}
		if (sysIp.getId() == null || sysIp.getId() == 0) {
			i = testDao.save(sysIp);
		} else {
			i = testDao.update(sysIp);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}
		return R.success(sysIp);
	}

	@PostMapping("/list")
	@ApiOperation(value = "测试查询")
	public ResultVO<?> listUsers() {
		return R.success(testDao.list1());
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> deleteRestful(Long id) {
		int i = testDao.delete(id);
		if (i <= 0) {
			return R.error(1, "删除失败");
		}
		return R.success();
	}

	@PostMapping("/testTask")
	@ApiOperation(value = "删除")
	public ResultVO<?> testTask(){
		initPlaceByXiaoShouYiTask.run();
		return R.success();
	}

}
