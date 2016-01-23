package cn.lee.bookstore.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class BookCatalog {
    /**
     * resultcode : 200
     * reason : success
     * result : [{"id":"242","catalog":"中国文学"}]
     */

    public String resultcode;//响应码
    public String reason;//响应状态
    public int error_code;
    /**
     * id : 242
     * catalog : 中国文学
     */

    public List<BookTitle> result;


    public static class BookTitle {
        public String id;
        public String catalog;

    }
}
