package cn.lee.bookstore.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.lee.bookstore.bean.BookCatalog;
import cn.lee.bookstore.pager.book.BookListPager;

/**
 * Created by Administrator on 2016/1/8.
 */
public class BookPagerAdapter extends PagerAdapter {

    private List<BookCatalog.BookTitle> mTitles;
    private List<BookListPager> pagers;

    public BookPagerAdapter(List<BookCatalog.BookTitle> mTitles,List<BookListPager> pagers, Activity activity) {
        this.mTitles = mTitles;
        this.pagers=pagers;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BookListPager page=pagers.get(position);
        container.addView(page.mRootView);
        return page.mRootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).catalog;
    }

}
