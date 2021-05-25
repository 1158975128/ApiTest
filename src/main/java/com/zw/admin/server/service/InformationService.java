package com.zw.admin.server.service;

import java.util.Map;

import com.zw.admin.server.model.ResultVO;

public interface InformationService {

	ResultVO<?> list(Map<String, Object> params, Integer page, Integer num);

}
