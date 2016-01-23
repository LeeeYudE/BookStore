package cn.lee.bookstore.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.adapter.MainViewPagerAdapter;
import cn.lee.bookstore.pager.BasePagerFragment;
import cn.lee.bookstore.pager.BookPageFragment;
import cn.lee.bookstore.pager.CollcetPagerFragment;
import cn.lee.bookstore.pager.ListenerPageFragment;
import cn.lee.bookstore.pager.MapPageFragment;
import cn.lee.bookstore.utils.ConstantUtil;
import cn.lee.bookstore.utils.LogUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.view.NoScrollViewPager;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private NoScrollViewPager mViewPager;
    private BroadcastReceiver mReceiver;
    private RadioGroup mRadioGroup;
    private ImageView iv_login;
    private NavigationView navigationView;

    @Override
    int getLayoutId() {
        ShareSDK.initSDK(getApplicationContext());
        return R.layout.activity_main;
    }

    @Override
    void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dlt_main);//初始化DrawerLayout
        navigationView = findView(R.id.navigation_view);
        iv_login= (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_login);

    }

    @Override
    void initListener() {
        iv_login.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(onNavigationListener);
    }

    NavigationView.OnNavigationItemSelectedListener onNavigationListener=
            new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_home:
                    mDrawerLayout.closeDrawers();
                    break;
                case R.id.menu_exit:
                    finish();
                    break;
            }
            return true;
        }
    };

    @Override
    void initData() {
        initViewPager();//初始化ViewPager
        initRadioGroup();
        initBroadcast();//初始化本地广播
    }

    private void initBroadcast() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(
                        ConstantUtil.ACTION_OPEN_MENU)){
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                if (intent.getAction().equals(
                        ConstantUtil.ACTION_SCORLL_TO_STORE)){
                    mViewPager.setCurrentItem(0,false);
                    mRadioGroup.check(R.id.rb_book);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtil.ACTION_OPEN_MENU);
        intentFilter.addAction(ConstantUtil.ACTION_SCORLL_TO_STORE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,intentFilter);
    }

    private void initRadioGroup() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_center);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.i("mRadioGroup","mRadioGroup");
                switch (checkedId){
                    case R.id.rb_book:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_map:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_service:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(3,false);
                        break;
                }
            }
        });
    }


    private void initViewPager() {
        mViewPager= (NoScrollViewPager) findViewById(R.id.vp_center);
        List<BasePagerFragment> fragments=new ArrayList<>(4);
        fragments.add(new BookPageFragment());
        fragments.add(new MapPageFragment());
        fragments.add(new CollcetPagerFragment());
        fragments.add(new ListenerPageFragment());
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(),fragments));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    //双击退出应用
    private long currentTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-currentTime<2000){
                finish();
            }else {
                ToastUtil.showToast("再次点击退出应用",ToastUtil.bottom);
                currentTime=System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login:
                ToastUtil.showToast("iv_login");
                break;
        }
    }
}
