package com.example.jingbin.cloudreader.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * @author jingbin
 * @data 2018/7/18
 * @Description �?��?处�?�
 * // 存储�?��?
 * Manifest.permission.WRITE_EXTERNAL_STORAGE
 * // 相机
 * Manifest.permission.CAMERA
 * // 获得�?纬度1
 * Manifest.permission.ACCESS_FINE_LOCATION
 * // 获得�?纬度2
 * Manifest.permission.ACCESS_COARSE_LOCATION
 * // 获�?�手机�?��? 为了获得uuid：
 * Manifest.permission.READ_PHONE_STATE
 * // 应用列表
 * Manifest.permission.GET_PACKAGE_SIZE
 */

public class PermissionHandler {

    private static final int PERMISSION_CODE = 10010;
    private static final int PERMISSION_CODE_ONE = 10011;

    /**
     * 检查是�?�有�?��?，没有请求�?��?
     */
    public static boolean isHandlePermission(Activity activity, String permission, String permission2) {
        if (isPermission(activity, permission, permission2)) {
            // 有�?��?
            return true;
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
            int permissionCheck2 = ContextCompat.checkSelfPermission(activity, permission2);
            // 没有�?��?
            ArrayList<String> list = new ArrayList<>();
            // 没有授�?�
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
            if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                list.add(permission2);
            }
            if (list.size() > 0) {
                String[] strings = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    strings[i] = list.get(i);
                }
                ActivityCompat.requestPermissions(activity, strings, PERMISSION_CODE);
            }
            return false;
        }
    }

    /**
     * 检查�?�有一个�?��?，现�?�用于相册�?��?
     */
    public static boolean isHandlePermission(Activity activity, String permission) {
        if (isPermission(activity, permission)) {
            // 有�?��?
            return true;
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
            // 没有授�?�
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_CODE_ONE);
            }
            return false;
        }
    }

    /**
     * 检测是�?�有�?�个�?��?
     */
    private static boolean isPermission(Activity activity, String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        // 有一个没有授�?�就作为 没有�?��?处�?�
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检测是�?�全部有�?��?
     */
    private static boolean isPermission(Activity activity, String permission, String permission2) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        int permissionCheck2 = ContextCompat.checkSelfPermission(activity, permission2);
        // 有一个没有授�?�就作为 没有�?��?处�?�
        return permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * vivo�?走回调
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, OnHandlePermissionListener listener) {
        onRequestPermissionsResult(null, null, requestCode, permissions, grantResults, listener);
    }

    /**
     * 两个�?��?时的�??示
     */
    public static void onRequestPermissionsResult(String toastTextTwo, int requestCode, String[] permissions, int[] grantResults, OnHandlePermissionListener listener) {
        onRequestPermissionsResult(toastTextTwo, null, requestCode, permissions, grantResults, listener);
    }

    /**
     * @param toastTextTwo 两个�?��?时的�??示
     * @param toastTextOne 一个�?��?时的�??示
     */
    public static void onRequestPermissionsResult(String toastTextTwo, String toastTextOne, int requestCode, String[] permissions, int[] grantResults, OnHandlePermissionListener listener) {
        if (grantResults != null) {
            DebugUtil.error("-----requestCode:" + requestCode);
            for (int i : grantResults) {
                DebugUtil.error("-----grantResults:" + i);
            }
            for (String p : permissions) {
                DebugUtil.error("-----permissions:" + p);
            }

            switch (requestCode) {
                case PERMISSION_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (listener != null) {
                            listener.granted();
                        }
                    } else {
                        if (TextUtils.isEmpty(toastTextTwo)) {
                            ToastUtil.showToastLong("�?��?被拒�?");
                        } else {
                            ToastUtil.showToastLong(toastTextTwo);
                        }
                    }
                    break;
                case PERMISSION_CODE_ONE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (listener != null) {
                            listener.granted();
                        }
                    } else {
                        if (TextUtils.isEmpty(toastTextOne)) {
                            ToastUtil.showToastLong("�?��?被拒�?");
                        } else {
                            ToastUtil.showToastLong(toastTextOne);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    interface OnHandlePermissionListener {
        /**
         * �?��?
         */
        void granted();
    }
}
