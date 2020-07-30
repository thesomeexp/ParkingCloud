package cn.edu.lingnan.config;

import cn.edu.lingnan.util.JWTConfig;
import cn.edu.lingnan.util.MagicVariable;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

//@Configuration
//public class InfoClientConfiguration implements RequestInterceptor {
//    // Feign转发请求头
//    // 为的是判断身份
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
////        HttpServletRequest request = ((ServletRequestAttributes)
////                RequestContextHolder.getRequestAttributes()).getRequest();
//        System.out.println("InfoClientConfiguration" + request.getHeader(JWTConfig.HEADER_TOKEN_KEY));
////        requestTemplate.header(JWTConfig.HEADER_TOKEN_KEY,
////                request.getHeader(JWTConfig.HEADER_TOKEN_KEY));
//        requestTemplate.header("Authorization", request.getHeader("Authorization"));
//
////        Enumeration<String> headerNames = request.getHeaderNames();
////        if (headerNames != null) {
////            while (headerNames.hasMoreElements()) {
////                String name = headerNames.nextElement();
////                String values = request.getHeader(name);
////                requestTemplate.header(name, values);
////            }
////        }
//
//}
//}
