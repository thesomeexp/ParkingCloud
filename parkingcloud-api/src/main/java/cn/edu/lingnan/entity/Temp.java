package cn.edu.lingnan.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Temp)实体类
 *
 * @author makejava
 * @since 2020-06-03 08:08:49
 */
public class Temp implements Serializable {
    private static final long serialVersionUID = 590997635549616383L;
    
    private Integer id;
    
    private Integer uid;
    
    private Integer pid;
    
    private Date submitdate;
    
    private Integer state;

    private Integer useful;

    private Integer bad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(Date submitdate) {
        this.submitdate = submitdate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUseful() {
        return useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    @Override
    public String toString() {
        return "Temp{" +
                "id=" + id +
                ", uid=" + uid +
                ", pid=" + pid +
                ", submitdate=" + submitdate +
                ", state=" + state +
                ", useful=" + useful +
                ", bad=" + bad +
                '}';
    }
}