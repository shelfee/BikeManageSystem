package org.gyf.common;

import java.io.Serializable;

/**
 *user类
 * id:用户id
 * balance:剩余金额
 * time:借车时的时间戳 如果没借出则为0
 * key:登录密码
 * bike:所借车id 若无车则为""空字符串
 * age:用户年龄
 * nickname:用户昵称
 * gender:用户性别
 */
public class UserInfo extends Info{
    private String id;
    private int balance;
    private long time;
    private String key = "";
    private String bike;
    private int age;
    private String nickname;
    private String gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
