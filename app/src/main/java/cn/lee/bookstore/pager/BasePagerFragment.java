package cn.lee.bookstore.pager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/8.
 */
public abstract class BasePagerFragment extends Fragment implements View.OnClickListener {

    public Activity mActivity;
    public View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mActivity=getActivity();
        mRootView=inflater.inflate(getLayoutResID(),null);
        initView();
        initListener();
        initData();
        return mRootView;
    }

    protected abstract int getLayoutResID();

    /** 初始化View */
    protected abstract void initView();

    /** 初始化监听器 */
    protected abstract void initListener();

    /** 初始化数据，并且把数据显示到界面上 */
    protected abstract void initData();

}
