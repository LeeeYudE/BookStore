package cn.lee.bookstore.ui.activity;

import android.app.Instrumentation;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import cn.lee.bookstore.R;
import cn.lee.bookstore.http.BitmapCache;

/**图书大图界面
 * Created by Administrator on 2016/1/11.
 */
public class BookImageActivity extends AppCompatActivity {


    public BookImageActivity() {
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_image);
        initView();
    }

    private void initView() {
        NetworkImageView photoView= (NetworkImageView) findViewById(R.id.photoView);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(requestQueue, BitmapCache.getBitmapCache());
        String url = getIntent().getStringExtra("url");
        photoView.setImageUrl(url,imageLoader);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BookImageActivity.this.onBackPressed();
                new Thread() {
                    @Override
                    public void run() {
                        // 使用仪器发送按键事件
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
