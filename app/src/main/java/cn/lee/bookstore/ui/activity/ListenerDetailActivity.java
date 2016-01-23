package cn.lee.bookstore.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.DetailViewPagerAdapter;
import cn.lee.bookstore.pager.DetailFragment;
import cn.lee.bookstore.pager.ListFragment;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ListenerDetailActivity extends BaseActivity {

    private TextView tv_back;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_name;
    private TextView tv_total;
    private TextView tv_current;
    private ImageView iv_next;
    private ImageView iv_play;
    private ImageView iv_pre;

    @Override
    int getLayoutId() {
        return R.layout.activity_listener_detail;
    }

    @Override
    void initView() {
        tv_back = findView(R.id.tv_back);
        tabLayout = findView(R.id.tlt_detail);
        viewPager = findView(R.id.vp_detail);
        tv_name = findView(R.id.tv_name);
        tv_total = findView(R.id.tv_total);
        tv_current = findView(R.id.tv_current);
        iv_next = findView(R.id.iv_next);
        iv_play = findView(R.id.iv_play);
        iv_pre = findView(R.id.iv_pre);
        initViewPager();
    }

    private void initViewPager() {
        int id=getIntent().getIntExtra("id",-1);
        List<Fragment> fragments=new ArrayList<>(2);
        Bundle bundle=new Bundle();
        bundle.putInt("id",id);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        fragments.add(detailFragment);
        fragments.add(listFragment);
        viewPager.setAdapter(new DetailViewPagerAdapter(getSupportFragmentManager(),fragments));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    void initListener() {
        tv_back.setOnClickListener(this);
    }

    @Override
    void initData() {
        tv_back.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
