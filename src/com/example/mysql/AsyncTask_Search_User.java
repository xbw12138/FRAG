package com.example.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncTask_Search_User extends AsyncTask<String, String, String>{
	JSONParser jsonParser = new JSONParser();
	private static String url_up = Config_mysql.urlsearchuser;
	Context context;
	public AsyncTask_Search_User(Context context) {
		this.context=context;
	}
	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... p) {
		// TODO �Զ����ɵķ������
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String message=null;
	    params.add(new BasicNameValuePair("user_phone", p[0]));
	       try{
	        JSONObject json = jsonParser.makeHttpRequest(url_up,
	                "POST", params);
	        Singleton.getInstance().setAdd(json.getString("user_schoolname"));
	        Singleton.getInstance().setAge(json.getString("user_age"));
	        Singleton.getInstance().setName(json.getString("user_name"));
	        Singleton.getInstance().setSex(json.getString("user_sex"));
	        Singleton.getInstance().setSign(json.getString("user_sign"));
	        Singleton.getInstance().setTime(json.getString("user_signtime"));
	        Singleton.getInstance().setBG(json.getString("user_bg"));
	        Singleton.getInstance().setHead(json.getString("user_image_head"));
	        Singleton.getInstance().setVIP(json.getString("user_vip"));
	        message=json.getString("message");
	       }catch(Exception e){
	           e.printStackTrace();    
	           message="NONET";
	       }
		return message;
	}
	@SuppressLint("ShowToast") protected void onPostExecute(String message) {      
		if(message.equals("NONET")){
			Toast.makeText(context, "网络连接失败", 8000).show(); 
		}else if(message.equals("YES")){
			Toast.makeText(context, "获取数据成功", 8000).show(); 
		}else{
			Toast.makeText(context, "获取数据失败", 8000).show(); 
		}
    }
}
