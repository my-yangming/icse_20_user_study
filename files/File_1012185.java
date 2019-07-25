package com.fly.tour.common.util;

/**
 * Description: <动�?获�?�资�?id><br>
 * Author: mxdl<br>
 * Date: 2018/6/19<br>
 * Version: V1.0.0<br>
 * Update: <br>
 */
import android.content.Context;

public class ResIdUtil {


    /**
     * 获�?�id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int id(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    /**
     * 获�?�anim类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int anim(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "anim", context.getPackageName());
    }

    /**
     * 获�?�layout类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int layout(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }

    /**
     * 获�?�drawable类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int drawable(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    /**
     * 获�?�string类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int string(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    /**
     * 获�?�raw类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int raw(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "raw", context.getPackageName());
    }

    /**
     * 获�?�style类型资�?id
     *
     * @param resName 资�?�??称
     * @return 资�?id
     */
    public static int style(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }
}
