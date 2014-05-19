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
    
    <title>My JSP 'newsM.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<link rel="stylesheet" type="text/css" href="styles.css">


  </head>
 
  <body    background="img/Jianyue10.jpg">
    <div  class="goback">
      <a href="main.jsp">返回</a>
      <a href="<%=path%>/servlet/Logout">退出登录</a>
    </div>
     <div id="addnews_form">
    <h1>添加新闻</h1> <br>
    <form name="addNews"  action="<%=path%>/servlet/HandleNews" method="post" enctype="multipart/form-data">
                新闻标题  <input type="text" name="news_title" ><br>
                新闻分类  <input type="text" name="news_type" ><br>   
                新闻来源  <input type="text" name="news_source" ><br>                   
                新闻摘要:<br><textarea name="news_digest"  cols="30" rows="5"></textarea><br><br> 
                 新闻内容:<br><textarea name="news_content" cols="50" rows="7"></textarea><br><br>                
                图片上传<input type="file" name="news_img"><br><br>
          <input type="submit" value="提交新闻">                                      
    </form>
    </div>
  </body>
  
</html>
