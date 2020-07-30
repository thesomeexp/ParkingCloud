package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.Info;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * (Info)表服务接口
 *
 * @author makejava
 * @since 2020-06-04 11:31:42
 */
public interface InfoService {

    Info insert(Info info);//添加停车位
    void setDefaultT(Info newInfo) throws Exception;//设置状态值
    List<Info> isLocationExist(Double longitude, Double latitude);//是否存在该停车位
    List<Info> queryByGeoHashAndVerified(String geoHash);//查询附近的停车位，并且这些停车位是已验证的
    Info queryById(int id);//查询停车位详情信息、查询是否存在该停车位
    Page<Info> queryByUid(int uid);//根据UID列出用户提交的停车场信息分页
    int deleteById(Integer id);//删除该ID的停车位
    Page<Info> queryAll();//管理员查询所有停车位
    Page<Info> queryByState(String state);//管理员查询（未验证/验证通过/验证不通过）的停车位
    int update(Info info);//管理员验证停车位

    int updateT(int pid, String parameterT, int valueT);// 更新T的值
}