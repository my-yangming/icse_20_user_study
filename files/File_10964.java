package com.vondear.rxui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.vondear.rxtool.RxLocationTool;
import com.vondear.rxtool.RxVibrateTool;
import com.vondear.rxtool.model.Gps;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialogGPSCheck;
import com.vondear.rxui.view.dialog.RxDialogTool;


/**
 * @author vondear
 */
public abstract class ActivityBaseLocation extends ActivityBase {

    //�?度
    public double mLongitude = 0;
    //纬度
    public double mLatitude = 0;

    //GPS信�?�
    public Gps mGps;

    public LocationManager mLocationManager;
    private LocationListener mLocationListener;

    public abstract void setGpsInfo(Location location);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //�?始化GPS
        initGPS();
        //GPS开�?�状�?检测
        gpsCheck();
    }

    private void initGPS() {
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    //----------------------------------------------------------------------------------------------检测GPS是�?�已打开 start
    protected void gpsCheck() {
        if (!RxLocationTool.isGpsEnabled(this)) {
            RxDialogGPSCheck rxDialogGPSCheck = new RxDialogGPSCheck(mContext);
            rxDialogGPSCheck.show();
        } else {
            getLocation();
        }
    }
    //==============================================================================================检测GPS是�?�已打开 end

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            RxDialogTool.initDialogSurePermission(mContext, "请先打开GPS定�?�?��?");
            return;
        }
        mLocationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
                mGps = new Gps(mLongitude, mLatitude);
                setGpsInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    //GPS状�?为�?��?时
                    case LocationProvider.AVAILABLE:
//                        RxToast.normal("当�?GPS�?务已�?��?");
                        break;
                    //GPS状�?为�?务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        RxToast.normal("当�?GPS信�?�弱");
                        RxVibrateTool.vibrateOnce(mContext, 3000);
                        break;
                    //GPS状�?为暂�?��?务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        RxToast.normal("当�?GPS已暂�?��?务");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                RxToast.normal("当�?GPS设备已打开");
                RxVibrateTool.vibrateOnce(mContext, 800);
            }

            @Override
            public void onProviderDisabled(String provider) {
                RxToast.normal("当�?GPS设备已关闭");
                RxVibrateTool.vibrateOnce(mContext, 800);
                gpsCheck();
            }
        };

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
