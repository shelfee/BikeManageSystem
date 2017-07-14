package org.gyf.common;

/**
 * 管理者类
 * ID:账号
 * password:密码
 */
public class AdminInfo  extends Info {
    private String ID;
    private String password;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
