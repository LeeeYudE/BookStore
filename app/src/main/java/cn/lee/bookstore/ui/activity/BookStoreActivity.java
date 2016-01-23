package cn.lee.bookstore.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.lee.bookstore.R;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/1/12.
 */
public class BookStoreActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView tv_name;
    private ImageView iv_back,iv_share,iv_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);
        initView();
    }

    private void initView() {
        mWebView= (WebView) findViewById(R.id.wv_book);
        mProgressBar= (ProgressBar) findViewById(R.id.pb_book);
        tv_name= (TextView) findViewById(R.id.tv_store_name);
        iv_back= (ImageView) findViewById(R.id.iv_stroe_back);
        iv_reload= (ImageView) findViewById(R.id.iv_reload);
        iv_share= (ImageView) findViewById(R.id.iv_share);
        tv_name.setText(getIntent().getStringExtra("name"));
        initWebView();
        initListener();
        }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_reload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_stroe_back:
                BookStoreActivity.this.finish();
                break;
            case R.id.iv_share:
                share();
                break;
            case R.id.iv_reload:
                mWebView.reload();
                break;
        }
    }

    private void share() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题：微信、QQ（新浪微博不需要标题）
//                oks.setTitle("我是分享标题");  //最多30个字符

        // text是分享文本：所有平台都需要这个字段
        String store=getIntent().getStringExtra("name");
        StringBuilder sb=new StringBuilder("我在"+store+"发现了一本好书,快来抢购啊！网址戳进:"+mWebView.getUrl());
        oks.setText(sb.toString());  //最多40个字符

        // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
        //oks.setImagePath(Environment.getExternalStorageDirectory() + "/meinv.jpg");//确保SDcard下面存在此张图片

        //网络图片的url：所有平台
        //oks.setImageUrl(getIntent().getStringExtra("url"));//网络图片rul

        // url：仅在微信（包括好友和朋友圈）中使用
//                oks.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情

        // Url：仅在QQ空间使用
//                oks.setTitleUrl("http://www.baidu.com");  //网友点进链接后，可以看到分享的详情

        // 启动分享GUI
        oks.show(BookStoreActivity.this);
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        settings.setUseWideViewPort(true);//支持双击缩放
        settings.setBuiltInZoomControls(true);//支持任意比例缩放
        settings.setSupportZoom(true);
        mWebView.requestFocusFromTouch();//支持手势焦点
        mWebView.reload();//重新加载
        mWebView.setWebViewClient(new WebViewClient(){
            //加载数据前
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }
            //加载完成后
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            //加载进度改变时回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 在加载完成可以关闭水平进度的显示
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //按下返回键上一个网页,而不是销毁Activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
