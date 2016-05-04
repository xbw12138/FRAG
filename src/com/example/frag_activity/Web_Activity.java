package com.example.frag_activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.frag2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Web_Activity extends Activity {
	public static WebView webView;
	public TextView textview1;
	public ProgressBar pb;
	private SwipeRefreshLayout swipeLayout;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		setContentView(R.layout.menu_activity_web);
		init();
		// 下拉刷新
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					@JavascriptInterface
					public void onRefresh() {
						// 重新刷新页面
						webView.loadUrl(webView.getUrl());
						pb.setVisibility(View.VISIBLE);
						webView.setWebViewClient(new webViewClient());
						webView.setWebChromeClient(new webChromeClient());
					}
				});
		swipeLayout.setColorScheme(R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

	}

	public void back(View view) {
		finish();
	}

	public void beback(View view) {
		Web_Activity.this.finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (webView.canGoBack())
				webView.goBack();
			else
				Web_Activity.this.finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("SetJavaScriptEnabled") public void init() {
		// 进度条显示
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		textview1 = (TextView) findViewById(R.id.textView_title);
		String title_name = getIntent().getStringExtra("title_name");
		textview1.setText(title_name);
		String url = getIntent().getStringExtra("url");
		webView = (WebView) findViewById(R.id.webView_sign);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportMultipleWindows(true);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开@JavascriptInterface
		webView.setWebViewClient(new webViewClient());
		webView.setWebChromeClient(new webChromeClient());
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		webView.loadUrl(url);

	}
	
	private class webChromeClient extends WebChromeClient {
		@Override
		@JavascriptInterface
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				// 加载完成刷新图标消失
				swipeLayout.setRefreshing(false);
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@SuppressWarnings("unused")
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
	}

	private class webViewClient extends WebViewClient {
		@SuppressWarnings("unused")
		@JavascriptInterface
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			pb.setVisibility(View.VISIBLE);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}

	}

	// 内部类
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		@JavascriptInterface
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Toast t = Toast.makeText(getApplication(), "需要SD卡。",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}
			DownloaderTask task = new DownloaderTask();
			task.execute(url);
		}
	}

	// 内部类
	private class DownloaderTask extends AsyncTask<String, Void, String> {
		public DownloaderTask() {
		}

		@Override
		@JavascriptInterface
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			// Log.i("tag", "url="+url);
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			fileName = URLDecoder.decode(fileName);
			Log.i("tag", "fileName=" + fileName);

			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			if (file.exists()) {
				Log.i("tag", "The file has already exists.");
				return fileName;
			}
			try {
				HttpClient client = new DefaultHttpClient();
				// client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();
					writeToSDCard(fileName, input);
					input.close();
					// entity.consumeContent();
					return fileName;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		@JavascriptInterface
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		@JavascriptInterface
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// closeProgressDialog();
			if (result == null) {
				Toast t = Toast.makeText(getApplication(), "连接错误！请稍后再试！",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}
			Toast t = Toast.makeText(getApplication(), "已保存到SD卡。",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, result);
			Log.i("tag", "Path=" + file.getAbsolutePath());
			Intent intent = getFileIntent(file);
			startActivity(intent);
		}

		@Override
		@JavascriptInterface
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// showProgressDialog();
		}

		@Override
		@JavascriptInterface
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}

	public Intent getFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		String type = getMIMEType(file);
		Log.i("tag", "type=" + type);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, type);
		return intent;
	}

	public void writeToSDCard(String fileName, InputStream input) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = new byte[2048];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					fos.write(b, 0, j);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.i("tag", "NO SDCard.");
		}
	}

	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 依扩展名的类型决定MimeType */
		if (end.equals("pdf")) {
			type = "application/pdf";//
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image/*";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*/*";
		}
		return type;
	}

}