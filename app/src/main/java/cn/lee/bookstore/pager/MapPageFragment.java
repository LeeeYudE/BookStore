package cn.lee.bookstore.pager;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import cn.lee.bookstore.R;
import cn.lee.bookstore.utils.CommonUtil;
import cn.lee.bookstore.utils.ConstantUtil;
import cn.lee.bookstore.utils.ToastUtil;
import cn.lee.bookstore.utils.overlayutil.PoiOverlay;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MapPageFragment extends BasePagerFragment {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient locationClient;
    private PoiSearch mPoiSearch;
    private TextView tv_address;
    private int mTotalPage;//书店总数量
    private LatLng latLng;//中心位置
    private int mPageNo=0;//搜索第几页数据，第一页为0
    private int mPageSize = 10;//每页显示多少条
    private ImageButton ib_more,ib_get_back;
    private boolean isFirshShow=true,isSearch=false,isInitMap=false;
    private double lat,lon;
    private PopupWindow popupWindow;
    private FrameLayout flt_loading,flt_location;
    private ImageView iv_menu;
    private Button btn_location;

    @Override
    protected int getLayoutResID() {
        return R.layout.page_map;
    }

    @Override
    protected void initView() {
        tv_address = (TextView) mRootView.findViewById(R.id.tv_address);
        ib_more = (ImageButton) mRootView.findViewById(R.id.ib_more);
        ib_get_back= (ImageButton) mRootView.findViewById(R.id.ib_get_back);
        flt_loading = (FrameLayout) mRootView.findViewById(R.id.flt_map_loading);
        flt_location = (FrameLayout) mRootView.findViewById(R.id.flt_loaction);
        iv_menu = (ImageView) mRootView.findViewById(R.id.iv_menu);
        btn_location= (Button) mRootView.findViewById(R.id.btn_location);
        mMapView = (MapView) mRootView.findViewById(R.id.mapView);
        initPopupWindow();//初始化对话框
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        // 设置地图的缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15f));
        initLocation();
        beginLocation();

    }

    private void initLocation() {
        mBaiduMap.setMyLocationEnabled(true);//打开定位图层
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,//普通模式
                false//不显示方向
                ,null//默认图标
        ));
        //定位对象
        locationClient = new LocationClient(mActivity);//注册监听
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if(bdLocation==null){
                    return;
                }
                lat = bdLocation.getLatitude();
                lon = bdLocation.getLongitude();
                MyLocationData data=new MyLocationData.Builder()
                        .latitude(lat)
                        .longitude(lon)
                        .accuracy(bdLocation.getRadius())
                        .build();
                // 设置地图显示的中心位置
                if (isFirshShow){
                    initSearch();
                    getBack();
                    beginSearch();
                    isFirshShow=false;
                }
                mBaiduMap.setMyLocationData(data);
            }
        });

    }

    private void beginLocation() {
        // 设置定位的参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");	// 设置坐标系
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);	// 定位模式，精度最高
        option.setScanSpan(60*1000); 	// 设置定位刷新频率：60s刷新一次位置
        option.setIsNeedAddress(true); // 是否返回详情的地址信息
        locationClient.setLocOption(option);
        locationClient.start(); // 开始定位
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        //初始化搜索对象
        mPoiSearch = PoiSearch.newInstance();
        //注册监听器,接收搜索结果
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override // 获取POI列表数据
            public void onGetPoiResult(PoiResult poiResult) {
                if(poiResult==null||poiResult.error== SearchResult.ERRORNO.RESULT_NOT_FOUND){
                    ToastUtil.showToast("没有搜索到任何书店");
                    tv_address.setText("附近没有任何书店");
                    return;
                }
                // 总条数
                int totalPoiNum = poiResult.getTotalPoiNum();
                // 总页数
                mTotalPage = poiResult.getTotalPageNum();
                // 当前第几页
                mPageNo= poiResult.getCurrentPageNum();
                // 当前页有多少条
//                int currentPageCapacity = poiResult.getCurrentPageCapacity();
                int num=mPageNo+ 1;
                String info = "附近书店：" + totalPoiNum
                        + "间	\n当前第" + num
                        + "页  总页数" + mTotalPage
                        + "页";

                // showToast(info);
                tv_address.setText(info);

                // 在地图上显示搜索结果数据
                showDatasInMap(poiResult);
            }

            @Override // 获取POI详情数据
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
               /* if (poiDetailResult == null || poiDetailResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    ToastUtil.showToast("没有搜索到结果");
                    return;
                }*/

                String name = poiDetailResult.getName();// 名称
                String address = poiDetailResult.getAddress();// 地址
                LatLng location = poiDetailResult.getLocation();// 经纬度
                double serviceRating = poiDetailResult.getServiceRating();// 服务星级
                double environmentRating = poiDetailResult.getEnvironmentRating(); // 环境星级

                String info = "name: " + name
                        + "  \n uid: " + poiDetailResult.getUid()
                        + "  \n address: " + address
                        + "  \n location: " + location
                        + "  \n environmentRating: " + environmentRating
                        + "  \n serviceRating: " + serviceRating;
                ToastUtil.showToast(info);
            }
        });
    }

    private void showDatasInMap(final PoiResult poiResult) {
        flt_loading.setVisibility(View.GONE);
        isSearch=false;
        PoiOverlay overlay = new PoiOverlay(mBaiduMap) {
            @Override
            public boolean onPoiClick(int position) {
                // 点击的实体对象
                PoiInfo poiInfo = poiResult.getAllPoi().get(position);
                // 根据uid根据详情信息
                searchPoiDetail(poiInfo.uid);
                return true;
            }
        };

        // 清除上一页数据的覆盖物
        mBaiduMap.clear();

        // 设置搜索结果覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(overlay);

        overlay.setData(poiResult);		// 设置显示的搜索结果
        overlay.addToMap();				// 添加到地图并显示
        overlay.zoomToSpan(); // 缩放地图，使所有的结果都显示在合适的视野范围内
    }

    /**
     * 根据uid显示详情信息
     * @param uid
     */
    private void searchPoiDetail(String uid) {
        PoiDetailSearchOption option = new PoiDetailSearchOption();
        option.poiUid(uid);
        mPoiSearch.searchPoiDetail(option);
    }

    private void beginSearch() {
        if(isSearch){
            return;
        }
        isSearch=true;
        flt_loading.setVisibility(View.VISIBLE);
        // 设置搜索参数
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword("书店"); 			// 搜索关键字
        option.location(latLng); 	// 搜索的中心位置
        option.radius(3000); 			// 	搜索的范围3000米以内
        option.pageNum(mPageNo);		// 获取第几页数据
        option.pageCapacity(mPageSize); 	// 每页显示多少条
        mPoiSearch.searchNearby(option);	// 发起异步搜索
    }

    private void getBack() {
        // 设置地图显示的中心位置
        latLng =   new LatLng(lat,lon);
        MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    @Override
    public void initListener() {
        btn_location.setOnClickListener(this);
        ib_more.setOnClickListener(this);
        ib_get_back.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_location:
                isInitMap=true;
                flt_location.setVisibility(View.GONE);
                initMap();
                break;
            case R.id.ib_more:
                showPopupWindow();
                break;
            case R.id.ib_get_back:
                getBack();
               break;
            case R.id.iv_menu:
                showMenu();
                break;
            case R.id.tv_pro:
                searchPro();
                break;
            case R.id.tv_next:
                searchNext();
                break;
            case R.id.tv_reset:
                resetSearch();
                break;
        }
    }

    private void resetSearch(){
        popupWindow.dismiss();
        isSearch=false;
        mPageNo=0;
        beginSearch();
    }

    private void showMenu() {
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(
           new Intent(ConstantUtil.ACTION_OPEN_MENU)
        );
    }

    private void searchPro(){
        if(mPageNo==0){
            mPageNo=mTotalPage;
        }else {
            mPageNo--;
        }
        beginSearch();
        popupWindow.dismiss();
    }

    private void searchNext(){
        if(mPageNo==mTotalPage-1){
            mPageNo=0;
        }else {
            mPageNo++;
        }
        beginSearch();
        popupWindow.dismiss();
    }

    private void initPopupWindow(){
        View popupView=View.inflate(mActivity,R.layout.popup_map,null);
        TextView tv_pro= (TextView) popupView.findViewById(R.id.tv_pro);
        TextView tv_next= (TextView) popupView.findViewById(R.id.tv_next);
        TextView tv_reset= (TextView) popupView.findViewById(R.id.tv_reset);
        tv_pro.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        popupWindow = new PopupWindow(popupView,
                CommonUtil.mScreenWidth/2,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.conversation_options_bottom_pressed));
    }

    private void showPopupWindow() {
        if (isInitMap){
            popupWindow.showAsDropDown(ib_more);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient!=null){
            locationClient.stop();
        }
        if (mPoiSearch!=null){
            mPoiSearch.destroy();
        }

        mMapView.onDestroy();
    }

}
