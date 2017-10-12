package com.example.frag_activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.applib.model.HXSDKModel;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.activity.BlacklistActivity;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_update.GetVersion;
import com.example.frag_update.IsNeedUpdate;
import com.example.frag_update.NetUtils;
import com.example.frag_update.ShowUpdateDialog;
import com.example.frag_update.UpdateInfo;
import com.example.frag_update.UpdateInfoService;
import com.example.frag_update.UpdateServices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MimeSettingActivity extends Activity implements OnClickListener {

	
    //private TextView activity_setting_back;
	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;
	
	/**
	 * 退出按钮
	 */
	private Button logoutBtn;

	private EMChatOptions chatOptions;
 
	/**
	 * 诊断
	 */
	private LinearLayout llDiagnose;
	private LinearLayout llAbout;
	private UpdateInfo info;
	Dialog dialog;
	private ProgressDialog pBar;
	private boolean isneedupdate=false;
	private String describe="";
	NetUtils net;
	public String url="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//沉浸式状态栏
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//沉浸式状态栏
		setContentView(R.layout.fragment_conversation_settings);
		 dialog = ProgressDialogs.createLoadingDialog(MimeSettingActivity.this,"数据加载中");
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);

		iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView)findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView)findViewById(R.id.iv_switch_close_speaker);
		logoutBtn = (Button) findViewById(R.id.btn_logout);
		if(!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())){
			logoutBtn.setText(getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
		}

		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		
		blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);
		llDiagnose=(LinearLayout) findViewById(R.id.ll_diagnose);
		llAbout=(LinearLayout) findViewById(R.id.II_aboutus);
		blacklistContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		llDiagnose.setOnClickListener(this);
		llAbout.setOnClickListener(this);
		chatOptions = EMChatManager.getInstance().getChatOptions();
		
		HXSDKModel model = HXSDKHelper.getInstance().getModel();
		
		// 震动和声音总开关，来消息时，是否允许此开关打开
		// the vibrate and sound notification are allowed or not?
		if (model.getSettingMsgNotification()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}
		
		// 是否打开声音
		// sound notification is switched on or not?
		if (model.getSettingMsgSound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}
		
		// 是否打开震动
		// vibrate notification is switched on or not?
		if (model.getSettingMsgVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		// 是否打开扬声器
		// the speaker is switched on or not?
		if (model.getSettingMsgSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}

	}
	public void back(View view) {
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				chatOptions.setNotificationEnable(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				chatOptions.setNotificationEnable(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				chatOptions.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				chatOptions.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				chatOptions.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				chatOptions.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				chatOptions.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				chatOptions.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
			break;
		case R.id.btn_logout: //退出登陆
			showLogoutDialog();
			//Config.islogin=false;
			break;
		case R.id.ll_black_list:
			startActivity(new Intent(MimeSettingActivity.this, BlacklistActivity.class));
			break;
		case R.id.ll_diagnose:
			//startActivity(new Intent(MimeSettingActivity.this, DiagnoseActivity.class));
			 Toast.makeText(MimeSettingActivity.this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
				// 自动检查有没有新版本 如果有新版本就提示更新
	    	  dialog.show();
						new Thread() {
							public void run() {
								try {
									if(net.isConnected(MimeSettingActivity.this)){
										//UpdateInfoService updateInfoService = new UpdateInfoService(
										//		MimeSettingActivity.this);
										//info = updateInfoService.getUpDateInfo();
										IsNeedUpdate isNeedUpdate=new IsNeedUpdate(MimeSettingActivity.this);
										isneedupdate=isNeedUpdate.isNeedUpdate(MimeSettingActivity.this);
										describe=isNeedUpdate.getDescribe();
										url=isNeedUpdate.getUrl();
										handler1.sendEmptyMessage(0);
									}
									else
									{
										Thread.sleep(1000);
										//dialog.dismiss();
										handler1.sendEmptyMessage(1);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							};
						}.start();  
			break;
		case R.id.II_aboutus:
			//Config.url=Config.url_aboutus;
	    	//Config.title="关于我们";
			startActivity(new Intent(MimeSettingActivity.this, About_Activity.class));
			break;
			
		default:
			break;
		}

	}
	public void showLogoutDialog() {

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				R.layout.dialog, null);
		final Dialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView tex = (TextView) layout.findViewById(R.id.dialog_text);
		TextView tex1 = (TextView) layout.findViewById(R.id.textView_title);
		tex.setMovementMethod(ScrollingMovementMethod.getInstance());
		tex.setText("确定要注销" + EMChatManager.getInstance().getCurrentUser()
				+ "用户吗？");
		tex1.setText("应用提示");
		// 确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setText("确定");
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logout(new EMCallBack() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						startActivity(new Intent(MimeSettingActivity.this,
								ChatLoginActivity.class));
						finish();
					}

					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
				dialog.dismiss();
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
	public void logout(final EMCallBack callback) {
		// setPassword(null);
		EMChatManager.getInstance().logout(new EMCallBack() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}
		});
	}
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        if(MainActivity.isConflict){
        	outState.putBoolean("isConflict", true);
        }else if(MainActivity.getCurrentAccountRemoved()){
        	outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }
    @SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			switch(msg.what){
			case 0:
			dialog.dismiss();
			ShowUpdateDialog show=new ShowUpdateDialog();
			if (isneedupdate)
				show.showUpdateDialog(MimeSettingActivity.this, describe,url);
			else
				show.showDialog(MimeSettingActivity.this);
			break;
			case 1:
				dialog.dismiss();
				 Toast.makeText(MimeSettingActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
				 break;
			}
		};
	};
	/*private void showdialog()
	{
		LayoutInflater layoutInflater =LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout)layoutInflater.inflate(R.layout.dialog, null );
		final Dialog dialog = new AlertDialog.Builder(MimeSettingActivity.this).create();
	    dialog.show();
	    dialog.getWindow().setContentView(layout);
	    TextView tex=(TextView)layout.findViewById(R.id.dialog_text);
	    TextView tex1=(TextView)layout.findViewById(R.id.textView_title);
	    tex.setText("您使用的是最新版本:"+getVersion()+"版本");
	    tex1.setText("更新提示");
	    Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
	    btnOK.setVisibility(View.INVISIBLE);
	  //关闭按钮
         ImageButton btnClose = (ImageButton) layout.findViewById(R.id.dialog_close);
         btnClose.setOnClickListener(new OnClickListener() {	          
           @Override
           public void onClick(View v) {
              dialog.dismiss();          
           }
         });
	}*/
	/*private void showUpdateDialog() {
		LayoutInflater layoutInflater =LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout)layoutInflater.inflate(R.layout.dialog, null );
		final Dialog dialog = new AlertDialog.Builder(MimeSettingActivity.this).create();
	    dialog.show();
	    dialog.getWindow().setContentView(layout);
	    TextView tex=(TextView)layout.findViewById(R.id.dialog_text);
	    TextView tex1=(TextView)layout.findViewById(R.id.textView_title);
	    tex.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	    tex.setText(info.getDescription());
         //确定按钮
         Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
         btnOK.setOnClickListener(new OnClickListener() {         
           @Override 
           public void onClick(View v) { 
        	   if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					//downFile(info.getUrl());
        		    //Intent intent = new Intent(MimeSettingActivity.this,UpdateServices.class);
	       			//intent.putExtra("Key_App_Name","frag2.apk");
	       			//intent.putExtra("Key_Down_Url",Config.versionurl);						
	       			//startService(intent);
        		   //service();
				} else {
					Toast.makeText(MimeSettingActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
				}
        	   dialog.dismiss();    
           }
         });	 
         //关闭按钮
         ImageButton btnClose = (ImageButton) layout.findViewById(R.id.dialog_close);
         btnClose.setOnClickListener(new OnClickListener() {	          
           @Override
           public void onClick(View v) {
              dialog.dismiss();          
           }
         });
     }*/
	/*private boolean isNeedUpdate() {
		 GetVersion version=new GetVersion();
		if (info.getVersion().equals(version.getVersion(MimeSettingActivity.this))) {
			return false;
		} else {
			return true;
		}
	}*/

	/*// 获取当前版本的版本号
	private String getVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}*/

	/*void downFile(final String url) { 
		pBar = new ProgressDialog(MimeSettingActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.show();
		new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {        
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				 
				 
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength();   //获取文件大小
					Log.d( Integer.toString(length), "s");
                                        pBar.setMax(length);                            //设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"frag2.apk");
						fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];   
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {       
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							pBar.setProgress(process);       //这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	void down() {
		handler1.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "frag2.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
*/
	
}
