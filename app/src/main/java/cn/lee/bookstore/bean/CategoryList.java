package cn.lee.bookstore.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class CategoryList {

    /**
     * count : 5
     * list : [{"announcer":"一杯香茗","author":"打眼","commentCount":3816,"commentMean":"4.42","cover":"http://bookpic.lrts.me/e3e96105078c46fc83cfd883b57c7bd3.jpg","hot":83679296,"id":1066,"lastUpdateTime":"2012-06-05 17:48:04","name":"黄金瞳","restype":2,"sections":1314,"sort":0,"state":2,"type":0},{"announcer":"晨诵无声","author":"五志","commentCount":3678,"commentMean":"4.28","cover":"http://bookpic.lrts.me/f998631da39d460ab3cc6690e6b2de88.jpg","hot":59149208,"id":3120,"lastUpdateTime":"2013-08-07 09:55:01","name":"超级仙医","restype":2,"sections":732,"sort":0,"state":2,"type":0},{"announcer":"家芮","author":"吃草的老羊","commentCount":54123,"commentMean":"4.60","cover":"http://bookpic.lrts.me/dece7fef199745b89f427726b54b428f.jpg","hot":49597854,"id":5077,"lastUpdateTime":"2015-06-11 16:12:46","name":"重生校园之商女","restype":2,"sections":820,"sort":0,"state":2,"type":0},{"announcer":"柴少鸿","author":"曾呓","commentCount":3113,"commentMean":"4.54","cover":"http://bookpic.lrts.me/7f1dfd6c1f3d4a72a9376a010b48b550.jpg","hot":35060026,"id":5314,"lastUpdateTime":"2015-01-22 16:36:24","name":"至尊小农民","restype":2,"sections":1069,"sort":0,"state":1,"type":0},{"announcer":"柴小玖","author":"花刺1913","commentCount":7765,"commentMean":"4.55","cover":"http://bookpic.lrts.me/da57d50e183d48e0a30a9ec1f04917ba.jpg","hot":19575212,"id":26486,"lastUpdateTime":"2015-11-28 08:00:02","name":"养个女鬼当老婆","restype":2,"sections":828,"sort":0,"state":2,"type":0}]
     * status : 0
     */

    public int count;
    public int status;
    public List<ListEntity> list;
    /**
     * announcer : 一杯香茗
     * author : 打眼
     * commentCount : 3816
     * commentMean : 4.42
     * cover : http://bookpic.lrts.me/e3e96105078c46fc83cfd883b57c7bd3.jpg
     * hot : 83679296
     * id : 1066
     * lastUpdateTime : 2012-06-05 17:48:04
     * name : 黄金瞳
     * restype : 2
     * sections : 1314
     * sort : 0
     * state : 2
     * type : 0
     */

    public static class ListEntity {
        public String announcer;
        public String author;
        public int commentCount;
        public String commentMean;
        public String cover;
        public int hot;
        public int id;
        public String lastUpdateTime;
        public String name;
        public int restype;
        public int sections;
        public int sort;
        public int state;
        public int type;

    }
}
