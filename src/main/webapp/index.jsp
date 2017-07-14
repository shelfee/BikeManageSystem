<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="sun.nio.cs.US_ASCII" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="org.gyf.dao.*" %>
<%@ page import="org.gyf.common.*" %>
<%--
  主页 显示各个管理页面
  表单参数
    typename:主页显示管理页面种类
        location 校园区域信息
        bike 自行车信息
        user 用户信息
        report 报告信息
            from to 为空 显示查询报告表单
            from to 非空 显示查询报告结果

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
</head>
<body>

    <%
        checkLogin(request, response);
    %>
    <table id ="links" cellpadding="1" cellspacing="1" border="1">
        <tr>
            <th><a href="index.jsp?typename=location">校园区域信息</a></th>
            <th><a href="index.jsp?typename=bike">自行车信息</a></th>
            <th><a href="index.jsp?typename=user">用户信息</a></th>
            <th><a href="index.jsp?typename=report">报告信息</a></th>
        </tr>
    </table>

    <%

        if(request.getParameter("typename") == null || request.getParameter("typename").equals("location")) {
            ArrayList<LocationInfo> locationInfos;
            LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
            locationInfos = locationMysqlDao.Query();
            out.println("<table id=\"loc_tab\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n" +
                    "       <tr>\n" +
                    "           <th>区域号</th>\n" +
                    "           <th>车数</th>\n" +
                    "           <th>桩数</th>\n" +
                    "       </tr>");
            for(LocationInfo locationInfo: locationInfos){
                out.println("<tr>");
                out.println("<th>" + locationInfo.getId() + "</th>");
                out.println("<th>" + String.valueOf(locationInfo.getBikeNum()) + "</th>");
                out.println("<th>" + String.valueOf(locationInfo.getStubNum()) + "</th>");
                out.println("<th> <a href = \"removeLocation.jsp?locationID=" + locationInfo.getId() + "\">删除区域</a></th>");
                out.println("<th> <a href = \"manageLocation.jsp?locationID=" + locationInfo.getId() + "\">管理区域</a></th>");
                out.println("</tr>");
            }
            out.println("<tr>\n" +
                    "            <a href = \"addLocationTable.jsp\">增加区域</a>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "    <a href = \"adminLogoutAction.jsp\"> 退出登录</a>");
        }
        else {
            if (request.getParameter("typename").equals("bike")) {
                BikeMysqlDaoImpl bikeMysqlDao = new BikeMysqlDaoImpl();
                ArrayList<BikeInfo> bikeInfos = bikeMysqlDao.Query();
                out.println("<table id=\"loc_tab\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n" +
                        "       <tr>\n" +
                        "           <th>车号</th>\n" +
                        "           <th>所在桩号</th>\n" +
                        "           <th>所在区域号</th>\n" +
                        "           <th>借车人号</th>\n" +
                        "       </tr>");
                for (BikeInfo bikeInfo : bikeInfos) {

                    out.println("<tr>");
                    out.println("<th>" + bikeInfo.getId() + "</th>");
                    if (!bikeInfo.getStub().equals("")) {
                        out.println("<th>" + bikeInfo.getStub() + "</th>");
                        StubMysqlDaoImpl stubMysqlDao = new StubMysqlDaoImpl();
                        StubInfo stubInfo = (StubInfo) stubMysqlDao.Query(bikeInfo.getStub());
                        out.println("<th>" + stubInfo.getLocation() + "</th>");
                        out.println("<th>NULL</th>");
                        out.println("<th> <a href = \"removeBike.jsp?bikeID=" + bikeInfo.getId() + "&locationID="+ stubInfo.getLocation()+"\">删除车</a></th>");
                        out.println("</tr>");
                    } else {


                        out.println("<th>NULL</th>");
                        out.println("<th>NULL</th>");
                        out.println("<th>" + bikeInfo.getUser() + "</th>");
                        out.println("<th>删除车</th>");
                        out.println("</tr>");

                    }
                }
                out.println("    </table>\n" +
                        "\n" +
                        "    <a href = \"adminLogoutAction.jsp\"> 退出登录</a>");

            }

            if (request.getParameter("typename").equals("user")) {
                UserMysqlDaoImpl userMysqlDao = new UserMysqlDaoImpl();
                ArrayList<UserInfo> userInfos = userMysqlDao.Query();
                out.println("<table id=\"loc_tab\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n" +
                        "       <tr>\n" +
                        "           <th>用户名</th>\n" +
                        "           <th>昵称</th>\n" +
                        "           <th>性别</th>\n" +
                        "           <th>余额</th>\n" +
                        "           <th>租车号</th>\n" +
                        "           <th>租车时间</th>\n" +
                        "       </tr>");
                for (UserInfo userInfo : userInfos) {
                    out.println("<tr>");
                    out.println("<th>" + userInfo.getId() + "</th>");
                    out.println("<th>" + userInfo.getNickname() + "</th>");
                    if (userInfo.getGender().equals("M"))
                        out.println("<th>男</th>");
                    else
                        out.println("<th>女</th>");
                    out.println("<th>" + String.valueOf(userInfo.getBalance()) + "</th>");
                    if (userInfo.getBike().equals("")) {
                        out.println("<th>NULL</th>");
                        out.println("<th>NULL</th>");
                        out.println("</tr>");
                    } else {
                        out.println("<th>" + userInfo.getBike() + "</th>");
                        Date date = new Date(userInfo.getTime());
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        out.println("<th>" + format.format(date) + "</th>");
                        out.println("</tr>");

                    }
                }
                out.println("    </table>\n" +
                        "\n" +
                        "    <a href = \"adminLogoutAction.jsp\"> 退出登录</a>");
            }

            if (request.getParameter("typename").equals("report")) {
                if (request.getParameter("from") == null || request.getParameter("to") == null)
                    out.println("    <form action=\"getReports.jsp\" method=\"post\">\n" +
                            "        起始时间:<input type=\"text\" name=\"from\"/><br>\n" +
                            "        终止时间:<input type=\"text\" name=\"to\"/><br>\n" +
                            "        <input type=\"submit\" value=\"submit\">\n" +
                            "    </form>");
                else {
                    ReportMysqlDaoImpl reportMysqlDao = new ReportMysqlDaoImpl();
                    Date from = String2Date(request.getParameter("from"));
                    Date to = String2Date(request.getParameter("to"));
                    if (from == null || to == null || from.compareTo(to) > 0) {
                        response.setCharacterEncoding("utf-8");
                        PrintWriter out1 = response.getWriter();
                        out1.print("<script>alert('非法日期格式 应输入yyyy-mm-dd...');window.location='index.jsp?typename=report'</script>");
                        out1.flush();
                        out1.close();
                    }

                    ArrayList<ReportInfo> reportInfos = reportMysqlDao.Query(from, to);
                    out.println("<table id=\"loc_tab\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n" +
                            "       <tr>\n" +
                            "           <th>报告号</th>\n" +
                            "           <th>报告时间</th>\n" +
                            "           <th>报告者</th>\n" +
                            "           <th>内容</th>\n" +
                            "       </tr>");
                    for (ReportInfo reportInfo : reportInfos) {
                        out.println("<tr>");
                        out.println("<th>" + String.valueOf(reportInfo.getReportID()) + "</th>");
                        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd");

                        out.println("<th>" + sdf.format(reportInfo.getReportTime()) + "</th>");
                        out.println("<th>" + reportInfo.getReporterID() + "</th>");
                        out.println("<th>" + reportInfo.getReportContent() + "</th>");
                        out.println("<th><a href=\"removeReport.jsp?reportID="+String.valueOf(reportInfo.getReportID())+"&from="+request.getParameter("from")+"&to="+request.getParameter("to")+"\">删除报告</a></th>");
                        out.println("</tr>");
                    }
                    out.println("    </table>\n" +
                            "\n" +
                            "    <a href = \"adminLogoutAction.jsp\"> 退出登录</a>");
                }
            }
        }


    %>






</body>
</html>

<%!

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
            response.sendRedirect("adminLoginTable");
            return false;
        } else if (!SessionUserListener.getAdminMap().get(unameg).equals(request.getSession())) {
            popAlert(response);
            response.sendRedirect("adminLoginTable");
            return false;
        } else {
            return true;
        }
    }
%>

<%!
    private void popAlert(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter out =response.getWriter();
        out.print("<script>alert('未登录...');window.location='adminLoginTable.jsp' </script>");
        out.flush();
        out.close();
    }
%>
