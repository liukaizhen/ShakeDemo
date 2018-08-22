package com.erly.shakedemo;

import android.Manifest;
import android.os.Bundle;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.erly.shakedemo.base.BaseActivity;
import com.erly.shakedemo.ifc.IMainView;
import com.erly.shakedemo.presenter.MainPresenter;

public class MainActivity extends BaseActivity<IMainView,MainPresenter> implements IMainView{
    private MapView mMapView ;
    private AMap aMap;
    //定位权限组
    protected String[] locationPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null)aMap = mMapView.getMap();
        mPresenter.initMap(aMap);
        checkPermissions(0,locationPermissions);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void onPermissionsGranted(int code, String[] permissions) {
        mPresenter.startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
