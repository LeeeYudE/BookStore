package cn.lee.bookstore.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.lee.bookstore.bean.BookDetail;
import cn.lee.bookstore.utils.ToastUtil;

/**DB管理类
 * Created by Administrator on 2016/1/15.
 */
public class DBManager {

    private static List<BookDetail.ResultEntity.DataEntity> bookDetail;
    private static List<String> bookTitles;
    private static BookDaoHelper bookDaoHelper;

    /**
     * 初始化数据
     */
    public static void initData(){

        bookDaoHelper=new BookDaoHelper();
        bookDetail=new ArrayList<>();
        bookTitles=new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                //获取所有图书书名
                SQLiteDatabase db = bookDaoHelper.getReadableDatabase();
                Cursor cs = db.query(BookDaoHelper.TABLE_NAME, new String[]{
                        BookDaoHelper.COLUMN_TITLE
                }, null, null, null, null, null);

                while (cs.moveToNext()){
                    bookTitles.add(cs.getString(0));
                }
                cs.close();

                Cursor cursor = db.query(BookDaoHelper.TABLE_NAME, new String[]{
                        BookDaoHelper.COLUMN_TITLE, BookDaoHelper.COLUMN_CATALOG,
                        BookDaoHelper.COLUMN_TAGS, BookDaoHelper.COLUMN_SUB1,
                        BookDaoHelper.COLUMN_SUB2, BookDaoHelper.COLUMN_IMG,
                        BookDaoHelper.COLUMN_READING, BookDaoHelper.COLUMN_ONLINE,
                        BookDaoHelper.COLUMN_BYTIME
                }, null, null, null, null, null);

                //获取所有图书信息
                BookDetail.ResultEntity.DataEntity  entity=null;
                while (cursor.moveToNext()){
                    entity= new BookDetail.ResultEntity.DataEntity();
                    entity.title=cursor.getString(0);
                    entity.catalog=cursor.getString(1);
                    entity.tags=cursor.getString(2);
                    entity.sub1=cursor.getString(3);
                    entity.sub2=cursor.getString(4);
                    entity.img=cursor.getString(5);
                    entity.reading=cursor.getString(6);
                    entity.online=cursor.getString(7);
                    entity.bytime=cursor.getString(8);
                    bookDetail.add(entity);
                }
                cursor.close();
                db.close();
            }
        }.start();

    };


    public static List<BookDetail.ResultEntity.DataEntity> getBookDetail(){
        return bookDetail;
    }

    public static List<String> getBookTitle(){
        return bookTitles;
    }

    public static void processBook(BookDetail.ResultEntity.DataEntity entity){
        if (bookTitles.contains(entity.title)){
                removeBook(entity);
        }else {
            addBook(entity);
        }
    }

    /*
        增加图书到数据库
     */
    public static void addBook(BookDetail.ResultEntity.DataEntity entity){
        SQLiteDatabase db = bookDaoHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(BookDaoHelper.COLUMN_TITLE,entity.title);
        values.put(BookDaoHelper.COLUMN_CATALOG,entity.catalog);
        values.put(BookDaoHelper.COLUMN_TAGS,entity.tags);
        values.put(BookDaoHelper.COLUMN_SUB1,entity.sub1);
        values.put(BookDaoHelper.COLUMN_SUB2,entity.sub2);
        values.put(BookDaoHelper.COLUMN_IMG,entity.img);
        values.put(BookDaoHelper.COLUMN_READING,entity.reading);
        values.put(BookDaoHelper.COLUMN_ONLINE,entity.online);
        values.put(BookDaoHelper.COLUMN_BYTIME,entity.bytime);
        long result=db.insert(BookDaoHelper.TABLE_NAME, null, values);
        db.close();
        if (result!=-1){
            bookDetail.add(entity);
            bookTitles.add(entity.title);
            for (OnDBChange dbChange:dbChangeList){
                dbChange.onDBChange();
            }
        }else {
            ToastUtil.showToast("收藏图书出错!");
        }

    }

    public static void removeBook(BookDetail.ResultEntity.DataEntity entity){
        SQLiteDatabase db = bookDaoHelper.getWritableDatabase();
        String whereClause=BookDaoHelper.COLUMN_TITLE+"=?";
        String[] whereArgs={entity.title};
        int result = db.delete(BookDaoHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
        if (result!=-1){
            bookDetail.remove(entity);
            bookTitles.remove(entity.title);
            for (OnDBChange dbChange:dbChangeList){
                dbChange.onDBChange();
            }
            if(onCollectChange!=null){
                onCollectChange.onCollectChange(bookDetail);
            }
        }else{
            ToastUtil.showToast("收藏图书出错!");
        }
    }


    public static void delAllBook(){
        if (bookDetail.size()>0){
            SQLiteDatabase db = bookDaoHelper.getWritableDatabase();
            int result = db.delete(BookDaoHelper.TABLE_NAME, null, null);
            if (result!=-1){
                bookDetail.clear();
                bookTitles.clear();
                for (OnDBChange dbChange:dbChangeList){
                    dbChange.onDBChange();
                }
                if(onCollectChange!=null){
                    onCollectChange.onCollectChange(bookDetail);
                }
            }

        }
    }

    private static List<OnDBChange> dbChangeList=new ArrayList<>();

    public static void addOnDBChange(OnDBChange dbChange){
        dbChangeList.add(dbChange);
    }

    public interface OnDBChange{
        void onDBChange();
    }

    private static OnCollectChange onCollectChange;

    public static void setOnCollectChange(OnCollectChange collectChange){
        onCollectChange=collectChange;
    }

    public interface OnCollectChange{
        void onCollectChange(List<BookDetail.ResultEntity.DataEntity> entityList);
    }

    public static boolean isCollect(String title){
        return bookTitles.contains(title);
    }
}
