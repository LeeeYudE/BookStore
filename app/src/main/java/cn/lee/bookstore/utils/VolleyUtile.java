package cn.lee.bookstore.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/1/20.
 */
public class VolleyUtile {
    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(GlobalApplication.context);
        }
        return requestQueue;
    }

}
