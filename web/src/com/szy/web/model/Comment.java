package com.szy.web.model;


public class Comment
{
	private int comid;
	private int nid;
	private String ptime;
	private String region;
	private String content;
	private int supportCount;
	private int opposeCount;
	private boolean deleted;

	public Comment()
	{
		super();
	}

	public Comment(int comid, int nid, String ptime, String region, String content, int supportCount, int opposeCount, boolean deleted)
	{
		super();
		this.comid = comid;
		this.nid = nid;
		this.ptime = ptime;
		this.region = region;
		this.content = content;
		this.supportCount = supportCount;
		this.opposeCount = opposeCount;
		this.deleted = deleted;
	}

	public int getComid()
	{
		return comid;
	}

	public void setComid(int comid)
	{
		this.comid = comid;
	}

	public int getNid()
	{
		return nid;
	}

	public void setNid(int nid)
	{
		this.nid = nid;
	}

	public String getPtime()
	{
		return ptime;
	}

	public void setPtime(String ptime)
	{
		this.ptime = ptime;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getSupportCount()
	{
		return supportCount;
	}

	public void setSupportCount(int supportCount)
	{
		this.supportCount = supportCount;
	}

	public int getOpposeCount()
	{
		return opposeCount;
	}

	public void setOpposeCount(int opposeCount)
	{
		this.opposeCount = opposeCount;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}
}
