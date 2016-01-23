package cn.lee.bookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtil {
	
	private static SharedPreferences sp;
	public static String IS_GUIDE="is_guide";
	public static String READ_IDS="read_ids";
	
	private static SharedPreferences getSharedPreferences(){
		if(sp==null){//如果sp为空就创建
			sp=GlobalApplication.context.getSharedPreferences("cache", Context.MODE_PRIVATE);
		}
		return sp;
	}
	
	//存储字符串
	public static void putString(String key,String value){
		getSharedPreferences().edit().putString(key, value).commit();
	}
	
	//储存boolean
	public static void putBoolean(String key,boolean value){
		getSharedPreferences().edit().putBoolean(key, value).commit();
	}
	
	//获取字符串,有默认值
	public static String getString(String key,String defValue){
		return getSharedPreferences().getString(key, defValue);
	}
	
	//获取字符串,没有默认值
	public static String getString(String key){
		return getSharedPreferences().getString(key, null);
	}
	
	//获取boolean,有默认值
	public static boolean getBoolean(String key,boolean defValue){
		return getSharedPreferences().getBoolean(key, defValue);
	}
	//获取boolean,没有默认值
	public static boolean getBoolean(String key){
		return getSharedPreferences().getBoolean(key, false);
	}
}
