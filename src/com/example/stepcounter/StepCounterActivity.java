package com.example.stepcounter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Calendar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.frag2.R;
import com.easemob.chatuidemo.DemoApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 应用程序的用户界面， 主要功能就是按照XML布局文件的内容显示界面， 并与用户进行交互 负责前台界面展示
 * 在android中Activity负责前台界面展示，service负责后台的需要长期运行的任务。
 * Activity和Service之间的通信主要由Intent负责
 */
@SuppressLint("HandlerLeak")
public class StepCounterActivity extends Activity {
	// 定义文本框控件
	private TextView tv_show_step;// 步数
	private TextView tv_distance;// 行程
	private TextView tv_calories;// 卡路里
	// private TextView tv_velocity;// 速度
	private Button btn_start;// 开始按钮
	private Button btn_stop;// 停止按钮
	private boolean isRun = false;
	public static Double distance = 0.0;// 路程：米
	private Double calories = 0.0;// 热量：卡路里
	// private Double velocity = 0.0;// 速度：米每秒
	private int step_length = 0; // 步长
	private int weight = 0; // 体重
	private int total_step = 0; // 走的总步数
	private Thread thread; // 定义线程对象
	private TableRow hide1, hide2;
	private TextView step_counter;
	private boolean isButton = false;
	private LocationClient mLocationClient;
	private TextView local;
	// private TextView direction;
	private TextView satellite;
	private TextView velocity;
	private TextView warnning;
	private ImageView img;
	ImageView imgView;
	SensorManager manager;
	public RelativeLayout title;
	int number = 0;
	Handler handler = new Handler() {
		@Override
		// 这个方法是从父类/接口 继承过来的，需要重写一次
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg); // 此处可以更新UI
			countDistance(); // 调用距离方法，看一下走了多远
			if (distance != 0.0) {
				// 体重、距离
				// 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
				calories = weight * distance * 0.001;
			} else {
				calories = 0.0;
			}
			countStep(); // 调用步数方法
			tv_show_step.setText(total_step + "");// 显示当前步数
			tv_distance.setText(formatDouble(distance));// 显示路程
			tv_calories.setText(formatDouble(calories));// 显示卡路里
			// tv_velocity.setText(formatDouble(velocity));// 显示速度
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 沉浸式状态栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 沉浸式状态栏
		this.setContentView(R.layout.activity_running); // 设置当前屏幕
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		local = (TextView) findViewById(R.id.text_local);
		velocity = (TextView) findViewById(R.id.velocity);
		// direction = (TextView)findViewById(R.id.text_direction);
		satellite = (TextView) findViewById(R.id.text_satellite);
		warnning = (TextView) findViewById(R.id.text_warnning);
		img = (ImageView) findViewById(R.id.imageView2);
		title = (RelativeLayout) findViewById(R.id.title_run);
		mlocalResult = local;
		mvelocityResult = velocity;
		// mdirectionResult =direction;
		msatelliteResult = satellite;
		mwarnningResult = warnning;
		mimgResult = img;
		initLocation();
		mLocationClient.start();// 定位SDK
								// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
		mLocationClient.requestLocation();
		imgView = (ImageView) findViewById(R.id.url_article_img);
		imgView.setKeepScreenOn(true);
		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (thread == null) {
			thread = new Thread() {// 子线程用于监听当前步数的变化
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					int temp = 0;
					while (true) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (StepCounterService.FLAG) {
							Message msg = new Message();
							if (temp != StepDetector.CURRENT_SETP) {
								temp = StepDetector.CURRENT_SETP;
							}

							handler.sendMessage(msg);// 通知主线程
						}
					}
				}
			};
			thread.start();
		}
	}

	SensorEventListener sensorListener = new SensorEventListener() {
		private float predegree = 0;

		public void onSensorChanged(SensorEvent event) {
			float degree = event.values[0];// 数组中的第一个数是方向值
			RotateAnimation anim = new RotateAnimation(predegree, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			anim.setDuration(200);
			imgView.startAnimation(anim);
			predegree = -degree;// 记录这一次的起始角度作为下次旋转的初始角度
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		manager.unregisterListener(sensorListener);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Device_Sensors);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系，
		int span = 1000;
		try {
		} catch (Exception e) {
		}
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		// option.setIsNeedAddress(checkGeoLocation.isChecked());//可选，设置是否需要地址信息，默认不需要
		option.setIsNeedAddress(true);
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		manager.registerListener(sensorListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		addView();
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 获取Activity相关控件
	 */
	private void addView() {
		tv_show_step = (TextView) this.findViewById(R.id.show_step);
		tv_distance = (TextView) this.findViewById(R.id.distance);
		tv_calories = (TextView) this.findViewById(R.id.calories);
		// tv_velocity = (TextView) this.findViewById(R.id.sudu);
		hide1 = (TableRow) findViewById(R.id.hide1);
		step_counter = (TextView) findViewById(R.id.step_counter);
		Intent service = new Intent(this, StepCounterService.class);
		stopService(service);
		tv_show_step.setText(total_step + "");
		tv_distance.setText(formatDouble(distance));
		tv_calories.setText(formatDouble(calories));
		handler.removeCallbacks(thread);
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		step_length = 50;// 步长
		weight = 65;// 体重
		BDLocation location = null;
		if (StepDetector.CURRENT_SETP == 0 && misGPSResult) {
			Intent service = new Intent(this, StepCounterService.class);
			startService(service);
			countDistance();
			countStep();
			if (distance != 0.0) { // tempTime记录运动的总时间，timer记录每次运动时间
				// 体重、距离
				// 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036，换算一下
				calories = weight * distance * 0.001;
			} else {
				calories = 0.0;
			}
			tv_distance.setText(formatDouble(distance));
			tv_calories.setText(formatDouble(calories));
			tv_show_step.setText(total_step + "");
		}

	}

	/**
	 * 计算并格式化doubles数值，保留两位有效数字
	 * 
	 * @param doubles
	 * @return 返回当前路程
	 */
	private String formatDouble(Double doubles) {
		DecimalFormat format = new DecimalFormat("####.##");
		String distanceStr = format.format(doubles);
		return distanceStr.equals(getString(R.string.zero)) ? getString(R.string.double_zero)
				: distanceStr;
	}

	/**
	 * 计算行走的距离
	 */
	private void countDistance() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			distance = (StepDetector.CURRENT_SETP / 2) * 3 * step_length * 0.01;
		} else {
			distance = ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length
					* 0.01;
		}
	}

	/**
	 * 实际的步数
	 */
	private void countStep() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			total_step = StepDetector.CURRENT_SETP;
		} else {
			total_step = StepDetector.CURRENT_SETP + 1;
		}

		total_step = StepDetector.CURRENT_SETP;
	}

	public void back(View view) {
		mLocationClient.stop();
		finish();
	}

	// 百度local
	public MyLocationListener mMyLocationListener;
	public TextView mlocalResult;
	// public TextView mdirectionResult;
	public TextView msatelliteResult;
	public TextView mvelocityResult;
	public TextView mwarnningResult;
	public ImageView mimgResult;
	public static boolean misGPSResult = false;

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结
				misGPSResult = true;
				if (number == 100)
					number = 2;
				number++;
			} else {
				misGPSResult = false;
				number = 0;
			}
			logMsgLocal(location.getAddrStr());
			logMsgSpeed(location.getSpeed() * 1000 / 3600 + "");
			// logMsgDirection(location.getDirection()+"°");
			logMsgSatellite(location.getSatelliteNumber() + "");
			logMsgWarnning();
		}
	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsgLocal(String str) {
		try {
			if (mlocalResult != null) {
				mlocalResult.setText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logMsgSpeed(String str) {
		try {
			if (mvelocityResult != null) {
				mvelocityResult.setText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void logMsgDirection(String str) { try { if (mdirectionResult !=
	 * null) { mdirectionResult.setText(str); } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	public void logMsgSatellite(String str) {
		try {
			if (msatelliteResult != null) {
				msatelliteResult.setText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logMsgWarnning() {
		try {
			if (mwarnningResult != null) {
				if (!misGPSResult) {
					mwarnningResult.setText("户  外  运  动  更  健  康");
					mimgResult.setVisibility(View.VISIBLE);
					title.setVisibility(View.VISIBLE);
					Log.i("GPS", "百度++++++++++++++++++++++11111111111");
				} else {
					if (number == 1) {
						mwarnningResult.setText("");
						mimgResult.setVisibility(View.GONE);
						title.setVisibility(View.GONE);
						init();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
