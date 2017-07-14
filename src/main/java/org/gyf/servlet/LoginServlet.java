package org.gyf.servlet;


import org.gyf.common.UserInfo;
import org.gyf.dao.MysqlDao;
import org.gyf.dao.UserMysqlDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 为客户端提供登录接口
 * 客户端处于注销状态（不登录）
 * 表单参数
 * username:用户名
 * password:密码
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示登录成功否则失败
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取安卓客户端传来的表单信息
        String unameg = request.getParameter("username");
        String upwdg = request.getParameter("password");

        System.out.println(unameg + " login:" + request.getSession().getId());

        String msg = null;
        //将响应的编码规范成 utf-8
        response.setContentType("text/html;charset=UTF-8");

        try {
            //调用数据访问层代码进行访问
            MysqlDao userDao = new UserMysqlDaoImpl();
            UserInfo user = (UserInfo) userDao.Query(unameg);
            if(user == null)
                msg = "Login Failure!";
            else {
                if(user.getKey().equals(upwdg)){
                    msg = "success!" + request.getSession().getId();
                    //在Session中保存用户名信息方便以后进行其他操作使用，Session直到注销都一直有效
                    request.getSession().setAttribute("username", unameg);
                    SessionUserListener.addUserSession(request.getSession());
                }
                else
                    msg = "Password is wrong!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "Login Failure!";
        }

        //创建输出对象，将处理结果返回到客户端
        PrintWriter outUser = response.getWriter();
        outUser.print(msg);
        //关闭输出对象
        outUser.flush();
        outUser.close();







    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
