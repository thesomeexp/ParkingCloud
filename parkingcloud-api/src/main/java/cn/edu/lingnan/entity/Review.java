package cn.edu.lingnan.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Review)实体类
 *
 * @author makejava
 * @since 2020-06-03 08:08:49
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 139054222688349610L;
    
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

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", uid=" + uid +
                ", pid=" + pid +
                ", submitdate=" + submitdate +
                ", accuracy=" + accuracy +
                ", easytofind=" + easytofind +
                ", content='" + content + '\'' +
                '}';
    }
}