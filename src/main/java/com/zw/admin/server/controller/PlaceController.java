package com.zw.admin.server.controller;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.zw.admin.server.dto.PageRequest;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.PlaceDao;
import com.zw.admin.server.model.Place;
import io.swagger.annotations.ApiOperation;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

@RestController
@RequestMapping("/place")
public class PlaceController {
    @Resource
    private PlaceDao placeDao;

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO save(@RequestBody Place place) {
        if(StringUtils.isBlank(place.getPlace())){
            return R.error(1,"场所名为空");
        }
        User currentUser = UserUtil.getCurrentUser();
        place.setCreateUser(currentUser.getName());
        //根据名称查询
        Place byPlace = placeDao.getByPlace(place.getPlace());
        if(byPlace!=null){
            return R.error(1,"场所名重复");
        }
        placeDao.save(place);
        return R.success();
    }

    /**
     * 功能描述:分页查询
     *
     * @param pageRequest
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/5 18:15
     */
    @PostMapping("/selectPlacePage")
    @ApiOperation(value = "分页查询")
    public ResultVO<?> selectPlacePage(@RequestBody PageRequest<Place> pageRequest) {
        //条数
        Integer num = pageRequest.getNum();
        Integer page = pageRequest.getPage();
        PageHelper.startPage(page, num);
        List<Place> list = placeDao.selectPlacePage(pageRequest.getParams());
        PageInfo<Place> placePage = new PageInfo<>(list);
        return R.success(placePage);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Place get(@PathVariable Long id) {
        return placeDao.getById(id);
    }

    /**
     * 功能描述:修改场所
     *
     * @param place
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/6 10:21
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public ResultVO<?> update(@RequestBody Place place) {
        if (place.getId() == null) {
            return R.error(1, "缺少Id");
        }
        //检查数据库中是否有重复的
        Place place1 = placeDao.checkPlaceDouble(place);
        if(place1!=null){
            return R.error(1, "名称已存在");
        }
        placeDao.update(place);
        return R.success();
    }

    /**
     * 功能描述:设备下拉框
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/6 10:53
     */
    @GetMapping("/selectPlaces")
    public ResultVO<?> selectPlaces() {
        List<Map<Long, String>> places = placeDao.selectPlaces();
        return R.success(places);
    }
}