package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.UserDao;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-06-03 08:23:18
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer id) {
        return this.userDao.queryById(id);
    }

    @Override
    public boolean isNameExist(String name) {
        User user = null;
        try {
            System.out.println("before isNameExist");
            user = this.userDao.queryByName(name);
            System.out.println("after isNameExist");

        } catch (Exception e) {
            System.out.println("catch isNameExist");
            e.printStackTrace();
        }
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isPhoneExist(String phone) {
        User user = this.userDao.queryByPhone(phone);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        try {
            this.userDao.insert(user);
        } catch (Exception e) {
            System.out.println("insert捕获异常");
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userDao.deleteById(id) > 0;
    }

    public List<User> queryAll(User user) {
        return this.userDao.queryAll(user);
    }

    @Override
    public int updateScore(int uid, int score) {
        return this.userDao.updateUserScore(uid, score);
    }

}