<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户登录</title>


  </head>
  
  <body  background="img/Jianyue10.jpg">
   <center>
   <br><br><br><br><br>
   <h1>管理员登录</h1><br><br>
 <form name="form" action="<%=path%>/LoginAction"   method="post">
用户名：<input type="text" name="username"  value=""/> <br>
密  码   ：<input type="password" name="pswd"  value=""/> <br>
<input type="submit" name="submit" value="提交表单" />


   </form>
   </center>
  </body>
</html>
