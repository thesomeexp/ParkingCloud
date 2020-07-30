package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.TempDao;
import cn.edu.lingnan.entity.Temp;
import cn.edu.lingnan.service.TempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (Temp)表服务实现类
 *
 * @author makejava
 * @since 2020-06-04 11:16:28
 */
@Service("tempService")
public class TempServiceImpl implements TempService {
    @Resource
    private TempDao tempDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Temp queryById(Integer id) {
        return this.tempDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Temp> queryAllByLimit(int offset, int limit) {
        return this.tempDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param temp 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Temp temp) {
        return this.tempDao.insert(temp);
    }

    /**
     * 修改数据
     *
     * @param temp 实例对象
     * @return 实例对象
     */
    @Override
    public Temp update(Temp temp) {
        this.tempDao.update(temp);
        return this.queryById(temp.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tempDao.deleteById(id) > 0;
    }

    @Override
    public List<Temp> getByPidAndUidAndSubmitDateAfterThan(int uid, int pid, Date beforeDate) {
        return tempDao.getByPidAndUidAndSubmitDateAfterThan(uid, pid, beforeDate);
    }

    @Override
    public List<Temp> queryLastByLimitDesc(int pid, int limit) {
        return tempDao.queryLastByLimitDesc(pid, limit);
    }

    @Override
    public Integer addUsefulCount(int tid) {
        return tempDao.addUsefulCount(tid);
    }

    @Override
    public Integer updateUsefulCount(int tid) {
        return tempDao.updateUsefulCount(tid);
    }

    @Override
    public Integer addBadCount(int tid) {
        return tempDao.addBadCount(tid);
    }

    @Override
    public Integer updateBadCount(int tid) {
        return tempDao.updateBadCount(tid);
    }
}