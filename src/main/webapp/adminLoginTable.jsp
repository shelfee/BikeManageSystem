<%--
  表单:管理员登录
  表单参数无
  表单输入
    username:管理员id
    password:管理员密码
  点击提交按钮执行登录动作
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<html>
<head>
    <title>登录表单</title>
</head>
<body>
    <%
        if(checkLogin(request, response)){
            response.sendRedirect("index.jsp");
        }
    %>
    <form action="adminLoginAction.jsp" method="post">
        username:<input type="text" name="username"/><br>
        password:<input type="password" name="password"/><br>
        <input type="submit" value="submit">
    </form>
</body>
</html>

<%!


    private boolean checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String unameg = (String) request.getSession().getAttribute("username");
        if (unameg == null || !SessionUserListener.getAdminMap().containsKey(unameg)) {

            return false;
        } else if (!SessionUserListener.getAdminMap().get(unameg).equals(request.getSession())) {

            return false;
        } else {
            return true;
        }
    }


%>
