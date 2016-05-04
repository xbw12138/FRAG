package com.example.frag_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import c.b.BP;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.GroupsActivity;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_fragment.ContentFragment;
import com.example.frag_fragment.FindFragment;
import com.example.frag_fragment.FriendFragment;
import com.example.frag_fragment.MeFragment;
import com.example.frag_update.IsNeedUpdate;
import com.example.frag_update.ShowUpdateDialog;
import com.example.mysql.AsyncTask_Search_User;
import com.mob.mobapi.MobAPI;
import com.nineoldandroids.view.ViewHelper;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements EMEventListener {
	protected static final String TAG = "MainActivity";
	private TextView unreadLabel;
	private TextView unreadAddressLable;
	private Button[] mTabs;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	private ContentFragment contentFragment;
	private FriendFragment friendFragment;
	private FindFragment findFragment;
	private MeFragment meFragment;
	private TextView title;
	// 抽屉
	private DrawerLayout mDrawerLayout;
	private long mExitTime = 0;
	Dialog dialog;
	public static boolean isConflict = false;
	private static boolean isCurrentAccountRemoved = false;
	private boolean isneedupdate = false;
	private String describe = "";
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;
	private String url = "";
	private ImageView img_refresh;

	public static boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BP.init(this, "8ee4ff4a694xxxxxxxxxxxxxxxxx");
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			DemoApplication.getInstance().logout(null);
			finish();
			startActivity(new Intent(this, ChatLoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			startActivity(new Intent(this, ChatLoginActivity.class));
			return;
		}
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//
		// 沉浸式状态栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_main);
		new AsyncTask_Search_User(MainActivity.this).execute(EMChatManager
				.getInstance().getCurrentUser());

		MobAPI.initSDK(this, "116xxxxxxxxxxx");
		MobclickAgent.updateOnlineConfig(this);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		dialog = ProgressDialogs
				.createLoadingDialog(MainActivity.this, "数据加载中");
		Context context = getApplicationContext();
		XGPushManager.registerPush(context);
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		img_refresh = (ImageView) findViewById(R.id.image_refresh);
		img_refresh.setVisibility(View.VISIBLE);
		// 自动检查有没有新版本 如果有新版本就提示更新
		download();
		initView();
		initViews();
		initEvents();
		initFragment();
		img_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				contentFragment.init();
			}
		});
		try {
			// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
			// ** manually load all local groups and
			// conversations in case we are auto login
			EMGroupManager.getInstance().loadAllGroups();
			EMChatManager.getInstance().loadAllConversations();
			// 处理好友和群组
			processContactsAndGroups();
		} catch (Exception e) {
			e.printStackTrace();
			// 取好友或者群聊失败，不让进入主页面
			return;
		}
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		// EMChatManager.getInstance().addConnectionListener(new
		// MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	private void initFragment() {
		contentFragment = new ContentFragment();
		friendFragment = new FriendFragment();
		findFragment = new FindFragment();
		meFragment = new MeFragment();
		fragments = new Fragment[] { contentFragment, friendFragment,
				findFragment, meFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.id_content, contentFragment)
				.add(R.id.id_content, friendFragment).hide(friendFragment)
				.show(contentFragment).commit();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.textView_title);
		title.setText("首页");
		ImageView img = (ImageView) findViewById(R.id.image);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				OpenLeftMenu();
			}
		});
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_contact);
		mTabs[1] = (Button) findViewById(R.id.btn_friend);
		mTabs[2] = (Button) findViewById(R.id.btn_find);
		mTabs[3] = (Button) findViewById(R.id.btn_me);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	private void initView() {
		// 抽屉
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				Gravity.RIGHT);
	}

	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_contact:
			title.setText("首页");
			img_refresh.setVisibility(View.VISIBLE);
			index = 0;
			break;
		case R.id.btn_friend:
			title.setText("通讯录");
			img_refresh.setVisibility(View.GONE);
			index = 1;
			break;
		case R.id.btn_find:
			title.setText("消息");
			img_refresh.setVisibility(View.GONE);
			index = 2;
			break;
		case R.id.btn_me:
			title.setText("我的");
			img_refresh.setVisibility(View.GONE);
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.id_content, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	private void download() {
		new Thread() {
			public void run() {
				try {
					IsNeedUpdate isNeedUpdate = new IsNeedUpdate(
							MainActivity.this);
					isneedupdate = isNeedUpdate.isNeedUpdate(MainActivity.this);
					describe = isNeedUpdate.getDescribe();
					url = isNeedUpdate.getUrl();
					handler1.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!DemoHXSDKHelper.getInstance().isLogined()) {
			finish();
			return;
		}
		initView();
		initEvents();
		Log.e("TAG", "start onResume~~~");
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}
		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager
				.getInstance()
				.registerEventListener(
						this,
						new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage });
		if (Config.isinit) {
			contentFragment.init();
			Config.isinit = false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("TAG", "start onPause~~~");
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	public void OpenLeftMenu()// onclick右边隐藏菜单
	{

		mDrawerLayout.openDrawer(Gravity.LEFT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.LEFT);
	}

	private void initEvents() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;
				if (drawerView.getTag().equals("LEFT")) {
					float leftScale = 1 - 0.3f * scale;
					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);

				} else {
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// 解锁右边菜单
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
				// 解锁左边菜单
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
			}

		});
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

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			if (isneedupdate) {
				ShowUpdateDialog show = new ShowUpdateDialog();
				show.showUpdateDialog(MainActivity.this, describe, url);
			}
		};
	};

	/*
	 * private boolean isNeedUpdate() { String v = Config.version;
	 * if(v.equals("0")) return false; else { if (v.equals(getVersion())) {
	 * return false; } else { return true; } } } // 获取当前版本的版本号 private String
	 * getVersion() { try { PackageManager packageManager = getPackageManager();
	 * PackageInfo packageInfo = packageManager.getPackageInfo(
	 * getPackageName(), 0); return packageInfo.versionName; } catch
	 * (NameNotFoundException e) { e.printStackTrace(); return "版本号未知"; } }
	 * private void showUpdateDialog() { LayoutInflater layoutInflater
	 * =LayoutInflater.from(this); RelativeLayout layout =
	 * (RelativeLayout)layoutInflater.inflate(R.layout.dialog, null ); final
	 * Dialog dialog = new AlertDialog.Builder(MainActivity.this).create();
	 * dialog.show(); dialog.getWindow().setContentView(layout); TextView
	 * tex=(TextView)layout.findViewById(R.id.dialog_text); TextView
	 * tex1=(TextView)layout.findViewById(R.id.textView_title);
	 * tex.setMovementMethod(ScrollingMovementMethod.getInstance());
	 * tex.setText(Config.Description); tex1.setText(Config.versiontitle);
	 * //确定按钮 Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
	 * btnOK.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if
	 * (Environment.getExternalStorageState().equals(
	 * Environment.MEDIA_MOUNTED)) { //downFile(Config.versionurl); Intent
	 * intent = new Intent(MainActivity.this,UpdateServices.class);
	 * intent.putExtra("Key_App_Name","frag2.apk");
	 * intent.putExtra("Key_Down_Url",Config.versionurl); startService(intent);
	 * } else { Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡",
	 * Toast.LENGTH_SHORT).show(); } dialog.dismiss(); } }); //关闭按钮 ImageButton
	 * btnClose = (ImageButton) layout.findViewById(R.id.dialog_close);
	 * btnClose.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { dialog.dismiss(); } }); }
	 */
	/*
	 * void downFile(final String url) { pBar = new
	 * ProgressDialog(MainActivity.this); //进度条，在下载的时候实时更新进度，提高用户友好度
	 * pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 * pBar.setTitle("正在下载"); pBar.setMessage("请稍候..."); pBar.setProgress(0);
	 * pBar.show(); new Thread() {
	 * 
	 * @SuppressWarnings("deprecation") public void run() { HttpClient client =
	 * new DefaultHttpClient(); HttpGet get = new HttpGet(url); HttpResponse
	 * response; try { response = client.execute(get); HttpEntity entity =
	 * response.getEntity(); int length = (int) entity.getContentLength();
	 * //获取文件大小 Log.d( Integer.toString(length), "s"); pBar.setMax(length);
	 * //设置进度条的总长度 InputStream is = entity.getContent(); FileOutputStream
	 * fileOutputStream = null; if (is != null) { File file = new File(
	 * Environment.getExternalStorageDirectory(), "frag2.apk"); fileOutputStream
	 * = new FileOutputStream(file); byte[] buf = new byte[1024]; int ch = -1;
	 * int process = 0; while ((ch = is.read(buf)) != -1) {
	 * fileOutputStream.write(buf, 0, ch); process += ch;
	 * pBar.setProgress(process); //这里就是关键的实时更新进度了！ }
	 * 
	 * } fileOutputStream.flush(); if (fileOutputStream != null) {
	 * fileOutputStream.close(); } down(); } catch (ClientProtocolException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * }.start(); }
	 * 
	 * void down() { handler1.post(new Runnable() { public void run() {
	 * pBar.cancel(); update(); } }); }
	 * 
	 * void update() {
	 * 
	 * Intent intent = new Intent(Intent.ACTION_VIEW);
	 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 * intent.setDataAndType(Uri.fromFile(new File(Environment
	 * .getExternalStorageDirectory(), "frag2.apk")),
	 * "application/vnd.android.package-archive"); startActivity(intent); }
	 */
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
		UserDao dao = new UserDao(MainActivity.this);
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
	public void onEvent(EMNotifierEvent event) {
		// TODO 自动生成的方法存根
		switch (event.getEvent()) {
		case EventNewMessage: // 鏅�氭秷鎭�
		{
			EMMessage message = (EMMessage) event.getData();

			// 鎻愮ず鏂版秷鎭�
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			refreshUI();
			break;
		}

		case EventOfflineMessage: {
			refreshUI();
			break;
		}

		default:
			break;
		}

	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 鍒锋柊bottom bar娑堟伅鏈鏁�
				updateUnreadLabel();
				if (currentTabIndex == 0) {
				}
			}
		});
	}

	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoApplication.getInstance().logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										ChatLoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoApplication.getInstance().logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										ChatLoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}

	}

	@Override
	public void back(View view) {
		super.back(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (DemoApplication.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = DemoApplication.getInstance()
					.getContactList().get(Constant.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/***
	 * 好友变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = DemoApplication.getInstance()
					.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (currentTabIndex == 1)
				friendFragment.refresh();

		}

		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = DemoApplication.getInstance()
					.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					String st10 = getResources().getString(
							R.string.have_you_removed);
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast.makeText(
								MainActivity.this,
								ChatActivity.activityInstance
										.getToChatUsername() + st10, 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// 刷新ui
					friendFragment.refresh();
					findFragment.refresh();
				}
			});

		}

		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");
		}

	}

	/**
	 * 连接监听listener
	 * 
	 */
	/*
	 * private class MyConnectionListener implements EMConnectionListener {
	 * 
	 * public void onConnected() { runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { mFind.errorItem.setVisibility(View.GONE); }
	 * 
	 * }); }
	 * 
	 * public void onDisconnected(final int error) { final String st1 =
	 * getResources().getString(R.string.Less_than_chat_server_connection);
	 * final String st2 =
	 * getResources().getString(R.string.the_current_network); runOnUiThread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { if(error == EMError.USER_REMOVED){ //
	 * 显示帐号已经被移除 showAccountRemovedDialog(); }else if (error ==
	 * EMError.CONNECTION_CONFLICT) { // 显示帐号在其他设备登陆dialog showConflictDialog();
	 * } else { mFind.errorItem.setVisibility(View.VISIBLE); if
	 * (NetUtils.hasNetwork(MainActivity.this)) mFind.errorText.setText(st1);
	 * else mFind.errorText.setText(st2);
	 * 
	 * } }
	 * 
	 * }); } }
	 */

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
			boolean hasGroup = false;
			for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
				if (group.getGroupId().equals(groupId)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup)
				return;

			// 被邀请
			String st3 = getResources().getString(
					R.string.Invite_you_to_join_a_group_chat);
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + st3));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						findFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							findFragment.refresh();
						if (CommonUtils.getTopActivity(MainActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}
				}
			});
		}

		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						findFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {
			String st4 = getResources().getString(
					R.string.Agreed_to_your_group_chat_application);
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + st4));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						findFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		if (currentTabIndex == 1)
			friendFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = DemoApplication.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
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
		return user;
	}

}
