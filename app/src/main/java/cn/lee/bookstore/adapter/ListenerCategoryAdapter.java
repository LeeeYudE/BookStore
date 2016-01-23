package cn.lee.bookstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.bean.ListenerCategory;
import cn.lee.bookstore.ui.activity.CategoryListActivity;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ListenerCategoryAdapter extends RecyclerView.Adapter {


    private List<ListenerCategory.CategoryEntity> categorys;
    private List<Integer> imgIdS;
    private Activity mActivity;

    public ListenerCategoryAdapter(List<ListenerCategory.CategoryEntity> list,Activity activity) {
        this.mActivity=activity;
        this.categorys=list;
        initData();
    }

    private void initData() {
        imgIdS=new ArrayList<>();
        imgIdS.add(R.drawable.image_note_ds);
        imgIdS.add(R.drawable.image_note_qcyq);
        imgIdS.add(R.drawable.image_note_kb);
        imgIdS.add(R.drawable.image_note_tr);
        imgIdS.add(R.drawable.image_note_xh);
        imgIdS.add(R.drawable.image_note_ls);
        imgIdS.add(R.drawable.image_note_wx);
        imgIdS.add(R.drawable.image_note_zt);
        imgIdS.add(R.drawable.image_note_gc);
        imgIdS.add(R.drawable.image_note_wykh);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(mActivity).inflate(R.layout.item_listener_category,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(item);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.iv_category.setImageResource(imgIdS.get(position));
        viewHolder.tv_category.setText(categorys.get(position).name);
        viewHolder.llt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CategoryListActivity.class);
                intent.putExtra("category",categorys.get(position).name);
                intent.putExtra("type",categorys.get(position).id);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorys.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_category;
        public ImageView iv_category;
        public LinearLayout llt_category;

        public MyViewHolder(View itemView) {
            super(itemView);
            llt_category= (LinearLayout) itemView.findViewById(R.id.llt_category);
            tv_category= (TextView) itemView.findViewById(R.id.tv_category);
            iv_category= (ImageView) itemView.findViewById(R.id.iv_category);
        }
    }
}
