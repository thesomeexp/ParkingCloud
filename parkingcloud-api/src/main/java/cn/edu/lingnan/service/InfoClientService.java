package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Temp;
import cn.edu.lingnan.service.fallbackFactory.InfoClientServiceFallbackFactory;
import cn.edu.lingnan.util.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
//, configuration= InfoClientConfiguration.class
@FeignClient(name = "parkingcloud-info", fallbackFactory = InfoClientServiceFallbackFactory.class)
public interface InfoClientService {

    @GetMapping("/infos/{id}")
    Result get(@PathVariable("id") int id);

    // Feign不能传递多个值, 所以还是在这边手动解析吧
//    @PutMapping("/info/{id}")
//    public int put(@PathVariable("id") int pid, int timeT, int valueT);

    @PutMapping("/infos")
    Result put(@RequestHeader("Authorization") String Authorization, @RequestBody Temp temp);
}
