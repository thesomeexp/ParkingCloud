package cn.edu.lingnan.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class BeanConfig {
    /**
     * 设置上传文件的最大大小
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory config = new MultipartConfigFactory();
        config.setMaxFileSize("80MB");
        config.setMaxRequestSize("100MB");
        return config.createMultipartConfig();
    }
}
