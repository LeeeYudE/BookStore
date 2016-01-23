package cn.lee.bookstore.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import cn.lee.bookstore.R;
import cn.lee.bookstore.dao.DBManager;

/**
 * Created by Administrator on 2016/1/22.
 */
public class SplashActivity extends BaseActivity {

    private ImageView iv_logo;

    @Override
    int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                );
        return R.layout.activity_splash;
    }

    @Override
    void initView() {
        iv_logo=findView(R.id.iv_logo);
    }

    @Override
    void initListener() {
        ViewHelper.setAlpha(iv_logo,0.5f);
        ViewPropertyAnimator.animate(iv_logo).alpha(1).setDuration(1000).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SplashActivity.this.startActivity(
                                new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                        overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
                    }
                    public void onAnimationStart(Animator animation) {
                    }
                    public void onAnimationCancel(Animator animation) {
                    }
                    public void onAnimationRepeat(Animator animation) {

                    }
                }
        ).start();
    }

    @Override
    void initData() {
        DBManager.initData();//获取数据库数据
    }

    @Override
    public void onClick(View v) {

    }
}
