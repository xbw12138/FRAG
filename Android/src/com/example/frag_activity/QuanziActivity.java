package com.example.frag_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frag2.R;
import com.mob.mobapi.sample.wxarticle.BoxOfficeAPIActivity;
import com.mob.mobapi.sample.wxarticle.WxArticleAPIActivity;
import com.mob.mobapi.sample.wxarticle.WxArticleListAPIActivity;

public class QuanziActivity extends Activity implements OnClickListener {

	private RelativeLayout rl1;
	private RelativeLayout rl2;
	private TextView tv1;
	private TextView tv2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_quanzi);
		init();
	}

	public void back(View view) {
		finish();
	}

	public void init() {
		rl1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
		rl2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		tv1 = (TextView) findViewById(R.id.tvName_wechat);
		tv2 = (TextView) findViewById(R.id.tvName_movie);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.relativeLayout1:
			Intent intent = new Intent(this, WxArticleAPIActivity.class);
			intent.putExtra("name", tv1.getText());
			startActivity(intent);
			break;
		case R.id.relativeLayout2:
			Intent intents = new Intent(this, BoxOfficeAPIActivity.class);
			intents.putExtra("name", tv2.getText());
			startActivity(intents);
			break;
		}
	}

}
