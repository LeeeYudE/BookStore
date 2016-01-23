package cn.lee.bookstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cn.lee.bookstore.R;
import cn.lee.bookstore.bean.BookDetail;
import cn.lee.bookstore.dao.DBManager;
import cn.lee.bookstore.http.BitmapCache;
import cn.lee.bookstore.ui.activity.BookImageActivity;
import cn.lee.bookstore.ui.activity.BookStoreActivity;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ConstantUtil;
import cn.lee.bookstore.utils.LogUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.utils.VolleyUtile;
import cn.lee.bookstore.view.FlowLayout;

/**
 * Created by Administrator on 2016/1/9.
 */
public class CollectRecyclerViewAdapter extends RecyclerView.Adapter {

    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private List<BookDetail.ResultEntity.DataEntity> mData;
    private Activity mActivity;
    private int textSize,titleSize,margins,rightMargin;
    private LinearLayout.LayoutParams layoutParams;

    public CollectRecyclerViewAdapter( Activity mActivity) {
        this.mData = DBManager.getBookDetail();
        this.mActivity = mActivity;
        initParams();
        DBManager.addOnDBChange(new DBManager.OnDBChange() {
            @Override
            public void onDBChange() {
                CollectRecyclerViewAdapter.this.notifyDataSetChanged();
                judgeSize();
            }
        });
        DBManager.setOnCollectChange(new DBManager.OnCollectChange() {
            @Override
            public void onCollectChange(List<BookDetail.ResultEntity.DataEntity> entityList) {
             mData=entityList;
                CollectRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
        judgeSize();
        requestQueue = VolleyUtile.getRequestQueue();
        imageLoader = new ImageLoader(requestQueue,BitmapCache.getBitmapCache());
    }

    private void initParams() {
        //设定字体大小
        textSize = mActivity.getResources().getDimensionPixelSize(R.dimen.sp_12);
        titleSize = mActivity.getResources().getDimensionPixelSize(R.dimen.sp_18);
        margins = CommonUtil.px2dp(5);
        //设定右外边距
        rightMargin = (int) CommonUtil.getDimesion(R.dimen.dp_5);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin= rightMargin;
    }

    private void judgeSize(){

        if(mData.size()==0){
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(
                    new Intent(ConstantUtil.ACTION_SHOW_BTN));
        }else{
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(
                    new Intent(ConstantUtil.ACTION_HIDE_BTN));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(mActivity).inflate(R.layout.item_book_list,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(item);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myholder = (MyViewHolder)holder;
        BookDetail.ResultEntity.DataEntity data = mData.get(position);
        StringBuilder sb=new StringBuilder();
        sb.append("《");
        sb.append(data.title);
        sb.append("》");
//        myholder.tv_book_title.setText("《"+data.title+"》");
        myholder.tv_book_title.setText(sb.toString());
        sb=null;
        LogUtil.i("book",mData.get(position).title+mData.get(position).online);

        if(!TextUtils.isEmpty(data.sub2)){
            myholder.tv_book_details.setText(data.sub2);
        }else {
            myholder.tv_book_details.setText("该图书没有简述...");
        }
        myholder.tv_book_reads.setText(data.reading);
        myholder.iv_book.setImageUrl(data.img,imageLoader);
        if (DBManager.isCollect(mData.get(position).title)){
            myholder.iv_heart.setImageResource(R.drawable.munion_bottombar_fav_selected);
        }else{
            myholder.iv_heart.setImageResource(R.drawable.munion_bottombar_fav_normal);
        }
        initListener(myholder,position);
        addTextView(data.catalog,myholder.llt_book_lable);
    }

    private void initListener(final MyViewHolder myholder, final int position) {
        myholder.iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager.processBook(mData.get(position));
            }
        });
        myholder.tv_book_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookDetail(position);
            }
        });

        myholder.iv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(myholder,position);
            }
        });

        myholder.tv_book_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookStore(position);
            }
        });

    }

    private void showBookStore(final int position) {//弹出对话框让用户选择各大商城
        if(!TextUtils.isEmpty(mData.get(position).online)){//如果存在Url才显示
            String[] stores=mData.get(position).online.split(" ");
            final String[] stroesName=new String[stores.length];//存放商城名字的数组
            final String[] stroesUrl=new String[stores.length];//存放商城连接的数组
            AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
            builder.setTitle("请选择购买平台");
            LinearLayout view= (LinearLayout) View.inflate(mActivity,R.layout.dialog_book_store,null);
            CardView cardView;
            TextView textView;
            //创建布局参数
            CardView.LayoutParams layoutParams=new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.MATCH_PARENT
            );
            LinearLayout.LayoutParams cardParams=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(margins,margins,margins,margins);//设置外边据
            for (int i=0;i<stores.length;i++){
                stroesName[i]=stores[i].split(":")[0];//收集商城名字
                stroesUrl[i]=stores[i].split(":")[2];//收集商城Url
                cardView=new CardView(mActivity);//动态创建CardView
                cardView.setRadius(margins);//设置圆角
                cardView.setCardBackgroundColor(R.color.cardview_light_background);//设置背景
                cardView.setLayoutParams(cardParams);//设置布局参数
                textView=new TextView(mActivity);//动态创建TextView
                textView.setText(stroesName[i]);//设置文本信息
                textView.setGravity(Gravity.CENTER);//设置对其方式
                textView.setLayoutParams(layoutParams);//设置布局参数
                textView.setTextColor(Color.BLACK);//设置颜色
                textView.setPadding(margins,margins,margins,margins);//设置内边距
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);//设置文字大小
                textView.setBackgroundResource(R.drawable.selector_item_book_bg);//设置背景
                final int temp=i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mActivity,BookStoreActivity.class);
                        intent.putExtra("url","http:"+stroesUrl[temp]);//把连接地址存进intent
                        intent.putExtra("name",stroesName[temp]);//把商城名字存进intent
                        intent.putExtra("title",mData.get(position).title);
                        intent.putExtra("image",mData.get(position).img);
                        mActivity.startActivity(intent);
                    }
                });
                cardView.addView(textView);//添加到父容器
                view.addView(cardView);//添加到父容器
            }
            builder.setView(view);
            builder.show();
        }else{
            ToastUtil.showToast("该图书已断货!",ToastUtil.center);
        }

    }

    private void showImage(MyViewHolder myholder, int position) {//点击图片显示大图
    //  跳转到大图页面
        Intent intent=new Intent(mActivity, BookImageActivity.class);
        intent.putExtra("url",mData.get(position).img);
        if(Build.VERSION.SDK_INT>=21){
            ActivityOptionsCompat option=ActivityOptionsCompat.makeSceneTransitionAnimation(
                  mActivity,myholder.iv_book,"tran_icon");
            mActivity.startActivity(intent,option.toBundle());
        }else {
            mActivity.startActivity(intent);
        }
    }

    private void showBookDetail(int position) {//显示更多内容
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        View view=View.inflate(mActivity,R.layout.dialog_book_detail,null);
        TextView tv= (TextView) view.findViewById(R.id.tv_book_details);
        tv.setText(mData.get(position).sub2);
        builder.setView(view);
        builder.setTitle(mData.get(position).title);
        final AlertDialog alertDialog = builder.create();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.onBackPressed();
            }
        });
        alertDialog.show();

    }

    //根据标签数量动态添加TextVeiw
    private void addTextView(String catalog, FlowLayout llt_book_lable) {
        llt_book_lable.removeAllViews();//移除布局所有子View
        //分离标签
        String[] catalogs = catalog.split(" ");
        TextView tv;
        int len=catalogs.length;
        for (int i=0;i<len;i++){
            tv=new TextView(mActivity);
            tv.setText(catalogs[i]);
            tv.setPadding(rightMargin,rightMargin/2,rightMargin,rightMargin/2);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setLayoutParams(layoutParams);//设定布局参数
            tv.setBackgroundResource(R.drawable.book_lable_bg);
            llt_book_lable.addView(tv);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_book_title,tv_book_details,tv_book_reads;
        public NetworkImageView iv_book;
        public ImageView iv_heart;
        public FlowLayout llt_book_lable;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_heart= (ImageView) itemView.findViewById(R.id.iv_heart);
            tv_book_reads= (TextView) itemView.findViewById(R.id.tv_book_reads);
            tv_book_title= (TextView)itemView.findViewById(R.id.tv_book_title);
            tv_book_details= (TextView)itemView.findViewById(R.id.tv_book_details);
            iv_book= (NetworkImageView) itemView.findViewById(R.id.iv_book);
            llt_book_lable= (FlowLayout) itemView.findViewById(R.id.llt_book_lable);
            iv_book.setDefaultImageResId(R.drawable.aio_image_default_round);
            iv_book.setErrorImageResId(R.drawable.book);
        }
    }
}
