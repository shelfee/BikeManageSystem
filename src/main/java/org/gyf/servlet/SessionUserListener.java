package org.gyf.servlet;
/**
 *  保存用户和管理员登录信息
 *  userMap:保存用户登录会话 键:用户id 值:登录Session对象
 *  adminMap:保存管理员登录会话 键:管理员id 值:登录Session对象
 */

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.*;

@WebListener()
public class SessionUserListener implements HttpSessionListener {

    // key为sessionId，value为HttpSession，使用static，定义静态变量，使之程序运行时，一直存在内存中。
    //private static java.util.Map<String, HttpSession> sessionMap = new java.util.concurrent.ConcurrentHashMap<String, HttpSession>(500);
    private static java.util.Map<String, HttpSession> userMap = new java.util.concurrent.ConcurrentHashMap<String, HttpSession>(500);
    private static java.util.Map<String, HttpSession> adminMap = new java.util.concurrent.ConcurrentHashMap<String, HttpSession>(500);
    /**
     * HttpSessionListener中的方法，在创建session
     */
    public void sessionCreated(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    }

    /**
     * HttpSessionListener中的方法，回收session时,删除sessionMap中对应的session
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        if(userMap.containsKey(event.getSession().getAttribute("username")))
            getUserMap().remove((String)event.getSession().getAttribute("username"));
        else{
            if(adminMap.containsKey(event.getSession().getAttribute("username")))
                adminMap.remove((String)event.getSession().getAttribute("username"));
        }
    }

    /**
     * 移除用户Session 注销时调用
     */
    public synchronized static void removeUserSession(String userId) {
        if(getUserMap().containsKey(userId)) {
            getUserMap().get(userId).invalidate();
            getUserMap().remove(userId);
        }
    }
    public synchronized static void removeAdminSession(String userId) {
            if(adminMap.containsKey(userId)){
                adminMap.get(userId).invalidate();
                adminMap.remove(userId);
            }
    }

    /**
     * 增加用户到session集合中 登录时调用
     */
    public static void addUserSession(HttpSession session) {
        getUserMap().put((String)session.getAttribute("username"), session);
        System.out.println("user:" + String.valueOf(userMap.size()));
    }

    public static void addAdminSession(HttpSession session) {
        adminMap.put((String)session.getAttribute("username"), session);
        System.out.println("admin:" + String.valueOf(adminMap.size()));
    }

    public static boolean containsKey(String key) {
        return getUserMap().containsKey(key);
    }

    /**
     * 判断该用户是否已重复登录，使用
     * 同步方法，只允许一个线程进入，才好验证是否重复登录
     * @param userID
     * @return
     */
    public synchronized static boolean checkIfHasLogin(String userID) {
        return userMap.containsKey(userID);

    }

    /**
     * 获取在线的sessionMap
     */
    public static Map<String, HttpSession> getUserMap() {
        return userMap;
    }
    public static Map<String, HttpSession> getAdminMap() {
        return adminMap;
    }

}
