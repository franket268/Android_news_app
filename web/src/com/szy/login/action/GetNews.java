package com.szy.login.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szy.web.dao.CommentDAO;
import com.szy.web.dao.NewsDAO;
import com.szy.web.model.News;
import com.szy.web.util.TextUtility;

public class GetNews extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetNews() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;chaeset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	    String st=(String) request.getParameter("status");
	    int  status=TextUtility.String2Int(st);
		PrintWriter out = response.getWriter();
		
		ArrayList<HashMap<String, Object>> newslist= new ArrayList<HashMap<String, Object>>();

		
        try {
			NewsDAO newsDAO=new NewsDAO();
			ArrayList<News> list = new ArrayList<News>();
			list=newsDAO.getAllNews();

			for (News news : list)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();		
				map.put("nid", news.getNid());
				map.put("cid", news.getCid());
				map.put("title", news.getTitle());
				map.put("imgsrc", news.getImgSrc());
				map.put("digest", news.getDigest());
				map.put("source", news.getSource());
				map.put("ptime", news.getPtime());
	            newslist.add(map);


			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        request.setAttribute("newslist",newslist);

        
        if(status==1){
        	request.getRequestDispatcher("../newsDisplay.jsp").forward(request,response);
        }
        else{
        request.getRequestDispatcher("../newsD.jsp").forward(request,response);
        }
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
		

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
