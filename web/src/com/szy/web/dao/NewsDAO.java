package com.szy.web.dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.szy.web.model.News;



public class NewsDAO
{
	SqlManager manager;
	String sql = "";
	ResultSet rs;

	public NewsDAO() throws IOException, ClassNotFoundException
	{
		manager = SqlManager.createInstance();
	}

	/**
	 * 获取新闻内容
	 * @param nid 新闻编号
	 * @return
	 * @throws SQLException
	 */
	public News getNews(int nid) throws SQLException
	{
		sql = "SELECT nid,cid,title,body,source,ptime,imgsrc FROM t_news WHERE nid=? and deleted=false";
		Object[] params = new Object[]{ nid };
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		News news = new News();
		if (rs.next())
		{
			news.setNid(rs.getInt("nid"));
			news.setCid(rs.getInt("cid"));
			news.setTitle(rs.getString("title"));
			news.setBody(rs.getString("body"));
			news.setSource(rs.getString("source"));
			news.setPtime(rs.getString("ptime"));
			news.setImgSrc(rs.getString("imgsrc"));
		}
		manager.closeDB();
		return news;
	}

	/**
	 * 获取指定类别的新闻列表
	 * @param cid 新闻类型
	 * @param startNid 起始编号
	 * @param count 返回数量
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<News> getSpecifyCategoryNews(int cid, int startNid, int count)
			throws SQLException
	{
		ArrayList<News> list = new ArrayList<News>();
		sql = "SELECT nid,cid,title,digest,source,ptime,imgsrc FROM t_news WHERE cid=? ORDER BY ptime desc LIMIT ?,?";
		Object[] params = new Object[]{ cid, startNid, count };
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		while (rs.next())
		{
			News news = new News();
			news.setNid(rs.getInt("nid"));
			news.setCid(rs.getInt("cid"));
			news.setTitle(rs.getString("title"));
			news.setDigest(rs.getString("digest"));
			news.setSource(rs.getString("source"));
			news.setPtime(rs.getString("ptime"));
			news.setImgSrc(rs.getString("imgsrc"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
	
	public ArrayList<News> getSearchNew(String key) throws SQLException
	{
		ArrayList<News> list = new ArrayList<News>();
		sql="SELECT nid,cid,title,digest,source,ptime,imgsrc FROM t_news WHERE title LIKE '%"+key+"%' ORDER BY ptime desc";
	//	Object[] params = new Object[]{key};

		manager.connectDB();
	//	rs = manager.executeQuery(sql, params);
		rs = manager.executeQuery2(sql);
		while (rs.next())
		{
			News news = new News();
			news.setNid(rs.getInt("nid"));
			news.setCid(rs.getInt("cid"));
			news.setTitle(rs.getString("title"));
			news.setDigest(rs.getString("digest"));
			news.setSource(rs.getString("source"));
			news.setPtime(rs.getString("ptime"));
			news.setImgSrc(rs.getString("imgsrc"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
	
	public ArrayList<News> getAllNews() throws SQLException
	{
		ArrayList<News> list = new ArrayList<News>();
		sql="SELECT nid,cid,title,digest,source,ptime,imgsrc FROM t_news  ORDER BY cid";

		manager.connectDB();
	
		rs = manager.executeQuery2(sql);
		while (rs.next())
		{
			News news = new News();
			news.setNid(rs.getInt("nid"));
			news.setCid(rs.getInt("cid"));
			news.setTitle(rs.getString("title"));
			news.setDigest(rs.getString("digest"));
			news.setSource(rs.getString("source"));
			news.setPtime(rs.getString("ptime"));
			news.setImgSrc(rs.getString("imgsrc"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
	
	
	
	
	public void addNews(int cid,String title,String digest,String body,String source,String ptime,String imgsrc) throws SQLException
	{
		sql = "INSERT INTO t_news (cid,title,digest,body,source,ptime,imgsrc) VALUES (?,?,?,?,?,?,?)";
		Object[] params = new Object[] {cid, title, digest,body,source,ptime,imgsrc};
		manager.connectDB();
		manager.executeUpdate(sql, params);
		manager.closeDB();
	}
	
	

	public void delNews(int nid) throws SQLException
	{
		sql = "DELETE FROM t_news WHERE nid=?";
		Object[] params = new Object[] {nid};
		manager.connectDB();
		manager.executeUpdate(sql, params);
		manager.closeDB();
	}
	

}
