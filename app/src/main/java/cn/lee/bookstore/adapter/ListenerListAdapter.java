package cn.lee.bookstore.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.bean.ListenerList;
import cn.lee.bookstore.utils.CommonUtil;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ListenerListAdapter extends RecyclerView.Adapter{

    private List<ListenerList.ListEntity> lists;
    private Activity mActivity;

    public ListenerListAdapter(List<ListenerList.ListEntity> lists, Activity mActivity) {
        this.lists=lists;
        this.mActivity=mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(mActivity.getApplication()).inflate(R.layout.item_detail_list,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(item);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        myViewHolder.tv_name.setText(lists.get(position).name);
        myViewHolder.tv_size.setText(CommonUtil.getSize(lists.get(position).size));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

      public  TextView tv_name,tv_size;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_size= (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

}
