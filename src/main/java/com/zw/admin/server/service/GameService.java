package com.zw.admin.server.service;

import java.io.IOException;
import java.util.Map;

import com.zw.admin.server.dto.GameFileReqDto;
import com.zw.admin.server.model.Game;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserGame;

public interface GameService {

	ResultVO<?> delGame(Long id);

	ResultVO<?> saveUserGame(String userId, Long gameId);

	ResultVO<?> saveGame(Game game) throws Exception;

	ResultVO<?> apiList(Long id, Integer isDefault,String apiList) throws IOException;

	ResultVO<?> webList(Map<String, Object> params, Integer page, Integer num);

	ResultVO<?> apiListNew(Map<String, Object> params, Integer page, Integer num);

	ResultVO<?> saveUserGameRpc(String userId, Long gameId);

	/**
	 * 功能描述:查询游戏文件列表
	 *
	 * @param gameFileReqDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/25 11:54
	 */
	ResultVO<?> selectGameFileList(GameFileReqDto gameFileReqDto);

	public ResultVO<?> deleteUserGame(UserGame userGame);

}
