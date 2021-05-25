package com.zw.admin.server.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dto.PageRequest;
import com.zw.admin.server.dto.ResponseDto;
import com.zw.admin.server.model.Place;
import com.zw.admin.server.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dao.GameDao;
import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.dto.UserDto;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.model.UserGame;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户相关接口
 *
 * @author THF
 */
@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GameDao gameDao;
	@Resource
	private LoginController loginController;
	@Resource
	private RedisTemplate<String,String> redisTemplate;

	private Long ICU = 1l;
	private Long ADMIN = 2l;

	// @LogAnnotation
	//@PostMapping("/add")
	@ApiOperation(value = "API注册保存用户")
	public ResultVO<?> saveUser(UserDto userDto) {
		return userService.saveUser(userDto);
	}

	@LogAnnotation(module = "API修改用户")
	@PostMapping("/update")
	@ApiOperation(value = "API修改用户")
	// @RequiresPermissions("sys:user:add")
	public ResultVO<?> updateUser(UserDto userDto, HttpServletRequest request) {
		return userService.updateUser1(userDto);
	}

	@LogAnnotation(module = "修改密码")
	@PostMapping("/changePassword")
	@ApiOperation(value = "修改密码")
	// @RequiresPermissions("sys:user:password")
	public ResultVO<?> changePassword(String id, String password) {
		return userService.changePassword(id, password);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/userList")
	@ApiOperation(value = "用户列表")
	@LogAnnotation(module = "修改密码")
	// @RequiresPermissions("sys:user:query")
	public ResultVO<?> listUsers(String params, Integer page, Integer num) {

		Map<String, Object> tmap = new HashMap<String, Object>();
		try {
			Map<String, Object> map = JSONObject.parseObject(params, Map.class);
			tmap = map == null ? tmap : map;
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}
		tmap.put("isDelete",0);

		return userService.getUserList(tmap, page, num);
	}

	@LogAnnotation(module = "用户列表")
	@PostMapping("/webList")
	@ApiOperation(value = "用户列表")
	public ResultVO<?> getUserList(String params, Integer page, Integer num) {
		Map<String, Object> tmap = new HashMap<>();
		try {
			Map<String, Object> map = JSONObject.parseObject(params, Map.class);
			tmap = map == null ? tmap : map;
			log.info("[方法]:[{}],[params]:[{}]","用户列表",params);
		} catch (Exception e) {
			return R.error(1, "params格式错误");
		}
		User user = UserUtil.getCurrentUser();
		String userId = user.getId();
		Long identity = user.getIdentity();
		if (identity == ICU || identity == ADMIN) {
			tmap.put("parentId", userId);
		}
		Integer total = userDao.count(tmap);
		PageObject page1 = PageUtils.pageClass(page, total, num);
		Map<Object, Object> map = new HashMap<>();

		List<User> list = userDao.list(tmap, page1.getStartIndex(), num);
		map.put("list", list);
		map.put("total", total);
		return R.success(map);
	}

	@LogAnnotation(module = "用户列表")
	@GetMapping
	@ApiOperation(value = "用户列表")
	// @RequiresPermissions("sys:user:query")
	public PageTableResponse adminListUsers(PageTableRequest request) {
		User user = UserUtil.getCurrentUser();
		String userId = user.getId();
		Long identity = user.getIdentity();
		if (identity == ICU || identity == ADMIN) {
			request.getParams().put("parentId", userId);
		}

		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return userDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<User> list(PageTableRequest request) {
				List<User> list = userDao.adminList(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).handle(request);
	}

	@ApiOperation(value = "当前登录用户")
	@GetMapping("/current")
	public User currentUser() {
		return UserUtil.getCurrentUser();
	}

	@LogAnnotation(module = "根据用户id获取用户")
	@ApiOperation(value = "根据用户id获取用户")
	@PostMapping("/getUser")
	// @RequiresPermissions("sys:user:query")
	public ResultVO<?> user(String id) {
		return R.success(userDao.getById(id));
	}

	@LogAnnotation(module = "根据用户id获取用户")
	@ApiOperation(value = "根据用户id获取用户")
	@GetMapping("/{id}")
	// @RequiresPermissions("sys:user:query")
	public User adminUser(@PathVariable String id) {
		User user = userDao.getById(id);
		//用户查询的角色信息有误
		/*Long identity = UserUtil.getCurrentUser().getIdentity();
		user.setIdentity(identity);*/
		return user;
	}

	//@ApiOperation(value = "根据用户id删除用户")
	//@PostMapping("/delete")
	// @RequiresPermissions("sys:user:query")
	public ResultVO<?> deleteUser(String id) {
		User user = userDao.getById(id);
		if (user != null) {
			userDao.deleteUserRole(user.getId());
		}
		userDao.deleteUser(id);
		// 删除用户关联游戏
		UserGame userGame = new UserGame();
		userGame.setUserId(id);
		gameDao.delUG(userGame);

		return R.success();
	}

	//@PutMapping
	@ApiOperation(value = "修改用户")
	// @RequiresPermissions("sys:user:add")
	public ResultVO<?> updateUser(@RequestBody UserDto userDto) {
		return userService.updateUser(userDto);
	}

	//@PostMapping("/sync")
	@ApiOperation(value = "用户同步")
	public ResultVO<?> saveRestful(User user) {
		return userService.syncUser(user);
	}

	/**
	 * 功能描述:重构用户保存API
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/29 9:58
	 */
	@LogAnnotation(module = "API注册保存用户")
	@PostMapping("/add")
	@ApiOperation(value = "API注册保存用户")
	public ResultVO<?> saveUserRpc(UserDto userDto) {
		return userService.saveUserRpc(userDto);
	}

	/**
	 * 功能描述:重构用户修改接口,暂时废弃
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/29 11:33
	 */
	@LogAnnotation(module = "修改用户")
	@PutMapping
	@ApiOperation(value = "修改用户")
	// @RequiresPermissions("sys:user:add")
	public ResultVO<?> updateUserRpc(@RequestBody UserDto userDto) {
		return userService.updateUserRpc(userDto);
	}

	/**
	 * 功能描述:重构用户同步API
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/5/29 11:57
	 */
	@LogAnnotation(module = "用户数据同步")
	@PostMapping("/sync")
	@ApiOperation(value = "用户同步")
	public ResultVO<?> saveRestfulRpc(UserDto user) {
		return userService.syncUserRpc(user);
	}

	/**
	 * 功能描述:删除用户
	 *
	 * @param id
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/6/28 10:52
	 */
	@LogAnnotation(module = "根据id删除用户")
	@ApiOperation(value = "根据用户id删除用户")
	@PostMapping("/delete")
	// @RequiresPermissions("sys:user:query")
	public ResultVO<?> deleteUserRpc(String id) {
		if(StringUtils.isBlank(id)){
			return R.error(1,"缺少用户id");
		}
		UserDto user = new UserDto();
		user.setId(id);
		//user.setIsDelete(Constant.IS_DELETE);
		user.setIsDelete(1);
		user.setUpdateTime(new Date());
		userService.updateUserRpc(user);
		log.info("根据id删除用户:"+ user.getId());
		return R.success();
		/*Boolean b = userService.deleteUserRpc(id);
		if(b){
			return R.success();
		}else {
			return R.error(1,"删除失败");
		}*/
	}

	/**
	 * 功能描述:同步用户数据到用户服务
	 *
	 * @param
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/7/2 17:24
	 */
	@ApiOperation(value = "同步用户数据到用户服务")
	@PostMapping("/initUser")
	public ResultVO<?> initUser() {
		int i = userService.initUser();
		return R.success();
	}

	/**
	 * 功能描述:在线用户查询
	 *
	 * @param pageRequest
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 10:30
	 */
	@LogAnnotation(module = "在线用户列表")
	@PostMapping("/selectOnlineUserList")
	@ApiOperation(value = "用户列表")
	public ResultVO<?> selectOnlineUserList(@RequestBody PageRequest<UserDto> pageRequest) {
		//查询条件
		UserDto params = pageRequest.getParams();
		//条数
		Integer pageSize = pageRequest.getNum();
		//页码
		Integer pageNum = pageRequest.getPage();
		//分页设置
		PageHelper.startPage(pageNum, pageSize);
		User user = UserUtil.getCurrentUser();
		String userId = user.getId();
		Long identity = user.getIdentity();
		if (identity == ICU || identity == ADMIN ) {
			params.setParentId(userId);
		}
		List<User> users = userDao.selectUserList(params);
		PageInfo<User> userPageInfo = new PageInfo<>(users);
		return R.success(userPageInfo);
	}

	/**
	 * 功能描述:获取用户服务中的数据
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 14:01
	 */
	@LogAnnotation(module = "获取用户服务中的数据")
	@PostMapping("/getOnlineUser")
	@ApiOperation(value = "获取用户服务中的数据")
	public ResultVO<?> getOnlineUser(@RequestBody UserDto user) {
		return userService.getOnlineUser(user);
	}

	/**
	 * 功能描述:添加在线用户接口
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 16:50
	 */
	@LogAnnotation(module = "添加在线用户接口")
	@PostMapping("/insertOnlineUser")
	@ApiOperation(value = "添加在线用户接口")
	public ResultVO<?> insertOnlineUser(@RequestBody UserDto user) {
		//在线用户
		user.setIsChecked(1);
		//判断校验字段
		if(!StringUtils.isBlank(user.getEmail())&&StringUtils.isBlank(user.getPhone())){
			user.setCheckColumn(Constant.EMAIL_CHECK);
		}else if(!StringUtils.isBlank(user.getPhone())&&StringUtils.isBlank(user.getEmail())){
			user.setCheckColumn(Constant.PHONE_CHECK);
		}
		return userService.registerUserRpc(user);
	}

	/**
	 * 功能描述:添加在线用户接口
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/6 16:50
	 */
	@LogAnnotation(module = "修改在线用户接口")
	@PostMapping("/updateOnlineUser")
	@ApiOperation(value = "添加在线用户接口")
	public ResultVO<?> updateOnlineUser(@RequestBody UserDto user) {
		return userService.updateOnlineUser(user);
	}

	/**
	 * 功能描述:
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/9 10:51
	 */
	public ResultVO<?> checkVerificationCode(@RequestBody UserDto user) {
		Boolean flag = userService.checkVerificationCode(user);
		return R.success(flag);
	}

	/**
	 * 功能描述:用户明细查询
	 *
	 * @param id
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/9 17:49
	 */
	@ApiOperation(value = "根据用户id获取用户DTO")
	@PostMapping("/selectUserDetail")
	// @RequiresPermissions("sys:user:query")
	public ResultVO<?> selectUserDetail(String id) {
		UserDto user = userDao.selectUserDetail(id);
		if (user == null) {
			return R.success(user);
		}
		List<Long> roleIds = userDao.selectRoleIdByUserId(id);
		user.setRoleIds(roleIds);
		List<Place> places = userDao.selectUserPlaceByUserId(id);

		user.setPlaces(places);
		return R.success(user);
	}

	/**
	 * 功能描述:离线用户转在线
	 *
	 * @param userDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/7 16:38
	 */
	@LogAnnotation(module = "离线用户转在线")
	@PostMapping("/offlineTurnOnline")
	@ApiOperation(value = "离线用户转在线")
	public ResultVO<?> offlineTurnOnline(@RequestBody UserDto userDto) {
		return userService.offlineTurnOnline(userDto);
	}

	/**
	 * 功能描述:
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/12/10 14:26
	 */
	@LogAnnotation(module = "获取激活连接")
	@PostMapping("/getActiveUrl")
	@ApiOperation(value = "获取激活连接")
	public ResultVO<?> getActiveUrl(User user) {
		String id = user.getId();
		ResultVO<?> activeUrl = userService.createActiveUrl(id);
		return activeUrl;
	}

	/**
	 * 功能描述:对康复接口
	 *
	 * @param user
	 * @return io.renren.common.utils.R
	 * @author larry
	 * @date 2020/12/24
	 */
	@LogAnnotation(module = "康复系统添加接口")
	@PostMapping("/save")
	@ApiOperation(value = "康复系统添加接口")
	public Boolean save(@RequestBody UserDto user) {
		List<Long> roleIds = user.getRoleIds();
		log.info("[方法]:[{}],[params]:[{}]","用户信息保存",user);
		return userService.save(user);
	}

	/**
	 * 功能描述:修改用户的对外接口
	 *
	 * @param user
	 * @return io.renren.common.utils.R
	 * @author larry
	 * @date 2020/5/11
	 */
	@ApiOperation("康复系统修改接口")
	@LogAnnotation(module = "康复系统修改接口")
	@PostMapping("/updateByFris")
	public Boolean update(@RequestBody UserDto user) {
		log.info("[方法]:[{}],[params]:[{}]","修改用户",user);
		User u = (User)user;
		if(userDao.update(u)>0){
			//修改角色
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 功能描述:微信小程序登录
	 *
	 * @param params
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/2/24 11:43
	 */
	public ResultVO<?> loginByWechatApplet(@RequestBody Map<String, String> params) {
		String phone = params.get("phone");
		String sessionCode = params.get("sessionCode");
		if(StringUtils.isBlank(phone)||StringUtils.isBlank(sessionCode)){
			return R.error(1,"必填字段为空");
		}
		//校验信息，sessionKey与手机号是否匹配，不匹配直接返回
		//1，数据库手机号不存在

		//2，在线手机号存在，直接登录
		//3，在线手机号不存在，转在线
		return null;
	}

	@PostMapping("/getQrCodeBase64")
	public ResultVO<?> getQrCodeBase64(User user) throws Exception{
		Map<String,String> result = new HashMap<String,String>();
		String id = user.getId();
		ResultVO<?> activeUrl = userService.createActiveUrl(id);
		Integer code = activeUrl.getCode();
		if(R.ok().getCode().equals(code)){
			String base64Str = QRCodeUtil.createQrCode((String)activeUrl.getData(), "jpg");
			result.put("activeUrl",(String)activeUrl.getData());
			result.put("qrCode",base64Str);
			return R.success(result);
		}else {
			return activeUrl;
		}
	}

	/**
	 * 功能描述:治疗师修改邮箱或手机号
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/15 17:29
	 */
	@LogAnnotation(module = "治疗师修改邮箱或手机号")
	@PostMapping("/updateEmailOrPhone")
	@ApiOperation(value = "治疗师修改邮箱或手机号")
	public ResultVO<?> updateEmailOrPhone(@RequestBody UserDto user) {
		List<Long> roleIds = user.getRoleIds();
		log.info("[方法]:[{}],[params]:[{}]", "治疗师修改邮箱或手机号", user);
		return userService.updateEmailOrPhone(user);
	}

	/**
	 * 功能描述:治疗师修改邮箱或手机号
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/15 17:29
	 */
	@LogAnnotation(module = "判断用户是否需要转在线")
	@PostMapping("/checkUserById")
	@ApiOperation(value = "判断用户是否需要转在线")
	public ResultVO<?> checkUserById(@RequestBody UserDto user) {
		String id = user.getId();
		if(StringUtils.isBlank(id)){
			return R.error(1,"用户id为空");
		}
		User byId = userDao.getById(id);
		if(byId==null){
			return R.error(1,"用户为空");
		}
		//拿到activeCode匹配redis
		String activeCode = user.getActiveCode();
		String openid = redisTemplate.opsForValue().get(activeCode);
		if(StringUtils.isBlank(openid)){
			//openid不存在
			return R.success(3);
		}
		//openid存在
		User byOpenid = userDao.getByOpenid(openid);
		Integer isChecked = byId.getIsChecked();
		//1，微信已经绑定，该id对应的用户Openid不为空
		if(byOpenid!=null && StringUtils.isNoneBlank(byId.getOpenid())){
			//跳转登录
			return R.success(1);
		}else if(byOpenid==null && StringUtils.isBlank(byId.getOpenid())){
			//openid找不到用户，该Id用户未关联Openid
			return R.success(2);
		}else if(byOpenid==null && StringUtils.isNoneBlank(byId.getOpenid())){
			//扫描的微信未绑定，id关联的用户已经绑定，提示扫描错误的二维码
			return R.success(3);
		}else {
			//TODO
			//byOpenid!=null && StringUtils.isBlank(byId.getOpenid()),已经绑定过的扫未绑定的账号提示合并
			return R.success(1);
		}
		//return R.success(result);
	}

	/**
	 * 功能描述:微信小程序登录或者转在线
	 *
	 * @param user
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2021/3/23 11:22
	 */
	@LogAnnotation(module = "治疗师修改邮箱或手机号")
	@PostMapping("/loginOrTurnByWxApplet")
	@ApiOperation(value = "治疗师修改邮箱或手机号")
	public ResultVO<?> loginOrTurnByWxApplet(@RequestBody UserDto user) {
		//User userByIdRpc = userService.getUserByIdRpc(id);
		return userService.loginOrTurnByWxApplet(user);
	}


}
