package com.example.frag_fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_activity.ChatLoginActivity;
import com.example.frag_activity.MimeDetailActivity;
import com.example.frag_activity.MimeSettingActivity;
import com.example.frag_activity.PayvipActivity;
import com.example.frag_activity.ProgressDialogs;
import com.example.frag_image_head.FileUtil;
import com.example.frag_image_head.NetUtil;
import com.example.frag_image_head.SelectPicPopupWindow;
import com.example.frag_image_head.Upload_image;
import com.example.frag_image_head.Upload_image.UpFile;
import com.example.frag_model.PullScrollView;
import com.example.mysql.Singleton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MeFragment extends Fragment implements
		PullScrollView.OnTurnListener {
	// public Button btn_exit;
	private RelativeLayout mime_layout_06;
	// private RelativeLayout mime_layout_05;
	private RelativeLayout mime_layout_07;
	private RelativeLayout mime_layout_08;
	// private ImageView bg;
	private TextView nick;
	private TextView sign;
	private ImageView sex;
	private CircleImg avatarImg;
	private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
	private static final int REQUESTCODE_PICK = 0; // 相册选图标记
	private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
	private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
	private String urlpath; // 图片本地路径
	private String resultStr = ""; // 服务端返回结果集
	private String imgUrl = Config.uploadUrl;
	private String imgUrlbg = Config.uploadbgUrl;
	Dialog dialogs;
	Upload_image upload;
	Bitmap bitmap;
	Bitmap bitmapbg;
	private boolean up = false;
	private ImageView imgbtn;
	// ImageView bg;
	private PullScrollView mScrollView;
	private ImageView mHeadImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_ms, container, false);
	}

	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				// 显示从网上下载的图片
				avatarImg.setImageBitmap(bitmap);
			}
		}
	};
	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				// 显示从网上下载的图片
				Bitmap bm = bitmapbg; // xxx根据你的情况获取
				BitmapDrawable bd = new BitmapDrawable(getResources(), bm);

				mHeadImg.setBackground(bd);// .setImageBitmap(bitmap);
				// bg.setBackground(bitmapbg);
			}
		}
	};

	public void UploadUI() {
		if (!Singleton.getInstance().getName().equals("null")) {
			nick.setText(Singleton.getInstance().getName());
		}
		if (!Singleton.getInstance().getSign().equals("null")) {
			sign.setText(Singleton.getInstance().getSign());
		}
		if (!Singleton.getInstance().getSex().equals("null")) {
			if (Singleton.getInstance().getSex().equals("男")) {
				sex.setImageResource(R.drawable.icn_boy);
			} else {
				sex.setImageResource(R.drawable.icn_girl);
			}
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialogs = ProgressDialogs.createLoadingDialog(getActivity(), "头像上传中");
		avatarImg = (CircleImg) getActivity().findViewById(R.id.avatarImg);
		// bg = (ImageView) getActivity().findViewById(R.id.background_img);
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(Singleton.getInstance().getHead());
					InputStream is = url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					handler1.sendEmptyMessage(0x9527);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(Singleton.getInstance().getBG());
					InputStream is = url.openStream();
					bitmapbg = BitmapFactory.decodeStream(is);
					handler2.sendEmptyMessage(0x9527);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		init();
		initView();
	}

	protected void initView() {
		mScrollView = (PullScrollView) getActivity().findViewById(
				R.id.scroll_view);
		imgbtn=(ImageView) getActivity().findViewById(R.id.imageView1);
		mHeadImg = (ImageView) getActivity().findViewById(R.id.background_img);
		imgbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				menuWindow = new SelectPicPopupWindow(getActivity(),
						itemsOnClick);
				menuWindow.showAtLocation(
						getActivity().findViewById(R.id.mainLayout11), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
				up = true;

			}

		});
		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
	}

	public void init() {
		mime_layout_06 = (RelativeLayout) getActivity().findViewById(
				R.id.mime_layout_06);
		mime_layout_07 = (RelativeLayout) getActivity().findViewById(
				R.id.mime_layout_07);
		mime_layout_08 = (RelativeLayout) getActivity().findViewById(
				R.id.mime_layout_08);
		// btn_exit = (Button) getActivity().findViewById(R.id.exit_login_btn);

		nick = (TextView) getActivity().findViewById(R.id.text_01);
		sign = (TextView) getActivity().findViewById(R.id.text_02);
		sex = (ImageView) getActivity().findViewById(R.id.row_img_05);

		if (!Singleton.getInstance().getName().equals("null")) {
			nick.setText(Singleton.getInstance().getName());
		}
		if (!Singleton.getInstance().getSign().equals("null")) {
			sign.setText(Singleton.getInstance().getSign());
		}
		if (!Singleton.getInstance().getSex().equals("null")) {
			if (Singleton.getInstance().getSex().equals("男")) {
				sex.setImageResource(R.drawable.icn_boy);
			} else {
				sex.setImageResource(R.drawable.icn_girl);
			}
		}
		avatarImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				menuWindow = new SelectPicPopupWindow(getActivity(),
						itemsOnClick);
				menuWindow.showAtLocation(
						getActivity().findViewById(R.id.mainLayout11),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				up = false;

			}

		});
		mime_layout_06.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), MimeDetailActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
		mime_layout_07.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), MimeSettingActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
		mime_layout_08.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(getActivity(), PayvipActivity.class);
				getActivity().startActivity(mIntent);
			}
		});
	}

	public void showLogoutDialog() {

		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				R.layout.dialog, null);
		final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView tex = (TextView) layout.findViewById(R.id.dialog_text);
		TextView tex1 = (TextView) layout.findViewById(R.id.textView_title);
		tex.setMovementMethod(ScrollingMovementMethod.getInstance());
		tex.setText("确定要注销" + EMChatManager.getInstance().getCurrentUser()
				+ "用户吗？");
		tex1.setText("应用提示");
		// 确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setText("确定");
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logout(new EMCallBack() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						startActivity(new Intent(getActivity(),
								ChatLoginActivity.class));

						getActivity().finish();
					}

					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
				dialog.dismiss();
			}
		});
		// 关闭按钮
		ImageButton btnClose = (ImageButton) layout
				.findViewById(R.id.dialog_close);
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void logout(final EMCallBack callback) {
		// setPassword(null);
		EMChatManager.getInstance().logout(new EMCallBack() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}
		});
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			// 拍照
			case R.id.takePhotoBtn:
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				takeIntent
						.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME)));
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
			// 相册选择图片
			case R.id.pickPhotoBtn:
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
				pickIntent
						.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
				startActivityForResult(pickIntent, REQUESTCODE_PICK);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:// 直接从相册获取
			try {
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {
				e.printStackTrace();// 用户点击取消操作
			}
			break;
		case REQUESTCODE_TAKE:// 调用相机拍照
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case REQUESTCODE_CUTTING:// 取得裁剪后的图片
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		if (!up) {
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
		} else {
			intent.putExtra("outputX", 640);
			intent.putExtra("outputY", 640);
		}
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			// 取得SDCard图片路径做显示
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(null, photo);
			// SimpleDateFormat df = new
			// SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
			// String user_signtime=df.format(new Date());
			if (!up) {
				urlpath = FileUtil.saveFile(getActivity(), "_"
						+ EMChatManager.getInstance().getCurrentUser()
						+ "_head.jpg", photo);
				avatarImg.setImageDrawable(drawable);
				Config.bitmap = drawable;
			} else {
				urlpath = FileUtil.saveFile(getActivity(), "_"
						+ EMChatManager.getInstance().getCurrentUser()
						+ "_bg.jpg", photo);
				mHeadImg.setImageDrawable(drawable);
				Config.bitmap_bg = drawable;
			}
			// 上传图片到服务器
			new Thread(uploadImageRunnable).start();
			// 上传图片到服务器第二种方法
			// new Task().execute();
			dialogs.show();
		}
	}

	/**
	 * 使用HttpUrlConnection模拟post表单进行文件 上传平时很少使用，比较麻烦 原理是：
	 * 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
	 */
	Runnable uploadImageRunnable = new Runnable() {
		@Override
		public void run() {
			if (up) {
				imgUrl = imgUrlbg;
			}
			if (TextUtils.isEmpty(imgUrl)) {
				Toast.makeText(getActivity(), "还没有设置上传服务器的路径！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();
			try {
				// 创建一个URL对象
				URL url = new URL(imgUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// 要上传的图片文件
				File file = new File(urlpath);
				fileparams.put("image", file);
				// 利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
				conn.setConnectTimeout(5000);
				// 设置允许输出（发送POST请求必须设置允许输出）
				conn.setDoOutput(true);
				// 设置使用POST的方式发送
				conn.setRequestMethod("POST");
				// 设置不使用缓存（容易出现问题）
				conn.setUseCaches(false);
				conn.setRequestProperty("Charset", "UTF-8");// 设置编码
				// 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// 设置contentType
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + NetUtil.BOUNDARY);
				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				// 对文件流操作完,要记得及时关闭
				os.close();
				// 服务器返回的响应吗
				int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
				// 对响应码进行判断
				if (code == 200) {// 返回的响应码200,是成功
					// 得到网络返回的输入流
					handler.sendEmptyMessage(200);
					// Toast.makeText(getActivity(), "上传头像成功",
					// Toast.LENGTH_SHORT).show();
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
				} else {

					handler.sendEmptyMessage(300);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(400);
			}
			// 执行耗时的方法之后发送消给handler
		}
	};

	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			dialogs.dismiss();
			switch (msg.what) {
			case 200:
				// dialogs.dismiss();
				if (!up) {
					Toast.makeText(getActivity(), "上传头像成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "上传背景成功", Toast.LENGTH_SHORT)
							.show();
				}
				try {
					// 返回数据示例，根据需求和后台数据灵活处理
					// {"status":"1","statusMessage":"上传成功","imageUrl":"http://120.24.219.49/726287_temphead.jpg"}
					JSONObject jsonObject = new JSONObject(resultStr);

					// 服务端以字符串“1”作为操作成功标记
					if (jsonObject.optString("status").equals("1")) {
						BitmapFactory.Options option = new BitmapFactory.Options();
						// 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图，3为三分之一
						option.inSampleSize = 1;

						// 服务端返回的JsonObject对象中提取到图片的网络URL路径
						//String imageUrl = jsonObject.optString("imageUrl");
						//Toast.makeText(getActivity(), imageUrl,
						//		Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(),
								jsonObject.optString("statusMessage"),
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 300:
				Toast.makeText(getActivity(), "请求URL失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 400:
				Toast.makeText(getActivity(), "未知异常", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			return false;
		}
	});

	public void UploadUIs() {
		//if (Config.bitmap_bg != null) {
			// Bitmap bm=Config.bitmap_bg; //xxx根据你的情况获取
			// BitmapDrawable bd= new BitmapDrawable(getResources(), bm);
		//	mHeadImg.setBackground(Config.bitmap_bg);// .setImageBitmap(bitmap);
		//}
		if (!Singleton.getInstance().getName().equals("null")) {
			nick.setText(Singleton.getInstance().getName());
		}
		if (!Singleton.getInstance().getSign().equals("null")) {
			sign.setText(Singleton.getInstance().getSign());
		}
		if (!Singleton.getInstance().getSex().equals("null")) {
			if (Singleton.getInstance().getSex().equals("男")) {
				sex.setImageResource(R.drawable.icn_boy);
			} else {
				sex.setImageResource(R.drawable.icn_girl);
			}
		}
		// bg.setImageDrawable(Config.bitmap_bg);
	}

	// 根据fragment生命周期进行ui更新；
	@Override
	public void onResume() {
		super.onResume();
		UploadUIs();
	}

	/*
	 * private void processContactsAndGroups() throws EaseMobException { //
	 * demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定 List<String> usernames =
	 * EMContactManager.getInstance().getContactUserNames(); EMLog.d("roster",
	 * "contacts size: " + usernames.size()); Map<String, User> userlist = new
	 * HashMap<String, User>(); for (String username : usernames) { User user =
	 * new User(); user.setUsername(username); setUserHearder(username, user);
	 * userlist.put(username, user); } // 添加user"申请与通知" User newFriends = new
	 * User(); newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME); String
	 * strChat = getResources().getString(R.string.Application_and_notify);
	 * newFriends.setNick(strChat);
	 * 
	 * userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends); // 添加"群聊" User
	 * groupUser = new User(); String strGroup =
	 * getResources().getString(R.string.group_chat);
	 * groupUser.setUsername(Constant.GROUP_USERNAME);
	 * groupUser.setNick(strGroup); groupUser.setHeader("");
	 * userlist.put(Constant.GROUP_USERNAME, groupUser);
	 * 
	 * // 存入内存 DemoApplication.getInstance().setContactList(userlist);
	 * System.out.println("----------------"+userlist.values().toString()); //
	 * 存入db UserDao dao = new UserDao(getActivity()); List<User> users = new
	 * ArrayList<User>(userlist.values()); dao.saveContactList(users);
	 * 
	 * //获取黑名单列表 List<String> blackList =
	 * EMContactManager.getInstance().getBlackListUsernamesFromServer(); //保存黑名单
	 * EMContactManager.getInstance().saveBlackList(blackList);
	 * 
	 * // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
	 * EMGroupManager.getInstance().getGroupsFromServer(); }
	 */
	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	/*
	 * protected void setUserHearder(String username, User user) { String
	 * headerName = null; if (!TextUtils.isEmpty(user.getNick())) { headerName =
	 * user.getNick(); } else { headerName = user.getUsername(); } if
	 * (username.equals(Constant.NEW_FRIENDS_USERNAME)) { user.setHeader(""); }
	 * else if (Character.isDigit(headerName.charAt(0))) { user.setHeader("#");
	 * } else {
	 * user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0,
	 * 1)).get(0).target.substring(0, 1).toUpperCase()); char header =
	 * user.getHeader().toLowerCase().charAt(0); if (header < 'a' || header >
	 * 'z') { user.setHeader("#"); } } }
	 */
	/*
	 * class Task extends AsyncTask<String, Integer, Integer> {
	 * 
	 * @Override protected Integer doInBackground(String... strings) { Integer
	 * result = -1; try { result = UpFile.post(config_a.uploadUrl, new
	 * File("/storage/extSdCard/b.png")); } catch (IOException e) {
	 * e.printStackTrace(); } return result; }
	 * 
	 * @Override protected void onPostExecute(Integer result) {
	 * Toast.makeText(getActivity(), "" + result, Toast.LENGTH_LONG).show(); } }
	 */

	@Override
	public void onTurn() {
		// TODO 自动生成的方法存根

	}

	
}