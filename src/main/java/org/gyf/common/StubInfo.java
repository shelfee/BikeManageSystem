package org.gyf.common;

/**
 *Stub类
 * id:桩id
 * location:所在区域id
 * bike:所停车id 若无车则为""空字符串
 */
public class StubInfo extends Info{
    private String id;
    private String location;
    private String bike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }
}
