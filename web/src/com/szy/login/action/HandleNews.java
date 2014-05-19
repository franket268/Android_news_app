package com.szy.login.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.szy.web.dao.NewsDAO;
import com.szy.web.util.TextUtility;



public class HandleNews extends HttpServlet {

	private ServletConfig config;
	
	/**
	 * Constructor of the object.
	 */
	public HandleNews() {
		super();
	}
	
	final public void init(ServletConfig config) throws ServletException {
	this.config = config;
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;chaeset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		int cid=0;
		String news_title="";
		String news_source="";
		String news_digest="";
		String news_content="";
		String content="";
		String ptime="";
		String news_type="";
		String imgsrc="";
		String fileName="";
	
		
		
		//getRealPath("/"):显示文件在服务器的路径，即 是D:\eclipse\apache-tomcat-7.0.35\webapps\web\
		String path=request.getRealPath("/");
		String dirpath=path+"/img";
		
		//本地图片文件夹路径
		String path2="D:/MyEclipse_Workspaces/MyEclipse 10/web/WebRoot/img";
		
		PrintWriter out = response.getWriter();
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);//检查输入请求是否为multipart表单数据。
	    if (isMultipart == true) {
	       FileItemFactory factory = new DiskFileItemFactory();//为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
	    
	       ServletFileUpload upload = new ServletFileUpload(factory);
	       List<FileItem> items = null;

	 
		    try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
		  		e.printStackTrace();
	    	}
	  	
	       Iterator<FileItem> itr = items.iterator();
	       while (itr.hasNext()) {
	           FileItem item = (FileItem) itr.next();
	           //检查当前项目是普通表单项目还是上传文件。
	           if (item.isFormField()) {//如果是普通表单项目，显示表单内容。
	                 String fieldName = item.getFieldName();
	           if (fieldName.equals("news_title")) 
	           {   
	        	   news_title=item.getString("utf-8");//显示表单内容。
	                
	           }
	           else if (fieldName.equals("news_type")) 
	           {   
	        	   news_type=item.getString("utf-8");//显示表单内容。
	                
	           }
	           else if (fieldName.equals("news_source")) 
	           {   
	        	   news_source=item.getString("utf-8");//显示表单内容。
	                
	           }
	           else if (fieldName.equals("news_digest")) 
	           {   
	        	   news_digest=item.getString("utf-8");//显示表单内容。
	                
	           }
	           else if (fieldName.equals("news_content")) 
	            {   
	        	   news_content=item.getString("utf-8");//显示表单内容。
	                
	            }
	           }
	           
	           else {//如果是上传文件，显示文件名。
	        	       fileName=item.getName();
	        	      File fullFile=new File(fileName);
	                  File savedFile=new File(dirpath,fullFile.getName());
	                   savedFile=new File(path2,fullFile.getName());
	                  try {
						item.write(savedFile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	              //   out.print("the upload file name is  " + fileName);
	                 
	           }
	       }
	       

		
		
		
		 //将news_type转为cid
		 if(news_type.equals("焦点"))
		 {
			 cid=1;
		 }
		 else if(news_type.equals("国内"))
		 {
			 cid=2;
		 }
		 else if(news_type.equals("国际"))
		 {
			 cid=3;
		 }
		 else if(news_type.equals("军事"))
		 {
			 cid=4;
		 }
		 else if(news_type.equals("体育"))
		 {
			 cid=5;
		 }
		 else if(news_type.equals("科技"))
		 {
			 cid=6;
		 }
		 else if(news_type.equals("汽车"))
		 {
			 cid=7;
		 }
		 else if(news_type.equals("财经"))
		 {
			 cid=8;
		 }
		 else if(news_type.equals("游戏"))
		 {
			 cid=9;
		 }
		 else
		 {
			 cid=10;
		 }
		 
		 //获得时间
		 Date now = new Date(); 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
          ptime = dateFormat.format( now ); 
         
         //换地址
         imgsrc="http://10.0.2.2:8080/web/img/"+fileName;
         	 
		 //实现正文的换行
         String[] temp = news_content.split("\n");
         for(int i=0;i<temp.length;i++)
         {
        	 content=content+"<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+temp[i]+"</p>";
             
        
         }
         //out.println(content);
	

		
		try {
			NewsDAO newsDAO = new NewsDAO();
			try {
				newsDAO.addNews(cid, news_title, news_digest, content, news_source, ptime, imgsrc);
				out.println("添加成功，5秒后将会返回添加页面");
				response.setHeader("refresh", "5;url=../newsM.jsp");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
