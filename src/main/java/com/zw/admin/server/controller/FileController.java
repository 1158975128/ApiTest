package com.zw.admin.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.FileInfoDao;
import com.zw.admin.server.dto.LayuiFile;
import com.zw.admin.server.dto.LayuiFile.LayuiFileData;
import com.zw.admin.server.enums.FileStatusEnum;
import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.service.CommonService;
import com.zw.admin.server.service.FileService;
import com.zw.admin.server.utils.PageObject;
import com.zw.admin.server.utils.PageUtils;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "文件")
@RestController
@RequestMapping("/files")
public class FileController {

	@Value("${bin.path}")
	private String binPath;

	@Autowired
	private FileService fileService;
	@Autowired
	private FileInfoDao fileInfoDao;

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public FileInfo get(@PathVariable String id) {
		return fileInfoDao.getById(id);
	}

	@PostMapping("/checkFile")
	@ApiOperation(value = "审核批准游戏文件")
	public void checkFile(Long id, Integer status, Boolean check) {
		Integer status1 = CommonService.check(status, check);
		FileInfo fileInfo = new FileInfo();
		fileInfo.setId(id);
		fileInfo.setStatus(status1);
		fileInfoDao.update(fileInfo);
	}

	@PostMapping("/getPlatformId")
	@ApiOperation(value = "getPlatformId")
	public ResultVO<?> getPlatformId(String platform,String type) {

		FileInfo fileInfo = new FileInfo();
		fileInfo.setName("platformbin");
		fileInfo.setPlatform(platform);
		fileInfo.setType(type);
		if(StringUtils.isBlank(type)){
			return R.success(null);
		}
		//添加机型参数
		FileInfo byName = fileInfoDao.getByName(fileInfo);
		Long version = 1L;
		if (byName != null) {
			version = byName.getVersion();
		}else {
			version = null;
		}
		return R.success(version);
	}

	@PostMapping("/webList")
	@ApiOperation(value = "文件列表")
	public ResultVO<?> getUserList(@RequestParam Map<String, Object> params) {

		//userType给不同的页面接口使用，1为给develop页面，2，为给v4页面
		String userType = (String) params.get("userType");
		Integer page = Integer.valueOf((String) params.get("page"));
		Integer num = Integer.valueOf((String) params.get("num"));
		String type = (String) params.get("type");

		String userId = "0";
		if ("1".equals(userType)) {
			userId = UserUtil.getCurrentUser().getId();
		}
		Integer total = fileInfoDao.userCount(params, userId);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();
		FileInfo file = new FileInfo();
		file.setName("platformbin");
		file.setPlatform((String) params.get("platform"));
		file.setType(type);
		FileInfo byName = fileInfoDao.getByName(file);
		List<FileInfo> list = fileInfoDao.userList(params, page1.getStartIndex(), num, userId);
		//平台版本号为主工程版本号
		if(!CollectionUtils.isEmpty(list) && byName !=null){
			for(FileInfo f :list){
				f.setPlatformId(byName.getVersion()+"");
			}
		}
		//获取主工程的版本
		if (list != null && list.size() == 0 && "1".equals(userType)) {
			//如果file数据库中查不到，默认查询的是主工程version
			params.put("name", "platformbin");
			params.put("id", "");
			List<FileInfo> fileInfos = fileInfoDao.userList(params, page1.getStartIndex(), num, userId);
			if (fileInfos != null && fileInfos.size() > 0) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setPlatformId(fileInfos.get(0).getPlatformId());
				//如果没有指定机型，显示为空
				if(StringUtils.isBlank(type)){
					fileInfo.setPlatformId(null);
				}

				list.add(fileInfo);
			}
		}
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	/**
	 * 游戏历史文件列表
	 * @param params 必须入参: 游戏id
	 * @return
	 */
	@PostMapping("/fileHistoryList")
	@ApiOperation(value = "文件历史列表")
	public ResultVO<?> getFileHistoryList(@RequestParam Map<String, Object> params) {
		if ("1".equals(params.get("userType").toString())) {
			Optional.ofNullable(UserUtil.getCurrentUser()).ifPresent(a -> {
				params.put("userId", a.getId());
				// ROLE_ID为4的是管理员，文件历史列表只有超级管理员角色才可以看到
				Optional.ofNullable(a.getRoleIds())
						.ifPresent(b -> {
							if (b.contains(4l)) {
								params.put("role", "ROOT");
							}
						});
			});
		}

		PageHelper.startPage(Integer.valueOf(params.get("page").toString()), Integer.valueOf(params.get("num").toString()));
		List<FileInfo> fileInfos = fileInfoDao.fileListWithoutPagination(params);
		PageInfo pageInfo = PageInfo.of(fileInfos);
		return R.success(ImmutableMap.of("list", fileInfos,
				"page", ImmutableMap.of("total", pageInfo.getTotal(),
											"page", pageInfo.getPageNum(),
											"num", pageInfo.getPageSize())));
	}

	@GetMapping("/userList")
	@ApiOperation(value = "当前用户文件查询")
	// @RequiresPermissions("sys:file:query")
	public PageTableResponse userListFiles(PageTableRequest request) {
		String userId = UserUtil.getCurrentUser().getId();
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return fileInfoDao.userCount(request.getParams(), userId);
			}
		}, new ListHandler() {

			@Override
			public List<FileInfo> list(PageTableRequest request) {
				List<FileInfo> list = fileInfoDao.userList(request.getParams(), request.getOffset(), request.getLimit(),
						userId);
				return list;
			}
		}).handle(request);
	}

	@LogAnnotation
	@PostMapping("/getVersion")
	@ApiOperation(value = "获取版本号")
	public ResultVO<?> getVersion(String name, String platform,String type) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		JSONArray array;
		if (name.contains("[")) {
			array = JSONObject.parseArray(name);
		} else {
			String[] split = name.split(",");
			List<String> asList = Arrays.asList(split);
			array = JSONObject.parseArray(JSON.toJSONString(asList));
		}

		for (int i = 0; i < array.size(); i++) {
			String str = String.valueOf(array.get(i));
			FileInfo fileInfo = new FileInfo();
			fileInfo.setName(str);
			fileInfo.setPlatform(platform);
			fileInfo.setType(type);
			FileInfo byName = fileInfoDao.getByName(fileInfo);
			if (byName != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", str);
				map.put("version", byName.getVersion());
				map.put("platformId", byName.getPlatformId());
				map.put("type", byName.getType());
				map.put("sn", byName.getSn());
				list.add(map);
			}
		}
		return R.success(list);
	}

	@LogAnnotation
	@PostMapping("/uploadBin")
	@ApiOperation(value = "bin上传")
	public ResultVO<?> uploadBin(MultipartFile file) throws IOException {
		return fileService.saveBin(file);
	}

	// @LogAnnotation
	@PostMapping("/uploadPic")
	@ApiOperation(value = "普通图片上传")
	public ResultVO<?> uploadcommonPic(MultipartFile file, String k) throws IOException {
		return fileService.savePic(file, k);
	}

	// @LogAnnotation
	@PostMapping
	@ApiOperation(value = "游戏文件上传")
	public ResultVO<?> uploadFile(MultipartFile file, String platform, String platformId, String type, String sn)
			throws IOException {
		/*if ("".equals(platform) || platformId == null || file == null || StringUtils.isBlank(type)) {
			return R.error(1, "参数错误");
		}*/
		return fileService.save(file, platform, platformId, type, sn,type);
	}

	// @LogAnnotation
	@PostMapping("/common")
	@ApiOperation(value = "普通文件上传")
	public FileInfo commonSave(MultipartFile file, String name, Integer type) throws IOException {
		FileInfo fileInfo = new FileInfo();
		if (StringUtils.isEmpty(name)) {
			fileInfo.setCode(1);
			return fileInfo;
		}

		return fileService.commonSave(file, name, type);
	}

	/**
	 * layui富文本文件自定义上传
	 *
	 * @param file
	 * @param domain
	 * @return
	 * @throws IOException
	 */
	// @LogAnnotation
	@PostMapping("/layui")
	@ApiOperation(value = "layui富文本文件自定义上传")
	public LayuiFile uploadLayuiFile(MultipartFile file, String domain) throws IOException {
		// FileInfo fileInfo = fileService.save(file);

		LayuiFile layuiFile = new LayuiFile();
		layuiFile.setCode(0);
		LayuiFileData data = new LayuiFileData();
		layuiFile.setData(data);
		// data.setSrc(domain + "/files" + fileInfo.getUrl());
		data.setTitle(file.getOriginalFilename());

		return layuiFile;
	}

	@GetMapping
	@ApiOperation(value = "文件查询")
	// @RequiresPermissions("sys:file:query")
	public PageTableResponse listFiles(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return fileInfoDao.userCount(request.getParams(), "0");// 0查所有
			}
		}, new ListHandler() {

			@Override
			public List<FileInfo> list(PageTableRequest request) {
				List<FileInfo> list = fileInfoDao.userList(request.getParams(), request.getOffset(), request.getLimit(),
						"0");
				return list;
			}
		}).handle(request);
	}

	// @LogAnnotation
	@PostMapping("/getList")
	@ApiOperation(value = "查询所有file")
	public ResultVO<?> getList() {
		return R.success(fileInfoDao.getList());
	}

	// @LogAnnotation
	@DeleteMapping("/{id}")
	@ApiOperation(value = "文件删除")
	// @RequiresPermissions("sys:file:del")
	public void delete(@PathVariable String id) {
		fileService.delete(id);
	}

}
