package cn.edu.lingnan.service.fallbackFactory;

import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Temp;
import cn.edu.lingnan.service.InfoClientService;
import cn.edu.lingnan.util.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Component
public class InfoClientServiceFallbackFactory implements FallbackFactory<InfoClientService> {

    @Override
    public InfoClientService create(Throwable cause) {
        return new InfoClientService() {
            @Override
            public Result get(int id){
                cause.printStackTrace();
                System.out.println("InfoClientServiceFallbackFactory.get()降级");
                return Result.fail("InfoClientServiceFallbackFactory.get()降级");
            }

            @Override
            public Result put(@RequestHeader("Authorization") String Authorization, @RequestBody Temp temp) {
                System.out.println("InfoClientServiceFallbackFactory.put()降级");
                return Result.fail("InfoClientServiceFallbackFactory.put()降级");
            }


        };
    }
}
