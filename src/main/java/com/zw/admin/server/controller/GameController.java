package com.zw.admin.server.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dto.GameFileReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.FileInfoDao;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.Game;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserGame;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.service.CommonService;
import com.zw.admin.server.service.GameService;
import com.zw.admin.server.service.TokenManager;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import com.zw.admin.server.utils.ValidateUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/games")
public class GameController {

	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private GameDao gameDao;

	@Autowired
	private FileInfoDao fileInfoDao;

	@Autowired
	private GameService gameService;

	@LogAnnotation(module = "审核批准游戏")
	@PostMapping("/checkGame")
	@ApiOperation(value = "审核批准游戏")
	public void checkGame(Long id, Integer status, Boolean check) {
		Integer status1 = CommonService.check(status, check);
		Game game = new Game();
		game.setId(id);
		game.setIsDefault(null);
		game.setStatus(status1);
		gameDao.update(game);
	}

	@LogAnnotation(module = "验证游戏文件名")
	@PostMapping("/checkFileName")
	@ApiOperation(value = "验证游戏文件名")
	public ResultVO<?> checkFileName(String fileName, String platform) {
		if (!fileName.contains(".")) {
			return R.error(1, "缺少后缀名");
		}
		// 获取文件名
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		Game game = gameDao.getByName(name);
		if (game == null) {
			return R.error(1, "请先添加游戏");
		} else {
			Integer gameStatus = game.getStatus();
			// 游戏批准没通过
			if (gameStatus != 4) {
				return R.error(1, "请先等待游戏批准通过");
				// 游戏批准通过
			} else {
				FileInfo file = new FileInfo();
				file.setName(name);
				file.setPlatform(platform);
				// 找相关游戏文件
				FileInfo fileInfo = fileInfoDao.getByName(file);
				if (fileInfo != null) {
					Integer status = fileInfo.getStatus();
					// 文件状态02不可上传
					if (status == 0 || status == 2) {
						return R.error(1, "当前状态不可上传");
					}
				}
			}
		}
		return R.success();
	}

	/**
	 * @param userId
	 * @param gameId
	 * @return 返回用户拥有游戏列表
	 */
	//@PostMapping("/saveUserGame")
	//@ApiOperation(value = "添加用户游戏关系")
	public ResultVO<?> saveUserGame(String userId, Long gameId) {
		return gameService.saveUserGame(userId, gameId);
	}

	@PostMapping("/userGameList")
	@ApiOperation(value = "用户游戏列表")
	public ResultVO<?> userGameList(String userId, Long gameId, Integer isDefault) {
		List<UserGame> listUG = gameDao.listUG(userId, gameId, isDefault);
		return R.success(listUG);
	}

	@LogAnnotation(module = "删除用户游戏列表")
	@PostMapping("/delUserGame")
	@ApiOperation(value = "删除用户游戏列表")
	public ResultVO<?> delUserGame(UserGame userGame) {
		int i = gameDao.delUG(userGame);
		if (i <= 0) {
			return R.error(1, "删除失败");
		}
		return R.success();
	}

	@PostMapping("/save")
	@ApiOperation(value = "保存")
	// @RequiresPermissions("sys:game:add")
	public ResultVO<?> saveRestful(@Valid @RequestBody Game game, BindingResult br) {
		String msg = ValidateUtil.check(br);
		if (game.getName() != null && "icon".equals(game.getName())) {
			return R.error(1, "游戏名不可用");
		}
		if (StringUtils.isNotEmpty(msg)) {
			return R.error(1, msg);
		}
		try {
			return gameService.saveGame(game);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(1, e.getMessage());
		}

	}

	/**
	 * @param id 0查询所有
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/list")
	@ApiOperation(value = "列表")
	public ResultVO<?> listRestful(Long id, Integer isDefault,String platform) throws IOException {
		ResultVO<?> apiList = gameService.apiList(id, isDefault,platform);
		return apiList;
	}
	
	/**
	 * 功能描述:查询游戏文件列表
	 *
	 * @param gameFileReqDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/25 11:56
	 */
	@PostMapping("/selectGameFileList")
	@ApiOperation(value = "列表")
	public ResultVO<?> selectGameFileList(GameFileReqDto gameFileReqDto) throws IOException {
		ResultVO<?> apiList = gameService.selectGameFileList(gameFileReqDto);
		return apiList;
	}

	/**
	 * 带购买状态游戏列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/webList")
	@ApiOperation(value = "带购买状态游戏列表")
	public ResultVO<?> webList(String params, Integer page, Integer num, Integer userType) {
		Map<String, Object> tmap = new HashMap<>();
		try {
			Map<String, Object> map = JSONObject.parseObject(params, Map.class);
			tmap = map == null ? tmap : map;
			System.err.println(tmap);
//			if (userType != null && userType != 0) {
			// String userId = UserUtil.getCurrentUser().getId();
			// String userId = (String) map.get("userId");
			// tmap.put("userId", userId);
			// }
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}

		return gameService.webList(tmap, page, num);
	}

	/**
	 * 游戏列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/apiList")
	@ApiOperation(value = "游戏列表")
	public ResultVO<?> apiList(String params, Integer page, Integer num) {
		Map<String, Object> tmap = new HashMap<>();
		try {
			Map<String, Object> map = JSONObject.parseObject(params, Map.class);
			tmap = map == null ? tmap : map;
			System.err.println(tmap);
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}
		return gameService.apiListNew(tmap, page, num);
	}

	@PostMapping("/delete")
	@ApiOperation(value = "删除")
	public ResultVO<?> deleteRestful(Long id) {
		return gameService.delGame(id);
	}

	@PostMapping
	@ApiOperation(value = "保存")
	public ResultVO<?> save(@RequestBody Game game) {
		try {
			int i = 1;
			return gameService.saveGame(game);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(1, e.getMessage());
		}
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Game get(@PathVariable Long id) {
		return gameDao.getById(id);
	}

	@PutMapping
	@ApiOperation(value = "修改")
	public ResultVO<?> update(@RequestBody Game game) {
		try {
			game.setStatus(0);
			return gameService.saveGame(game);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(1, e.getMessage());
		}
	}

	@GetMapping
	@ApiOperation(value = "列表")
	public PageTableResponse list(PageTableRequest request) {
		String userType = (String) request.getParams().get("userType");
		if ("0".equals(userType)) {
			String userId = UserUtil.getCurrentUser().getId();
			request.getParams().put("userId", userId);
		}
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return gameDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Game> list(PageTableRequest request) {
				List<Game> list = gameDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).handle(request);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除")
	public void delete(@PathVariable Long id) {
		gameService.delGame(id);
	}

	/**
	 * 功能描述:重构添加用户和游戏关联接口
	 *
	 * @param userId
	 * @param gameId
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/21 16:57
	 */
	@PostMapping("/saveUserGame")
	@ApiOperation(value = "添加用户游戏关系")
	public ResultVO<?> saveUserGameRpc(String userId, Long gameId) {
		return gameService.saveUserGame(userId, gameId);
	}

	/**
	 * 功能描述:根据userId与gameId删除游戏与用户关联
	 *
	 * @param userGame
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/15 15:18
	 */
	@LogAnnotation(module = "删除游戏与用户关联")
	@PostMapping("/deleteUserGame")
	@ApiOperation(value = "删除游戏与用户关联")
	public ResultVO<?> deleteUserGame(UserGame userGame) {
		return gameService.deleteUserGame(userGame);
	}
}
