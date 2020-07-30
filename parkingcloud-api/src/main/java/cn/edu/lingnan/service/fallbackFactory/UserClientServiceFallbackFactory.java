package cn.edu.lingnan.service.fallbackFactory;

import cn.edu.lingnan.dto.TempUserDTO;
import cn.edu.lingnan.service.InfoClientService;
import cn.edu.lingnan.service.UserClientService;
import cn.edu.lingnan.util.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserClientServiceFallbackFactory implements FallbackFactory<UserClientService> {
    @Override
    public UserClientService create(Throwable throwable) {
        return new UserClientService(){

            @Override
            public Result addScore(String Authorization, TempUserDTO tempuserDTO) {
                System.out.println("UserClientServiceFallbackFactory.addScore()降级");
                return Result.fail("UserClientServiceFallbackFactory.addScore()降级");
            }


        };
    }
}
