package com.example.stepcounter;

import java.text.DecimalFormat;

import com.example.frag2.R;
import com.example.frag_activity.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.RemoteViews;
//service负责后台的需要长期运行的任务
// 计步器服务
// 运行在后台的服务程序，完成了界面部分的开发后
// 就可以开发后台的服务类StepService
// 注册或注销传感器监听器，在手机屏幕状态栏显示通知，与StepActivity进行通信，走过的步数记到哪里了？？？

public class StepCounterService extends Service {

	public static Boolean FLAG = false;// 服务运行标志
	private SensorManager mSensorManager;// 传感器服务
	private StepDetector detector;// 传感器监听对象
	private PowerManager mPowerManager;// 电源管理服务
	private WakeLock mWakeLock;// 屏幕灯

	private NotificationManager notificationManager;
	private Notification notification;
	private RemoteViews contentView;
	private Thread thread; // 定义线程对象

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FLAG = true;// 标记为服务正在运行
		// 创建监听器类，实例化监听对象
		detector = new StepDetector(this);
		// 获取传感器的服务，初始化传感器
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// 注册传感器，注册监听器
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		// 电源管理服务
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
		mWakeLock.acquire();
		createNotification();
		if (thread == null) {
			thread = new Thread() {// 子线程用于监听当前步数的变化
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					while (true) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						handlers.sendMessage(msg);// 通知主线程
					}
				}
			};
			thread.start();
		}

	}

	public void createNotification() {
		if (notification == null) {
			Log.d("server",
					"llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
			notification = new Notification(R.drawable.ic_launcher,
					getResources().getString(R.string.app_name) + "  运动",
					System.currentTimeMillis());
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			contentView = new RemoteViews(getPackageName(),
					R.layout.notification_running);
			contentView.setTextViewText(R.id.notificationTitle, getResources()
					.getString(R.string.app_name) + "  运动");
			contentView.setTextViewText(R.id.text_local,
					StepDetector.CURRENT_SETP + "");
			contentView.setTextViewText(R.id.textView4,
					StepCounterActivity.distance + "");
			notification.contentView = contentView;
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(R.layout.notification_running,
					notification);
		} else {
			// this code will solve the blink issue
			// notification.deleteIntent = pendingIntent1; // this will solve
			// the deleteintent issue
			// notification.contentView = mRemoteViews1;
		}

	}

	Handler handlers = new Handler() {
		@Override
		// 这个方法是从父类/接口 继承过来的，需要重写一次
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg); // 此处可以更新UI
			DecimalFormat df = new DecimalFormat("#.###");
			contentView.setTextViewText(R.id.text_local,
					StepDetector.CURRENT_SETP + "   步");
			contentView.setTextViewText(R.id.textView4,
					df.format(StepCounterActivity.distance / 1000) + "  公里");
			notification.contentView = contentView;
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(R.layout.notification_running,
					notification);
		}
	};
}
