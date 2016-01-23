package cn.lee.bookstore.pager;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.ListenerCategoryAdapter;
import cn.lee.bookstore.bean.ListenerCategory;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ConstantUtil;
import cn.lee.bookstore.utils.LogUtil;
import cn.lee.bookstore.utils.ToastUtil;

/**
 * Created by Administrator on 2016/1/18.
 */
public class ListenerPageFragment extends BasePagerFragment {

    private ImageView iv_menu;
    private ImageView iv_listener;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private RelativeLayout rlt_book_error;
    private Button btn_reload;

    @Override
    protected int getLayoutResID() {
        return R.layout.page_listener;
    }

    @Override
    protected void initView() {
        iv_menu = (ImageView) mRootView.findViewById(R.id.iv_menu);
        iv_listener = (ImageView) mRootView.findViewById(R.id.iv_listener);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rcv_listener);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.proBar);
        rlt_book_error = (RelativeLayout) mRootView.findViewById(R.id.rlt_book_error);
        btn_reload = (Button) mRootView.findViewById(R.id.btn_reload);

    }



    @Override
    protected void initListener() {
        iv_menu.setOnClickListener(this);
        iv_listener.setOnClickListener(this);
        btn_reload.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        boolean isNewWork = CommonUtil.isNetWorkAvalible();//判断网络状态
        rlt_book_error.setVisibility(View.GONE);//显示读取中的布局
        if (isNewWork) {//如果是有网络
            requestNetWork();//请求数据
        } else {//如果没有网络
            //显示错误页面
            ToastUtil.showToast("网络连接错误!",ToastUtil.center);
            rlt_book_error.setVisibility(View.VISIBLE);
        }
    }

    private void requestNetWork() {
        progressBar.setVisibility(View.VISIBLE);//显示进度条
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Url.Listener_host,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {//数据正常返回时回调
                        LogUtil.i("onResponse", json);
                        progressBar.setVisibility(View.GONE);//隐藏进度条
                        processData(json);//处理数据
                    }
                },
                new Response.ErrorListener() {//访问数据出错时
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtil.e("onResponse", volleyError.getMessage());
                        progressBar.setVisibility(View.GONE);//隐藏进度条
                        rlt_book_error.setVisibility(View.VISIBLE);//显示错误页面
                    }
                });
        //把请求加进队列
        requestQueue.add(stringRequest);
    }

    private void processData(String json) {//处理数据
        if (json!=null){
            Gson gson = new Gson();
            ListenerCategory category= gson.fromJson(json, ListenerCategory.class);
            LogUtil.e("ListenerCategory",category.list.toString());
            initRecyclerView(category.list);
        }
    }

    private void initRecyclerView(List<ListenerCategory.CategoryEntity> list) {
        GridLayoutManager manager=new GridLayoutManager(mActivity.getApplication(),2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new ListenerCategoryAdapter(list,mActivity));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_menu:
                openMenu();
                break;
            case R.id.btn_reload:
                initData();
                break;
            case R.id.iv_listener:
                ToastUtil.showToast("iv_listener");
                break;
        }
    }

    private void openMenu() {//打开左菜单
        LocalBroadcastManager.getInstance(mActivity.getApplicationContext())
                .sendBroadcast(new Intent(ConstantUtil.ACTION_OPEN_MENU));
    }
}
