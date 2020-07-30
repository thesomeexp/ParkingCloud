package cn.edu.lingnan.vo;

import java.util.Date;

public class TempUserVO {
    private static final long serialVersionUID = 590887638549656383L;

    private Integer tid;

    private Integer uid;

    private Integer pid;

    private Date submitdate;

    private Integer state;

    private Integer useful;

    private Integer bad;

    private Integer myOpt;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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

    public Integer getMyOpt() {
        return myOpt;
    }

    public void setMyOpt(Integer myOpt) {
        this.myOpt = myOpt;
    }

    @Override
    public String toString() {
        return "TempUserVO{" +
                "tid=" + tid +
                ", uid=" + uid +
                ", pid=" + pid +
                ", submitdate=" + submitdate +
                ", state=" + state +
                ", useful=" + useful +
                ", bad=" + bad +
                ", myOpt=" + myOpt +
                '}';
    }
}
