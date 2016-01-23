package cn.lee.bookstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.bean.CategoryList;
import cn.lee.bookstore.http.BitmapCache;
import cn.lee.bookstore.ui.activity.BookImageActivity;
import cn.lee.bookstore.ui.activity.ListenerDetailActivity;
import cn.lee.bookstore.utils.VolleyUtile;

/**
 * Created by Administrator on 2016/1/20.
 */
public class CategoryListAdapter extends RecyclerView.Adapter {


    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private Activity mActivity;
    private List<CategoryList.ListEntity> lists;

    public CategoryListAdapter(List<CategoryList.ListEntity> lists, Activity activity) {
        this.lists=lists;
        this.mActivity=activity;
        requestQueue = VolleyUtile.getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getBitmapCache());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(mActivity).inflate(R.layout.item_listener_list,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(item);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder= (MyViewHolder) holder;
        CategoryList.ListEntity listEntity=lists.get(position);
        viewHolder.iv_list.setImageUrl(listEntity.cover,imageLoader);
        viewHolder.tv_state.setText(listEntity.state==1?"完结":"连载");
        viewHolder.tv_title.setText(listEntity.name);
        viewHolder.tv_announcer.setText("播音 : "+listEntity.announcer);
        viewHolder.tv_set.setText(listEntity.sections+"集");
        viewHolder.tv_hot.setText(listEntity.hot+"人气");
        initListener(viewHolder,position);
    }


    private void initListener(final MyViewHolder viewHolder,final int position) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case  R.id.iv_list:
                        showImage(viewHolder,position);
                    break;
                    case R.id.llt_category:
                        jump2Detail(position);
                        break;
                }
            }
        };
        viewHolder.iv_list.setOnClickListener(onClickListener);
        viewHolder.llt_category.setOnClickListener(onClickListener);
    }

    private void jump2Detail(int position) {
        Intent intent= new Intent(mActivity, ListenerDetailActivity.class);
        intent.putExtra("name",lists.get(position).name);
        intent.putExtra("id",lists.get(position).id);
        mActivity.startActivity(intent);
    }

    private void showImage(RecyclerView.ViewHolder viewHolder,int position){
        //  跳转到大图页面
        CategoryListAdapter.MyViewHolder holder= (MyViewHolder) viewHolder;
        Intent intent=new Intent(mActivity, BookImageActivity.class);
        intent.putExtra("url",lists.get(position).cover);
        if(Build.VERSION.SDK_INT>=21){
            ActivityOptionsCompat option=ActivityOptionsCompat.makeSceneTransitionAnimation(
                    mActivity,holder.iv_list,"tran_icon");
            mActivity.startActivity(intent,option.toBundle());
        }else {
            mActivity.startActivity(intent);
        }
    };

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView iv_list;
        public LinearLayout llt_category;
        public TextView tv_state,tv_title,tv_announcer,tv_set,tv_hot;

        public MyViewHolder(View itemView) {
            super(itemView);
            llt_category= (LinearLayout) itemView.findViewById(R.id.llt_category);
            iv_list= (NetworkImageView) itemView.findViewById(R.id.iv_list);
            tv_state= (TextView) itemView.findViewById(R.id.tv_state);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_announcer= (TextView) itemView.findViewById(R.id.tv_announcer);
            tv_set= (TextView) itemView.findViewById(R.id.tv_set);
            tv_hot= (TextView) itemView.findViewById(R.id.tv_hot);
            iv_list.setErrorImageResId(R.drawable.aio_image_default_round);
            iv_list.setDefaultImageResId(R.drawable.aio_image_default_round);
        }
    }
}
