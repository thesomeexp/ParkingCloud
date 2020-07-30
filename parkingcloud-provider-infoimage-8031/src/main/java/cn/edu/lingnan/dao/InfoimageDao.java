package cn.edu.lingnan.dao;

import cn.edu.lingnan.entity.Infoimage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Infoimage)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-04 11:28:14
 */
@Mapper
public interface InfoimageDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Infoimage queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Infoimage> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param infoimage 实例对象
     * @return 对象列表
     */
    List<Infoimage> queryAll(Infoimage infoimage);

    /**
     * 新增数据
     *
     * @param infoimage 实例对象
     * @return 影响行数
     */
    int insert(Infoimage infoimage);

    /**
     * 修改数据
     *
     * @param infoimage 实例对象
     * @return 影响行数
     */
    int update(Infoimage infoimage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}