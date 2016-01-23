package cn.lee.bookstore.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GlobalApplication extends Application{
	public static Handler handler;
	public static Context context;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context=this;
		handler=new Handler();
		//初始化百度地图
		SDKInitializer.initialize(getApplicationContext());
		CommonUtil.initScreenSize(this);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
	}



}
