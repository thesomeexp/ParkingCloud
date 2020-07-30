package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.Review;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * (Review)表服务接口
 *
 * @author makejava
 * @since 2020-06-04 11:22:24
 */
public interface ReviewService {
    int insert(Review review);//为某个停车场添加评论信息
    Page<Review> queryByPid(int pid);//查询某个停车场的所有评论信息
    Page<Review> queryByUid(int Uid);//查询用户提交的评论信息
    Review queryById(int id);//通过ID查询某条评论信息
    int deleteById(int id);//通过ID删除某条评论信息
}