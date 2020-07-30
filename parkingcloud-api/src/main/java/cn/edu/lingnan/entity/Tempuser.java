package cn.edu.lingnan.entity;

import java.io.Serializable;

public class Tempuser implements Serializable {
    private static final long serialVersionUID = 590997655549616385L;

    private Integer id;

    private Integer uid;

    private Integer tid;

    private Integer useful;

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

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getUseful() {
        return useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    @Override
    public String toString() {
        return "Tempuser{" +
                "id=" + id +
                ", uid=" + uid +
                ", tid=" + tid +
                ", useful=" + useful +
                '}';
    }
}
