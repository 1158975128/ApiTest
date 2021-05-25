package com.zw.admin.server.config;

import java.io.File;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zw.admin.server.page.table.PageTableArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 跨域支持
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*")
						.allowCredentials(true).maxAge(18000L);
			}
		};
	}

	/**
	 * datatable分页解析
	 * 
	 * @return
	 */
	@Bean
	public PageTableArgumentResolver tableHandlerMethodArgumentResolver() {
		return new PageTableArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(tableHandlerMethodArgumentResolver());
	}

	/**
	 * 上传文件根路径
	 */
	@Value("${files.path}")
	private String filesPath;

	/**
	 * 游戏图片
	 */
	@Value("${pic.path}")
	private String picPath;

	/**
	 * 普通图片
	 */
	@Value("${commonpic.path}")
	private String commonpicPath;

	/**
	 * 用户头像
	 */
	@Value("${headImage.path}")
	private String headImage;

	/**
	 * 文件缓存目录(防止系统删除)
	 */
	@Value("${uploadTmp.path}")
	private String uploadTmp;

	/**
	 * 外部文件访问
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/file/**")
				.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + picPath + File.separator)
				.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + commonpicPath + File.separator);
	}

	/**
	 * 文件上传临时路径
	 */
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		File tmpFile = new File(uploadTmp);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		factory.setLocation(uploadTmp);
		return factory.createMultipartConfig();
	}

}
