package com.example.frag_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.activity.ForwardMessageActivity;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_activity.Event_detail;
import com.example.frag_activity.Event_send;
import com.example.frag_model.CustomFAB;
import com.example.frag_model.ListWorkAdapter;
import com.example.mysql.AsyncTask_Change_Finish;
import com.example.mysql.AsyncTask_Change_Finish.MysqlListenerss;
import com.example.mysql.AsyncTask_Delete_Work;
import com.example.mysql.AsyncTask_Delete_Work.MysqlListeners;
import com.example.mysql.AsyncTask_Search_Single_work;
import com.example.mysql.AsyncTask_Search_Single_work.MysqlListenerx;
import com.example.mysql.AsyncTask_Search_Work.MysqlListener;
import com.example.mysql.AsyncTask_Search_Work;
import com.example.mysql.Singleton;

public class ContentFragment extends Fragment {
	private ListView lis1;
	private ListWorkAdapter mAdapter;
	CustomFAB btn_fab;
	private ImageView img;

	// private SwipeRefreshLayout swipeLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_content, container, false);
	}

	@SuppressWarnings("deprecation")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lis1 = (ListView) getActivity().findViewById(R.id.listview_order);
		btn_fab = (CustomFAB) getActivity().findViewById(R.id.fab_btn);
		img = (ImageView) getActivity().findViewById(R.id.image_refresh);
		init();
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				init();
			}
		});
	}

	public void init() {
		AsyncTask_Search_Work search = new AsyncTask_Search_Work(getActivity());
		search.setMysqlListener(new MysqlListener() {
			@Override
			public void Success() {
				// TODO 自动生成的方法存根
				mAdapter = new ListWorkAdapter(getActivity(), Singleton
						.getInstance().getlistWork());
				lis1.setAdapter(mAdapter);
				// swipeLayout.setRefreshing(false);
			}

			@Override
			public void Fail() {
				// TODO 自动生成的方法存根
				Toast.makeText(getActivity(), "无数据", 8000).show();
			}
		});
		search.execute(EMChatManager.getInstance().getCurrentUser());

		lis1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView txt_type = (TextView) view
						.findViewById(R.id.order_type);
				TextView txt_num = (TextView) view.findViewById(R.id.order_num);
				TextView txt_add = (TextView) view.findViewById(R.id.order_add);
				TextView txt_time = (TextView) view
						.findViewById(R.id.order_time);
				TextView txt_send = (TextView) view
						.findViewById(R.id.order_send);

				Intent intent = new Intent();
				// 设置cityName
				intent.setClass(getActivity(), Event_detail.class);
				intent.putExtra("start_time", txt_type.getText());
				intent.putExtra("end_time", txt_num.getText());
				intent.putExtra("describe", txt_add.getText());
				intent.putExtra("position", txt_time.getText());
				intent.putExtra("send", txt_send.getText());
				startActivity(intent);
			}
		});

		lis1.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				TextView txt_id = (TextView) view.findViewById(R.id.order_id);
				showChangeDialog(txt_id.getText().toString());
				return false;
			}
		});

		btn_fab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(getActivity(), Event_send.class);
				Config.username = EMChatManager.getInstance().getCurrentUser();
				// intent.putExtra("userId", EMChatManager
				// .getInstance().getCurrentUser());
				startActivity(intent);
			}

		});
	}

	public void showChangeDialog(final String id) {
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				R.layout.delete_dialog, null);
		final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// 确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		Button btndel = (Button) layout.findViewById(R.id.btn_delete);
		Button btnfinish = (Button) layout.findViewById(R.id.btn_finish);
		Button btnnofinish = (Button) layout.findViewById(R.id.btn_nofinish);
		Button btnzhuanfa = (Button) layout.findViewById(R.id.btn_zhuan);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btndel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTask_Delete_Work changes = new AsyncTask_Delete_Work(
						getActivity());
				changes.setMysqlListeners(new MysqlListeners() {
					@Override
					public void Success() {
						// TODO 自动生成的方法存根
						init();
						dialog.dismiss();
					}

					@Override
					public void Fail() {
						// TODO 自动生成的方法存根
					}
				});
				changes.execute(id);

			}
		});
		btnfinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTask_Change_Finish changes = new AsyncTask_Change_Finish(
						getActivity());
				changes.setMysqlListenerss(new MysqlListenerss() {
					@Override
					public void Success() {
						// TODO 自动生成的方法存根
						init();
						dialog.dismiss();
					}

					@Override
					public void Fail() {
						// TODO 自动生成的方法存根
					}
				});
				changes.execute(id, "Y");
			}
		});
		btnnofinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTask_Change_Finish changes = new AsyncTask_Change_Finish(
						getActivity());
				changes.setMysqlListenerss(new MysqlListenerss() {
					@Override
					public void Success() {
						// TODO 自动生成的方法存根
						init();
						dialog.dismiss();
					}

					@Override
					public void Fail() {
						// TODO 自动生成的方法存根

					}
				});
				changes.execute(id, "N");
			}
		});
		btnzhuanfa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AsyncTask_Search_Single_work changes = new AsyncTask_Search_Single_work(
						getActivity());
				changes.setMysqlListenerx(new MysqlListenerx() {
					@Override
					public void Success() {
						// TODO 自动生成的方法存根
						Intent intent = new Intent(getActivity(),
								ForwardMessageActivity.class);
						intent.putExtra("forward_msg_id", "任务已发送，请注意查收");
						intent.putExtra("is_work", "yes");
						startActivity(intent);
					}

					@Override
					public void Fail() {
						// TODO 自动生成的方法存根
					}
				});
				changes.execute(id);
			}
		});
	}

}