package cn.edu.lingnan.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Review)实体类
 *
 * @author makejava
 * @since 2020-06-04 11:22:23
 */
public class Review implements Serializable {
    private static final long serialVersionUID = -89754332934087790L;
    
    private Integer id;
    
    private Integer uid;
    
    private Integer pid;
    
    private Date submitdate;
    
    private Integer accuracy;
    
    private Integer easytofind;
    
    private String content;


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

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getEasytofind() {
        return easytofind;
    }

    public void setEasytofind(Integer easytofind) {
        this.easytofind = easytofind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}