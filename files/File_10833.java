package com.vondear.rxtool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.io.File;

/**
 *
 * @author vondear
 * @date 2016/1/24
 */
public class RxIntentTool {

    /**
     * 获�?�安装App(支�?7.0)的�?图
     *
     * @param context
     * @param filePath
     * @return
     */
    public static Intent getInstallAppIntent(Context context, String filePath) {
        //apk文件的本地路径
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = RxFileTool.getUriForFile(context, apkfile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获�?��?�载App的�?图
     *
     * @param packageName 包�??
     * @return �?图
     */
    public static Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获�?�打开App的�?图
     *
     * @param context     上下文
     * @param packageName 包�??
     * @return �?图
     */
    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return getIntentByPackageName(context, packageName);
    }

    /**
     * 根�?�包�??获�?��?图
     *
     * @param context     上下文
     * @param packageName 包�??
     * @return �?图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 获�?�App信�?�的�?图
     *
     * @param packageName 包�??
     * @return �?图
     */
    public static Intent getAppInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 获�?�App信�?�分享的�?图
     *
     * @param info 分享信�?�
     * @return �?图
     */
    public static Intent getShareInfoIntent(String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        return intent.putExtra(Intent.EXTRA_TEXT, info);
    }

    /**
     * 获�?�其他应用的Intent
     *
     * @param packageName 包�??
     * @param className   全类�??
     * @return �?图
     */
    public static Intent getComponentNameIntent(String packageName, String className) {
        return getComponentNameIntent(packageName, className, null);
    }

    /**
     * 获�?�其他应用的Intent
     *
     * @param packageName 包�??
     * @param className   全类�??
     * @return �?图
     */
    public static Intent getComponentNameIntent(String packageName, String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获�?�应用详情页�?�具体设置 intent
     *
     * @return
     */
    public static Intent getAppDetailsSettingsIntent(Context mContext) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        return localIntent;
    }

    /**
     * 获�?�应用详情页�?�具体设置 intent
     *
     * @param packageName 包�??
     * @return intent
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        return localIntent;
    }
}
