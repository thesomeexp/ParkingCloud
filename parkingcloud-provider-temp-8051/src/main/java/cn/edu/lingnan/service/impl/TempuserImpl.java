package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.TempUserDao;
import cn.edu.lingnan.service.TempuserService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("tempuserService")
public class TempuserImpl implements TempuserService {

    @Resource
    private TempUserDao tempUserDao;

    @Override
    public Integer searchUserOptDESC(Integer tid, int uid) {
        return tempUserDao.searchUserOptDESC(tid, uid);
    }

    @Override
    public Integer addSelect(int uid, Integer tid, Integer opt) {
        return tempUserDao.addSelect(uid, tid, opt);
    }

    @Override
    public Integer updateSelect(int uid, Integer tid, Integer opt) {
        return tempUserDao.updateSelect(uid, tid, opt);
    }
}
