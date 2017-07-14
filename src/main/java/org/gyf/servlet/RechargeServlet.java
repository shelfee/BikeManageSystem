package org.gyf.servlet;


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
 * 为客户端提供充值接口
 * 客户端处于登录状态
 * 表单参数
 * money:充值金额
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示充值成功否则失败
 */
@WebServlet(name = "RechargeServlet")
public class RechargeServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String unameg = (String) request.getSession().getAttribute("username");
        int money = Integer.valueOf(request.getParameter("money"));
        String msg = "";
        if(SessionUserListener.checkIfHasLogin(unameg) && SessionUserListener.getUserMap().get(unameg).equals(request.getSession())){
            UserMysqlDaoImpl userDao = new UserMysqlDaoImpl();
            UserInfo user = (UserInfo) userDao.Query(unameg);
            //查询不到用户信息或者充值金额小于0失败
            if(user == null || money <= 0)
                msg = "Invalid ID";
            else{
                user.setBalance(user.getBalance() + money);
                boolean flag = userDao.Update(user);
                msg = "success!" + String.valueOf(user.getBalance());
            }
        }
        else{
            msg = "Unlogged account!";
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
