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

/**
 * 为客户端提供注销接口
 * 客户端处于登录状态
 * 表单参数
 * username:用户名
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示注销成功否则失败
 */
@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String unameg = (String) request.getSession().getAttribute("username");


        System.out.println(unameg + " logout:" + request.getSession().getId());
        String msg = null;
        //将响应的编码规范成 utf-8
        response.setContentType("text/html;charset=UTF-8");
        //判断此用户是否登陆过
        if(!SessionUserListener.checkIfHasLogin(unameg))
            msg = "Not logged before!";
        else{
            if(SessionUserListener.getUserMap().get(unameg).equals(request.getSession())) {
                SessionUserListener.removeUserSession(unameg);
                msg = "success!" + request.getSession().getId();
            }
            else{
                msg = "Not loged before!";
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
