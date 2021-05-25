package com.zw.admin.server.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.ResultVO;

public interface FileService {

	ResultVO<?> saveBin(MultipartFile file) throws IOException;

	ResultVO<?> save(MultipartFile file, String platform, String platformId, String type, String sn,String machineType) throws IOException;

	FileInfo commonSave(MultipartFile file, String name, Integer type) throws IOException;

	void delete(String id);

	ResultVO<?> savePic(MultipartFile file, String k) throws IOException;

}
