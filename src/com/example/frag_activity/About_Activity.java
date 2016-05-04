package com.example.frag_activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_update.UpdateInfo;

public class About_Activity extends Activity {
	private UpdateInfo info;
	private ProgressDialog pBar;
	private ScrollView _textScrollView;
	private Button btn1, btn2, btn3;
	private WebView web1;
	Dialog dialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.menu_activity_about);
		dialog = ProgressDialogs.createLoadingDialog(About_Activity.this,
				"数据加载中");
		init();
	}

	public void back(View view) {
		finish();
	}

	public void init() {
		TextView tv1=(TextView)findViewById(R.id.TextView02);
		TextView tv2=(TextView)findViewById(R.id.TextView01);
		TextView tv3=(TextView)findViewById(R.id.textView2);
		tv1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent mIntent = new Intent();
				mIntent.setClass(About_Activity.this, Web_Activity.class);
				mIntent.putExtra("url", Config.gongnengjieshao);
				mIntent.putExtra("title_name", "功能介绍");
				About_Activity.this.startActivity(mIntent);
			}
			
		});
		tv2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent mIntent = new Intent();
				mIntent.setClass(About_Activity.this, Web_Activity.class);
				mIntent.putExtra("url", Config.guanwang);
				mIntent.putExtra("title_name", "官网");
				About_Activity.this.startActivity(mIntent);
			}
			
		});
		tv3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent mIntent = new Intent();
				mIntent.setClass(About_Activity.this, Web_Activity.class);
				mIntent.putExtra("url", Config.xieyi);
				mIntent.putExtra("title_name", "用户服务协议");
				About_Activity.this.startActivity(mIntent);
			}
			
		});
	}
}
