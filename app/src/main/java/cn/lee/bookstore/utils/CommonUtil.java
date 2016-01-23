package cn.lee.bookstore.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

public class CommonUtil {

	public static int mScreenWidth;
	public static int mScreenHeight;
	public static float mDensity;

	/**
	 * 常用工具类
	 */

	public static void initScreenSize(Context context){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		mScreenWidth =displayMetrics.widthPixels;
		mScreenHeight =displayMetrics.heightPixels;
		mDensity =displayMetrics.density;
	}
	//在主线程执行任务
	public static void runOnUiThread(Runnable running){
		GlobalApplication.handler.post(running);
	}
	
	//获取系统相关资源
	public static String[] getStringArray(int resId){
		return GlobalApplication.context.getResources().getStringArray(resId);
	}
	
	public static String getString(int resId){
		return GlobalApplication.context.getResources().getString(resId);
	}
	
	public static int getColor(int resId){
		return GlobalApplication.context.getResources().getColor(resId);
	}
	
	public static Drawable getDrawable(int resId){
		return GlobalApplication.context.getResources().getDrawable(resId);
	}
	
	/**
	 * 获取dp资源，并且获取后的值是已经将dp转化为px了
	 * @param resId
	 * @return
	 */
	public static float getDimesion(int resId){
		return GlobalApplication.context.getResources().getDimension(resId);
	}

	//px转dp
	public static int px2dp(int px){
		return (int) (mDensity*px+0.5);
	}

	/** 是wifi连接 */
	public static final int WIFI = 1;
	/** 不是wifi连接 */
	public static final int NOWIFI = 2;
	/** 网络不可用 */
	public static final int NONETWORK = 0;

	//判断网络连接类型
	public static int getNetWorkType() {
		if (!isNetWorkAvalible()) {
			return CommonUtil.NONETWORK;
		}
		ConnectivityManager cm = (ConnectivityManager) GlobalApplication.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
			return CommonUtil.WIFI;
		else
			return CommonUtil.NOWIFI;
	}

	//判断是网络连接状态
	public static boolean isNetWorkAvalible() {
		ConnectivityManager cm = (ConnectivityManager) GlobalApplication.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null || !ni.isAvailable()) {
			return false;
		}
		return true;
	}

	public static String getSize(int size){
		return Formatter.formatFileSize(GlobalApplication.context.getApplicationContext(),size);
	}
}
