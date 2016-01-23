package cn.lee.bookstore.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2016/1/20.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected Activity context;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initListener();
        initData();
    }

    /** 查找View，提供这个方法是为了省略强转 */
    public <T> T findView(int id) {
        @SuppressWarnings("unchecked")
        T view = (T) findViewById(id);
        return view;
    }

    abstract int getLayoutId();

    abstract void initView();

    abstract void initListener();

    abstract void initData();
}
