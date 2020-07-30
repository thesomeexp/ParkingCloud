package cn.edu.lingnan.service;

import cn.edu.lingnan.dto.TempUserDTO;
import cn.edu.lingnan.service.fallbackFactory.UserClientServiceFallbackFactory;
import cn.edu.lingnan.util.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "parkingcloud-user", fallbackFactory = UserClientServiceFallbackFactory.class)
public interface UserClientService {

    @PutMapping("/score")
    Result addScore(@RequestHeader("Authorization") String Authorization, @RequestBody TempUserDTO tempuserDTO);

}
