package org.gyf.servlet;

import org.gyf.servlet.*;
import org.gyf.common.UserInfo;
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
 * 为客户端提供注册接口
 * 客户端处于任意状态
 * 表单参数
 * username:用户id
 * password:用户密码
 * nickname:用户昵称
 * gender:用户性别
 * age:用户年龄
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示登录成功否则失败
 */
@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String unameg = request.getParameter("username");
        String key = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");


        //返回给客户端的值
        String msg = "";

        UserMysqlDaoImpl userDao = new UserMysqlDaoImpl();
        UserInfo user = (UserInfo) userDao.Query(unameg);

        if(user != null)
            msg = "Repeat ID";
        else {
            user = new UserInfo();
            try{
                user.setAge(Integer.valueOf(age));
                user.setGender(gender);
                user.setNickname(nickname);
                user.setId(unameg);
                user.setKey(key);
                if(userDao.Insert(user))
                    msg = "success!";
                else{
                    msg = "fail! Insert.";
                }
            }
            catch(NumberFormatException e){
                msg = "Invalid age!";
            }

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
