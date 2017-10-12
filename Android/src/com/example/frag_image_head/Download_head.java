package com.example.frag_image_head;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.easemob.chat.EMChatManager;
import com.example.Config.Config;
import com.example.frag_activity.SplashActivity;

public class Download_head {

	private Bitmap bitmap;
	Information_info infos;

	public String getheadurl(Context context, String ID) {
		Information in = new Information(context);
		infos = in.user_Information(ID);
		return infos.get_user_image_head();
	}

	public Bitmap getWebImg(final Context context, final String ID) {
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(getheadurl(context, ID));
					InputStream is = url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					handler1.sendEmptyMessage(0x9527);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		return bitmap;
	}

	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				// 显示从网上下载的图片
				// avatarImg.setImageBitmap(bitmap);
			}
		}
	};

}
