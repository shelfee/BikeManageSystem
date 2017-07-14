package org.gyf.servlet;


import org.gyf.common.UserInfo;
import org.gyf.dao.MysqlDao;
import org.gyf.dao.UserMysqlDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * 向安卓客户端提供用户信息的接口
 * 客户端处于登录状态
 * 表单参数无
 * 输出对象：UserInfo
 * 接收到对象为空则表示服务器端查询失败
 *
 */
@WebServlet(name = "UserInfoServlet")
public class UserInfoServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String unameg = (String) request.getSession().getAttribute("username");
        System.out.println(unameg + "  userInfo:" + request.getSession().getId());
        UserInfo user  = null;
        if(SessionUserListener.checkIfHasLogin(unameg) && SessionUserListener.getUserMap().get(unameg).equals(request.getSession())){
            //将响应的编码规范成 utf-8
            response.setContentType("text/html;charset=UTF-8");
            try {
                //调用数据访问层代码进行访问
                MysqlDao userDao = new UserMysqlDaoImpl();
                user = (UserInfo) userDao.Query(unameg);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        //创建输出对象，将处理结果返回到客户端
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outUser = response.getOutputStream();
        ObjectOutputStream objectUser = new ObjectOutputStream(outUser);
        objectUser.writeObject(user);
        objectUser.close();
        //关闭输出对象
        outUser.flush();
        outUser.close();



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
