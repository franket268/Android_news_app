<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

 String username=(String)session.getAttribute("username");
 if(username==null)
 {
     response.sendRedirect("login.jsp");
 }

ArrayList<HashMap<String, Object>> newslist= new ArrayList<HashMap<String, Object>>();
newslist=(ArrayList<HashMap<String, Object>>)request.getAttribute("newslist");
if(newslist.size()!=0)
{
int count=newslist.size();
}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'newsD.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="styles.css">


  </head>
  
  <body  background="img/Jianyue10.jpg">
    <div  class="goback">
      <a href="main.jsp">返回</a>
      <a href="<%=path%>/servlet/Logout">退出登录</a>
    </div>
   
  
   <div id=delnews>
   <h1>新闻：</h1> <br>
   <table style="border:solid" cellspacing= "1 ">
   <tr  style="background-color: powderblue">
        <td>新闻Id</td>
        <td>新闻种类</td>
        <td>                新闻标题                                                          </td>
   </tr>
   <%
             
            for(HashMap<String, Object> hashmap :newslist)
            {
                int nid=(Integer)hashmap.get("nid");
                int cid=(Integer)hashmap.get("cid");
                String type=null;
                switch (cid)
                {
                   case 1:type="焦点";break;
                   case 2:type="国内";break;
                   case 3:type="国际";break;
                   case 4:type="军事";break;
                   case 5:type="体育";break;
                   case 6:type="科技";break;
                   case 7:type="汽车";break;
                   case 8:type="财经";break;
                   case 9:type="游戏";break;
                   case 10:type="女人";break;
                   
                }
                
                String title=(String)hashmap.get("title");
   %>  
   <tr>
    <td><%=nid%></td>
    <td><%=type%></td>
    <td><%=title%></td>
   </tr>  
           
   <%        
     }
    %>
   </table>
 
   <br><br>
   

  
   
   
  </body>
</html>
