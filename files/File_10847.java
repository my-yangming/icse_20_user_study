package com.vondear.rxtool;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.vondear.rxtool.model.Gps;
import com.vondear.rxtool.view.RxToast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author vondear
 * @date 2016/11/13
 * @desc 定�?相关工具类
 */
public class RxLocationTool {

    //圆周率
    public static double pi = 3.1415926535897932384626;
    //Krasovsky 1940 (北京54)椭�?�长�?�轴
    public static double a = 6378245.0;
    //椭�?�的�??心率
    public static double ee = 0.00669342162296594323;
    private static OnLocationChangeListener mListener;
    private static MyLocationListener myLocationListener;
    private static LocationManager mLocationManager;

    /**
     * 判断Gps是�?��?�用
     *
     * @return {@code true}: 是<br>{@code false}: �?�
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定�?是�?��?�用
     *
     * @return {@code true}: 是<br>{@code false}: �?�
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界�?�
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unRegisterLocation()}</p>
     * <p>需添加�?��? {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加�?��? {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加�?��? {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}�?�定时更新；</p>
     * <p>{@code minDistance}�?为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则�?时刷新。</p>
     *
     * @param minTime     �?置信�?�更新周期（�?��?：毫秒）
     * @param minDistance �?置�?�化最�?�?离：当�?置�?离�?�化超过此值时，将更新�?置信�?�（�?��?：米）
     * @param listener    �?置刷新的回调接�?�
     * @return {@code true}: �?始化�?功<br>{@code false}: �?始化失败
     */
    public static boolean registerLocation(Context context, long minTime, long minDistance, OnLocationChangeListener listener) {
        if (listener == null) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mListener = listener;
        if (!isLocationEnabled(context)) {
            RxToast.showToast(context, "无法定�?，请打开定�?�?务", 500);
            return false;
        }
        String provider = mLocationManager.getBestProvider(getCriteria(), true);

        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            listener.getLastKnownLocation(location);
        }
        if (myLocationListener == null) {
            myLocationListener = new MyLocationListener();
        }
        mLocationManager.requestLocationUpdates(provider, minTime, minDistance, myLocationListener);
        return true;
    }

    /**
     * 注销
     */
    public static void unRegisterLocation() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager.removeUpdates(myLocationListener);
                myLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    /**
     * 设置定�?�?�数
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定�?精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是�?��?求速度
        criteria.setSpeedRequired(true);
        // 设置是�?��?许�?�?�商收费
        criteria.setCostAllowed(true);
        //设置是�?�需�?方�?信�?�
        criteria.setBearingRequired(true);
        //设置是�?�需�?海拔信�?�
        criteria.setAltitudeRequired(true);
        // 设置对电�?的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 根�?��?纬度获�?�地�?��?置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude �?度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根�?��?纬度获�?�所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude �?度
     * @return 所在国家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根�?��?纬度获�?�所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude �?度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根�?��?纬度获�?�所在街�?�
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude �?度
     * @return 所在街�?�
     */
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    //------------------------------------------�??标转�?�工具start--------------------------------------

    /**
     * GPS�??标 转�?��? 角度
     * 例如 113.202222 转�?��? 113°12′8″
     *
     * @param location
     * @return
     */
    public static String gpsToDegree(double location) {
        double degree = Math.floor(location);
        double minute_temp = (location - degree) * 60;
        double minute = Math.floor(minute_temp);
//        double second = Math.floor((minute_temp - minute)*60);
        String second = new DecimalFormat("#.##").format((minute_temp - minute) * 60);
        return (int) degree + "°" + (int) minute + "′" + second + "″";
    }

    /**
     * 国际 GPS84 �??标系
     * 转�?��?
     * [国测局�??标系] �?�星�??标系 (GCJ-02)
     * <p>
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param lon �?度
     * @param lat 纬度
     * @return GPS实体类
     */
    public static Gps GPS84ToGCJ02(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLon, mgLat);
    }

    /**
     * [国测局�??标系] �?�星�??标系 (GCJ-02)
     * 转�?��?
     * 国际 GPS84 �??标系
     *
     * @param lon �?�星�?度
     * @param lat �?�星纬度
     */
    public static Gps GCJ02ToGPS84(double lon, double lat) {
        Gps gps = transform(lon, lat);
        double lontitude = lon * 2 - gps.getLongitude();
        double latitude = lat * 2 - gps.getLatitude();
        return new Gps(lontitude, latitude);
    }

    /**
     * �?�星�??标系 (GCJ-02)
     * 转�?��?
     * 百度�??标系 (BD-09)
     *
     * @param ggLon �?度
     * @param ggLat 纬度
     */
    public static Gps GCJ02ToBD09(double ggLon, double ggLat) {
        double x = ggLon, y = ggLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new Gps(bdLon, bdLat);
    }

    /**
     * 百度�??标系 (BD-09)
     * 转�?��?
     * �?�星�??标系 (GCJ-02)
     *
     * @param bdLon 百度*�?度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    public static Gps BD09ToGCJ02(double bdLon, double bdLat) {
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double ggLon = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta);
        return new Gps(ggLon, ggLat);
    }

    /**
     * 百度�??标系 (BD-09)
     * 转�?��?
     * 国际 GPS84 �??标系
     *
     * @param bdLon 百度*�?度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    public static Gps BD09ToGPS84(double bdLon, double bdLat) {
        Gps gcj02 = BD09ToGCJ02(bdLon, bdLat);
        Gps map84 = GCJ02ToGPS84(gcj02.getLongitude(), gcj02.getLatitude());
        return map84;

    }

    /**
     * 国际 GPS84 �??标系
     * 转�?��?
     * 百度�??标系 (BD-09)
     *
     * @param gpsLon  国际 GPS84 �??标系下 的�?度
     * @param gpsLat  国际 GPS84 �??标系下 的纬度
     * @return 百度GPS�??标
     */
    public static Gps GPS84ToBD09(double gpsLon, double gpsLat) {
        Gps gcj02 = GPS84ToGCJ02(gpsLon, gpsLat);
        Gps bd09 = GCJ02ToBD09(gcj02.getLongitude(), gcj02.getLatitude());
        return bd09;
    }

    /**
     * �?在中国范围内
     *
     * @param lon �?度
     * @param lat 纬度
     * @return boolean值
     */
    public static boolean outOfChina(double lon, double lat) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    /**
     * 转化算法
     *
     * @param lon �?度
     * @param lat 纬度
     * @return  GPS信�?�
     */
    private static Gps transform(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return new Gps(lon, lat);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLon, mgLat);
    }

    /**
     * 纬度转化算法
     *
     * @param x x�??标
     * @param y y�??标
     * @return  纬度
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * �?度转化算法
     *
     * @param x  x�??标
     * @param y  y�??标
     * @return �?度
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    public interface OnLocationChangeListener {

        /**
         * 获�?�最�?�一次�?留的�??标
         *
         * @param location �??标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当�??标改�?�时触�?�此函数，如果Provider传进相�?�的�??标，它就�?会被触�?�
         *
         * @param location �??标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在�?�用�?暂时�?�?�用和无�?务三个状�?直接切�?�时触�?�此函数
         *
         * @param provider �??供者
         * @param status   状�?
         * @param extras   provider�?�选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//�?置状�?�?�生改�?�
    }

    private static class MyLocationListener implements LocationListener {
        /**
         * 当�??标改�?�时触�?�此函数，如果Provider传进相�?�的�??标，它就�?会被触�?�
         *
         * @param location �??标
         */
        @Override
        public void onLocationChanged(Location location) {
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }

        /**
         * provider的在�?�用�?暂时�?�?�用和无�?务三个状�?直接切�?�时触�?�此函数
         *
         * @param provider �??供者
         * @param status   状�?
         * @param extras   provider�?�选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListener != null) {
                mListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("onStatusChanged", "当�?GPS状�?为�?��?状�?");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("onStatusChanged", "当�?GPS状�?为�?务区外状�?");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("onStatusChanged", "当�?GPS状�?为暂�?��?务状�?");
                    break;
                default:
                    break;
            }
        }

        /**
         * provider被enable时触�?�此函数，比如GPS被打开
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider被disable时触�?�此函数，比如GPS被关闭
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    }
    //===========================================�??标转�?�工具end====================================

    public static boolean isMove(Location location, Location preLocation) {
        boolean isMove;
        if (preLocation != null) {
            double speed = location.getSpeed() * 3.6;
            double distance = location.distanceTo(preLocation);
            double compass = Math.abs(preLocation.getBearing() - location.getBearing());
            double angle;
            if (compass > 180) {
                angle = 360 - compass;
            } else {
                angle = compass;
            }
            if (speed != 0) {
                if (speed < 35 && (distance > 3 && distance < 10)) {
                    isMove = angle > 10;
                } else {
                    isMove = (speed < 40 && distance > 10 && distance < 100) ||
                            (speed < 50 && distance > 10 && distance < 100) ||
                            (speed < 60 && distance > 10 && distance < 100) ||
                            (speed < 9999 && distance > 100);
                }
            } else {
                isMove = false;
            }
        } else {
            isMove = true;
        }
        return isMove;
    }
}
