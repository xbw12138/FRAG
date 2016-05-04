package com.example.mysql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.frag_model.Get_Set;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncTask_Insert_Work{
	//mysql
		 JSONParser jsonParser = new JSONParser();
		 private static String url_up = Config_mysql.mysql_url_insert_order;
		 private static final String TAG_MESSAGE = "message";
		 Get_Set USERYETEXIST=new Get_Set();
		 public AsyncTask_Insert_Work(Context context) {
		 }
    public Get_Set user_Information(String a,String b,String c,String d,String e) {
        String user_phone =  a;
        String start_time = b;
        String end_time=c;
        String work_describe=d;
        String work_position=e;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
        String send_time=df.format(new Date());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_phone", user_phone));
        params.add(new BasicNameValuePair("start_time", start_time));
        params.add(new BasicNameValuePair("end_time", end_time));
        params.add(new BasicNameValuePair("work_describe", work_describe));
        params.add(new BasicNameValuePair("work_position", work_position));
        params.add(new BasicNameValuePair("send_time", send_time));
       try{
        JSONObject json = jsonParser.makeHttpRequest(url_up,
                "POST", params);
        String message = json.getString(TAG_MESSAGE);
        if(message.equals("NONET")){
	    	USERYETEXIST.Set_isNetWork(false);
	    }else{
	    	USERYETEXIST.Set_httpjsonsuccess(message.equals("YES"));
	    }
       }catch(Exception t){
           t.printStackTrace();          
       }
       return USERYETEXIST;
    }
}

