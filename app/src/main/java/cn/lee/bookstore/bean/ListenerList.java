package cn.lee.bookstore.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ListenerList {

    /**
     * bookId : 5077
     * list : [{"downPrice":0,"feeType":0,"hasLyric":0,"id":601652,"length":1130,"listenPrice":0,"name":"第001集_重生校园之商女","path":"http://wting.info:81/asdb/fiction/dushi/csxyzsn/ctuyx90p.mp3","section":1,"size":4512964},{"downPrice":0,"feeType":0,"hasLyric":0,"id":601653,"length":972,"listenPrice":0,"name":"第002集_重生校园之商女","path":"http://wting.info:81/asdb/fiction/dushi/csxyzsn/8sjmwrmv.mp3","section":2,"size":3880174},{"downPrice":0,"feeType":0,"hasLyric":0,"id":601654,"length":999,"listenPrice":0,"name":"第003集_重生校园之商女","path":"http://wting.info:81/asdb/fiction/dushi/csxyzsn/5e3rv7vg.mp3","section":3,"size":3987844},{"downPrice":0,"feeType":0,"hasLyric":0,"id":601655,"length":981,"listenPrice":0,"name":"第004集_重生校园之商女","path":"http://wting.info:81/asdb/fiction/dushi/csxyzsn/yv2off22.mp3","section":4,"size":3917000},{"downPrice":0,"feeType":0,"hasLyric":0,"id":601656,"length":1022,"listenPrice":0,"name":"第005集_重生校园之商女","path":"http://wting.info:81/asdb/fiction/dushi/csxyzsn/quxe5wor.mp3","section":5,"size":4081839}]
     * msg : null
     * sections : 820
     * status : 0
     * userType : 0
     */

    public int bookId;
    public String msg;
    public int sections;
    public int status;
    public int userType;
    /**
     * downPrice : 0.0
     * feeType : 0
     * hasLyric : 0
     * id : 601652
     * length : 1130
     * listenPrice : 0.0
     * name : 第001集_重生校园之商女
     * path : http://wting.info:81/asdb/fiction/dushi/csxyzsn/ctuyx90p.mp3
     * section : 1
     * size : 4512964
     */

    public List<ListEntity> list;


    public static class ListEntity {
        public double downPrice;
        public int feeType;
        public int hasLyric;
        public int id;
        public int length;
        public double listenPrice;
        public String name;
        public String path;
        public int section;
        public int size;

    }
}
