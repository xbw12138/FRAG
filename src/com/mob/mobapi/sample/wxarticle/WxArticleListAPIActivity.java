/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.mob.mobapi.sample.wxarticle;

import static com.mob.tools.utils.R.forceCast;

import java.util.HashMap;

import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_activity.ProgressDialogs;
import com.example.frag_activity.Web_Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class WxArticleListAPIActivity extends Activity implements
		AdapterView.OnItemClickListener {

	String cid;
	WxArticleAdapter adapter;
	public static Dialog dialogss;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String name = getIntent().getStringExtra("name");
		cid = getIntent().getStringExtra("cid");
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_wxarticle_list);
		dialogss = ProgressDialogs.createLoadingDialog(
				WxArticleListAPIActivity.this, "数据加载中");
		ListView lvResult = forceCast(findViewById(R.id.lvResults));
		TextView textview = (TextView) findViewById(R.id.textView_title);
		textview.setText(name);
		// setTitle(getString(R.string.wxarticle_api_title_list, name));
		// getListView().setBackgroundColor(0xffffffff);
		// getListView().setDivider(new ColorDrawable(0xff7f7f7f));
		// getListView().setDividerHeight(1);
		// View
		// view=LayoutInflater.from(this).inflate(R.layout.activity_article_list,
		// null);
		// getListView().addHeaderView(view);
		// 请求和现实操作由WxArticleAdapter完成
		adapter = new WxArticleAdapter(cid, this);
		lvResult.setAdapter(adapter);
		lvResult.setOnItemClickListener(this);
		adapter.requestData();
		dialogss.show();
	}

	public void back(View view) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存
		HashMap<String, Object> item = forceCast(adapter.getItem(position));
		Intent intent = new Intent(this, Web_Activity.class);
		intent.putExtra("url", (String) item.get("sourceUrl"));
		intent.putExtra("title_name", (String) item.get("title"));
		startActivity(intent);

	}
}
