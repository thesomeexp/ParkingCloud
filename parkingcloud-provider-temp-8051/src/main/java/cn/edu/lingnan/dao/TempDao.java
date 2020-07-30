package cn.edu.lingnan.dao;

import cn.edu.lingnan.entity.Temp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * (Temp)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-04 11:16:28
 */
@Mapper
public interface TempDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Temp queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Temp> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param temp 实例对象
     * @return 对象列表
     */
    List<Temp> queryAll(Temp temp);

    /**
     * 新增数据
     *
     * @param temp 实例对象
     * @return 影响行数
     */
    int insert(Temp temp);

    /**
     * 修改数据
     *
     * @param temp 实例对象
     * @return 影响行数
     */
    int update(Temp temp);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<Temp> getByPidAndUidAndSubmitDateAfterThan(@Param("uid")int uid, @Param("pid")int pid, @Param("beforedate")Date beforeDate);

    List<Temp> queryLastByLimitDesc(@Param("pid") int pid, @Param("limit")int limit);

    Integer addUsefulCount(int tid);

    Integer updateUsefulCount(int tid);

    Integer addBadCount(int tid);

    Integer updateBadCount(int tid);
}