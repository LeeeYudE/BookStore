package cn.lee.bookstore.pager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.BookPagerAdapter;
import cn.lee.bookstore.bean.BookCatalog;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.pager.book.BookListPager;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ConstantUtil;
import cn.lee.bookstore.utils.LogUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.utils.VolleyUtile;

/**
 * Created by Administrator on 2016/1/18.
 */
public class BookPageFragment extends BasePagerFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<BookCatalog.BookTitle> bookCatalogs;
    private List<BookListPager> pagers;
    private List<Integer> positions;//收集pager的下标
    private RelativeLayout rlt_book_error, mLlt_bar;
    private Button btn_reload;
    private ImageView iv_type,iv_menu;
    private ProgressBar proBar;
    public static boolean isList = true;
    private boolean isShowBar = true, isAnimation = false;
    private int barHeight;
    private LinearLayout llt_titles;

    @Override
    protected int getLayoutResID() {
        return R.layout.page_book;
    }

    @Override
    protected void initView() {
        rlt_book_error = (RelativeLayout) mRootView.findViewById(R.id.rlt_book_error);
        btn_reload = (Button) mRootView.findViewById(R.id.btn_reload);
        iv_type = (ImageView) mRootView.findViewById(R.id.iv_type);
        iv_menu= (ImageView) mRootView.findViewById(R.id.iv_menu);
        mLlt_bar = (RelativeLayout) mRootView.findViewById(R.id.llt_bar);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.vp_book);
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tlt_book);
        llt_titles =(LinearLayout) mRootView.findViewById(R.id.llt_titles);
        proBar= (ProgressBar) mRootView.findViewById(R.id.proBar);
    }


    @Override
    protected void initListener() {
        //获取bar高度
        mLlt_bar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                barHeight = mLlt_bar.getHeight();
                mLlt_bar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        btn_reload.setOnClickListener(this);
        iv_type.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        boolean isNewWork = CommonUtil.isNetWorkAvalible();//判断网络状态
        rlt_book_error.setVisibility(View.GONE);//显示读取中的布局
        if (isNewWork) {//如果是有网络
            proBar.setVisibility(View.VISIBLE);
            requestNetWork();//请求数据
        } else {//如果没有网络
            //显示错误页面
            ToastUtil.showToast("网络连接错误!",ToastUtil.center);
            rlt_book_error.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 请求数据
     */
    private void requestNetWork() {
        RequestQueue requestQueue = VolleyUtile.getRequestQueue();
        StringRequest stringRequest = new StringRequest(Url.BOOK_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {//数据正常返回时回调
                        LogUtil.e("onResponse", json);
                        proBar.setVisibility(View.GONE);
                        processData(json);//处理数据
                    }
                },
                new Response.ErrorListener() {//访问数据出错时
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtil.e("onResponse", volleyError.getMessage());
                        ToastUtil.showToast("访问服务器异常!",ToastUtil.center);
                        proBar.setVisibility(View.GONE);
                        rlt_book_error.setVisibility(View.VISIBLE);//显示错误页面
                    }
                });
        //把请求加进队列
        requestQueue.add(stringRequest);
    }

    /**
     * 处理数据
     * @param json 服务器返回的json数据
     */
    private void processData(String json) {
        if (json!=null){
            llt_titles.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            BookCatalog bookCatalog = gson.fromJson(json, BookCatalog.class);
            if (bookCatalog.resultcode.equals("200")) {
                bookCatalogs = bookCatalog.result;
                setViewPagerData();
            }
        }

    }

    /**
     * 根据数据动态创建布局
     */
    private void setViewPagerData() {
        pagers = new ArrayList<>(bookCatalogs.size());
        positions = new ArrayList<>(bookCatalogs.size());
        for (int i = 0; i < bookCatalogs.size(); i++) {
            BookListPager pager = new BookListPager(mActivity, bookCatalogs.get(i).id);
            pager.setOnScrollListener(new BookListPager.OnScrollListener() {
                @Override
                public void onScroll(int dy) {
                    if (dy > 30 && isShowBar && !isAnimation) {
                        hideBar();
                    } else if (dy < -30 && !isShowBar && !isAnimation) {
                        showBar();
                    }
                }
            });
            pagers.add(pager);
        }
        mViewPager.setAdapter(new BookPagerAdapter(bookCatalogs, pagers, mActivity));
        mTabLayout.setupWithViewPager(mViewPager);

        if (bookCatalogs.size() > 0) {
            loadData(0);
        }
        //给ViewPager添加滚动监听,根据下标加载布局数据
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                loadData(position);
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void showBar() {//显示bar
        isAnimation = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, barHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mLlt_bar.getLayoutParams().height = (Integer) animator.getAnimatedValue();
                mLlt_bar.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                isShowBar=true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private void hideBar() {//隐藏bar
        isAnimation = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(barHeight, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mLlt_bar.getLayoutParams().height = (Integer) animator.getAnimatedValue();
                mLlt_bar.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                isShowBar=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private void loadData(int position) {
        //如果不是第一次滚动,就返回
        if (!positions.contains(position)) {
            //否则加载数据并把下标添加到集合中
            positions.add(position);
            pagers.get(position).initData();
            //预加载上下两页的数据
        }
        if (position + 1 <= bookCatalogs.size() - 1 && !positions.contains(position + 1)) {
            positions.add(position + 1);
            pagers.get(position + 1).initData();
        }
        if (position - 1 >= 0 && !positions.contains(position - 1)) {
            positions.add(position - 1);
            pagers.get(position - 1).initData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reload:
                initData();
                break;
            case R.id.iv_type:
                changeManger();
                break;
            case R.id.iv_menu:
                sendBroadCast();
                break;
        }
    }

    private void changeManger(){//改变布局
        if (pagers != null) {
            if (isList) {
                iv_type.setImageResource(R.drawable.tab_barrage );
            } else {
                iv_type.setImageResource(R.drawable.lhj);
            }
            isList = !isList;
            for (BookListPager pager : pagers) {
                pager.changeManager(isList);
            }
        }
    }

    private void sendBroadCast() {//发送广播
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(
                new Intent(ConstantUtil.ACTION_OPEN_MENU)
        );
    }
}
