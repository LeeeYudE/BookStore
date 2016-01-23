package cn.lee.bookstore.pager.book;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.BookRecyclerViewAdapter;
import cn.lee.bookstore.adapter.MyItemTouchHelperCallback;
import cn.lee.bookstore.bean.BookDetail;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.utils.VolleyUtile;

/**图书列表类
 * Created by Administrator on 2016/1/9.
 */
public class BookListPager implements Response.Listener<String>, Response.ErrorListener {
    private Activity mActivity;
    private String catalog_id;
    public View mRootView;
    private XRecyclerView mRecyclerView;
    private FloatingActionButton mFloatingButton;
    private Button mBtn_reload;
    private RelativeLayout mRlt_error;
    private FrameLayout mFlt_loading;
    private List<BookDetail.ResultEntity.DataEntity> mData;
    private LinearLayoutManager linearManager;
    private final RequestQueue requestQueue;
    private boolean isLoading=false;
    private StaggeredGridLayoutManager staggeredManager;
    private OnScrollListener onScrollListener;
    private float floatButtonHeight;
    private boolean isAnimation=false,isShowBtn=true;
    private BookRecyclerViewAdapter mAdapter;
    private Gson gson=new Gson();

    public BookListPager(Activity mActivity, String catalog_id) {
        this.mActivity = mActivity;
        this.catalog_id = catalog_id;
        requestQueue = VolleyUtile.getRequestQueue() ;
        mData=new ArrayList<>();
        initView();//初始化视图
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mRootView=View.inflate(mActivity, R.layout.page_book_list,null);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.rcv_book);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        mFloatingButton = (FloatingActionButton) mRootView.findViewById(R.id.fab_book);
        mRlt_error = (RelativeLayout) mRootView.findViewById(R.id.rlt_book_error);
        mBtn_reload = (Button) mRootView.findViewById(R.id.btn_reload);
        mFlt_loading = (FrameLayout) mRootView.findViewById(R.id.flt_book_loading);
        initManager();
        initListener();//初始化事件
    }

    /**
     * 初始化LayoutManager
     */
    private void initManager() {
        mAdapter = new BookRecyclerViewAdapter(mData, mActivity);
        ItemTouchHelper.Callback callback=new MyItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        linearManager = new LinearLayoutManager(mActivity,
                    LinearLayoutManager.VERTICAL,false);
        staggeredManager = new StaggeredGridLayoutManager
                (2,StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearManager);
            mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * 改变LayoutManager
     * @param isList
     */
    public void changeManager(boolean isList){
        if (!isList){
            mRecyclerView.setLayoutManager(staggeredManager);
        }else {
            mRecyclerView.setLayoutManager(linearManager);
        }
        mAdapter = new BookRecyclerViewAdapter(mData, mActivity);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        boolean isNewWork= CommonUtil.isNetWorkAvalible();
        //如果网络连接正常则获取数据
        mRlt_error.setVisibility(View.GONE);//隐藏错误页面
        if(isNewWork){
            if(mData.isEmpty()){//如果是第一次加载就显示进度条
                mFlt_loading.setVisibility(View.VISIBLE);
            }
            //创建请求队列
            String url=String.format(Url.BOOK_CONTENT,catalog_id,mData.size(),20);
            StringRequest stringRequest= new StringRequest(url,
                    this, this);

            requestQueue.add(stringRequest);
        }else{
            ToastUtil.showToast("网络连接错误!",ToastUtil.center);
            mRecyclerView.loadMoreComplete();
            if (mData.isEmpty()){
                mRlt_error.setVisibility(View.VISIBLE);//显示错误页面
            }
        }

    }

    @Override
    public void onResponse(String json) {
        processData(json);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if(isLoading){
            ToastUtil.showToast("加载数据出错!",ToastUtil.center);
            mRecyclerView.loadMoreComplete();
            mRecyclerView.setLoadingMoreEnabled(true);
            isLoading=false;
        }
        mFlt_loading.setVisibility(View.GONE);
        if(mData.isEmpty()){
            mRlt_error.setVisibility(View.VISIBLE);
        }
    }

    private void processData(String json) {
        if(isLoading){
            isLoading=false;
            mRecyclerView.setLoadingMoreEnabled(true);
            mRecyclerView.loadMoreComplete();
        }

        BookDetail bookDetail=gson.fromJson(json,BookDetail.class);
        if(bookDetail.resultcode.equals("200")){
            mData.addAll(bookDetail.result.data);
        }
        mAdapter.notifyDataSetChanged();

        mFlt_loading.setVisibility(View.GONE);
    }

    private void initListener() {

        mFloatingButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFloatingButton.measure(0,0);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mFloatingButton.getLayoutParams();
                floatButtonHeight=layoutParams.bottomMargin+mFloatingButton.getHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mFloatingButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView.getLayoutManager()!=null){
                    mRecyclerView.getLayoutManager().scrollToPosition(0);
                }
            }
        });
        mBtn_reload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initData();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

               if (onScrollListener!=null){
                   onScrollListener.onScroll(dy);
               }

                if (dy > 30 && isShowBtn && !isAnimation) {
                    hideBtn();
                } else if (dy < -30 && !isShowBtn && !isAnimation) {
                    showBtn();
                }
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                    mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.setLoadingMoreEnabled(false);
                isLoading=true;
                initData();
            }
        });
    }

    private void showBtn() {//
        ViewPropertyAnimator.animate(mFloatingButton).translationY(0)
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

        ViewPropertyAnimator.animate(mFloatingButton).translationY(floatButtonHeight)
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

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener=onScrollListener;
    }

   public interface OnScrollListener{
       void onScroll(int dy);
   }
}
