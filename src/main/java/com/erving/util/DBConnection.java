package com.erving.util;			//指定类所在的包

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.io.*;


public class DBConnection					
{
	private Connection conn = null;			//连接对象



	public  Connection getConn()
	{

		//数据库类型
		int DBType = new Function().StrToInt(getPara("DBType"));
	
		switch(DBType)
		{
			case 1:return(getConnToMySql());
			default:return null;
		}	
	}

	public String getPara(String ParaName) 
	{
		//配置文件名
		String fileName = "/db.properties";
		Properties prop= new Properties();
		try
		{
			InputStream is = getClass().getResourceAsStream(fileName);
			prop.load(is);
			if(is!=null) {
				is.close();
			}
		}
		catch(Exception e) {
			return "Error!";
		}
		return prop.getProperty(ParaName);
	}

    public Connection getConnToMySql()
    {
		//MYSQL Server驱动程序
		String mySqlDriver = getPara("MysqlDriver");
		//MYSQL Server连接字符串
		String mySqlURL = getPara("URL");
		String user = getPara("USER");
		String password = getPara("PASSWORD");
		try{
	    	Class.forName(mySqlDriver);
	    	conn = DriverManager.getConnection(mySqlURL,user,password);
	    	return conn;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		return null;
		    	//return "操作数据库出错，请仔细检查" ;
		    	//System.err.println(e.getMessage());
	    	}
    }

}
