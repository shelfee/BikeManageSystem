<%--
  动作:管理员登录
  表单参数
    username:管理员id
    password:管理员密码
  执行完跳转到主页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.dao.AdminMysqlDaoImpl" %>
<%@ page import="org.gyf.common.AdminInfo" %>
<html>
<head>
    <title>登录动作</title>
</head>

<body>

    <%
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AdminMysqlDaoImpl adminMysqlDao = new AdminMysqlDaoImpl();
        AdminInfo adminInfo = (AdminInfo) adminMysqlDao.Query(username);

        if(adminInfo!=null){
            String adminPassword=adminInfo.getPassword();
            if(adminPassword.equals(password)){
                session.setAttribute("username",username);
                SessionUserListener.addAdminSession(request.getSession());
                response.sendRedirect("index.jsp?typename=location");
            }
            else {
                popAlert(response);
            }
        }
        else{
            popAlert(response);

        }


    %>
</body>

</html>
<%!
    private void popAlert(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('账号密码错误...');window.location='adminLoginTable.jsp' </script>");
        out.flush();
        out.close();
    }
%>