package com.example.frag_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.frag2.R;

public class Event_detail extends Activity {
	String start_time;
	String end_time;
	String describe;
	String position;
	String send;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		start_time = getIntent().getStringExtra("start_time");
		end_time = getIntent().getStringExtra("end_time");
		describe = getIntent().getStringExtra("describe");
		position = getIntent().getStringExtra("position");
		send = getIntent().getStringExtra("send");

		setContentView(R.layout.event_detail);
		EditText tx_1=(EditText)findViewById(R.id.start_time);
		EditText tx_2=(EditText)findViewById(R.id.end_time);
		EditText tx_3=(EditText)findViewById(R.id.work_describe);
		EditText tx_4=(EditText)findViewById(R.id.work_position);
		tx_1.setText(start_time);
		tx_2.setText(end_time);
		tx_3.setText(describe);
		tx_4.setText(position);
	}
	public void back(View view) {
		finish();
	}
}
