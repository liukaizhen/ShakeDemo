package com.erly.shakedemo.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.erly.shakedemo.MainActivity;
import com.erly.shakedemo.R;
import com.erly.shakedemo.ShakeActivity;
import com.erly.shakedemo.base.BasePresenter;
import com.erly.shakedemo.ifc.IMainView;
import com.erly.shakedemo.util.Utils;

public class MainPresenter extends BasePresenter<IMainView> implements AMap.OnMapLoadedListener,
        AMap.OnCameraChangeListener,AMapLocationListener{
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    private static final String TAG = MainPresenter.class.getSimpleName();
    private LatLng mapLatLng = new LatLng(34.7522529795,113.6665302515);
    private static final int ZOOM = 15;//比例尺
    private AMapLocationClient locationClient;
    private Marker locationMarker;
    private boolean locationIng;
    private AMap aMap;
    private GeoFenceClient mGeoFenceClient;
    private Circle circle;

    /**
     * 初始化地图一些参数
     * @param aMap
     */
    public void initMap(AMap aMap){
        this.aMap = aMap;
        Utils.setMapUI(aMap);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnCameraChangeListener(this);
        locationClient = new AMapLocationClient(((MainActivity) mReference.get()));
        locationClient.setLocationOption(Utils.getLocationOption());
        locationClient.setLocationListener(this);
        //设置定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER) ;
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        //围栏设置
        mGeoFenceClient = new GeoFenceClient(((MainActivity) mReference.get()));
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN| GeoFenceClient.GEOFENCE_OUT|
                GeoFenceClient.GEOFENCE_STAYED);
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //注册广播
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        MyBroadcastReceiver mBReceiver = new MyBroadcastReceiver();
        ((MainActivity) mReference.get()).registerReceiver(mBReceiver, filter);
        //设置infoWindow
        final View infoRoot = LayoutInflater.from(((MainActivity) mReference.get()))
                .inflate(R.layout.layout_amap_info_window, null);
        infoRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShakeActivity.selfJump(((MainActivity) mReference.get()));
            }
        });
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoContents(Marker marker) { return null; }
            @Override
            public View getInfoWindow(Marker marker) {
                return infoRoot;
            }
        });
    }

    /**
     * 开始定位
     */
    public void startLocation(){
        if (!locationIng){
            locationClient.startLocation();
            locationIng = true;
        }
    }

    @Override
    protected void detachView() {
        super.detachView();
        aMap = null;
    }

    @Override
    public void onMapLoaded() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, ZOOM));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mapLatLng = cameraPosition.target;
        addLocationMarker();
        setFence();
    }

    /**
     * 设置围栏
     */
    private void setFence() {
        //移除原来的地理围栏和绘制面
        //mGeoFenceClient.removeGeoFence();
        if (circle != null && circle.isVisible()){
            circle.remove();
        }
        //添加围栏
        mGeoFenceClient.addGeoFence(
                new DPoint(mapLatLng.latitude,mapLatLng.longitude),
                300,
                "customID");
        //添加绘制面
        circle = aMap.addCircle(new CircleOptions()
                .center(mapLatLng)
                .radius(300)
                .strokeColor(Color.CYAN)
                .fillColor(Color.TRANSPARENT));

    }

    /**
     * 添加当前位置的marker
     */
    private void addLocationMarker(){
        if (locationMarker == null){
            LatLng latLng = aMap.getCameraPosition().target;
            Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
            locationMarker = aMap.addMarker(
                    new MarkerOptions()
                            .position(mapLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_position_needle)));
            //设置Marker在屏幕上,不跟随地图移动
            locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
            mapLatLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, ZOOM));
        }
        locationIng = false;
        locationClient.stopLocation();
    }

    /**
     * 自定义广播
     */
    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            //获取当前有触发的围栏对象：
            GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
            int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
            switch (status){
                case GeoFence.STATUS_LOCFAIL:
                    Log.e(TAG,"定位失败");
                    break;
                case GeoFence.STATUS_IN:
                    Log.e(TAG,"进入围栏 ");
                    locationMarker.showInfoWindow();
                    break;
                case GeoFence.STATUS_OUT:
                    Log.e(TAG,"离开围栏 ");
                    locationMarker.hideInfoWindow();
                    break;
                case GeoFence.STATUS_STAYED:
                    Log.e(TAG,"停留在围栏内 ");
                    break;
                default:
                    break;
            }
        }
    }
}
