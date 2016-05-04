/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.mob.mobapi.sample.wxarticle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frag2.R;
import com.example.frag_activity.ProgressDialogs;
import com.example.frag_activity.SplashActivity;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.WxArticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mob.tools.utils.R.forceCast;

public class WxArticleAPIActivity extends Activity implements APICallback,
		AdapterView.OnItemClickListener {

	private SimpleAdapter categoryAdapter;
	private ArrayList<HashMap<String, Object>> categoryList;
	Dialog dialogss;
	String name = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = getIntent().getStringExtra("name");
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_wxarticle_class);
		dialogss = ProgressDialogs.createLoadingDialog(
				WxArticleAPIActivity.this, "数据加载中");
		ListView lvResult = forceCast(findViewById(R.id.lvResult));
		TextView tvtitle = (TextView) findViewById(R.id.textView_title);
		tvtitle.setText(name);
		lvResult.setOnItemClickListener(this);

		// init data
		categoryList = new ArrayList<HashMap<String, Object>>();
		categoryAdapter = new SimpleAdapter(this, categoryList,
				R.layout.view_wxarticle_category_item, new String[] { "name" },
				new int[] { R.id.tvName_wechat });
		lvResult.setAdapter(categoryAdapter);

		// 查询分类信息
		((WxArticle) forceCast(MobAPI.getAPI(WxArticle.NAME)))
				.queryCategory(this);
		dialogss.show();
	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long l) {
		HashMap<String, Object> item = forceCast(categoryAdapter
				.getItem(position));
		// goto wx articles
		Intent intent = new Intent(this, WxArticleListAPIActivity.class);
		intent.putExtra("name", (String) item.get("name"));
		intent.putExtra("cid", (String) item.get("cid"));
		startActivity(intent);
	}

	public void onSuccess(API api, int action, Map<String, Object> result) {
		ArrayList<HashMap<String, Object>> res = forceCast(result.get("result"));
		if (null != res && res.size() > 0) {
			categoryList.clear();
			categoryList.addAll(res);
			categoryAdapter.notifyDataSetChanged();
		}
		dialogss.dismiss();
	}

	public void onError(API api, int action, Throwable details) {
		details.printStackTrace();
		Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
		dialogss.dismiss();
	}

	public void back(View view) {
		finish();
	}

}
