package com.example.frag_model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.example.Config.Config;

public class vip {
	
	//mysql
	 JSONParser jsonParser = new JSONParser();
	 private static String url_up ;//= Config.mysql_url_change;
	 private String urls="/change_vip.php";
	 private boolean isVIP=false;
	 public void PayVIP()
	 {
		 new Up().execute(); 
	 }
	 public boolean IsVIP()
	 {
		 return isVIP;
	 }
	 class Up extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	        }
	        protected String doInBackground(String... args) {
	            String user_phone =  EMChatManager.getInstance().getCurrentUser();
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("user_phone", user_phone));
	            params.add(new BasicNameValuePair("user_vip", "YES"));
	           try{
	            JSONObject json = jsonParser.makeHttpRequest(url_up+urls,
	                    "POST", params);
	            String messages=json.getString("message");
	            return messages;
	           }catch(Exception e){
	               e.printStackTrace(); 
	               return "";          
	           }

	        }
	        protected void onPostExecute(String message) {                     
	            if(message.equals("Product successfully created."))
	            	isVIP=true;
	            else
	            	isVIP=false;
	        }
	 }

}
