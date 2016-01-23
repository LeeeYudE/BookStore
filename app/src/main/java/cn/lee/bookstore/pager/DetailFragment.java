package cn.lee.bookstore.pager;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import cn.lee.bookstore.R;
import cn.lee.bookstore.bean.Detail;
import cn.lee.bookstore.http.BitmapCache;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.VolleyUtile;

/**
 * Created by Administrator on 2016/1/21.
 */
public class DetailFragment extends BasePagerFragment implements Response.Listener<String>, Response.ErrorListener {

    private NetworkImageView iv_detail;
    private RatingBar ratingBar;
    private TextView tv_name;
    private TextView tv_announcer;
    private TextView tv_author;
    private TextView tv_sections;
    private TextView tv_update;
    private TextView tv_desc;
    private RelativeLayout llt_error;
    private Button btn_reload;
    private int id;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private LinearLayout llt_detail;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView() {
        iv_detail = (NetworkImageView) mRootView.findViewById(R.id.iv_detail);
        iv_detail.setDefaultImageResId(R.drawable.aio_image_default_round);
        iv_detail.setErrorImageResId(R.drawable.aio_image_default_round);
        ratingBar = (RatingBar) mRootView.findViewById(R.id.ratingBar);
        tv_name = (TextView) mRootView.findViewById(R.id.tv_name);
        tv_announcer = (TextView) mRootView.findViewById(R.id.tv_announcer);
        tv_author = (TextView) mRootView.findViewById(R.id.tv_author);
        tv_sections = (TextView) mRootView.findViewById(R.id.tv_sections);
        tv_update = (TextView) mRootView.findViewById(R.id.tv_update);
        tv_desc = (TextView) mRootView.findViewById(R.id.tv_desc);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.proBar);
        llt_error = (RelativeLayout) mRootView.findViewById(R.id.rlt_book_error);
        btn_reload = (Button) mRootView.findViewById(R.id.btn_reload);
        llt_detail = (LinearLayout) mRootView.findViewById(R.id.llt_detail);
    }

    @Override
    protected void initListener() {
        btn_reload.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        id = getArguments().getInt("id");
        requestQueue = VolleyUtile.getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getBitmapCache());
        requestData();
    }

    private void requestData() {//请求数据
        boolean isNetWork= CommonUtil.isNetWorkAvalible();
        llt_error.setVisibility(View.GONE);
        if (isNetWork){
            progressBar.setVisibility(View.VISIBLE);
            String url=String.format(Url.Listener_introduce,id);
            StringRequest stringRequest=new StringRequest(url,this,this);
            requestQueue.add(stringRequest);
        }else {
            progressBar.setVisibility(View.GONE);
            llt_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reload:
                requestData();
                break;
        }
    }

    @Override
    public void onResponse(String json) {
        progressBar.setVisibility(View.GONE);
        Gson gson =new Gson();
        Detail detail = gson.fromJson(json, Detail.class);
        processData(detail);
    }

    private void processData(Detail detail) {
        iv_detail.setImageUrl(detail.cover,imageLoader);
        tv_name.setText(detail.name);
        tv_announcer.setText("播音 : "+detail.announcer);
        tv_author.setText("作者 : "+detail.author);
        tv_sections.setText("集数 : "+detail.sections);
        tv_update.setText("更新 : "+detail.update);
        tv_desc.setText(detail.desc);
        ratingBar.setRating(Float.valueOf(detail.commentMean));
        llt_detail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        llt_error.setVisibility(View.VISIBLE);
    }

}
