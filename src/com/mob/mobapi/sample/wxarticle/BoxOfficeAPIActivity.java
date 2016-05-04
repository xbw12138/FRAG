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
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frag2.R;
import com.example.frag_activity.ProgressDialogs;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.BoxOffice;

import java.util.ArrayList;
import java.util.Map;

import static com.mob.tools.utils.R.forceCast;

public class BoxOfficeAPIActivity extends Activity implements APICallback,
		View.OnClickListener {
	private Button btnWeekCN;
	private Button btnWeekHK;
	private Button btnWeekendCN;
	private Button btnWeekendUS;
	private TextView tvTittle;
	private ListView lvResult;

	// 当前查询的票房类别对应的view id
	private int currentViewId;
	String name = "";
	Dialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = getIntent().getStringExtra("name");
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.activity_boxoffice);
		btnWeekCN = forceCast(findViewById(R.id.btnWeekCN));
		btnWeekHK = forceCast(findViewById(R.id.btnWeekHK));
		btnWeekendCN = forceCast(findViewById(R.id.btnWeekendCN));
		btnWeekendUS = forceCast(findViewById(R.id.btnWeekendUS));
		tvTittle = forceCast(findViewById(R.id.tvTittle));
		lvResult = forceCast(findViewById(R.id.lvResult));
		TextView tvtitle = (TextView) findViewById(R.id.textView_title);
		dialog = ProgressDialogs.createLoadingDialog(BoxOfficeAPIActivity.this,
				"数据加载中");
		tvtitle.setText(name);
		btnWeekCN.setOnClickListener(this);
		btnWeekHK.setOnClickListener(this);
		btnWeekendCN.setOnClickListener(this);
		btnWeekendUS.setOnClickListener(this);
	}

	private void setBtnEnable(boolean enable) {
		btnWeekCN.setEnabled(enable);
		btnWeekHK.setEnabled(enable);
		btnWeekendCN.setEnabled(enable);
		btnWeekendUS.setEnabled(enable);
	}

	public void onClick(View view) {
		setBtnEnable(false);
		// 查询相关票房
		currentViewId = view.getId();
		switch (currentViewId) {
		case R.id.btnWeekCN:
			tvTittle.setText(R.string.boxoffice_api_btn_week_cn);
			((BoxOffice) forceCast(MobAPI.getAPI(BoxOffice.NAME))).queryWeek(
					"CN", BoxOfficeAPIActivity.this);
			break;
		case R.id.btnWeekHK:
			tvTittle.setText(R.string.boxoffice_api_btn_week_hk);
			((BoxOffice) forceCast(MobAPI.getAPI(BoxOffice.NAME))).queryWeek(
					"HK", BoxOfficeAPIActivity.this);
			break;
		case R.id.btnWeekendCN:
			tvTittle.setText(R.string.boxoffice_api_btn_weekend_cn);
			((BoxOffice) forceCast(MobAPI.getAPI(BoxOffice.NAME)))
					.queryWeekend("CN", BoxOfficeAPIActivity.this);
			break;
		case R.id.btnWeekendUS:
			tvTittle.setText(R.string.boxoffice_api_btn_weekend_us);
			((BoxOffice) forceCast(MobAPI.getAPI(BoxOffice.NAME)))
					.queryWeekend("US", BoxOfficeAPIActivity.this);
			break;

		}
		dialog.show();
	}

	public void onSuccess(API api, int action, Map<String, Object> result) {
		dialog.dismiss();
		ArrayList<Map<String, Object>> res = forceCast(result.get("result"));
		if (res != null && res.size() > 0) {
			switch (action) {
			case BoxOffice.ACTION_DAY:
				showDayResult(res);
				break;
			case BoxOffice.ACTION_WEEK:
				showWeekResult(res);
				break;
			case BoxOffice.ACTION_WEEKEND:
				showWeekendResult(res);
				break;
			}
		}
		setBtnEnable(true);
	}

	public void onError(API api, int action, Throwable details) {
		details.printStackTrace();
		dialog.dismiss();
		Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
		setBtnEnable(true);
	}

	private void showDayResult(ArrayList<Map<String, Object>> result) {
		// 内地实时票房
		SimpleAdapter adapter = new SimpleAdapter(this, result,
				R.layout.view_boxoffice_day_cn_list_item, new String[] {
						"name", "cur", "days", "sum" }, new int[] {
						R.id.tvName, R.id.tvCur, R.id.tvDays, R.id.tvSum });
		lvResult.setAdapter(adapter);
	}

	private void showWeekResult(ArrayList<Map<String, Object>> result) {
		SimpleAdapter adapter = null;
		if (currentViewId == R.id.btnWeekCN) {
			// 内地周票房
			adapter = new SimpleAdapter(this, result,
					R.layout.view_boxoffice_week_cn_list_item, new String[] {
							"name", "weekSum", "weekPeriod", "sum", "days" },
					new int[] { R.id.tvName, R.id.tvＷeekSum, R.id.tvWeekPeriod,
							R.id.tvSum, R.id.tvDays });
		} else if (currentViewId == R.id.btnWeekHK) {
			// 香港周票房
			adapter = new SimpleAdapter(this, result,
					R.layout.view_boxoffice_week_hk_list_item, new String[] {
							"name", "sumOfWeekHK", "weekPeriodOfHK", "sumOfHK",
							"daysOfHK" }, new int[] { R.id.tvName,
							R.id.tvＷeekSum, R.id.tvWeekPeriod, R.id.tvSum,
							R.id.tvDays });
		}
		if (adapter != null) {
			lvResult.setAdapter(adapter);
		}
	}

	private void showWeekendResult(ArrayList<Map<String, Object>> result) {
		SimpleAdapter adapter = null;
		if (currentViewId == R.id.btnWeekendCN) {
			// 内地周末票房
			adapter = new SimpleAdapter(this, result,
					R.layout.view_boxoffice_weekend_cn_list_item, new String[] {
							"name", "weekendSum", "weekendPeriod", "sum",
							"days" }, new int[] { R.id.tvName,
							R.id.tvＷeekendSum, R.id.tvWeekendPeriod,
							R.id.tvSum, R.id.tvDays });
		} else if (currentViewId == R.id.btnWeekendUS) {
			// 北美周末票房
			adapter = new SimpleAdapter(this, result,
					R.layout.view_boxoffice_weekend_us_list_item, new String[] {
							"name", "sumOfWeekendUS", "weekendPeriodOfUS",
							"sumOfUS", "weeksOfUS" }, new int[] { R.id.tvName,
							R.id.tvＷeekendSum, R.id.tvWeekendPeriod,
							R.id.tvSum, R.id.tvDays });
		}
		if (adapter != null) {
			lvResult.setAdapter(adapter);
		}
	}

	public void back(View view) {
		finish();
	}
}
