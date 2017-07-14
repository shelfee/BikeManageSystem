<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%--
  动作:管理员注销
  表单参数无
  执行完跳转到登录表单
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登出表单</title>
</head>
<body>
    <%
        if(checkLogin(request, response)){
            String unameg = (String) request.getSession().getAttribute("username");
            SessionUserListener.removeAdminSession(unameg);
            response.sendRedirect("adminLoginTable.jsp");
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