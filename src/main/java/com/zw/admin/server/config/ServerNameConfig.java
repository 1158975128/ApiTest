package com.zw.admin.server.config;

/**
 * @author larry
 * @Description:服务的版本
 * @date 2021/3/19 18:11
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "server")
public class ServerNameConfig {
    private Map<String,String> version;
}
