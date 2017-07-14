<%--
  表单:将车加到某区域的某桩上
  表单参数
    stubID:所加桩id
    locationID:所加区域id
  表单输入
    bikeID:所加车id
  点击提交按钮执行加自行车动作
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入自行车表单</title>
</head>
<body>

<%
    out.println("<form action=\"addBikeAction.jsp?stubID=" + request.getParameter("stubID") + "&locationID="  + request.getParameter("locationID") + "\" method=\"POST\">");
%>
    自行车号:<input type="text" name="bikeID"/><br>
    <input type="submit" value="submit">
</form>
</body>
</html>
