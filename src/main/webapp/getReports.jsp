<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.gyf.common.ReportInfo" %>
<%@ page import="org.gyf.dao.ReportMysqlDaoImpl" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.StringTokenizer" %>
<%--
  动作:获取用户报告
  表单参数
    from:搜索报告起始时间
    to:搜索报告终止时间
  执行完跳转到主页(报告查询结果页面)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>



<head>
    <title>获取所需日期的报告动作</title>
</head>
<body>

    <%
        if(checkLogin(request, response)){
            String from = request.getParameter("from");
            String to = request.getParameter("to");

            Date fromTime = String2Date(from);
            Date toTime = String2Date(to);

            if(fromTime == null || toTime == null || fromTime.compareTo(toTime) > 0){

                response.setCharacterEncoding("utf-8");
                PrintWriter out1 =response.getWriter();
                out1.print("<script>alert('非法日期格式 应输入yyyy-mm-dd...');window.location='index.jsp?typename=report'</script>");
                out1.flush();
                out1.close();
            }
            else{
                response.sendRedirect("index.jsp?typename=report&from=" + from + "&to=" + to);
            }

        }
    %>

</body>
</html>
<%!

    // 字符串转java.sql.Date对象，失败返回null
    private Date String2Date( String strDate){
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date date = bartDateFormat.parse(strDate);
            return new Date(date.getTime());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
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
    //弹出警告框并跳转登录页面
    private void popAlert(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('未登录...');window.location='adminLoginTable.jsp' </script>");
        out.flush();
        out.close();
    }
%>