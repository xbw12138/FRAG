package com.example.frag_image_head;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.example.Config.Config;
import com.example.frag_model.JSONParser;

public class Information {
	
	Information_info information=new Information_info();
	JSONParser jsonParser = new JSONParser();
	private static String url_up ;//= Config.mysql_url_search;
	public Information(Context context) {
	}
	@SuppressWarnings("deprecation")
	public Information_info user_Information(String ID)
	{
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("user_phone", ID));
	       try{
	        JSONObject json = jsonParser.makeHttpRequest(url_up,
	                "POST", params);
	        information.set_user_name(json.getString("user_name"));
	        information.set_user_age(json.getString("user_age"));
	        information.set_user_image_head(json.getString("user_image_head"));
	        information.set_user_schoolname(json.getString("user_schoolname"));
	        information.set_user_sex(json.getString("user_sex"));
	        information.set_user_sign(json.getString("user_sign"));
	        information.set_user_signtime(json.getString("user_signtime"));
	        information.set_user_talk(json.getString("user_talk"));
	        information.set_user_bg(json.getString("user_bg"));
	        information.set_user_vip(json.getString("user_vip"));
	        information.set_messages(json.getString("messages"));
	       }catch(Exception e){
	           e.printStackTrace();        
	       }
		return information;
	}
}
