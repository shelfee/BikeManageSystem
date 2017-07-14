<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.dao.BikeMysqlDaoImpl" %><%--
  动作:将车从某区域的某桩上删去
  表单参数
    bikeID:车id
    stubID:桩id
    locationID:区域id
    执行完跳转到区域管理页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>删除自行车动作</title>
</head>
<body>
<%
    if(checkLogin(request, response)){
        String id = request.getParameter("bikeID");
        BikeMysqlDaoImpl bikeMysqlDao = new BikeMysqlDaoImpl();
        if(bikeMysqlDao.Remove(id)){
            out.print("<script>alert('成功删除自行车" + id + "');window.location='manageLocation.jsp?locationID="+request.getParameter("locationID")+"' </script>");
        }
        else{
            out.print("<script>alert('未能删除自行车" + id + "');window.location='manageLocation.jsp?locationID="+request.getParameter("locationID")+"' </script>");
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