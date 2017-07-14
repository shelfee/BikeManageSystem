package org.gyf.servlet;

import org.gyf.common.ReportInfo;
import org.gyf.dao.ReportMysqlDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 为客户端提供向管理员报告异常状态接口
 * 客户端处于登录状态
 * 表单参数
 * report:报告内容
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示借车成功否则失败
 */
@WebServlet(name = "ReportServlet")
public class ReportServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String unameg = (String) request.getSession().getAttribute("username");
        String report = request.getParameter("report");
        String msg = "";
        if(SessionUserListener.checkIfHasLogin(unameg) && SessionUserListener.getUserMap().get(unameg).equals(request.getSession())){
            ReportMysqlDaoImpl reportMysqlDao = new ReportMysqlDaoImpl();
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setReporterID(unameg);
            reportInfo.setReportContent(report);
            if(reportMysqlDao.Insert(reportInfo))
                msg = "success!";
            else
                msg = "Fail!";

        }else{
            msg = "Not Logged!";
        }
        //创建输出对象，将处理结果返回到客户端
        response.setCharacterEncoding("UTF-8");
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
