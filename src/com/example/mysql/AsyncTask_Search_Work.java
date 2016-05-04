package com.example.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.frag_model.ListWorkModel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncTask_Search_Work extends AsyncTask<String, String, String>{
	JSONParsers jsonParser = new JSONParsers();
	private static String url_up = Config_mysql.urlsearch;
	Context context;
	ProgressDialog dialog;
	List<ListWorkModel> list = new ArrayList<ListWorkModel>();
	public AsyncTask_Search_Work(Context context) {
		this.context=context;
	}
/////////////////////////////////////////////////////////////////////////////////////
public interface MysqlListener {                                       		  ///
public void Success();                                                    ///
public void Fail();                                                       ///
} 																			  ///
private MysqlListener mysqlListener=null;                                     ///
public void setMysqlListener(MysqlListener mysqlListener) {                   ///
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
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String message=null;
	    params.add(new BasicNameValuePair("user_phone", p[0]));
	       try{
	    	   JSONArray jsons = jsonParser.makeHttpRequest(url_up,
	                "POST", params);
	    	   int iSize = jsons.length();
	    	   Log.i("######3",iSize+"");
				for (int i = iSize-1; i >=0; i--) {//倒叙查看，最新消息最上显示
					JSONObject json = (JSONObject) jsons.opt(i);
					ListWorkModel model = new ListWorkModel();
					model.setStart_time(json.getString("start_time"));
					model.setEnd_time(json.getString("end_time"));
					model.setWork_describe(json.getString("work_describe"));
					model.setWork_position(json.getString("work_position"));
					model.setFinish(json.getString("is_finish"));
					model.setWorkID(json.getString("ID"));
					model.setWorkSendTime(json.getString("send_time"));
					list.add(model);
					message = json.getString("message");
				}
				Singleton.getInstance().setlistWork(list);
	       }catch(Exception e){
	           e.printStackTrace();    
	           message="NONET";
	           Log.i("######3","%%%%%%%%%%%%");
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
