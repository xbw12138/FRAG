package com.example.frag_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_image_head.Information;
import com.example.frag_image_head.Information_info;
import com.example.frag_model.JSONParser;
import com.example.frag_model.ListWorkAdapter;
import com.example.mysql.AsyncTask_Search_User;
import com.example.mysql.AsyncTask_Search_Work;
import com.example.mysql.Singleton;
import com.example.mysql.AsyncTask_Search_Work.MysqlListener;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatLoginActivity extends BaseActivity {

	private EditText mUsernameET;
	private EditText mPasswordET;
	// private TextView mPasswordForgetTV;
	private Button mSigninBtn;
	private Button mForget;
	private TextView mSignupTV;
	private CheckBox mPasswordCB;
	Dialog dialog;
	private boolean autoLogin = false;
	private long mExitTime = 0;
	// Information in;
	// Information_info infos;
	// mysql
	// JSONParser jsonParser = new JSONParser();
	// private static String url_up = Config.mysql_url_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_chat_logins);
		dialog = ProgressDialogs.createLoadingDialog(ChatLoginActivity.this,
				"正在登陆……");
		mUsernameET = (EditText) findViewById(R.id.chat_login_username);
		mPasswordET = (EditText) findViewById(R.id.chat_login_password);
		// mPasswordForgetTV = (TextView)
		// findViewById(R.id.chat_forgot_password);
		mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
		mSignupTV = (TextView) findViewById(R.id.chat_login_signup);
		mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
		mForget = (Button) findViewById(R.id.chat_forgot_password);
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			Log.d("TAG", "已经登陆过");
			autoLogin = true;
			EMGroupManager.getInstance().loadAllGroups();
			EMChatManager.getInstance().loadAllConversations();

			startActivity(new Intent(ChatLoginActivity.this, MainActivity.class));
			ChatLoginActivity.this.finish();
			return;
		}

		mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB.setChecked(true);
					// 动态设置密码是否可见
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
		mForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Toast.makeText(getApplicationContext(), "暂未开放",
						Toast.LENGTH_SHORT).show();
			}

		});
		mSigninBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String userName = mUsernameET.getText().toString().trim();
				final String password = mPasswordET.getText().toString().trim();

				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(getApplicationContext(), "请输入用户名",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "请输入密码",
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.show();
					EMChatManager.getInstance().login(userName, password,
							new EMCallBack() {// 回调
						
								@Override
								public void onSuccess() {
									new AsyncTask_Search_User(
											ChatLoginActivity.this)
											.execute(mUsernameET.getText()
													.toString());
									dialog.dismiss();
									// 登陆成功，保存用户名密码
									DemoApplication.getInstance().setUserName(
											mUsernameET.getText().toString().trim());
									DemoApplication.getInstance().setPassword(
											mPasswordET.getText().toString().trim());
									
									
									/*AsyncTask_Search_Work search = new AsyncTask_Search_Work(ChatLoginActivity.this);
									search.setMysqlListener(new MysqlListener() {
										@Override
										public void Success() {
											// TODO 自动生成的方法存根
										}
										@Override
										public void Fail() {
											// TODO 自动生成的方法存根
											Toast.makeText(ChatLoginActivity.this, "无数据", 8000).show(); 
										}
									});
									search.execute(mUsernameET.getText().toString().trim());
									*/
									
									//Toast.makeText(ChatLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT)
									//		.show();
									//Config.UI = false;// 取消UI更新，对应mainactivity的onresume
									startActivity(new Intent(ChatLoginActivity.this, MainActivity.class));
									ChatLoginActivity.this.finish();
								}

								@Override
								public void onProgress(int progress,
										String status) {

								}

								@Override
								public void onError(int code, String message) {
									if (code == -1005) {
										message = "用户名或密码错误";
									}
									final String msg = message;
									dialog.show();
									runOnUiThread(new Runnable() {
										public void run() {
											Log.d("main", "登陆聊天服务器失败！");
											Toast.makeText(
													getApplicationContext(),
													msg, Toast.LENGTH_SHORT)
													.show();
											dialog.dismiss();
										}
									});
								}
							});
				}
			}
		});

		mSignupTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ChatLoginActivity.this,
						ChatRegisterActivity.class));
			}
		});
	}


	private void processContactsAndGroups() throws EaseMobException {
		// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
		List<String> usernames = EMContactManager.getInstance()
				.getContactUserNames();
		EMLog.d("roster", "contacts size: " + usernames.size());
		Map<String, User> userlist = new HashMap<String, User>();
		for (String username : usernames) {
			User user = new User();
			user.setUsername(username);
			setUserHearder(username, user);
			userlist.put(username, user);
		}
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		// 存入内存
		DemoApplication.getInstance().setContactList(userlist);
		System.out.println("----------------" + userlist.values().toString());
		// 存入db
		UserDao dao = new UserDao(ChatLoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);

		// 获取黑名单列表
		List<String> blackList = EMContactManager.getInstance()
				.getBlackListUsernamesFromServer();
		// 保存黑名单
		EMContactManager.getInstance().saveBlackList(blackList);

		// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
		EMGroupManager.getInstance().getGroupsFromServer();
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}
	// 按两次返回退出
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(getApplicationContext(), R.string.action_quit_c,
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
}
