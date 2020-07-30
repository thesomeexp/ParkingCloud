package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.Infoimage;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * (Infoimage)表服务接口
 *
 * @author makejava
 * @since 2020-06-04 11:28:15
 */
public interface InfoimageService {

    Infoimage insert(Infoimage infoimage);//提交一张停车场详情信息
    int deleteById(Integer id);//通过主键删除该条信息
    List<Infoimage> queryByPid(int pid);//通过停车位ID查找该停车位详情图片(已验证的）
    Page<Infoimage> queryByState(String state);//管理员查询（未验证/验证通过/已禁用）的用户提交的停车位详情图片
    Infoimage queryById(int id);//查询某个详情图片的提交信息
    int updateStateById(Infoimage infoimage);//管理员验证图片的状态信息
    Page<Infoimage> queryByUid(int uid);
}