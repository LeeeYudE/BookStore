package cn.lee.bookstore.pager;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.ListenerListAdapter;
import cn.lee.bookstore.bean.ListenerList;
import cn.lee.bookstore.http.Url;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.VolleyUtile;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ListFragment extends BasePagerFragment implements Response.ErrorListener, Response.Listener<String> {

    private XRecyclerView xRecyclerView;
    private ProgressBar progressBar;
    private RelativeLayout rlt_error;
    private Button btn_reload;
    private RequestQueue requestQueue;
    private List<ListenerList.ListEntity> lists=new ArrayList<>();
    private Gson gson =new Gson();
    private int id,pageNum=1;
    public boolean isLoadingMore;
    private ListenerListAdapter adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView() {
        xRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.rcv_list);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.proBar);
        rlt_error = (RelativeLayout) mRootView.findViewById(R.id.rlt_book_error);
        btn_reload =(Button) mRootView.findViewById(R.id.btn_reload);
        initRecyclerView();
    }

    private void initRecyclerView() {
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setPullRefreshEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity.getApplicationContext());
        xRecyclerView.setLayoutManager(manager);
        adapter = new ListenerListAdapter(lists, mActivity);
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        btn_reload.setOnClickListener(this);
        xRecyclerView.setLoadingListener(loadingListener);
    }

    XRecyclerView.LoadingListener loadingListener=new XRecyclerView.LoadingListener() {

        @Override
        public void onRefresh() {
        }
        @Override
        public void onLoadMore() {
            isLoadingMore=true;
            requestData();
        }
    };

    @Override
    protected void initData() {
        id = getArguments().getInt("id");
        requestQueue = VolleyUtile.getRequestQueue();
        requestData();
    }

    private void requestData() {
        boolean isNetWork= CommonUtil.isNetWorkAvalible();
        rlt_error.setVisibility(View.GONE);
        if (isNetWork){
            if (lists.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);
            }
            String url=String.format(Url.listener_list,id,pageNum);
            StringRequest stringRequest=new StringRequest(url,this,this);
            requestQueue.add(stringRequest);
        }else {
            progressBar.setVisibility(View.GONE);
            if (isLoadingMore){
                isLoadingMore=false;
                xRecyclerView.loadMoreComplete();
            }
            if (lists.isEmpty()){
                rlt_error.setVisibility(View.VISIBLE);
            }
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
        if (isLoadingMore){
            isLoadingMore=false;
            xRecyclerView.loadMoreComplete();
        }
        pageNum++;
        ListenerList listenerList=gson.fromJson(json, ListenerList.class);
        lists.addAll(listenerList.list);
        if (!lists.isEmpty()){
            progressBar.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        if (isLoadingMore){
            isLoadingMore=false;
            xRecyclerView.loadMoreComplete();
        }
        if (lists.isEmpty()){
            rlt_error.setVisibility(View.VISIBLE);
        }
    }
}
