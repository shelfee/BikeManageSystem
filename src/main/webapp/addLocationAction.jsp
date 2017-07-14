<%@ page import="java.sql.*" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.gyf.common.LocationInfo" %>
<%@ page import="org.gyf.dao.LocationMysqlDaoImpl" %>
<%--
  动作:加入新的区域
  表单参数
    locationID:所加区域id
    cord_x:区域x坐标
    cord_y:区域y坐标
  执行完跳转到主页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入区域动作</title>
</head>
<body>

<%
    checkLogin(request, response);

    String locationID = request.getParameter("locationID");
    String cord_x = request.getParameter("cord_x");
    String cord_y = request.getParameter("cord_y");
    try {
        float x = Float.valueOf(cord_x);
        float y = Float.valueOf(cord_y);
        LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
        LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(locationID);
        if(locationInfo!=null)
            errorInput(response, "区域ID已存在");
        else{
            locationInfo = new LocationInfo();
            locationInfo.setId(locationID);
            locationInfo.setX(x);
            locationInfo.setY(y);

            if(locationMysqlDao.Insert(locationInfo)) {
                out.print("<script>alert('成功添加地址" + locationID + "');window.location='index.jsp' </script>");
                response.sendRedirect("index.jsp");
            }
            else
                errorInput(response, "添加地址失败");
        }
    }catch(NumberFormatException e){
        errorInput(response, "坐标输入数字");
    }



%>
</body>

</html>
<%!
    private void errorInput(HttpServletResponse response, String cause) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('" + cause + "');window.location='addLocationTable.jsp' </script>");
        out.flush();
        out.close();
        response.sendRedirect("addLocationTable.jsp");
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

    private void popAlert(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('未登录...');window.location='adminLoginTable.jsp' </script>");
        out.flush();
        out.close();
    }
%>

