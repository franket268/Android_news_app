package com.example.util;

public class StringUtil
{
	
	public static int String2Int(String str)
	{
		try
		{
			int value = Integer.valueOf(str);
			return value;
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
