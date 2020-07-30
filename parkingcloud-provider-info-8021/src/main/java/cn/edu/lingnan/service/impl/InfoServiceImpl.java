package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.dao.InfoDao;
import cn.edu.lingnan.service.InfoService;
import cn.edu.lingnan.util.MagicVariable;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

/**
 * (Info)表服务实现类
 *
 * @author makejava
 * @since 2020-06-04 11:31:43
 */
@Service("infoService")
public class InfoServiceImpl implements InfoService {
    @Resource
    private InfoDao infoDao;

    //添加停车位
    @Override
    public Info insert(Info info) {
        this.infoDao.insert(info);
        return info;
    }
    //设置状态值
    @Override
    public void setDefaultT(Info newInfo) throws Exception {
        Field f1 = null;
        for (int i = 0; i < 24; i++) {
            f1 = newInfo.getClass().getDeclaredField("t" + i);
            f1.setAccessible(true);
            f1.set(newInfo, MagicVariable.INFO_DEFAULT_STATE_VALUES);
        }
    }
    //是否存在该停车位
    @Override
    public List<Info> isLocationExist(Double longitude, Double latitude) {
        Info info = new Info();
        info.setLongitude(longitude);
        info.setLatitude(latitude);
        List<Info> infos = this.infoDao.queryAll(info);
        return infos;
    }
    //查询附近的停车位，并且这些停车位是已验证的
    @Override
    public List<Info> queryByGeoHashAndVerified(String geoHash) {
        Info info = new Info();
        info.setGeohash(geoHash+"%");
        info.setState(MagicVariable.VERIFIED);
        List<Info> infos = infoDao.queryAll(info);
        return infos;
    }
    //根据ID查询停车场信息
    @Override
    public Info queryById(int id) {
        return this.infoDao.queryById(id);
    }

    @Override
    public Page<Info> queryByUid(int uid) {
        Info info = new Info();
        info.setUid(uid);
        return (Page<Info>)this.infoDao.queryAll(info);
    }
    //删除该ID的停车位
    @Override
    public int deleteById(Integer id) {
        return this.infoDao.deleteById(id);
    }
    //管理员查询所有停车位
    @Override
    public Page<Info> queryAll() {
        Info info = new Info();
        return (Page<Info>)this.infoDao.queryAll(info);
    }
    //管理员查询（未验证/验证通过/验证不通过）的停车位
    @Override
    public Page<Info> queryByState(String state) {
        Info info = new Info();
        info.setState(state);
        return (Page<Info>)this.infoDao.queryAll(info);
    }
    //管理员验证停车位
    @Override
    public int update(Info info) {
        return this.infoDao.update(info);
    }

    @Override
    public int updateT(int pid, String parameterT, int valueT) {
        System.out.println("shijian: "+Calendar.getInstance().getTime());
        return this.infoDao.updateT(pid, parameterT, valueT, System.currentTimeMillis());
    }

}