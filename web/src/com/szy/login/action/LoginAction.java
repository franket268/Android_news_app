package com.szy.login.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szy.login.dao.LoginDao;

public class LoginAction extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginAction() {
		super();
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


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		response.setContentType("text/html;chaeset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String path=request.getContextPath();
		PrintWriter out = response.getWriter();
		String username=request.getParameter("username");
		String pswd=request.getParameter("pswd");
		
		try {
			LoginDao loginDao=new LoginDao();
			boolean flag =loginDao.login(username, pswd);
			if(flag){
				request.getSession().setAttribute("username", username);
				request.getSession().setAttribute("pswd", pswd);
             	response.sendRedirect(path+"/main.jsp");
			}
			else{
				out.println("登录失败，5秒后将会调回登录页面");
				response.setHeader("refresh", "5;url=login.jsp");
		
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
