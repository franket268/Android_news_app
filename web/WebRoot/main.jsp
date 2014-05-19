<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 String username=(String)session.getAttribute("username");
 if(username==null)
 {
     response.sendRedirect("login.jsp");
 }

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="styles.css">


  </head>
  
  <body  background="img/Jianyue10.jpg">
     <div  class="goback">
      <a href="<%=path%>/servlet/Logout">退出登录</a>
    </div>
  <div id="main">
      <a href="<%=path%>/servlet/GetNews?status=1" >查看所有新闻</a><br>
     <a href="newsM.jsp" >添加新闻</a><br>
     <a href="<%=path%>/servlet/GetNews?status=2" >删除新闻</a><br>
     
 </div>
  </body>
</html>
