package com.example.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mysql.Info_Type.INFOTYPE;

public class AsyncTask_Change extends AsyncTask<String, String, String> {
	// mysql
	JSONParser jsonParser = new JSONParser();
	private static String url_up = Config_mysql.Get_URLPATH();
	private static String url_s = "";
	private static final String TAG_MESSAGE = "message";
	Config_mysql USERYETEXIST = new Config_mysql();
	Context context;
	ProgressDialog dialog;
	boolean result = false;

	public AsyncTask_Change(Context context) {
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	public Config_mysql Mysql_Change(String ID, String INFO, INFOTYPE TYPE) {
		String id = ID;
		String info = INFO;
		INFOTYPE type = TYPE;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_phone", id));
		switch (type) {
		
		case start_time:
			url_s = INFOTYPE.start_time.getUrl();
			params.add(new BasicNameValuePair("start_time", info));
			break;
		case end_time:
			url_s = INFOTYPE.end_time.getUrl();
			params.add(new BasicNameValuePair("end_time", info));
			break;
		case work_describe:
			url_s = INFOTYPE.work_describe.getUrl();
			params.add(new BasicNameValuePair("work_describe", info));
			break;
		case work_position:
			url_s = INFOTYPE.work_position.getUrl();
			params.add(new BasicNameValuePair("work_position", info));
			break;
			
			
		case user_name:
			url_s = INFOTYPE.user_name.getUrl();
			params.add(new BasicNameValuePair("user_name", info));
			break;
		case user_age:
			url_s = INFOTYPE.user_age.getUrl();
			params.add(new BasicNameValuePair("user_age", info));
			break;
		case user_vip:
			url_s = INFOTYPE.user_vip.getUrl();
			params.add(new BasicNameValuePair("user_vip", info));
			break;
		case user_sex:
			url_s = INFOTYPE.user_sex.getUrl();
			params.add(new BasicNameValuePair("user_sex", info));
			break;
		case user_schoolname:
			url_s = INFOTYPE.user_schoolname.getUrl();
			params.add(new BasicNameValuePair("user_schoolname", info));
			break;
		case user_sign:
			url_s = INFOTYPE.user_sign.getUrl();
			params.add(new BasicNameValuePair("user_sign", info));
			break;
		}
		try {
			JSONObject json = jsonParser.makeHttpRequest(url_up + url_s,
					"POST", params);
			String message = json.getString(TAG_MESSAGE);
			if (message.equals("NONET")) {
				USERYETEXIST.Set_isNetWork(false);
			} else {
				USERYETEXIST.Set_httpjsonsuccess(message.equals("YES"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return USERYETEXIST;
	}
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = Progress_Dialog.CreateProgressDialog(context);
		dialog.show();
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO �Զ����ɵķ������
		String ID = params[0];
		String INFO = params[1];
		String TYPE = params[2];
		Config_mysql LOG;
		LOG = Mysql_Change(ID, INFO, STRING_INFOTYPE(TYPE));
		String message;
		if (!LOG.Get_isNetWork()) {
			message = "1";
		} else if (LOG.Get_httpjsonsuccess()) {
			message = "2";
		} else {
			message = "3";
		}
		return message;
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
	@SuppressLint("ShowToast")
	protected void onPostExecute(String message) {
		dialog.dismiss();
		if(mysqlListener!=null){
			if (message.equals("2")) {
				mysqlListener.Success();
			} else {
				mysqlListener.Fail();
			}
		}
		if (message.equals("1")) {
			Toast.makeText(context, "网络连接失败", 8000).show();
		} else if (message.equals("2")) {
			Toast.makeText(context, "修改成功", 8000).show();
		} else {
			Toast.makeText(context, "修改失败", 8000).show();
		}
	}

	public INFOTYPE STRING_INFOTYPE(String TYPE) {
		INFOTYPE type = null;
		switch (TYPE) {
		case "start_time":
			type = INFOTYPE.start_time;
			break;
		case "end_time":
			type = INFOTYPE.end_time;
			break;
		case "work_describe":
			type = INFOTYPE.work_describe;
			break;
		case "work_position":
			type = INFOTYPE.work_position;
			break;
			
			
		case "user_name":
			type = INFOTYPE.user_name;
			break;
		case "user_age":
			type = INFOTYPE.user_age;
			break;
		case "user_schoolname":
			type = INFOTYPE.user_schoolname;
			break;
		case "user_sex":
			type = INFOTYPE.user_sex;
			break;
		case "user_sign":
			type = INFOTYPE.user_sign;
			break;
		case "user_vip":
			type = INFOTYPE.user_vip;
			break;
		}
		return type;
	}

	public static String INFOTYPE_STRING(INFOTYPE TYPE) {
		String type = null;
		switch (TYPE) {
		
		case start_time:
			type = "start_time";
			break;
		case end_time:
			type = "end_time";
			break;
		case work_describe:
			type = "work_describe";
			break;
		case work_position:
			type = "work_position";
			break;
			
		case user_name:
			type = "user_name";
			break;
		case user_age:
			type = "user_age";
			break;
		case user_schoolname:
			type = "user_schoolname";
			break;
		case user_sex:
			type = "user_sex";
			break;
		case user_sign:
			type = "user_sign";
			break;
		case user_vip:
			type = "user_vip";
			break;
		}
		return type;
	}
}
