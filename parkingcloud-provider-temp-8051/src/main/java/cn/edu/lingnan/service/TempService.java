package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.Temp;

import java.util.Date;
import java.util.List;

/**
 * (Temp)表服务接口
 *
 * @author makejava
 * @since 2020-06-04 11:16:28
 */
public interface TempService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Temp queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Temp> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param temp 实例对象
     * @return 实例对象
     */
    int insert(Temp temp);

    /**
     * 修改数据
     *
     * @param temp 实例对象
     * @return 实例对象
     */
    Temp update(Temp temp);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Temp> getByPidAndUidAndSubmitDateAfterThan(int uid, int pid, Date beforeDate);

    List<Temp> queryLastByLimitDesc(int pid, int limit);

    /**
     * 原来没选过, 添加一个有用的状态
     */
    Integer addUsefulCount(int tid);

    /**
     * 原来选择了0, 更改为1
     */
    Integer updateUsefulCount(int tid);
    /**
     * 原来没选过, 添加一个无用的状态
     */
    Integer addBadCount(int tid);
    /**
     * 原来选了1, 更改为0
     */
    Integer updateBadCount(int tid);
}