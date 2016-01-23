package cn.lee.bookstore.bean;

/**
 * Created by Administrator on 2016/1/21.
 */
public class Detail {

    /**
     * announcer : 一杯香茗
     * author : 打眼
     * commentCount : 3824
     * commentMean : 4.42
     * cover : http://bookpic.lrts.me/e3e96105078c46fc83cfd883b57c7bd3.jpg
     * desc : 典当行工作的小职员庄睿，在一次意外中眼睛发生异变。 美轮美奂的陶瓷，古拙大方的青铜器，惊心动魄的赌石，惠质兰心的漂亮护士，冷若冰霜的豪门千金接踵而来，他的生活也随之产生了天翻地覆的变化。眼生双瞳，财富人生……
     * downPrice : 0
     * download : 0
     * feeType : 0
     * id : 1066
     * isLike : 0
     * length : 0
     * listenPrice : 0
     * msg :
     * name : 黄金瞳
     * play : 84182579
     * sections : 1314
     * sort : 0
     * state : 2
     * status : 0
     * type : 都市传说
     * update : 2012-06-05
     * user : {"cover":"http://785j53.com2.z0.glb.qiniucdn.com/yibeixiangzuo?imageMogr/v2/auto-orient/thumbnail/180x180&e=1453773351&token=OOHK9_MIwdSJxAHYi5os2taDVS13CVvcEa1cZDb9:X0lVuKjLqFh6tukwVdVmTetQjfc=","desc":"有声小说演播家，代表作《黄金瞳》","flag":256,"isFollow":0,"nickName":"一杯香茗","userId":79939572}
     */

    public String announcer;
    public String author;
    public int commentCount;
    public String commentMean;
    public String cover;
    public String desc;
    public int downPrice;
    public int download;
    public int feeType;
    public int id;
    public int isLike;
    public int length;
    public int listenPrice;
    public String msg;
    public String name;
    public int play;
    public int sections;
    public int sort;
    public int state;
    public int status;
    public String type;
    public String update;
    /**
     * cover : http://785j53.com2.z0.glb.qiniucdn.com/yibeixiangzuo?imageMogr/v2/auto-orient/thumbnail/180x180&e=1453773351&token=OOHK9_MIwdSJxAHYi5os2taDVS13CVvcEa1cZDb9:X0lVuKjLqFh6tukwVdVmTetQjfc=
     * desc : 有声小说演播家，代表作《黄金瞳》
     * flag : 256
     * isFollow : 0
     * nickName : 一杯香茗
     * userId : 79939572
     */

    public UserEntity user;

    public static class UserEntity {
        public String cover;
        public String desc;
        public int flag;
        public int isFollow;
        public String nickName;
        public int userId;

    }
}
