package com.example.frag_activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.SMSSDK;

import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_image_head.FileUtil;
import com.example.frag_image_head.NetUtil;
import com.example.frag_image_head.SelectPicPopupWindow;
import com.example.frag_model.JSONParser;
import com.example.mysql.AsyncTask_Change;
import com.example.mysql.AsyncTask_Change.MysqlListener;
import com.example.mysql.Info_Type.INFOTYPE;
import com.example.mysql.Singleton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MimeDetailActivity extends Activity implements OnClickListener {
	// private TextView detail_back;
	private RelativeLayout sign_rl;
	private RelativeLayout age_rl;
	private RelativeLayout nickname_rl;
	private RelativeLayout signtime_rl;
	private RelativeLayout phone_rl;
	private RelativeLayout sex_rl;
	private RelativeLayout school_rl;
	private RelativeLayout bg_rl;
	private TextView age_tv;
	private TextView sex_tv;
	private TextView school_tv;
	private TextView signtime_tv;
	private TextView phone_tv;
	private TextView nickname_tv;
	private TextView sign_tv;

	private TextView age_tx;
	private TextView sex_tx;
	private TextView school_tx;
	private TextView signtime_tx;
	private TextView phone_tx;
	private TextView nickname_tx;
	private TextView sign_tx;

	private EditText edit;
	private TextView charnum_tx;
	private int charnum = 0;
	private boolean btn = true;
	Dialog dialogs;
	Dialog dialogss;
	// private ImageButton boy;
	// private ImageButton girl;
	private String sexxx = "男";
	// private ImageView line1;
	// private ImageView line2;
	private SelectPicPopupWindow menuWindow; // 自定义的名片背景编辑弹出框
	private static final int REQUESTCODE_PICK = 0; // 相册选图标记
	private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
	private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
	private String urlpath; // 图片本地路径
	private String resultStr = ""; // 服务端返回结果集
	private String imgUrl = Config.uploadbgUrl;
	// mysql
	//JSONParser jsonParser = new JSONParser();
	//private static String url_up = Config.mysql_url_change;
	//private String urls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.mimedetail);
		dialogs = ProgressDialogs.createLoadingDialog(MimeDetailActivity.this,
				"正在修改……");
		dialogss = ProgressDialogs.createLoadingDialog(MimeDetailActivity.this,
				"背景上传中");
		initView();
	}

	private void initView() {
		// detail_back=(TextView)findViewById(R.id.mimedetail_back);
		// detail_back.setOnClickListener(this);

		sign_rl = (RelativeLayout) findViewById(R.id.sign_rl);
		age_rl = (RelativeLayout) findViewById(R.id.age_rl);
		nickname_rl = (RelativeLayout) findViewById(R.id.nickname_rl);
		sex_rl = (RelativeLayout) findViewById(R.id.sex_rl);
		school_rl = (RelativeLayout) findViewById(R.id.school_rl);
		signtime_rl = (RelativeLayout) findViewById(R.id.signtime_rl);
		phone_rl = (RelativeLayout) findViewById(R.id.phone_rl);
		bg_rl = (RelativeLayout) findViewById(R.id.bg_rl);

		sign_rl.setOnClickListener(this);
		age_rl.setOnClickListener(this);
		nickname_rl.setOnClickListener(this);
		sex_rl.setOnClickListener(this);
		school_rl.setOnClickListener(this);
		signtime_rl.setOnClickListener(this);
		phone_rl.setOnClickListener(this);
		bg_rl.setOnClickListener(this);

		sex_tv = (TextView) findViewById(R.id.sex_tx);
		age_tv = (TextView) findViewById(R.id.age_tx);
		sign_tv = (TextView) findViewById(R.id.sign_tx);
		signtime_tv = (TextView) findViewById(R.id.signtime_tx);
		school_tv = (TextView) findViewById(R.id.school_tx);
		phone_tv = (TextView) findViewById(R.id.phone_tx);
		nickname_tv = (TextView) findViewById(R.id.nickname_tx);

		sex_tv.setText(Singleton.getInstance().getSex());
		age_tv.setText(Singleton.getInstance().getAge());
		sign_tv.setText(Singleton.getInstance().getSign());
		signtime_tv.setText(Singleton.getInstance().getTime());
		school_tv.setText(Singleton.getInstance().getAdd());
		phone_tv.setText(EMChatManager.getInstance().getCurrentUser());
		nickname_tv.setText(Singleton.getInstance().getName());

		sex_tx = (TextView) findViewById(R.id.tx2);
		age_tx = (TextView) findViewById(R.id.tx3);
		sign_tx = (TextView) findViewById(R.id.tx4);
		// signtime_tx=(TextView)findViewById(R.id.tx7);
		school_tx = (TextView) findViewById(R.id.tx6);
		// phone_tx=(TextView)findViewById(R.id.tx5);
		nickname_tx = (TextView) findViewById(R.id.tx1);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sex_rl:
			charnum=1;
			showChangeDialog(1, "性别", sex_tv.getText().toString(), 1);
			break;
		case R.id.age_rl:
			charnum=3;
			showChangeDialog(3, "年龄", age_tv.getText().toString(), 2);
			
			break;

		case R.id.sign_rl:
			charnum=14;
			showChangeDialog(14, "签名", sign_tv.getText().toString(), 3);
			break;
		case R.id.school_rl:
			charnum=14;
			showChangeDialog(14, "住址", school_tv.getText().toString(), 4);
			
			break;
		case R.id.nickname_rl:
			charnum=14;
			showChangeDialog(14, "昵称", nickname_tv.getText().toString(), 5);

			break;
		case R.id.signtime_rl:
			Toast.makeText(MimeDetailActivity.this, "不支持修改", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.phone_rl:
			Toast.makeText(MimeDetailActivity.this, "不支持修改", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	public void showChangeDialog(final int charnum, final String title,
			String context,final int id) {

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				R.layout.change_mes_dialog, null);
		final Dialog dialog = new AlertDialog.Builder(MimeDetailActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		TextView tex1 = (TextView) layout.findViewById(R.id.textView_title);
		tex1.setText(title);
		edit = (EditText) layout.findViewById(R.id.dialog_text);
		edit.setText(context);
		edit.addTextChangedListener(myWatcher);
		charnum_tx = (TextView) layout.findViewById(R.id.charnum_tx);
		// 确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);

		btnOK.setText("更新");
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit.getText().length() > charnum) {
					Toast.makeText(MimeDetailActivity.this, "字数超限",
							Toast.LENGTH_SHORT).show();
				} else {
					switch (id) {
					case 3:
						AsyncTask_Change change = new AsyncTask_Change(
								MimeDetailActivity.this);
						change.setMysqlListener(new MysqlListener() {
							@Override
							public void Success() {
								// TODO 自动生成的方法存根
								sign_tv.setText(edit.getText().toString());
								Singleton.getInstance().setSign(edit.getText().toString());
								dialog.dismiss();
							}
							@Override
							public void Fail() {
								// TODO 自动生成的方法存根
							}
						});
						change.execute(EMChatManager.getInstance().getCurrentUser(), edit.getText()
								.toString(), AsyncTask_Change
								.INFOTYPE_STRING(INFOTYPE.user_sign));
						break;
					case 2:
						AsyncTask_Change change1 = new AsyncTask_Change(
								MimeDetailActivity.this);
						change1.setMysqlListener(new MysqlListener() {
							@Override
							public void Success() {
								// TODO 自动生成的方法存根
								age_tv.setText(edit.getText().toString());
								Singleton.getInstance().setAge(edit.getText().toString());
								dialog.dismiss();
							}
							@Override
							public void Fail() {
								// TODO 自动生成的方法存根
							}
						});
						change1.execute(EMChatManager.getInstance().getCurrentUser(), edit.getText()
								.toString(), AsyncTask_Change
								.INFOTYPE_STRING(INFOTYPE.user_age));
						break;
					case 5:
						AsyncTask_Change change2 = new AsyncTask_Change(
								MimeDetailActivity.this);
						change2.setMysqlListener(new MysqlListener() {
							@Override
							public void Success() {
								// TODO 自动生成的方法存根
								nickname_tv.setText(edit.getText().toString());
								Singleton.getInstance().setName(edit.getText().toString());
								dialog.dismiss();
							}
							@Override
							public void Fail() {
								// TODO 自动生成的方法存根
							}
						});
						change2.execute(EMChatManager.getInstance().getCurrentUser(), edit.getText()
								.toString(), AsyncTask_Change
								.INFOTYPE_STRING(INFOTYPE.user_name));
						break;
					case 1:
						AsyncTask_Change change3 = new AsyncTask_Change(
								MimeDetailActivity.this);
						change3.setMysqlListener(new MysqlListener() {
							@Override
							public void Success() {
								// TODO 自动生成的方法存根
								sex_tv.setText(edit.getText().toString());
								Singleton.getInstance().setSex(edit.getText().toString());
								dialog.dismiss();
							}
							@Override
							public void Fail() {
								// TODO 自动生成的方法存根
							}
						});
						change3.execute(EMChatManager.getInstance().getCurrentUser(), edit.getText()
								.toString(), AsyncTask_Change
								.INFOTYPE_STRING(INFOTYPE.user_sex));
						break;
					case 4:
						AsyncTask_Change change4 = new AsyncTask_Change(
								MimeDetailActivity.this);
						change4.setMysqlListener(new MysqlListener() {
							@Override
							public void Success() {
								// TODO 自动生成的方法存根
								school_tv.setText(edit.getText().toString());
								Singleton.getInstance().setAdd(edit.getText().toString());
								dialog.dismiss();
							}
							@Override
							public void Fail() {
								// TODO 自动生成的方法存根
							}
						});
						change4.execute(EMChatManager.getInstance().getCurrentUser(), edit.getText()
								.toString(), AsyncTask_Change
								.INFOTYPE_STRING(INFOTYPE.user_schoolname));
						break;
					}
				}

			}
		});
		// 关闭按钮
		ImageButton btnClose = (ImageButton) layout
				.findViewById(R.id.dialog_close);
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	TextWatcher myWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			int len = charnum - edit.getText().length();
			if (len >= 0)
				charnum_tx.setText("您还可以输入" + len + " \\ " + charnum);
			else
				charnum_tx.setText("输入超限");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO 自动生成的方法存根
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO 自动生成的方法存根
		}
	};

	public void back(View view) {
		finish();
	}
}