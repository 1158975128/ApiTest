package com.zw.admin.server.controller;

import java.util.List;

import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.dao.UserTrainStateDao;
import com.zw.admin.server.model.UserTrainState;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/userTrainStates")
public class UserTrainStateController {

    @Resource
    private UserTrainStateDao userTrainStateDao;

    /**
     * 功能描述:用户训练数据保存或者更新
     *
     * @param userTrainState
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/7/6 17:49
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存")
    public ResultVO<?> saveOrUpdate(UserTrainState userTrainState) {
        log.info("userTrainStates保存或更新:" + userTrainState.toString());
        String userId = userTrainState.getUserId();
        String machineType = userTrainState.getMachineType();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(machineType)) {
            return R.error(1, "参数缺失");
        }
        UserTrainState userTrainState1 = userTrainStateDao.selectTrainStateByUser(userTrainState);
        if (userTrainState1 != null) {
            //更新
            userTrainStateDao.update(userTrainState);
        } else {
            //添加
            userTrainStateDao.save(userTrainState);
        }
        return R.success();
    }

    /**
     * 功能描述:根据用户id查询训练数据
     *
     * @param userTrainState
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/7/6 17:52
     */
    @PostMapping("/selectTrainStateByUser")
    @ApiOperation(value = "根据用户id获取")
    public ResultVO<?> get(UserTrainState userTrainState) {
        log.info("userTrainStates查询:" + userTrainState.toString());
        String userId = userTrainState.getUserId();
        String machineType = userTrainState.getMachineType();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(machineType)) {
            return R.success();
        }
        UserTrainState userTrainState1 = userTrainStateDao.selectTrainStateByUser(userTrainState);
        return R.success(userTrainState1);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public UserTrainState update(@RequestBody UserTrainState userTrainState) {
        userTrainStateDao.update(userTrainState);
        return userTrainState;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {
            @Override
            public int count(PageTableRequest request) {
                return userTrainStateDao.count(request.getParams());
            }
        }, new ListHandler() {
            @Override
            public List<UserTrainState> list(PageTableRequest request) {
                return userTrainStateDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        userTrainStateDao.delete(id);
    }
}