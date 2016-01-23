package cn.lee.bookstore.pager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.CollectRecyclerViewAdapter;
import cn.lee.bookstore.dao.DBManager;
import cn.lee.bookstore.utils.ConstantUtil;

/**
 * Created by Administrator on 2016/1/18.
 */
public class CollcetPagerFragment extends BasePagerFragment {

    private static Button btn_collect;
    private ImageView iv_menu;
    private ImageView iv_del;
    private RecyclerView mRecyclerView;
    private BroadcastReceiver receiver;

    @Override
    protected int getLayoutResID() {
        return R.layout.page_collect;
    }

    @Override
    protected void initView() {
        btn_collect = (Button) mRootView.findViewById(R.id.btn_collect);
        iv_menu = (ImageView) mRootView.findViewById(R.id.iv_menu);
        iv_del = (ImageView) mRootView.findViewById(R.id.iv_del);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyckerView);
        initBrocast();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new CollectRecyclerViewAdapter(mActivity));
    }

    @Override
    protected void initListener() {
        btn_collect.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        iv_del.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    private void initBrocast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConstantUtil.ACTION_HIDE_BTN)){
                    hideBtn();
                }else {
                    showBtn();
                }
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConstantUtil.ACTION_HIDE_BTN);
        intentFilter.addAction(ConstantUtil.ACTION_SHOW_BTN);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiver,intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_collect:
                scorllToStore();
                break;
            case R.id.iv_menu:
                openMenu();
                break;
            case R.id.iv_del:
                delAllBook();
                break;
        }
    }

    private void openMenu(){
        LocalBroadcastManager.getInstance(mActivity).
                sendBroadcast(new Intent(ConstantUtil.ACTION_OPEN_MENU));
    }

    private void scorllToStore(){
        LocalBroadcastManager.getInstance(mActivity).
                sendBroadcast(new Intent(ConstantUtil.ACTION_SCORLL_TO_STORE));
    }

    private void delAllBook() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        builder.setMessage("是否确定清空收藏?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManager.delAllBook();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showBtn(){
        btn_collect.setVisibility(View.VISIBLE);
    }

    public static void hideBtn(){
        btn_collect.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(receiver);
    }
}
