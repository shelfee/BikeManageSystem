<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.gyf.dao.StubMysqlDaoImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.gyf.common.StubInfo" %>
<%@ page import="java.io.PrintWriter" %>
<%--
  管理区域页面
  表单参数
    locationID:区域id
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理区域内桩和自行车</title>
</head>
<body>
    <%!
        ArrayList<StubInfo> stubInfos = new ArrayList<>();
        String id;
    %>
    <%
        if(checkLogin(request, response)) {
            id = request.getParameter("locationID");
            StubMysqlDaoImpl stubMysqlDao = new StubMysqlDaoImpl();
            stubInfos = stubMysqlDao.Query("LocationID", id);
        }
    %>
    <table id="loc_tab" cellpadding="1" cellspacing="1" border="1">
        <tr>
            <th>桩号</th>
            <th>车号</th>
        </tr>
        <%
            for(StubInfo stubInfo: stubInfos){
                String bike = stubInfo.getBike();
                out.println("<tr>");
                out.println("<th>" + stubInfo.getId() + "</th>");

                if(bike.equals("")) {
                    out.println("<th>NULL</th>");
                    out.println("<th><a href=addBikeTable.jsp?stubID=" + stubInfo.getId() + "&locationID="+id+">添加自行车</a></th>");
                    out.println("<th><a href=removeStub.jsp?stubID=" + stubInfo.getId() + "&locationID="+id+">删除桩</a></th>");
                }
                else{
                    out.println("<th>" + bike + "</th>");
                    out.println("<th><a href=removeBike.jsp?bikeID=" + stubInfo.getBike() + "&locationID="+id+">删除自行车</a></th>");
                    out.println("<th>删除桩</th>");
                }
            }
        %>
        <tr>
            <%
                out.println("<a href = \"addStubTable.jsp?locationID=" + id + "\">增加桩</a></th>");
            %>
        </tr>

    </table>
    <a href = "index.jsp"> 返回主页</a>
    <a href = "adminLogoutAction.jsp"> 退出登录</a>

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