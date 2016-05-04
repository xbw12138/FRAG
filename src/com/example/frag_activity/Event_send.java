package com.example.frag_activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.byl.testdate.widget.NumericWheelAdapter;
import com.byl.testdate.widget.OnWheelScrollListener;
import com.byl.testdate.widget.WheelView;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.activity.ForwardMessageActivity;
import com.easemob.chatuidemo.activity.SelectuserActivity;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_model.Get_Set;
import com.example.mysql.AsyncTask_Insert_Work;

public class Event_send extends Activity {
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;

	PopupWindow menuWindow_date;
	PopupWindow menuWindow_time;
	Button start_time;
	Button end_time;
	private String times = "2016-4-30/14:30";
	private int jud = 0;
	private EditText title_edit;
	private EditText describe_edit;
	private TextView tv_x;
	private Button btn;
	Dialog dialog;
	Get_Set userisexist;
	private ImageView adduser;
	private TextView ttt;

	// private ContentFragment contentFragment=new ContentFragment();;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.event_send);
		Config.isinit = false;
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		start_time = (Button) findViewById(R.id.edt_start);
		end_time = (Button) findViewById(R.id.edt_end);
		title_edit = (EditText) findViewById(R.id.edt_title);
		describe_edit = (EditText) findViewById(R.id.edt_describe);
		tv_x = (TextView) findViewById(R.id.textView2);
		adduser = (ImageView) findViewById(R.id.picImg);
		ttt = (TextView) findViewById(R.id.textView3);
		ttt.setText("接收者："+Config.username);
		tv_x.setText("您还可以输入" + 140 + " \\ " + 140);
		btn = (Button) findViewById(R.id.send_btn);
		title_edit.addTextChangedListener(myWatcher1);
		describe_edit.addTextChangedListener(myWatcher2);
		start_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPopwindow_time(getTimePick());// 弹出时间选择器
				showPopwindow_date(getDataPick());// 弹出日期选择器
				jud = 1;
			}
		});
		end_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPopwindow_time(getTimePick());// 弹出时间选择器
				showPopwindow_date(getDataPick());// 弹出日期选择器
				jud = 2;
			}
		});
		adduser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Event_send.this,
						SelectuserActivity.class);
				// intent.putExtra("forward_msg_id", "任务已发送，请注意查收");
				// intent.putExtra("is_work", "yes");
				startActivity(intent);
			}
		});
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dialog = ProgressDialogs.createLoadingDialog(Event_send.this,
						"数据加载中");
				dialog.show();
				new Thread() {
					public void run() {
						try {
							AsyncTask_Insert_Work in = new AsyncTask_Insert_Work(
									Event_send.this);
							userisexist = in.user_Information(Config.username, start_time
									.getText().toString(), end_time.getText()
									.toString(), title_edit.getText()
									.toString(), describe_edit.getText()
									.toString());
							handler.sendEmptyMessage(0);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {

						}
					}
				}.start();
			}
		});

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
					Config.isinit = true;
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

	private void showPopwindow_date(View view) {
		menuWindow_date = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow_date.setFocusable(true);
		menuWindow_date.setBackgroundDrawable(new BitmapDrawable());
		menuWindow_date.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow_date.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow_date = null;
			}
		});
	}

	private void showPopwindow_time(View view) {
		menuWindow_time = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow_time.setFocusable(true);
		menuWindow_time.setBackgroundDrawable(new BitmapDrawable());
		menuWindow_time.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow_time.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow_time = null;
			}
		});
	}

	private View getTimePick() {
		View view = inflater.inflate(R.layout.timepick, null);
		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel("时");
		hour.setCyclic(true);
		mins = (WheelView) view.findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel("分");
		mins.setCyclic(true);

		hour.setCurrentItem(8);
		mins.setCurrentItem(30);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = hour.getCurrentItem() + ":"
						+ mins.getCurrentItem();
				Toast.makeText(Event_send.this, str, Toast.LENGTH_LONG).show();
				menuWindow_time.dismiss();
				times += "/" + str;
				if (jud == 1) {
					start_time.setText(times);
				} else if (jud == 2) {
					end_time.setText(times);
				}

			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow_time.dismiss();
				times += "/" + "14:30";
				if (jud == 1) {
					start_time.setText(times);
				} else if (jud == 2) {
					end_time.setText(times);
				}
			}
		});

		return view;
	}

	/**
	 * 
	 * @return
	 */
	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = inflater.inflate(R.layout.datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, curYear + 50));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = (year.getCurrentItem() + 1950) + "-"
						+ (month.getCurrentItem() + 1) + "-"
						+ (day.getCurrentItem() + 1);
				Toast.makeText(Event_send.this, str, Toast.LENGTH_LONG).show();
				menuWindow_date.dismiss();
				times = str;
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow_date.dismiss();
				times = "2016-4-30";
			}
		});
		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 1950;
			int n_month = month.getCurrentItem() + 1;
			initDay(n_year, n_month);
		}
	};

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

	TextWatcher myWatcher1 = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			if (title_edit.getText().length() >= 15) {
				Toast.makeText(Event_send.this, "字数超限", 8000).show();
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
	TextWatcher myWatcher2 = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO 自动生成的方法存根
			int len = 140 - describe_edit.getText().length();
			if (len >= 0)
				tv_x.setText("您还可以输入" + len + " \\ " + 140);
			else {
				tv_x.setText("输入超限");
				Toast.makeText(Event_send.this, "字数超限", 8000).show();
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

	public void back(View view) {
		finish();
	}

	protected void onResume() {
		super.onResume();
		ttt.setText("接收者："+Config.username);
	}
}
