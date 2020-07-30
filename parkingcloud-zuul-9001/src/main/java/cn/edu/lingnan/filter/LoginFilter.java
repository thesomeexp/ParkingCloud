package cn.edu.lingnan.filter;

import cn.edu.lingnan.util.JWTConfig;
import cn.edu.lingnan.util.MagicVariable;
import cn.edu.lingnan.util.MyTool;
import cn.edu.lingnan.util.Result;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 登录过滤器
@Component
public class LoginFilter extends ZuulFilter {

    //四种类型：pre,routing,error,post
    //pre：主要用在路由映射的阶段是寻找路由映射表的
    //routing:具体的路由转发过滤器是在routing路由器，具体的请求转发的时候会调用
    //error:一旦前面的过滤器出错了，会调用error过滤器。
    //post:当routing，error运行完后才会调用该过滤器，是在最后阶段的
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
//            int i = 1/0;
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String uri = request.getRequestURI();
        List<String> no_filter_list = getFilterUriList();
        System.out.println("uri: " + uri);
        // zuul转发头
        String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
        requestContext.addZuulRequestHeader(JWTConfig.HEADER_TOKEN_KEY,header);
        for (String path : no_filter_list) {
            // 如果和不用登录的uri匹配, 那么放行, 否则检查JWT (好像除了登录注册, 普通查询, 其他都是要鉴权)
            if (path.equals(uri))
                return null;
        }
        System.out.println("检查jwt: ");
        // 需要登陆才能访问的内容
        // 检查 1.JWT是否存在, 2.格式是否合法, 3.JWT签名是否合法, 不合法则返回"未登录"
        if (header != null
                && header.startsWith(JWTConfig.TOKEN_PREFIX)
                && MyTool.isJWTAuthorized(header.replace(JWTConfig.TOKEN_PREFIX, ""))) {
            System.out.println("成功通过过滤");
//            requestContext.addZuulRequestHeader(JWTConfig.HEADER_TOKEN_KEY,header);
            return null;
        }
        // 返回未登录
        requestContext.setSendZuulResponse(false);
        //设置返回内容类型及编码
        requestContext.getResponse().setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
//        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        // 强迫症
        requestContext.setResponseStatusCode(HttpStatus.OK.value());
        requestContext.setResponseBody("{\"status\":\"fail\",\"data\":{},\"message\":\"未登录\"}");
        } catch (Exception ex) {
            // 过滤抛出异常
            RequestContext requestContext = RequestContext.getCurrentContext();
            requestContext.setSendZuulResponse(false);
            //设置返回内容类型及编码
            requestContext.getResponse().setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
//        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            // 强迫症
            requestContext.setResponseStatusCode(HttpStatus.OK.value());
            requestContext.setResponseBody("{\"status\":\"fail\",\"data\":{},\"message\":\"过滤器抛出异常\"}");
            System.out.println("过滤器触发异常");
            ex.printStackTrace();
        }
        return null;
    }

    private List<String> getFilterUriList() {
        String[] arrayUri = new String[]{"/user-api/login", "/user-api/register"};
        return Arrays.asList(arrayUri);
    }
}
