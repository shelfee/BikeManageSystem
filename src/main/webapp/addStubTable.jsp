<%--
  表单:加入新的桩
  表单参数
    locationID:所加区域id
  表单输入
    stubID:所加桩id
  点击提交按钮执行加桩动作
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>加入桩表单</title>
</head>
<body>
    <%!
        String id;
    %>
    <%
        id = request.getParameter("locationID");
    %>

    <%="<form action=\"addStubAction.jsp?locationID="+id+"\"  method=\"post\">"%>
        桩号:<input type="text" name="stubID"/><br>
        <input type="submit" value="submit">
    </form>
</body>
</html>
