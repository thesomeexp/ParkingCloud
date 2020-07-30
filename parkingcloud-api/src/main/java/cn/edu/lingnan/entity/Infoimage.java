package cn.edu.lingnan.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Infoimage)实体类
 *
 * @author makejava
 * @since 2020-06-03 08:08:49
 */
public class Infoimage implements Serializable {
    private static final long serialVersionUID = -62201139100320326L;
    
    private Integer id;
    
    private Integer pid;
    
    private Integer uid;
    
    private Date submitdate;
    
    private String state;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(Date submitdate) {
        this.submitdate = submitdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Infoimage{" +
                "id=" + id +
                ", pid=" + pid +
                ", uid=" + uid +
                ", submitdate=" + submitdate +
                ", state='" + state + '\'' +
                '}';
    }
}