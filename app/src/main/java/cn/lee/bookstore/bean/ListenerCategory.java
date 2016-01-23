package cn.lee.bookstore.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ListenerCategory {

    /**
     * count : 10
     * list : [{"commentCount":0,"cover":"","desc":"","hot":0,"id":8,"name":"都市传说","restype":1,"sections":0,"sort":0,"state":0,"type":1},{"commentCount":0,"cover":"","desc":"","hot":0,"id":3020,"name":"青春言情","restype":1,"sections":0,"sort":0,"state":0,"type":1}]
     * status : 0
     */

    public int count;
    public int status;
    public List<CategoryEntity> list;
    /**
     * commentCount : 0
     * cover :
     * desc :
     * hot : 0
     * id : 8
     * name : 都市传说
     * restype : 1
     * sections : 0
     * sort : 0
     * state : 0
     * type : 1
     */



    public static class CategoryEntity {
        public int commentCount;
        public String cover;
        public String desc;
        public int hot;
        public int id;
        public String name;
        public int restype;
        public int sections;
        public int sort;
        public int state;
        public int type;

        @Override
        public String toString() {
            return "ListEntity{" +
                    "commentCount=" + commentCount +
                    ", cover='" + cover + '\'' +
                    ", desc='" + desc + '\'' +
                    ", hot=" + hot +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", restype=" + restype +
                    ", sections=" + sections +
                    ", sort=" + sort +
                    ", state=" + state +
                    ", type=" + type +
                    '}';
        }
    }
}
