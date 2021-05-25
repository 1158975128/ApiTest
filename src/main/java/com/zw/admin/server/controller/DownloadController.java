package com.zw.admin.server.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zw.admin.server.dao.FileInfoDao;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.Game;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.FileUtil;
import com.zw.admin.server.utils.R;

import io.swagger.annotations.Api;

@Api(tags = "下载")
@RestController
@RequestMapping
public class DownloadController {

	// 模块路径前缀
	@Value("${files.path}")
	private String filesPath;

	// 游戏路径前缀
	@Value("${pic.path}")
	private String picPath;

	// 用户头像前缀
	@Value("${headImage.path}")
	private String headImage;

	// zip包路径
	@Value("${zip.path}")
	private String zip;

	// bin包路径
	@Value("${bin.path}")
	private String bin;

	@Autowired
	private GameDao gameDao;

	@Autowired
	private FileInfoDao fileInfoDao;

	/**
	 * 下载bin
	 * 
	 * @throws IOException
	 */
	@PostMapping("/download/bin")
	private ResultVO<?> downloadBin(String mode) throws IOException {

		List<Object> list = new ArrayList<>();
		final Base64.Encoder encoder = Base64.getEncoder();
		File f = new File(bin);

		if (!f.exists()) {
			return R.error(1, "File not found!");
		}
		File[] fs = f.listFiles();
//		if (fs.length != 2) {
//			return R.error(1, "File not found!");
//		}
		String name = "";
		for (int i = 0; i < fs.length; i++) {

			Map<String, Object> map = new HashMap<>();
			String file = fs[i].getName().substring(0, fs[i].getName().lastIndexOf("."));
			String version = "";
			String modeStr = "";
			String[] split = file.split("_");

			try {
				if (file.contains("app")) {
					name = "app";
					modeStr = split[1];
					if (StringUtils.isNotBlank(mode) && !modeStr.equals(mode)) {
						continue;
					}
					version = split[2];
				} else {
					name = "iap";
					version = split[1];
				}
			} catch (Exception e) {
				return R.error(1, "文件名有误");
			}
			byte[] bin = FileUtil.getContent(fs[i].getPath());
			int length = bin.length;
			System.err.println(length);
			String encodeToString = encoder.encodeToString(bin);

			map.put("name", name);
			map.put("mode", modeStr);
			map.put("bin", encodeToString);
			map.put("version", version);
			list.add(map);
		}

		return R.success(list);
	}

	/**
	 * 模板文件下载校验
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@PostMapping("/download/checkDownloadZip")
	public ResultVO<?> checkDownloadZip(HttpServletRequest request, HttpServletResponse response, String id)
			throws Exception {
		FileInfo fileInfo = fileInfoDao.getById(id);
		String filePath = fileInfo.getUrl();
		File f = new File(filePath);
		if (!f.exists()) {
			return R.error(1, "文件不存在");
		}

		return R.success();
	}

	/**
	 * 模板文件打包下载
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/download/downloadZip")
	public void downloadZip(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		FileInfo fileInfo = fileInfoDao.getById(id);
		download(request, response, fileInfo.getUrl());
	}

	/**
	 * 模块文件下载
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 得到要下载的文件�?
		String path = request.getParameter("path");
		String filePath = filesPath + File.separator + path;
		download(request, response, filePath);
	}

	/**
	 * 游戏图片下载
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/downloadPic")
	public void downloadPic(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 得到要下载的文件�?
		String name = request.getParameter("name");
		Game game = gameDao.getByName(name);
		if (game == null) {
			response.sendError(404, "Game not found!");
			return;
		}
		String pic = game.getPic();
		String picName = pic.substring(pic.lastIndexOf("/") + 1);
		String filePath = picPath + File.separator + name + "/" + picName;
		download(request, response, filePath);
	}

	/**
	 * 头像包下载
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/downloadHeadImage")
	public void downloadHeadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		File sourceDir = new File(headImage);
		File zipFile = new File(headImage + ".zip");
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			compress(sourceDir, zip, zos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (zos != null)
				try {
					zos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		download(request, response, zipFile.getPath());

	}

	/**
	 * 下载
	 * 
	 * @param request
	 * @param response
	 * @param filePath 文件绝对路径
	 * @throws IOException
	 */
	private void download(HttpServletRequest request, HttpServletResponse response, String filePath)
			throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			throw new IOException("File not found! filePath: " + filePath);
		}
		/*
		 * BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		 * byte[] buf = new byte[1024]; int len = 0;
		 */
		response.reset(); // 非常重要

		String fname = f.getName();
		String userAgent = request.getHeader("User-Agent");
		if (/* IE 8 至 IE 10 */
		userAgent.toUpperCase().contains("MSIE") ||
		/* IE 11 */
				userAgent.contains("Trident/7.0")) {
			fname = URLEncoder.encode(fname, "UTF-8");
		} else if (userAgent.toUpperCase().contains("MOZILLA") || userAgent.toUpperCase().contains("CHROME")) {
			fname = new String(fname.getBytes(), "ISO-8859-1");
		} else {
			fname = URLEncoder.encode(fname, "UTF-8");
		}
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment; filename=" + fname);

		FileUtils.copyFile(f, response.getOutputStream());

		/*
		 * OutputStream out = response.getOutputStream(); while ((len = br.read(buf)) >
		 * 0) { out.write(buf, 0, len); } br.close(); out.close();
		 */
	}

	/**
	 * 文件夹打包
	 * 
	 * @param f
	 * @param baseDir
	 * @param zos
	 */
	public static void compress(File f, String baseDir, ZipOutputStream zos) {
		if (!f.exists()) {
			System.out.println("待压缩的文件目录或文件" + f.getName() + "不存在");
			return;
		}

		File[] fs = f.listFiles();
		BufferedInputStream bis = null;
		// ZipOutputStream zos = null;
		byte[] bufs = new byte[1024 * 10];
		FileInputStream fis = null;

		try {
			// zos = new ZipOutputStream(new FileOutputStream(zipFile));
			for (int i = 0; i < fs.length; i++) {
				String fName = fs[i].getName();
				System.out.println("压缩：" + baseDir + "/" + fName);
				if (fs[i].isFile()) {
					ZipEntry zipEntry = new ZipEntry(fName);//
					zos.putNextEntry(zipEntry);
					// 读取待压缩的文件并写进压缩包里
					fis = new FileInputStream(fs[i]);
					bis = new BufferedInputStream(fis, 1024 * 10);
					int read = 0;
					while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
						zos.write(bufs, 0, read);
					}
					// 如果需要删除源文件，则需要执行下面2句
					// fis.close();
					// fs[i].delete();
				} else if (fs[i].isDirectory()) {
					compress(fs[i], baseDir + "/" + fName + "/", zos);
				}
			} // end for
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				// if(null!=zos)
				// zos.close();
				if (null != fis)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
