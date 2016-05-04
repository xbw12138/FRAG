package com.example.frag_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.exceptions.EaseMobException;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_model.Get_Set;
import com.example.frag_model.JSONParser;
import com.example.frag_model.SmsContent;
import com.example.frag_model.TimeCountUtil;
import com.example.mysql.AsyncTask_Insert_Work;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ChatRegisterActivity extends BaseActivity {

	private EditText mUsernameET;
	private EditText mPasswordET;
	private EditText mCodeET;
	private EditText mPasswordET2;
	private Button mSendmsgBtn;
	private Button mSignupBtn;
	private Handler mHandler;
	private CheckBox mPasswordCB;
	private CheckBox mPasswordCB2;
	private String currCode;

	private boolean success = false;
	public String phString = "";
	Dialog dialog;
	SmsContent content;
	Get_Set userisexist;
	// mysql
	JSONParser jsonParser = new JSONParser();
	private static String url_up = Config.mysql_url_connect;
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_chat_registers);
		// mob短信验证初始化
		dialog = ProgressDialogs.createLoadingDialog(ChatRegisterActivity.this,
				"数据加载中");
		SMSSDK.initSDK(this, Config.mob_APPKEY, Config.mob_APPSECRET);

		EventHandler eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handlersms.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
		// 启动环信sdk线程
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1000:
					Toast.makeText(getApplicationContext(), "注册成功",
							Toast.LENGTH_SHORT).show();

					break;
				case 1001:
					Toast.makeText(getApplicationContext(), "网络异常，请检查网络！",
							Toast.LENGTH_SHORT).show();
					break;
				case 1002:
					Toast.makeText(getApplicationContext(), "用户已存在！",
							Toast.LENGTH_SHORT).show();
					break;
				case 1003:
					Toast.makeText(getApplicationContext(), "注册失败，无权限",
							Toast.LENGTH_SHORT).show();
					break;
				case 1004:
					Toast.makeText(getApplicationContext(),
							"注册失败: " + (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}
				dialog.dismiss();
			};
		};

		mUsernameET = (EditText) findViewById(R.id.chat_register_username);
		mPasswordET = (EditText) findViewById(R.id.chat_register_password);
		mPasswordET2 = (EditText) findViewById(R.id.chat_register_password2);
		mCodeET = (EditText) findViewById(R.id.chat_register_code);

		mSendmsgBtn = (Button) findViewById(R.id.chat_register_sendmsg_btn);
		mSignupBtn = (Button) findViewById(R.id.chat_register_signup_btn);
		mPasswordCB = (CheckBox) findViewById(R.id.chat_register_password_checkbox);
		mPasswordCB2 = (CheckBox) findViewById(R.id.chat_register_password_checkbox2);
		mCodeET.addTextChangedListener(myWatcher);
		mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB.setChecked(true);
					mPasswordET
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					mPasswordCB.setChecked(false);
					mPasswordET
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});
		mPasswordCB2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB2.setChecked(true);
					mPasswordET2
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					mPasswordCB2.setChecked(false);
					mPasswordET2
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});
		mSendmsgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (TextUtils.isEmpty(mUsernameET.getText().toString())) {
					Toast.makeText(getApplicationContext(), "手机号码不能为空", 1)
							.show();
				} else if (!judgephone(mUsernameET.getText().toString())) {

					Toast.makeText(getApplicationContext(), "请输入正确的手机号码", 1)
							.show();
				} else {
					dialog.show();
					new Thread() {
						public void run() {
							try {
								SMSSDK.getVerificationCode("86", mUsernameET
										.getText().toString());
								phString = mUsernameET.getText().toString();
								success = true;
							} catch (Exception e) {
								e.printStackTrace();
							} finally {

							}
						}
					}.start();
				}
			}
		});
		mSignupBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String userName = mUsernameET.getText().toString().trim();
				final String password = mPasswordET.getText().toString().trim();
				final String password2 = mPasswordET2.getText().toString()
						.trim();
				final String code = mCodeET.getText().toString().trim();

				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(getApplicationContext(), "请输入手机号",
							Toast.LENGTH_SHORT).show();
				} else if (!judgephone(userName)) {
					Toast.makeText(getApplicationContext(), "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "请输入密码",
							Toast.LENGTH_SHORT).show();
				} else if (password.length() < 6) {
					Toast.makeText(getApplicationContext(), "密码长度不小于6位",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password2)) {
					Toast.makeText(getApplicationContext(), "请确认输入密码",
							Toast.LENGTH_SHORT).show();
				} else if (!password.equals(password2)) {
					Toast.makeText(getApplicationContext(), "两次密码输入不一致",
							Toast.LENGTH_SHORT).show();

				} else {
					dialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								// 调用sdk注册方法
								EMChatManager.getInstance()
										.createAccountOnServer(userName,
												password);
								new Up().execute();

								new Thread() {
									public void run() {
										try {
											AsyncTask_Insert_Work in = new AsyncTask_Insert_Work(
													ChatRegisterActivity.this);
											userisexist = in.user_Information(
													mUsernameET.getText()
															.toString().trim(),
													"2020-4-30/00:00",
													"2050-4-30/00:00", "时空穿梭",
													"回到30年前删掉这条任务");
											handler.sendEmptyMessage(0);
										} catch (Exception e) {
											e.printStackTrace();
										} finally {

										}
									}
								}.start();

								mHandler.sendEmptyMessage(1000);
							} catch (final EaseMobException e) {
								// 注册失败
								Log.i("TAG", "getErrorCode:" + e.getErrorCode());
								int errorCode = e.getErrorCode();
								if (errorCode == EMError.NONETWORK_ERROR) {
									mHandler.sendEmptyMessage(1001);
								} else if (errorCode == EMError.USER_ALREADY_EXISTS) {
									mHandler.sendEmptyMessage(1002);
								} else if (errorCode == EMError.UNAUTHORIZED) {
									mHandler.sendEmptyMessage(1003);
								} else {
									Message msg = Message.obtain();
									msg.what = 1004;
									msg.obj = e.getMessage();
									mHandler.sendMessage(msg);
								}
							} finally {

							}
						}
					}).start();
				}
			}
		});
		mSignupBtn.setClickable(false);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!userisexist.Get_isNetWork()) {
					Toast.makeText(getApplicationContext(), "网络连接失败", 8000)
							.show();
				} else if (userisexist.Get_httpjsonsuccess()) {
					Toast.makeText(getApplicationContext(), "发布成功", 8000)
							.show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "发布失败", 8000)
							.show();
				}
				break;
			}
			dialog.dismiss();
		}
	};
	Handler handlersms = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				// new ProgressDialog(ChatRegisterActivity.this).dismiss();
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					if (result == SMSSDK.RESULT_COMPLETE) {
						Toast.makeText(getApplicationContext(), "短信验证成功",
								Toast.LENGTH_SHORT).show();
						mSignupBtn.setClickable(true);
					}
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
					TimeCountUtil timeCountUtil = new TimeCountUtil(
							ChatRegisterActivity.this, 60000, 1000, mSendmsgBtn);
					timeCountUtil.start();
					content = new SmsContent(ChatRegisterActivity.this,
							new Handler(), mCodeET);
					getContentResolver().registerContentObserver(
							Uri.parse("content://sms/"), true, content);
					// dialog.dismiss();
				} else if (event == SMSSDK.RESULT_ERROR) {
					Toast.makeText(getApplicationContext(), "------", 1000)
							.show();
				}
			} else {
				// dialog.dismiss();
				((Throwable) data).printStackTrace();
				Toast.makeText(getApplicationContext(), "错误" + data, 10000)
						.show();
			}
			dialog.dismiss();

		}

	};
	TextWatcher myWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			if (mCodeET.getText().length() == 4 && judgephone(phString)
					&& success) {
				dialog.show();
				new Thread() {
					public void run() {
						try {
							SMSSDK.submitVerificationCode("86", phString,
									mCodeET.getText().toString());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {

						}
					}
				}.start();

			}
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

	public boolean judgephone(String phone) {
		String str = "";
		str = phone;
		String pattern = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		return m.matches();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
		if (content != null)
			this.getContentResolver().unregisterContentObserver(content);
	}

	class Up extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		protected String doInBackground(String... args) {
			String user_phone = mUsernameET.getText().toString();
			String user_password = mPasswordET.getText().toString();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String user_signtime = df.format(new Date());
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_phone", user_phone));
			params.add(new BasicNameValuePair("user_password", user_password));
			params.add(new BasicNameValuePair("user_signtime", user_signtime));
			params.add(new BasicNameValuePair("user_vip", "NO"));
			// getting JSON Object
			// Note that create product url accepts POST method
			try {
				JSONObject json = jsonParser.makeHttpRequest(url_up, "POST",
						params);
				String message = json.getString(TAG_MESSAGE);
				return message;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		protected void onPostExecute(String message) {
			dialog.dismiss();
			// message 为接收doInbackground的返回值
			Toast.makeText(getApplicationContext(), message, 8000).show();
		}
	}

}
