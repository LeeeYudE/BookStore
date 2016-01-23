package cn.lee.bookstore.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.CategoryListAdapter;
import cn.lee.bookstore.bean.CategoryList;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.utils.VolleyUtile;

/**
 * Created by Administrator on 2016/1/20.
 */
public class CategoryListActivity extends BaseActivity implements Response.ErrorListener, Response.Listener<String> {

    private TextView tv_catrgory;
    private ImageView iv_listener;
    private ProgressBar progressBar;
    private XRecyclerView mRecyclerView;
    private RelativeLayout rlt_error,llt_bar;
    private Button btn_reload;
    private int type,pageNum=1;
    private RequestQueue requestQueue;
    private List<CategoryList.ListEntity> lists=new ArrayList<>();
    private CategoryListAdapter adapter;
    private FloatingActionButton floatingButton;
    private LinearLayoutManager manager;
    private boolean isLoadingMore=false,isRefresh=false;
    private float floatButtonHeight;
    private int barHeight;


    @Override
    int getLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    void initView() {
        type=getIntent().getIntExtra("type",-1);
        llt_bar=findView(R.id.llt_bar);
        iv_listener = findView(R.id.iv_listener);
        tv_catrgory = findView(R.id.tv_category);
        progressBar = findView(R.id.proBar);
        mRecyclerView = findView(R.id.rcv_category);
        rlt_error = findView(R.id.rlt_book_error);
        btn_reload = findView(R.id.btn_reload);
        floatingButton = findView(R.id.fab_list);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        manager = new LinearLayoutManager(context.getApplicationContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new CategoryListAdapter(lists, context);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    void initListener() {
        tv_catrgory.setOnClickListener(this);
        btn_reload.setOnClickListener(this);
        iv_listener.setOnClickListener(this);
        floatingButton.setOnClickListener(this);
        mRecyclerView.setLoadingListener(loadingListener);
        mRecyclerView.addOnScrollListener(scrollListener);
        floatingButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {//获取按钮高度和自身高度的和
                floatingButton.measure(0,0);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) floatingButton.getLayoutParams();
                floatButtonHeight=layoutParams.bottomMargin+floatingButton.getHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    floatingButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        //获取bar高度
        llt_bar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                barHeight = llt_bar.getHeight();
                llt_bar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private boolean isShowBtn=false;
    private boolean isAnimation=false;
    private RecyclerView.OnScrollListener scrollListener=new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 30 && isShowBtn && !isAnimation) {
                hideBtn();
                hideBar();
            } else if (dy < -30 && !isShowBtn && !isAnimation) {
                showBtn();
                showBar();
            }
        }
    };
    private void showBtn() {//
        ViewPropertyAnimator.animate(floatingButton).translationY(0)
                .setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimation = true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                isShowBtn=true;
            }
            public void onAnimationCancel(Animator animation) {
            }
            public void onAnimationRepeat(Animator animation) {
            }
        }).start();
    }

    private void hideBtn() {//

        ViewPropertyAnimator.animate(floatingButton).translationY(floatButtonHeight)
                .setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimation = true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                isShowBtn=false;
            }
            public void onAnimationCancel(Animator animation) {
            }
            public void onAnimationRepeat(Animator animation) {
            }
        }).start();

    }
    private void hideBar() {//隐藏bar
        isAnimation = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(barHeight, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                llt_bar.getLayoutParams().height = (Integer) animator.getAnimatedValue();
                llt_bar.requestLayout();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private void showBar() {//显示bar
        isAnimation = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, barHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                llt_bar.getLayoutParams().height = (Integer) animator.getAnimatedValue();
                llt_bar.requestLayout();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private XRecyclerView.LoadingListener loadingListener=
            new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            isRefresh=true;
            lists.clear();
            pageNum=1;
            requestData();
        }

        @Override
        public void onLoadMore() {
            mRecyclerView.setLoadingMoreEnabled(false);
            isLoadingMore=true;
            requestData();
        }
    };

    @Override
    void initData() {//
        requestQueue = VolleyUtile.getRequestQueue();
        setCategory();
        requestData();
    }

    private void setCategory() {//设置类别
        String catrgory = getIntent().getStringExtra("category");
        tv_catrgory.setText(catrgory);
    }

    private void requestData() {
        boolean isNetWork = CommonUtil.isNetWorkAvalible();//判断网络状态
        if (isNetWork){
            rlt_error.setVisibility(View.GONE);
            if (lists.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);
            }
            String url=String.format(Url.category_list,type,pageNum);

            StringRequest stringRequest=new StringRequest(url,this,this);
            requestQueue.add(stringRequest);
        }else {
            progressBar.setVisibility(View.GONE);
            if (isLoadingMore){
                isLoadingMore=false;
                mRecyclerView.loadMoreComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
            }
            if (isRefresh){
                isRefresh=false;
                mRecyclerView.refreshComplete();
                mRecyclerView.setPullRefreshEnabled(true);
            }

            if (lists.isEmpty()){
                rlt_error.setVisibility(View.VISIBLE);
            }
            ToastUtil.showToast("网络连接错误!",ToastUtil.center);
        }

    }

    @Override
    public void onResponse(String json) {
        if (isLoadingMore){
            mRecyclerView.setLoadingMoreEnabled(true);
            isLoadingMore=false;
            mRecyclerView.loadMoreComplete();
        }
        if (isRefresh){
            mRecyclerView.setPullRefreshEnabled(true);
            isRefresh=false;
            mRecyclerView.refreshComplete();
        }
        pageNum++;
        Gson gson=new Gson();
        CategoryList categoryList=gson.fromJson(json, CategoryList.class);
        lists.addAll(categoryList.list);
        progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        if (isLoadingMore){
            isLoadingMore=false;
            mRecyclerView.loadMoreComplete();
            mRecyclerView.setLoadingMoreEnabled(true);
        }
        if (isRefresh){
            isRefresh=false;
            mRecyclerView.refreshComplete();
            mRecyclerView.setPullRefreshEnabled(true);
        }
        if (lists.isEmpty()){
            rlt_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_category:
                finish();
                break;
            case R.id.btn_reload:
                requestData();
                break;
            case R.id.iv_listener:
                ToastUtil.showToast("iv_listener");
                break;
            case R.id.fab_list:
                backTop();
                break;
        }
    }

    private void backTop() {
        if (mRecyclerView!=null){
            manager.scrollToPosition(0);
        }
    }

}
