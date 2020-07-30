package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Infoimage;
import cn.edu.lingnan.dao.InfoimageDao;
import cn.edu.lingnan.service.InfoimageService;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sound.sampled.Line;
import java.util.List;

/**
 * (Infoimage)表服务实现类
 *
 * @author makejava
 * @since 2020-06-04 11:28:15
 */
@Service("infoimageService")
public class InfoimageServiceImpl implements InfoimageService {
    @Resource
    private InfoimageDao infoimageDao;


    @Override
    public Infoimage insert(Infoimage infoimage) {
        this.infoimageDao.insert(infoimage);
        return infoimage;
    }

    @Override
    public int deleteById(Integer id) {
        return this.infoimageDao.deleteById(id);
    }


    @Override
    public List<Infoimage> queryByPid(int pid) {
        Infoimage infoimage = new Infoimage();
        infoimage.setPid(pid);
        infoimage.setState("verified");
        return this.infoimageDao.queryAll(infoimage);
    }

    @Override
    public Page<Infoimage> queryByState(String state) {
        Infoimage infoimage = new Infoimage();
        infoimage.setState(state);
        return (Page<Infoimage>)this.infoimageDao.queryAll(infoimage);
    }

    @Override
    public Infoimage queryById(int id) {
        return this.infoimageDao.queryById(id);
    }

    @Override
    public int updateStateById(Infoimage infoimage) {
        return this.infoimageDao.update(infoimage);
    }

    @Override
    public Page<Infoimage> queryByUid(int uid) {
        Infoimage infoimage = new Infoimage();
        infoimage.setUid(uid);
        return (Page<Infoimage>) this.infoimageDao.queryAll(infoimage);
    }

}