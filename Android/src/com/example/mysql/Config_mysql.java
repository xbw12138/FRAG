package com.example.mysql;

public class Config_mysql {
	
	public static String URLPATH="http://127.0.0.1/androidapp/mysql/";
	public static String urlsearch="http://127.0.0.1/androidapp/mysql/work/search_work.php";
	public static String urlsearchuser="http://127.0.0.1/androidapp/mysql/user/search_user.php";
	public static String mysql_url_insert_order="http://127.0.0.1/androidapp/mysql/work/insert_work.php";
	public static String mysql_url_delete_work="http://127.0.0.1/androidapp/mysql/work/delete_work.php";
	public static String mysql_change_finish="http://127.0.0.1/androidapp/mysql/work/change_finish.php";
	public static String urlsearchsinglework="http://127.0.0.1/androidapp/mysql/work/searchsingle.php";
	public boolean httpjsonsuccess=false;
	public boolean isNetWork=true;//http�����Ƿ�ɹ�
	public void Set_httpjsonsuccess(boolean httpjsonsuccess)
	{
		this.httpjsonsuccess=httpjsonsuccess;
	}
	public boolean Get_httpjsonsuccess()
	{
		return httpjsonsuccess;
	}
	public void Set_isNetWork(boolean isNetWork)
	{
		this.isNetWork=isNetWork;
	}
	public boolean Get_isNetWork()
	{
		return isNetWork;
	}
	public void Set_URLPATH(String URLPATH)
	{
		this.URLPATH=URLPATH;
	}
	public static String Get_URLPATH()
	{
		return URLPATH;
	}
	

}
