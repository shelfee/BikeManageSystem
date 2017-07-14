<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="org.gyf.dao.ReportMysqlDaoImpl" %>
<%@ page import="org.gyf.common.ReportInfo" %>
<%--
  动作:删除报告
  表单参数
    reportID:报告id
    from:前一页面from参数
    to:前一页面to参数
  执行完跳转到主页报告页面查询结果页面（表单参数 from to此处相同）
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>删除报告动作</title>
</head>
<body>
    <%
        if(checkLogin(request, response)){
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            try {
                int id = Integer.parseInt(request.getParameter("reportID"));

                ReportMysqlDaoImpl reportMysqlDao = new ReportMysqlDaoImpl();


                if(reportMysqlDao.Remove(id)){
                    out.print("<script>alert('成功删除报告');window.location='index.jsp?typename=report&from=" + from + "&to=" + to + "' </script>");
                }
                else{
                    out.print("<script>alert('未能删除报告');window.location='index.jsp?typename=report&from=" + from + "&to=" + to + "' </script>");
                }
            }catch (Exception exception){
                response.setCharacterEncoding("utf-8");
                PrintWriter out1 =response.getWriter();
                out1.print("<script>alert('无效报告格式...');window.location='index.jsp?typename=report&from=" + from + "&to=" + to + "' </script>");
                out1.flush();
                out1.close();
            }

        }
    %>
</body>
</html>
<%!

    // 判断是否登录，未登录则跳转登录表单页面
    private boolean checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String unameg = (String) request.getSession().getAttribute("username");
        if (unameg == null || !SessionUserListener.getAdminMap().containsKey(unameg)) {
            popAlert(response);
            response.sendRedirect("adminLoginTable.jsp");
            return false;
        } else if (!SessionUserListener.getAdminMap().get(unameg).equals(request.getSession())) {
            popAlert(response);
            response.sendRedirect("adminLoginTable.jsp");
            return false;
        } else {
            return true;
        }
    }

    private void popAlert(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('未登录...');window.location='adminLoginTable.jsp' </script>");
        out.flush();
        out.close();
    }
%>