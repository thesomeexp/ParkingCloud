package cn.edu.lingnan.dao;

import cn.edu.lingnan.entity.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * (Info)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-04 11:31:42
 */
@Mapper
public interface InfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Info queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Info> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param info 实例对象
     * @return 对象列表
     */
    List<Info> queryAll(Info info);

    /**
     * 新增数据
     *
     * @param info 实例对象
     * @return 影响行数
     */
    int insert(Info info);

    /**
     * 修改数据
     *
     * @param info 实例对象
     * @return 影响行数
     */
    int update(Info info);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int updateT(@Param("pid")int pid, @Param("parameterT")String parameterT, @Param("valueT")int valueT,
                @Param("stateupdatedate") long stateUpdateDate);

    int countAll();

    int countNoVerified();

    int countVerified();

    int countDisable();
}