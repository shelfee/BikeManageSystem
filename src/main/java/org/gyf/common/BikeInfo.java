package org.gyf.common;

/**
 * Bike类
 * id:车号
 * time:借车时的时间戳 如果没借出则为0
 * stub:所停桩号 如果没借出则为""(空字符串)
 * user:用户id 如果没借出则为""(空字符串)
 */
public class BikeInfo extends Info{
    private String id;
    private long time;
    private String stub;
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStub() {
        return stub;
    }

    public void setStub(String stub) {
        this.stub = stub;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
