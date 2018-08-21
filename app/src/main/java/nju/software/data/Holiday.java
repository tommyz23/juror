package nju.software.data;

/**
 * 请假记录类
 * Created by Administrator on 2018/7/30.
 */

public class Holiday {
    private String startTime;
    private String endTime;
    private String reason;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Holiday() {
    }

    public Holiday(String startTime, String endTime, String reason) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
    }
}
