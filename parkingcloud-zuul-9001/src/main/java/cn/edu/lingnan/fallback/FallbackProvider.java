package cn.edu.lingnan.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.client.ClientHttpResponse;

// 服务熔断接口
public interface FallbackProvider extends ZuulFallbackProvider {

}
