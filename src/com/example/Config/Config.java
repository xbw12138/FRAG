package com.example.Config;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.frag_update.UpdateInfo;

public class Config {

	private static UpdateInfo info;
	public static int login_control = 0;
	

	public static String getUrl() {
		return urls;
	}

	public static boolean isemp = true;
	public static String titles = "";
	public static boolean isinit=false;
	public static boolean islogin=true;
	public static String username=null;
	// 填写从短信SDK应用后台注册得到的APPKEY
	public static String mob_APPKEY = "xxxxxxxx";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	public static String mob_APPSECRET = "13ace138xxxxxxxxxxxxxxxxxx";
	// mysql
	public static String urls = "http://127.0.0.1/androidapp/appupdate";
	public static String gongnengjieshao = "http://ecfun.cc/fragfunction/";
	public static String guanwang = "http://ecfun.cc/fragui/";
	public static String xieyi = "http://ecfun.cc/fragprotocol/";
	public static String mysql_url_connect = "http://127.0.0.1/androidapp/mysql/insert_login.php";
	public static String uploadUrl = "http://127.0.0.1/androidapp/image_head_upload/receive_file.php";
	public static String uploadbgUrl = "http://127.0.0.1/androidapp/image_bg_upload/receive_file.php";
	
	// public static String user_name="";
	// public static String user_sign="";
	// public static String user_image_head="";
	// public static String user_image_bg="";
	// public static String user_sex="";
	// public static String user_age="";
	// public static String user_schoolname="";
	// public static String user_talk="";
	// public static String user_signtime="";
	// public static String user_vip="";
	// public static boolean UI=false;
	// public static boolean isnetwork=false;
	// public static String
	// mysql_url_change="http://127.0.0.1/androidapp/mysql/change";
	// public static String function_open_price="";//控制查询价格功能开关;
	// public static String version="0";
	// public static String Description="";
	// public static String versiontitle="";
	// public static String versionurl="";
	public static Drawable bitmap;
	public static Drawable bitmap_bg;

	// 扫码
	public static final String KEY_DECODE_1D = "preferences_decode_1D";
	public static final String KEY_DECODE_1D_PRODUCT = "preferences_decode_1D_product";
	public static final String KEY_DECODE_1D_INDUSTRIAL = "preferences_decode_1D_industrial";
	public static final String KEY_DECODE_QR = "preferences_decode_QR";
	public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
	public static final String KEY_DECODE_AZTEC = "preferences_decode_Aztec";
	public static final String KEY_DECODE_PDF417 = "preferences_decode_PDF417";
	public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";
	public static final String KEY_PLAY_BEEP = "preferences_play_beep";
	public static final String KEY_VIBRATE = "preferences_vibrate";
	public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
	public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
	public static final String KEY_BULK_MODE = "preferences_bulk_mode";
	public static final String KEY_REMEMBER_DUPLICATES = "preferences_remember_duplicates";
	public static final String KEY_SUPPLEMENTAL = "preferences_supplemental";
	public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
	public static final String KEY_INVERT_SCAN = "preferences_invert_scan";
	public static final String KEY_SEARCH_COUNTRY = "preferences_search_country";
	public static final String KEY_DISABLE_AUTO_ORIENTATION = "preferences_orientation";
	public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
	public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";
	public static final String KEY_DISABLE_METERING = "preferences_disable_metering";
	public static final String KEY_DISABLE_BARCODE_SCENE_MODE = "preferences_disable_barcode_scene_mode";
	public static final String KEY_AUTO_OPEN_WEB = "preferences_auto_open_web";
}
