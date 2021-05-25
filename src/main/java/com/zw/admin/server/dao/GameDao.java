package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.dto.GameFileReqDto;
import com.zw.admin.server.dto.GameFileResDto;
import com.zw.admin.server.model.FileInfo;
import com.zw.admin.server.model.ResultVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zw.admin.server.model.Game;
import com.zw.admin.server.model.UserGame;

@Mapper
public interface GameDao {

	// List<UserGame> getByUserDefault();

	@Select("select t.id as gameId,t.name as gamename from game t where t.isDefault = 1 and t.status = 4")
	List<UserGame> getByIsDefault();

	@Select("select u.id as userId,u.username as username from user u")
	List<UserGame> queryByAllUser();

	@Select("select * from game t where t.name = #{name}")
	Game getByName(String name);

	List<UserGame> listUG(@Param("userId") String userId, @Param("gameId") Long gameId,
			@Param("isDefault") Integer isDefault);

	@Select("select *from user_game ug where  ug.userId = #{userId} ")
	List<UserGame> getByUid(@Param("userId") String userId);

	@Select("SELECT * from user_game ug left join game g on ug.gameId= g.id WHERE g.isDefault=0 and ug.userId=#{userId}")
	List<UserGame> getByUidIsDefault(@Param("userId") String userId);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into user_game(userId, username, gameId, gamename,isBuy) values(#{userId}, #{username},#{gameId}, #{gamename}, #{isBuy})")
	int saveUG(UserGame userGame);

	int saveUGBatch(List<UserGame> userGames);

	int delUG(UserGame userGame);

	@Select("select u.id as userId,u.username as username,g.id as gameId,g.name as gamename from game g,user u where g.id = #{gameId} and u.id = #{userId} and g.status=4")
	UserGame getByUidGid(@Param("userId") String userId, @Param("gameId") Long gameId);

	@Select("select count(1) from game t where t.name = #{name}")
	int check(String name);

	List<Game> listRf(@Param("id") Long id, @Param("isDefault") Integer isDefault,@Param("platform") String platform);

	// 删除用户游戏关联
	@Delete("delete from user_game where gameId = #{gameId} and isBuy =0 ")
	int delUGByGid(Long gameId);

	@Select("select * from game t where t.id = #{id} ")
	Game getById(Long id);

	@Delete("delete from game where id = #{id}")
	int delete(Long id);

	int updateUserGame(UserGame game);

	int update(Game game);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into game( userId,name,gamename,modulename, pic, description,isDefault,type,mode,categoty,deviceType,icon, createTime, updateTime) values( #{userId},#{name},#{gamename},#{modulename}, #{pic}, #{description},#{isDefault},#{type},#{mode},#{categoty},#{deviceType},#{icon}, NOW(), NOW())")
	int save(Game game);

	int count(@Param("params") Map<String, Object> params);

	List<Game> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	int webCount(@Param("params") Map<String, Object> params);

	List<Game> apiList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	List<Game> webList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	Game getGameById(Game game);

	@Select("select gamename from game  where id = #{id} ")
	String getGameNameById(String id);

	/**
	 * 功能描述:游戏名下拉
	 *
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @author larry
	 * @Date 2020/11/14 18:12
	 */
	List<String> selectGameNameList();

	/**
	 * 功能描述:查询游戏文件列表
	 *
	 * @param gameFileReqDto
	 * @return com.zw.admin.server.model.ResultVO<?>
	 * @author larry
	 * @Date 2020/11/25 11:54
	 */
	List<GameFileResDto> selectGameFileList(GameFileReqDto gameFileReqDto);

	/**
	 * 功能描述:删除用户与游戏关联
	 *
	 * @param userGame
	 * @return java.lang.Integer
	 * @author larry
	 * @Date 2021/3/15 15:28
	 */
	Integer deleteUserGame(UserGame userGame);
}
