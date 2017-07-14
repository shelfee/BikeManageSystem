<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="org.gyf.dao.BikeMysqlDaoImpl" %>
<%@ page import="org.gyf.common.BikeInfo" %>
<%@ page import="org.gyf.dao.StubMysqlDaoImpl" %>
<%@ page import="org.gyf.common.StubInfo" %>
<%@ page import="org.gyf.dao.LocationMysqlDaoImpl" %>
<%@ page import="org.gyf.common.LocationInfo" %><%--
  动作:将车加到某区域的某桩上
  表单参数
    bikeID:所加车id
    stubID:所加桩id
    locationID:所加区域id
    执行完跳转到区域管理页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入自行车动作</title>
</head>
<body>
    <%
        String bikeID = request.getParameter("bikeID");
        String stubID = request.getParameter("stubID");
        String locationID = request.getParameter("locationID");
        if(checkLogin(request, response)){

            BikeMysqlDaoImpl bikeMysqlDao = new BikeMysqlDaoImpl();
            BikeInfo bikeInfo = (BikeInfo) bikeMysqlDao.Query(bikeID);
            if(bikeInfo == null){
                StubMysqlDaoImpl stubMysqlDao = new StubMysqlDaoImpl();
                StubInfo stubInfo = (StubInfo) stubMysqlDao.Query(stubID);
                if(stubInfo!=null && stubInfo.getBike().equals("")){
                    bikeInfo = new BikeInfo();
                    bikeInfo.setId(bikeID);
                    bikeInfo.setStub(stubID);
                    boolean flag =  bikeMysqlDao.Insert(bikeInfo);
                    stubInfo.setBike(bikeID);
                    flag = flag && stubMysqlDao.Update(stubInfo);
                    LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
                    LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(stubInfo.getLocation());
                    locationInfo.setBikeNum(locationInfo.getBikeNum() + 1);
                    flag = flag && locationMysqlDao.Update(locationInfo);
                    if(flag) {
                        out.print("<script>alert('成功添加自行车" + bikeID + "');window.location='manageLocation.jsp?locationID="+locationID+"'</script>");
                        out.flush();
                        out.close();
                    }
                }
            }
            out.print("<script>alert('添加自行车失败');window.location='manageLocation.jsp?locationID="+locationID+"' </script>");
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
        out.print("<script>alert('" + cause + "');window.location='addBikeTable.jsp' </script>");
        out.flush();
        out.close();
        response.sendRedirect("addBikeTable.jsp");
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
