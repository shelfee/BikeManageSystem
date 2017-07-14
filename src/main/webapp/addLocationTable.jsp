<%@ page import="org.gyf.servlet.SessionUserListener" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%--
  表单:加入新的区域
  表单参数无
  表单输入
    locationID:所加区域id
    cord_x:所加区域x坐标
    cord_y:所加区域y坐标
  点击提交按钮执行加区域动作
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入区域表单</title>
</head>
<body>

<form action="addLocationAction.jsp" method="post">
    区域号:<input type="text" name="locationID"/>x坐标:<input type="text" name="cord_x"/>y坐标:<input type="text" name="cord_y"/><br>
    <input type="submit" value="submit">
</form>

</body>
</html>
