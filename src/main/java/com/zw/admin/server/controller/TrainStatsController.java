package com.zw.admin.server.controller;

import java.util.Date;
import java.util.List;

import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.model.ResultDTO;

import com.zw.admin.server.utils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.TrainStatsDao;
import com.zw.admin.server.model.TrainStats;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户训练统计
 * @author larry
 * @Date 2020/10/22 20:24
 */
@Slf4j
@RestController
@Api(tags = "用户训练统计")
@RequestMapping("/trainStatss")
public class TrainStatsController {

    @Resource
    private TrainStatsDao trainStatsDao;

    /**
     * 功能描述:用户训练统计添加
     *
     * @param trainStats
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/22 20:24
     */
    @PostMapping("/save")
    @LogAnnotation(module = "用户训练统计添加")
    @ApiOperation(value = "保存")
    public ResultDTO<?> save(TrainStats trainStats) {
        String userId = trainStats.getUserId();
        String machineType = trainStats.getMachineType();
        Date unityUpdateTime = trainStats.getUpdateTime();
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(machineType) || unityUpdateTime==null){
            return R.fail(1,"必填字段为空");
        }
        //查询数据库是否已经存在
        List<TrainStats> stats = trainStatsDao.selectTrainStats(trainStats);
        if(CollectionUtils.isEmpty(stats)){
            //数据库没有添加
            trainStatsDao.save(trainStats);
        }else {
            //数据库种有一个则更新，有多个则先删除再更新
            //判断更新的时间
            TrainStats t = stats.get(0);
            Date updateTime = t.getUpdateTime();
            //上次更新时间戳
            long timeStamp = updateTime.getTime();
            long unityTimeStramp = unityUpdateTime.getTime();
            if(timeStamp>=unityTimeStramp){
                //数据库更新的时间比本次提交的时间完，不需要更新或者保存，直接返回
                return R.ok();
            }else {
                //数据库时间小于unity本次提交时间，更新
                if(stats.size()>1){
                    //先删除再添加
                    trainStatsDao.deleteTrainStats(trainStats);
                    trainStatsDao.save(trainStats);
                    return R.ok();
                }else {
                    //直接更新
                    trainStats.setId(stats.get(0).getId());
                    trainStatsDao.update(trainStats);
                    return R.ok();
                }
            }
        }
        return R.ok();
    }


    /**
     * 功能描述:查询用户存档
     *
     * @param trainStats
     * @return com.zw.admin.server.model.ResultDTO<?>
     * @author larry
     * @Date 2020/10/23 17:49
     */
    @PostMapping("/selectTrainStats")
    @ApiOperation(value = "查询用户存档")
    public ResultDTO<?> selectTrainStats(TrainStats trainStats) {
        String userId = trainStats.getUserId();
        String machineType = trainStats.getMachineType();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(machineType)) {
            return R.fail(1, "userId或machineType参数为空");
        }
        List<TrainStats> stats = trainStatsDao.selectTrainStats(trainStats);
        if(!CollectionUtils.isEmpty(stats)){
            return R.ok(stats.get(0));
        }else {
            return R.ok();
        }
    }

}