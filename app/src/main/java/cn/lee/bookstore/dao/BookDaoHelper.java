package cn.lee.bookstore.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.lee.bookstore.utils.GlobalApplication;

/**
 * Created by Administrator on 2016/1/15.
 */
public class BookDaoHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="Book.db";
    public static final int version=1;
    public static String TABLE_NAME="t_book";
    public static String COLUMN_ID="_id";
    public static String COLUMN_TITLE="title";
    public static String COLUMN_CATALOG="catalog";
    public static String COLUMN_TAGS="tags";
    public static String COLUMN_SUB1="sub1";
    public static String COLUMN_SUB2="sub2";
    public static String COLUMN_IMG="img";
    public static String COLUMN_READING="reading";
    public static String COLUMN_ONLINE="online";
    public static  String COLUMN_BYTIME="bytime";

    public BookDaoHelper() {
        super(GlobalApplication.context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+TABLE_NAME+"("+COLUMN_ID+" integer primary key autoincrement, "+
                COLUMN_TITLE+" text unique,"+
                COLUMN_CATALOG+" text unique,"+
                COLUMN_TAGS+" text," +
                COLUMN_SUB1+" text,"+
                COLUMN_SUB2+" text,"+
                COLUMN_IMG+" text,"+
                COLUMN_READING+" text,"+
                COLUMN_ONLINE+" text, "+
                COLUMN_BYTIME+" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
