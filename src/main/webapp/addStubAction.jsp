<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.dao.StubMysqlDaoImpl" %>
<%@ page import="org.gyf.common.StubInfo" %>
<%@ page import="org.gyf.dao.LocationMysqlDaoImpl" %>
<%@ page import="org.gyf.common.LocationInfo" %>
<%--
  动作:加入新的桩
  表单参数
    locationID:所加区域id
    stubID:所加桩id
  执行完跳转到对应区域管理页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入桩动作</title>
</head>
<body>
<%
    if(checkLogin(request, response)){
        String locationID = request.getParameter("locationID");
        String stubID = request.getParameter("stubID");
        StubMysqlDaoImpl stubMysqlDao = new StubMysqlDaoImpl();
        StubInfo stubInfo = (StubInfo) stubMysqlDao.Query(stubID);
        if(stubInfo == null){
            LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
            LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(locationID);
            if(locationInfo!=null){
                locationInfo.setStubNum(locationInfo.getStubNum() + 1);
                stubInfo = new StubInfo();
                stubInfo.setId(stubID);
                stubInfo.setLocation(locationID);
                boolean flag = stubMysqlDao.Insert(stubInfo);
                flag = flag && locationMysqlDao.Update(locationInfo);
                if(flag) {
                    out.print("<script>alert('成功添加桩" + stubID + "');window.location='manageLocation.jsp?locationID="+locationID+"' </script>");
                    out.flush();
                    out.close();
                }
            }
        }
        out.print("<script>alert('添加桩失败');window.location='index.jsp' </script>");
        out.flush();
        out.close();
    };
%>
</body>
</html>
<%!
    private void errorInput(HttpServletResponse response, String cause) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('" + cause + "');window.location='index.jsp' </script>");
        out.flush();
        out.close();
        response.sendRedirect("index.jsp");
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