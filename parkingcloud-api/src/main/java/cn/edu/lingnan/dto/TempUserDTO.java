package cn.edu.lingnan.dto;

public class TempUserDTO {

    private Integer toUid;

    private String reason;

    private Integer score;

    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "TempUserDTO{" +
                "toUid=" + toUid +
                ", reason='" + reason + '\'' +
                ", score=" + score +
                '}';
    }
}
