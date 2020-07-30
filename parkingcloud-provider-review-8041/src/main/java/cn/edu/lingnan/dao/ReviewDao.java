package cn.edu.lingnan.dao;

import cn.edu.lingnan.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Review)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-04 11:22:24
 */
@Mapper
public interface ReviewDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Review queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Review> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param review 实例对象
     * @return 对象列表
     */
    List<Review> queryAll(Review review);

    /**
     * 新增数据
     *
     * @param review 实例对象
     * @return 影响行数
     */
    int insert(Review review);

    /**
     * 修改数据
     *
     * @param review 实例对象
     * @return 影响行数
     */
    int update(Review review);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}