package cn.lee.bookstore.http;

/**
 * Created by Administrator on 2016/1/7.
 */
public interface Url {
    String BOOK_KEY="a1063af5913ccdcd11e64e0a9e0b055d";
    String BOOK_CATEGORY="http://apis.juhe.cn/goodbook/catalog?key="+BOOK_KEY+"&dtype=json";
    String BOOK_CONTENT="http://apis.juhe.cn/goodbook/query?key="+BOOK_KEY+"&catalog_id=%s&rn=%d&rn=%d";

    String MACHINE_KEY="888be724d84d2dd1a845106cda5a30c4";

    //听书首页
    String Listener_host="http://117.25.143.73/yyting/bookclient/ClientTypeResource.action?" +
            "type=1&pageNum=1&pageSize=500&token=9fwghp8OTOK3Hzgu3tPkfXbpCdKo8XTn9-voBCDJj8A*&imei=MDAwMDAwMDAwMDAwMDAw";
    //类别列表
    String category_list="http://117.25.143.73/yyting/bookclient/ClientTypeResource.action?" +
            "type=%d&pageNum=%d&pageSize=30&sort=0&token=9fwghp8OTOK3Hzgu3tPkfXbpCdKo8XTn9-voBCDJj8A*&imei=MDAwMDAwMDAwMDAwMDAw";

    //具体介绍
    String Listener_introduce="http://117.25.143.73/yyting/bookclient/ClientGetBookDetail.action?id=%d&token=9fwghp8OTOK3Hzgu3tPkfXbpCdKo8XTn9-" +
            "voBCDJj8A*&imei=MDAwMDAwMDAwMDAwMDAw";

    //具体列表
    String listener_list="http://117.25.143.73/yyting/bookclient/ClientGetBookResource.action?" +
            "bookId=%d&pageNum=%d&pageSize=50&sortType=0&token=9fwghp8OTOK3Hzgu3tPkfXbpCdKo8XTn9-voBCDJj8A*&imei=MDAwMDAwMDAwMDAwMDAw";
}
