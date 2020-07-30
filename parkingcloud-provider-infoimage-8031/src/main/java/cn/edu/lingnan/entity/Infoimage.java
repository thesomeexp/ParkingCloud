package cn.edu.lingnan.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Infoimage)实体类
 *
 * @author makejava
 * @since 2020-06-04 11:28:13
 */
public class Infoimage implements Serializable {
    private static final long serialVersionUID = 238791205162648621L;
    
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

}