package com.zw.admin.server.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.GameFileReqDto;
import com.zw.admin.server.dto.GameFileResDto;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zw.admin.server.dao.FileInfoDao;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.model.Game;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.model.UserGame;
import com.zw.admin.server.service.GameService;

import javax.annotation.Resource;

@Service
public class GameServiceImpl implements GameService {

	// 游戏路径前缀
	@Value("${pic.path}")
	private String picPath;

	@Autowired
	private GameDao gameDao;

	@Autowired
	private FileInfoDao fileInfoDao;

	@Resource
	UserService userService;

	@Override
	@Transactional
	public ResultVO<?> delGame(Long id) {
		Game game = gameDao.getById(id);
		String gameName = "";
		// 删除相关游戏文件(真实文件未删)
		if (game != null) {
			gameName = game.getName();
			fileInfoDao.deleteByName(gameName);
		}
		int i = gameDao.delete(id);
		if (i <= 0) {
			return R.error(1, "删除失败");
		} else {
			gameDao.delUGByGid(id);
		}
		return R.success();
	}

	@Override
	public ResultVO<?> saveUserGame(String userId, Long gameId) {
		// 判断用户和游戏都存在
		UserGame userGame = gameDao.getByUidGid(userId, gameId);
		if (userGame != null) {
			List<UserGame> listUG = gameDao.listUG(userId, gameId, null);
			if (listUG.size() > 0 && listUG.get(0).getIsBuy() == 1) {
				return R.error(1, "此购买此游戏");
			} else if (listUG.size() > 0 && listUG.get(0).getIsBuy() == 0) {
				listUG.get(0).setIsBuy(1L);// 已购买
				gameDao.updateUserGame(listUG.get(0));
				return R.success(gameDao.getByUid(userId));
			} else if (listUG.size() == 0) {
				userGame.setIsBuy(1L);// 已购买
				int saveUG = gameDao.saveUG(userGame);
				if (saveUG > 0) {
					return R.success(gameDao.getByUid(userId));
				}
			}
			return R.error(1, "添加失败");
		}
		return R.error(1, "未找到相关用户或游戏");
	}

	@Override
	@Transactional
	public ResultVO<?> saveGame(Game game) throws Exception {
		int i = 0;
		String string = StringUtils.join(game.getPics(), ",");
		game.setPic(string);
		if (game.getId() != null && game.getId() != 0L) {
			game.setStatus(0);
			i = gameDao.update(game);
		} else {
			if (StringUtils.isEmpty(game.getName())) {
				return R.error(1, "游戏名为空");
			}
			int b = gameDao.check(game.getName());
			if (b > 0) {
				return R.error(1, "游戏名重复");
			}
			User user = UserUtil.getCurrentUser();
			if (user == null) {
				return R.error(1, "请先登录");
			}
			String userId = user.getId();
			game.setUserId(userId);
			i = gameDao.save(game);
		}
		if (i <= 0) {
			return R.error(1, "保存失败");
		}

		//2020/05/21添加或者修改默认游戏不对游戏关联做新增或者修改，只针对添加的用户做添加默认游戏，而且即使关联也是对审核后游戏做操作
		/*// 设默认
		if (game.getIsDefault() == 1) {
			List<UserGame> allUser = gameDao.queryByAllUser();
			for (UserGame userGame : allUser) {
				List<UserGame> list = gameDao.listUG(userGame.getUserId(), game.getId(), null);
				if (list.size() > 0) {
					continue;
				}
				userGame.setGameId(game.getId());
				userGame.setGamename(game.getName());
				userGame.setIsBuy(1L);
				int ug = gameDao.saveUG(userGame);
				if (ug <= 0) {
					throw new Exception("保存失败");
				}
			}
		} else {
			// 不是默认游戏,删除所有此游戏用户关联
			if (game.getId() != null && game.getId() != 0) {
				gameDao.delUGByGid(game.getId());
			}
		}*/
		return R.success(game);
	}

	@Override
	public ResultVO<?> apiList(Long id, Integer isDefault,String platform) throws IOException {
		List<Game> list = gameDao.listRf(id, isDefault,platform);
		Integer total = list.size();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	@Override
	public ResultVO<?> webList(Map<String, Object> params, Integer page, Integer num) {
		//判断登录的是否为管理员，如果为管理，设置用户id为空,可以查询所有
		List<Long> roleIds = UserUtil.getRoleIds();
		if (roleIds != null && roleIds.contains(Constant.ROOT)) {
			params.put("userId", null);
		}
		Integer total = gameDao.webCount(params);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<Game> list = gameDao.webList(params, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	@Override
	public ResultVO<?> apiListNew(Map<String, Object> params, Integer page, Integer num) {
		// Integer total = gameDao.webCount(params);
		// PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<Game> list = gameDao.apiList(params, 0, 0);
		map.put("list", list);
		map.put("total", list.size());
		return R.success(map);
	}


	/**
	 * 功能描述:重构添加游戏于用户关系
	 *
	 * @param userId
	 * @param gameId
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/21 13:33
	 */
	@Override
	public ResultVO<?> saveUserGameRpc(String userId, Long gameId) {
		// 判断用户和游戏都存在
		// 单独查询用户表或者游戏表，而不是查询关系表
		User user = userService.getUserByIdRpc(userId);
		Game game = new Game();
		game.setId(gameId);
		//已批准
		game.setStatus(4);
		game = gameDao.getGameById(game);
		if (user == null || game == null) {
			return R.error(1, "未找到相关用户或游戏");
		}
		//查询用户于游戏关联
		//UserGame userGame = gameDao.getByUidGid(userId, gameId);
		//查询数据库中是否已经存在
		List<UserGame> listUG = gameDao.listUG(userId, gameId, null);
		if (listUG.size() == 0) {
			UserGame userGame = new UserGame();
			userGame.setIsBuy(1l);
			userGame.setUserId(userId);
			userGame.setGameId(gameId);
			userGame.setGamename(game.getName());
			userGame.setGameId(game.getId());
			int saveUG = gameDao.saveUG(userGame);
			if (saveUG > 0) {
				return R.success(gameDao.getByUid(userId));
			}
		} else if (listUG.size() > 0) {
			//关联关系已经存在且是购买状态，直接返回
			if (listUG.get(0).getIsBuy() == 1) {
				return R.error(1, "此购买此游戏");
			} else if (listUG.get(0).getIsBuy() == 0) {
				//关联关系已经存在且为非购买状态更新数据
				listUG.get(0).setIsBuy(1L);// 已购买
				gameDao.updateUserGame(listUG.get(0));
				return R.success(gameDao.getByUid(userId));
			}
		}
		return R.error(1, "添加失败");
	}

	/**
	 * 功能描述:查询游戏文件列表
	 *
	 * @param gameFileReqDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/25 11:54
	 */
	public ResultVO<?> selectGameFileList(GameFileReqDto gameFileReqDto) {
		List<GameFileResDto> gameFileResDtos = gameDao.selectGameFileList(gameFileReqDto);
		return R.success(gameFileResDtos);
	}

	/**
	 * 功能描述:删除用户与游戏的关联
	 *
	 * @param userGame
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/15 15:23
	 */
	public ResultVO<?> deleteUserGame(UserGame userGame) {
		Long gameId = userGame.getGameId();
		String userId = userGame.getUserId();
		if (gameId == null || userId == null) {
			return R.error(1, "用户id或者游戏id为空");
		}
		Integer integer = gameDao.deleteUserGame(userGame);
		if (integer > 0) {
			return R.success();
		} else {
			return R.error(1, "删除用户与游戏的关联失败");
		}
	}
}
