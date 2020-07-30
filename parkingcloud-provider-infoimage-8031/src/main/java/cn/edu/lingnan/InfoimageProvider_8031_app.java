package cn.edu.lingnan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"cn.edu.lingnan"})
public class InfoimageProvider_8031_app {
    public static void main(String[] args) {
        SpringApplication.run(InfoimageProvider_8031_app.class,args);
    }
}
