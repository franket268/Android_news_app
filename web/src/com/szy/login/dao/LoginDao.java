package com.szy.login.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.szy.web.dao.SqlManager;

public class LoginDao {

	 	SqlManager manager;
		String sql = "";
		ResultSet rs;
		
		public LoginDao() throws IOException, ClassNotFoundException
		{
			manager = SqlManager.createInstance();
		}
		
		public boolean login(String username,String pswd) throws SQLException {
			// TODO Auto-generated method stub\
			boolean flag=false;
			String sql="select * from userinfo where username =? and pswd=?";
			Object[] params = new Object[]{username,pswd};
			manager.connectDB();
			rs = manager.executeQuery(sql, params);
			if(rs.next()){
				 flag=true;
			}
			return flag;
		}
		
		
}
