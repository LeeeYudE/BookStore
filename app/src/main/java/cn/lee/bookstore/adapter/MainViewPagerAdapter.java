package cn.lee.bookstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.lee.bookstore.pager.BasePagerFragment;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<BasePagerFragment> fragments;

    public MainViewPagerAdapter(FragmentManager fm, List<BasePagerFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
