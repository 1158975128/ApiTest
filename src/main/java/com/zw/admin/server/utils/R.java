package com.zw.admin.server.utils;

import com.zw.admin.server.model.ResultDTO;
import com.zw.admin.server.model.ResultVO;

public class R {

	public static ResultVO<Object> success(Object object) {

		ResultVO<Object> resultVO = new ResultVO<Object>();
		resultVO.setCode(0);
		resultVO.setMsg("成功");
		resultVO.setData(object);
		return resultVO;
	}

	public static ResultDTO<?> ok(Object object){
		ResultDTO<?> resultVO = new ResultDTO();
		resultVO.setCode(0);
		resultVO.setMsg("成功");
		resultVO.setData(object);
		resultVO.setTime(System.currentTimeMillis());
		return resultVO;
	}

	public static ResultDTO<?> ok(){
		return ok(null);
	}

	public static ResultDTO<?> fail(Integer code, String msg){
		ResultDTO resultVO = new ResultDTO();
		resultVO.setCode(code);
		resultVO.setMsg(msg);
		resultVO.setTime(System.currentTimeMillis());
		return resultVO;
	}

	public static ResultDTO<?> fail(Integer code, String msg, Object object){
		ResultDTO resultVO = new ResultDTO();
		resultVO.setCode(code);
		resultVO.setMsg(msg);
		resultVO.setData(object);
		resultVO.setTime(System.currentTimeMillis());
		return resultVO;
	}

	public static ResultVO<Object> success() {
		return success(null);
	}

	public static ResultVO<?> error(Integer code, String msg) {
		ResultVO<?> resultVO = new ResultVO<Object>();
		resultVO.setCode(code);
		resultVO.setMsg(msg);

		return resultVO;
	}

	public static ResultVO<?> error(Integer code, String msg, Object object) {
		ResultVO<Object> resultVO = new ResultVO<Object>();
		resultVO.setCode(code);
		resultVO.setMsg(msg);
		resultVO.setData(object);
		return resultVO;
	}
}
