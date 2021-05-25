package com.zw.admin.server.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author larry
 * @Description:
 * @date 2020/11/5 18:22
 */
@Configuration
public class PageHelperConfig {

    /**
     * 功能描述:分页助手配置类
     *
     * @param
     * @return com.github.pagehelper.PageHelper
     * @author larry
     * @Date 2020/11/5 18:24
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
