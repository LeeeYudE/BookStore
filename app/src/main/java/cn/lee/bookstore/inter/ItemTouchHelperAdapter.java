package cn.lee.bookstore.inter;

/**
 * Created by Administrator on 2016/1/19.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);


    void onItemDismiss(int position);

}
