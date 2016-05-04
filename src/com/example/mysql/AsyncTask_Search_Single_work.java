package com.example.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncTask_Search_Single_work extends AsyncTask<String, String, String>{
	JSONParser jsonParser = new JSONParser();
	private static String url_up = Config_mysql.urlsearchsinglework;
	Context context;
	Dialog dialog;
	public AsyncTask_Search_Single_work(Context context) {
		this.context=context;
	}
/////////////////////////////////////////////////////////////////////////////////////
public interface MysqlListenerx {                                       		  ///
public void Success();                                                    ///
public void Fail();                                                       ///
} 																			  ///
private MysqlListenerx mysqlListener=null;                                     ///
public void setMysqlListenerx(MysqlListenerx mysqlListener) {                   ///
this.mysqlListener = mysqlListener;                                       /// 
}                                                                             ///
///
/////////////////////////////////////////////////////////////////////////////////////
protected void onPreExecute() {
super.onPreExecute();
dialog = Progress_Dialog.CreateProgressDialog(context);
dialog.show();
}
	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... p) {
		// TODO �Զ����ɵķ������
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String message=null;
	    params.add(new BasicNameValuePair("ID", p[0]));
	       try{
	        JSONObject json = jsonParser.makeHttpRequest(url_up,
	                "POST", params);
	        Singleton.getInstance().setIs_finish(json.getString("is_finish"));
	        Singleton.getInstance().setSend_time(json.getString("send_time"));
	        Singleton.getInstance().setStart_time(json.getString("start_time"));
	        Singleton.getInstance().setEnd_time(json.getString("end_time"));
	        Singleton.getInstance().setWork_describe(json.getString("work_describe"));
	        Singleton.getInstance().setWork_position(json.getString("work_position"));
	   
	        message=json.getString("message");
	       }catch(Exception e){
	           e.printStackTrace();    
	           message="NONET";
	       }
		return message;
	}
	@SuppressLint("ShowToast") protected void onPostExecute(String message) {     
		dialog.dismiss();
		if(mysqlListener!=null){
			if (message.equals("YES")) {
				mysqlListener.Success();
			} else {
				mysqlListener.Fail();
			}
		}
		if(message.equals("NONET")){
			Toast.makeText(context, "网络连接失败", 8000).show(); 
		}else if(message.equals("YES")){
			Toast.makeText(context, "获取数据成功", 8000).show(); 
		}else{
			Toast.makeText(context, "获取数据失败", 8000).show(); 
		}
    }
}
