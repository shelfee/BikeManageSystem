package org.gyf.common;

import java.io.Serializable;

/**
 * Location类
 * id:区域号
 * bikeNum:所停车数目
 * x:x坐标位置
 * y:y坐标位置
 */
public class LocationInfo extends Info{
    private String id;
    private int bikeNum;
    private int stubNum;
    private float x;
    private float y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBikeNum() {
        return bikeNum;
    }

    public void setBikeNum(int bikeNum) {
        this.bikeNum = bikeNum;
    }

    public int getStubNum() {
        return stubNum;
    }

    public void setStubNum(int stubNum) {
        this.stubNum = stubNum;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
