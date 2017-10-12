package com.example.frag_activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.easemob.chat.EMChatManager;
import com.example.frag2.R;


import com.example.mysql.AsyncTask_Change;
import com.example.mysql.AsyncTask_Change.MysqlListener;
import com.example.mysql.Info_Type.INFOTYPE;
import com.example.mysql.Singleton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import android.view.View.OnClickListener;

public class PayvipActivity extends Activity implements OnClickListener{

	private Button payNowByALiPay;
	private Button payNowByWeiXin;
	private Button payResult;
	Dialog dialog;
	private String DingDan="";//存储订单号
	private String failReason="";//存储错误原因
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//沉浸式状态栏
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//沉浸式状态栏
		setContentView(R.layout.payvip_layout);//设置布局文件
		initView();
		
		payNowByALiPay=(Button)findViewById(R.id.payNowByALiPay);
		payNowByALiPay.setOnClickListener(new payMoneyByALiPayListener());
		payNowByWeiXin=(Button)findViewById(R.id.payNowByWeiXin);
		payNowByWeiXin.setOnClickListener(new payMoneyByWeiXinListener());
		payResult=(Button)findViewById(R.id.payResult);
		payResult.setOnClickListener(new payResultListener());
	}
	
	class payMoneyByALiPayListener implements OnClickListener{
		//选择支付方式为     支付宝
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			dialog = ProgressDialogs
					.createLoadingDialog(PayvipActivity.this, "数据加载中");
			BP.pay(PayvipActivity.this, "vip", "vipBuy", 0.02, true, new PListener(){

				@Override
				public void fail(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(PayvipActivity.this, "支付失败！", Toast.LENGTH_SHORT)
					.show();
					dialog.dismiss();
				}

				@Override
				public void orderId(String arg0) {
					// TODO Auto-generated method stub
					DingDan=arg0;
					dialog.dismiss();
				}

				@Override
				public void succeed() {
					// TODO Auto-generated method stub
					AsyncTask_Change change = new AsyncTask_Change(
							PayvipActivity.this);
					change.setMysqlListener(new MysqlListener() {
						@Override
						public void Success() {
							// TODO 自动生成的方法存根
							//sign_tv.setText(edit.getText().toString());
							Singleton.getInstance().setVIP("YES");
							//dialog.dismiss();
						}
						@Override
						public void Fail() {
							// TODO 自动生成的方法存根
						}
					});
					change.execute(EMChatManager.getInstance().getCurrentUser(), "YES", AsyncTask_Change
							.INFOTYPE_STRING(INFOTYPE.user_vip));
					
					
					Toast.makeText(PayvipActivity.this, "支付成功！", Toast.LENGTH_SHORT)
					.show();
					dialog.dismiss();
				}

				@Override
				public void unknow() {
					// TODO Auto-generated method stub
					Toast.makeText(PayvipActivity.this, "网络不畅...",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					//tv.append("vip" + "'s pay status is unknow\n\n");
				}
				
				
			});
		}
		
	}
	
	class payMoneyByWeiXinListener implements OnClickListener{
		//选择支付方式为       微信
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			
			dialog = ProgressDialogs
					.createLoadingDialog(PayvipActivity.this, "数据加载中");
			BP.pay(PayvipActivity.this, "vip", "vipBuy", 0.02, false, new PListener(){
			
				@Override
				public void fail(int arg0, String arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if (arg0 == -3) {
						new AlertDialog.Builder(PayvipActivity.this)
								.setMessage(
										"请安装支付插件")
								.setPositiveButton("安装",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												installBmobPayPlugin("bp_wx.db");
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												//payByAli();
											}
										}).create().show();
					} else {
						Toast.makeText(PayvipActivity.this, "支付失败！",
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void orderId(String arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}

				@Override
				public void succeed() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					AsyncTask_Change change = new AsyncTask_Change(
							PayvipActivity.this);
					change.setMysqlListener(new MysqlListener() {
						@Override
						public void Success() {
							// TODO 自动生成的方法存根
							//sign_tv.setText(edit.getText().toString());
							Singleton.getInstance().setVIP("YES");
							//dialog.dismiss();
						}
						@Override
						public void Fail() {
							// TODO 自动生成的方法存根
						}
					});
					change.execute(EMChatManager.getInstance().getCurrentUser(), "YES", AsyncTask_Change
							.INFOTYPE_STRING(INFOTYPE.user_vip));
					Toast.makeText(PayvipActivity.this, "支付成功！", Toast.LENGTH_SHORT)
					.show();
				}

				@Override
				public void unknow() {
					// TODO Auto-generated method stub
					Toast.makeText(PayvipActivity.this, "网络不畅...",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
				
				
			});
		}
		
	}
	
	class payResultListener implements OnClickListener{
		//订单查询
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			BP.query(PayvipActivity.this, DingDan, new QListener(){

				@Override
				public void fail(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void succeed(String arg0) {
					// TODO Auto-generated method stub
					
				}});
		}
		
	}
	
	//安装微信插件
	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName + ".apk");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initView() {
		// TODO 自动生成的方法存根
		
	}
	public void back(View view) {
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}
	

}
