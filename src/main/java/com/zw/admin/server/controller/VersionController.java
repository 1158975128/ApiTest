package com.zw.admin.server.controller;

import com.zw.admin.server.config.ServerNameConfig;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author larry
 * @Description:服务端版本
 * @date 2021/3/19 16:59
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    @Resource
    private ServerNameConfig serverNameConfig;

    @PostMapping("/info")
    public ResultVO<?> info(String serviceName){
        String version = serverNameConfig.getVersion().get(serviceName);
        return R.success(version);
    }

}
