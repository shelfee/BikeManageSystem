<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="org.gyf.dao.LocationMysqlDaoImpl" %>
<%@ page import="org.gyf.common.LocationInfo" %>
<%--
  动作:删除新的区域
  表单参数
    locationID:所加区域id
  执行完跳转到主页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>删除区域动作</title>
</head>
<body>
    <%
        if(checkLogin(request, response)){
            String id = request.getParameter("locationID");
            LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
            if(locationMysqlDao.Remove(id)){
                out.print("<script>alert('成功删除区域" + id + "');window.location='index.jsp' </script>");
            }
            else{
                out.print("<script>alert('未能删除区域" + id + "');window.location='index.jsp' </script>");
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