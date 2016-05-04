package com.example.frag_fragment;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.easemob.chat.EMChatManager;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_activity.About_Activity;
import com.example.frag_activity.CaptureActivity;
import com.example.frag_activity.ChatLoginActivity;
import com.example.frag_activity.MainActivity;
import com.example.frag_activity.QuanziActivity;
import com.example.frag_activity.Web_Activity;
import com.example.frag_update.UpdateInfo;
import com.example.frag_update.UpdateInfoService;
import com.example.mysql.Singleton;
import com.example.stepcounter.StepCounterActivity;
import com.mob.mobapi.sample.wxarticle.WxArticleAPIActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuLeftFragment extends Fragment {
	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;
	Button btn6;
	TextView tex;
	private UpdateInfo info;
	Bitmap bitmap;
	// ImageView imageview;
	private CircleImg avatarImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_drawer_left, container, false);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				// 显示从网上下载的图片
				avatarImg.setImageBitmap(bitmap);
			}
		}
	};

	// 根据fragment生命周期进行ui更新；
	@Override
	public void onResume() {
		super.onResume();
		Log.e("TAG111111111111111111111", "start onResume~~~");
		UploadUI();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("TAG11111111111111111111111", "start onResume~~~");
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		avatarImg = (CircleImg) getActivity().findViewById(R.id.user_logo);
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(Singleton.getInstance().getHead());
					InputStream is = url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					handler.sendEmptyMessage(0x9527);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		init();
	}

	public void init() {
		btn1 = (Button) getActivity().findViewById(R.id.button_imga);
		btn2 = (Button) getActivity().findViewById(R.id.button_imgb);
		btn3 = (Button) getActivity().findViewById(R.id.button_imgc);
		btn4 = (Button) getActivity().findViewById(R.id.button_imgd);
		btn5 = (Button) getActivity().findViewById(R.id.button_imge);
		btn6 = (Button) getActivity().findViewById(R.id.button_imgf);
		tex = (TextView) getActivity().findViewById(R.id.user_nickname);
		tex.setText("ID : " + EMChatManager.getInstance().getCurrentUser());
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), Web_Activity.class);
				mIntent.putExtra("url", "http://www.baidu.com");
				mIntent.putExtra("title_name", "搜索");
				getActivity().startActivity(mIntent);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), QuanziActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), StepCounterActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
		btn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), Web_Activity.class);
				mIntent.putExtra("url", "http://115.159.26.120/talk/");
				mIntent.putExtra("title_name", "广场");
				getActivity().startActivity(mIntent);
			}
		});
		btn5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), CaptureActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
		btn6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), About_Activity.class);
				getActivity().startActivity(mIntent);
			}
		});
		avatarImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				Animation shake = AnimationUtils.loadAnimation(context,
						R.anim.shake);
				v.startAnimation(shake);
			}
		});
	}

	public void UploadUI() {
		if (Config.bitmap != null)
			avatarImg.setImageDrawable(Config.bitmap);
	}
}
