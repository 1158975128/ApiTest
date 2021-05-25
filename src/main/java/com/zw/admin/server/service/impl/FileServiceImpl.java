package com.zw.admin.server.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.zw.admin.server.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zw.admin.server.dao.FileInfoDao;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.service.FileService;
import com.zw.admin.server.utils.FileUtil;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Value("${files.path}")
	private String filesPath;

	@Value("${pic.path}")
	private String picPath;

	@Value("${bin.path}")
	private String binPath;

	@Value("${commonpic.path}")
	private String commonpicPath;

	@Autowired
	private FileInfoDao fileInfoDao;

	@Autowired
	private GameDao gameDao;

	@Override
	public void delete(String id) {
		FileInfo fileInfo = fileInfoDao.getById(id);
		if (fileInfo != null) {
			String fullPath = fileInfo.getPath();
			FileUtil.deleteFile(fullPath);

			fileInfoDao.delete(id);
			log.debug("删除文件：{}", fileInfo.getPath());
		}
	}

	@Override
	public ResultVO<?> savePic(MultipartFile file, String k) throws IOException {
		if (StringUtils.isEmpty(k)) {
			return R.error(1, "k不能为空");
		}
		String fileOrigName = file.getOriginalFilename();
		if (!fileOrigName.contains(".")) {
			return R.error(1, "缺少后缀名");
		}
		String path = commonpicPath + File.separator + k + File.separator + fileOrigName;
		String relativeFilePath = "/file" + File.separator + k + File.separator + fileOrigName;
		FileUtil.deletefile(path);
		FileUtil.saveFile(file, path);
		return R.success(relativeFilePath);
	}

	@Override
	public ResultVO<?> saveBin(MultipartFile file) throws IOException {
		String fileOrigName = file.getOriginalFilename();
		if (!fileOrigName.contains(".")) {
			return R.error(1, "缺少后缀名");
		}
		String path = binPath + File.separator + fileOrigName;

		File bins = new File(binPath);
		File[] listFiles = bins.listFiles();
		String string = "app_M2_V2.11.4.12.bin";
		if (fileOrigName.indexOf("app") != -1) { // 是app
			// 获取中间名
			String appmode = "";
			try {
				String[] split = fileOrigName.split("_");
				appmode = split[0] + "_" + split[1];
			} catch (Exception e) {
				return R.error(1, "文件名有误");
			}
			for (int i = 0; i < listFiles.length; i++) {// 遍历文件夹
				if (listFiles[i].getName().indexOf(appmode) != -1) {// 如果有一样
					FileUtil.deletefile(listFiles[i].getPath());// 删除
				}
			}
		} else if (fileOrigName.indexOf("iap") != -1) {
			for (int i = 0; i < listFiles.length; i++) {// 遍历文件夹
				if (listFiles[i].getName().indexOf("iap") != -1) {// 如果有一样
					FileUtil.deletefile(listFiles[i].getPath());// 删除
				}
			}
		}
		FileUtil.saveFile(file, path);
		return R.success();
	}

	@Override
	public ResultVO<?> save(MultipartFile file, String platform, String platformId, String type, String sn,String machineType)
			throws IOException {
		log.info("platform:"+platform+",platformId:"+platformId+",type:"+type+",machineType:"+machineType);
		FileInfo fileInfo = new FileInfo();
		String fileOrigName = file.getOriginalFilename();

		if (!fileOrigName.contains(".")) {
			return R.error(1, "缺少后缀名");
		}
		// 获取文件名
		String name = fileOrigName.substring(0, fileOrigName.lastIndexOf("."));
		if("platformbin".equalsIgnoreCase(name)){
			if ("".equals(platform) || file == null || StringUtils.isBlank(type)) {
				return R.error(1, "参数错误");
			}
		}else {
			if ("".equals(platform) || platformId == null || file == null || StringUtils.isBlank(type)) {
				return R.error(1, "参数错误");
			}
		}

		String userId = UserUtil.getCurrentUser().getId();
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("userId", userId);
		//判断登录的是否为管理员，如果为管理，设置用户id为空,可以查询所有
		List<Long> roleIds = UserUtil.getRoleIds();
		if (roleIds != null && roleIds.contains(Constant.ROOT)) {
			map.put("userId", null);
		}
		int count = gameDao.count(map);
		if (count <= 0) {
			return R.error(1, "您未上传此游戏");
		}

		//加上型号
		//fileInfo = fileInfoDao.getByName(name, platform, type);
		fileInfo.setName(name);
		fileInfo.setPlatform(platform);
		fileInfo.setType(type);
		fileInfo = fileInfoDao.getByName(fileInfo);

		String path = filesPath + File.separator + platform + File.separator + type + File.separator + name;
		String fullPath = filesPath + File.separator + platform + File.separator + type + File.separator + fileOrigName;// pathname;
		FileUtil.deletefile(path);
		FileUtil.saveFile1(file, fullPath, path);
		// long size = file.getSize();

		String versionPath = filesPath + File.separator + platform + File.separator + type + File.separator + name + File.separator + name
				+ File.separator;
		log.info("versionPath:"+versionPath);
		String version = FileUtil.getText(versionPath);
		if(StringUtils.isBlank(version)){
			return R.error(1,"version.txt文件为空");
		}
		String platfromBinVerPath ;
		String binVer =null;
		if("platformbin".equalsIgnoreCase(name)){
			//version的取值文件更改成Platform_Data/StreamingAssets/version.txt
			//platformbin为Platform_Data/StreamingAssets/version.txt
			platfromBinVerPath = versionPath + FileUtil.VERSIONFILEOTHER + FileUtil.BIN_FILE;
			binVer = FileUtil.getBinText(platfromBinVerPath);
			if(StringUtils.isBlank(binVer)){
				return R.error(1,"binversion.txt文件为空");
			}
		}


		String contentType = file.getContentType();
		if (fileInfo == null) {

			Long version1 = version == null ? 1L : Long.valueOf(version);
			fileInfo = new FileInfo();
			// fileInfo.setId(md5);
			if ("platformbin".equals(name) && StringUtils.isNotBlank(binVer)) {
				platformId = binVer;
			}
			fileInfo.setPlatformId(platformId);
			fileInfo.setContentType(contentType);
			fileInfo.setSize(0);
			fileInfo.setPath(path);
			fileInfo.setUrl(fullPath);
			fileInfo.setType(type);
			fileInfo.setSn(sn);
			fileInfo.setName(name);
			fileInfo.setVersion(version1);
			fileInfo.setPlatform(platform);
			fileInfoDao.save(fileInfo);
		} else {
			Long version1 = version == null ? fileInfo.getVersion() : Long.valueOf(version);
			if ("platformbin".equals(name)) {
				platformId = binVer;
			}
			fileInfo.setPlatformId(platformId);
			fileInfo.setContentType(contentType);
			fileInfo.setSize(0);
			fileInfo.setPath(path);
			fileInfo.setUrl(fullPath);
			fileInfo.setPlatform(platform);
			fileInfo.setType(type);
			fileInfo.setSn(sn);
			fileInfo.setVersion(version1);
			fileInfo.setUpdateTime(new Date());
			fileInfo.setStatus(0);
			fileInfoDao.update(fileInfo);
		}
		return R.success();
	}

	@Override
	public FileInfo commonSave(MultipartFile file, String name, Integer type) throws IOException {
		String fileOrigName = file.getOriginalFilename();
		if (!fileOrigName.contains(".")) {
			throw new IllegalArgumentException("缺少后缀名");
		}
		//20210320对上传的图片进行重命名uuid+后缀
		String newName = UUID.randomUUID().toString();
		String suffix = fileOrigName.substring(fileOrigName.lastIndexOf("."),fileOrigName.length());
		fileOrigName = newName+suffix;
		String typePath = type == 1 ? "/" : "/icon/";
		String path = picPath + typePath + name + "/" + fileOrigName;
		String delPath = picPath + typePath + name;
		String relativeFilePath = "/file" + typePath + name + "/" + fileOrigName;
		FileInfo fileInfo = new FileInfo();
		fileInfo.setCode(0);
		fileInfo.setPath(relativeFilePath);
		// FileUtil.deletefile(delPath);// 业务需求
		FileUtil.saveFile(file, path);
		return fileInfo;
	}

}
