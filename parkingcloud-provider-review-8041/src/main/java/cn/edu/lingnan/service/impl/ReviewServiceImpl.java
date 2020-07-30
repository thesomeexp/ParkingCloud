package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.Review;
import cn.edu.lingnan.dao.ReviewDao;
import cn.edu.lingnan.service.ReviewService;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Review)表服务实现类
 *
 * @author makejava
 * @since 2020-06-04 11:22:24
 */
@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
    @Resource
    private ReviewDao reviewDao;


    @Override
    public int insert(Review review) {
        return this.reviewDao.insert(review);
    }

    @Override
    public Page<Review> queryByPid(int pid) {
        Review review = new Review();
        review.setPid(pid);
        return (Page<Review>) this.reviewDao.queryAll(review);
    }

    @Override
    public Page<Review> queryByUid(int Uid) {
        Review review = new Review();
        review.setUid(Uid);
        return (Page<Review>) this.reviewDao.queryAll(review);
    }

    @Override
    public Review queryById(int id) {
        return this.reviewDao.queryById(id);
    }

    @Override
    public int deleteById(int id) {
        return this.reviewDao.deleteById(id);
    }
}