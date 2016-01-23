package cn.lee.bookstore.utils;


import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	public static Toast toast;//单例的吐司
	public static final int center=0;
	public static final int bottom=1;
	public static boolean isShow=true;
	/**
	 * 能够连续弹的吐司，不用等上个吐司消失
	 * @param text
	 */
	public static void showToast(String text,int gravity){
		if (isShow){
			if(toast==null){
				//创建toast
				toast = Toast.makeText(GlobalApplication.context, text,Toast.LENGTH_SHORT);
			}else {
				//改变当前toast的文本
				toast.setText(text);
			}
			switch (gravity){
				case center:
					toast.setGravity(Gravity.CENTER,0,0);
					break;
				case bottom:
					toast.setGravity(Gravity.BOTTOM,0,250);
					break;
			}
			toast.show();
		}

	}

	public static void showToast(String text){
		if(isShow){
			if(toast==null){
				//创建toast
				toast = Toast.makeText(GlobalApplication.context, text,Toast.LENGTH_SHORT);

			}else {
				//改变当前toast的文本
				toast.setText(text);
			}

			toast.show();
		}
		}

}
