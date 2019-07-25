package com.example.chat.manager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.chat.base.ConstantUtil;
import com.example.chat.events.LocationEvent;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * 项目�??称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     21:46
 * QQ:         1981367757
 */

public class NewLocationManager implements AMapLocationListener {
    private static NewLocationManager instance;
    private AMapLocationClient mLocationClient = null;
    private double latitude;
    private double longitude;
    private String address;

    public static NewLocationManager getInstance() {
        if (instance == null) {
            instance = new NewLocationManager();
        }
        return instance;
    }


    public void startLocation() {
        if (mLocationClient == null) {
            CommonLogger.e("这里新建定�?");
            mLocationClient = new AMapLocationClient(BaseApplication.getInstance());
            mLocationClient.setLocationOption(getDefaultOption());
            mLocationClient.setLocationListener(this);
            mLocationClient.startLocation();
        }
    }


    private NewLocationManager() {
    }

    private AMapLocationClientOption getDefaultOption() {
        CommonLogger.e("获�?�定�?选项");
        AMapLocationClientOption option = new AMapLocationClientOption();
//          定�?模�?：1 高精度�?2仅设备�?3仅网络
// 设置高精度模�?
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                设置是�?�优先使用GPS
        option.setGpsFirst(false);
//                连接超时3秒
        option.setHttpTimeOut(3000);
//                设置定�?间隔60秒
        option.setInterval(60000);
//                设置是�?�返回地�?�，默认返回
        option.setNeedAddress(true);
//                设置是�?��?�次定�?
        option.setOnceLocation(false);
        //�?�选，设置是�?�等待wifi刷新，默认为false.如果设置为true,会自动�?�为�?�次定�?，�?续定�?时�?�?使用
        option.setOnceLocationLatest(false);
//                设置网络请求�??议
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
//                设置是�?�使用传感器,�?使用
        option.setSensorEnable(false);
        return option;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                CommonLogger.e("1获�?�到�?置信�?�拉");
                //                获�?�纬度
                if (latitude != aMapLocation.getLatitude() || longitude != aMapLocation.getLongitude()) {
                    latitude = aMapLocation.getLatitude();
                    //                获�?��?度
                    longitude = aMapLocation.getLongitude();
                    address = aMapLocation.getAddress();
                    CommonLogger.e(aMapLocation.toString());
//                                        aMapLocation.getLocationType();//获�?�当�?定�?结果�?��?
//                                        aMapLocation.getLatitude();//获�?�纬度
//                                        aMapLocation.getLongitude();//获�?��?度
//                                        aMapLocation.getAccuracy();//获�?�精度信�?�
//                                        aMapLocation.getAddress();//地�?�，如果option中设置isNeedAddress为false，则没有此结果，网络定�?结果中会有地�?�信�?�，GPS定�?�?返回地�?�信�?�。
//                                        aMapLocation.getCountry();//国家信�?�
//                                        aMapLocation.getProvince();//�?信�?�
//                                        aMapLocation.getCity();//城市信�?�
//                                        aMapLocation.getDistrict();//城区信�?�
//                                        aMapLocation.getStreet();//街�?�信�?�
//                                        aMapLocation.getStreetNum();//街�?�门牌�?�信�?�
//                                        aMapLocation.getCityCode();//城市编�?
//                                        aMapLocation.getAdCode();//地区编�?
//                                        aMapLocation.getAoiName();//获�?�当�?定�?点的AOI信�?�
                    LocationEvent locationEvent = new LocationEvent();
                    locationEvent.setLongitude(longitude);
                    locationEvent.setLatitude(latitude);
                    locationEvent.setLocation(address);
                    locationEvent.setCountry(aMapLocation.getCountry());
                    locationEvent.setProvince(aMapLocation.getProvince());
                    locationEvent.setCity(aMapLocation.getCity());
                    locationEvent.setTitle(aMapLocation.getPoiName());
                    RxBusManager.getInstance().post(locationEvent);
                    BaseApplication.getAppComponent().getSharedPreferences().edit().putString(ConstantUtil.LATITUDE, latitude+"")
                            .putString(ConstantUtil.LONGITUDE,longitude+"")
                            .putString(ConstantUtil.ADDRESS, address)
                    .putString(ConstantUtil.CITY,aMapLocation.getCity()).apply();
                    if (UserManager.getInstance().getCurrentUser() != null) {
                        UserManager.getInstance().updateUserInfo(ConstantUtil.LOCATION,longitude+"&"+latitude,null);
                    }
                } else {
                    CommonLogger.e("定�?相�?�,�?定�?");
                }
            } else {
                CommonLogger.e("出错消�?�：" + aMapLocation.getErrorInfo() + "\n" + "错误�?:" + aMapLocation.getErrorCode() + "\n" + "错误的细节" +
                        aMapLocation.getLocationDetail());
            }
        }
    }


    public void clear() {
        CommonLogger.e("这里清除定�?");
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        longitude = 0;
        latitude = 0;
    }

}
