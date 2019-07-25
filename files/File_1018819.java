/*
 *
 * Copyright 2018 iQIYI.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.qiyi.pluginlibrary;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;

import org.qiyi.pluginlibrary.component.wraper.NeptuneInstrument;
import org.qiyi.pluginlibrary.component.wraper.PluginInstrument;
import org.qiyi.pluginlibrary.install.IInstallCallBack;
import org.qiyi.pluginlibrary.install.IUninstallCallBack;
import org.qiyi.pluginlibrary.pm.PluginLiteInfo;
import org.qiyi.pluginlibrary.pm.PluginPackageManagerNative;
import org.qiyi.pluginlibrary.runtime.PluginManager;
import org.qiyi.pluginlibrary.utils.PluginDebugLog;
import org.qiyi.pluginlibrary.utils.ReflectionUtils;
import org.qiyi.pluginlibrary.utils.VersionUtils;

import java.io.File;

/**
 * Neptune对外暴露的统一调用类
 */
public class Neptune {
    private static final String TAG = "Neptune";

    public static final boolean SEPARATED_CLASSLOADER = true;
    public static final boolean NEW_COMPONENT_PARSER = true;

    @SuppressLint("StaticFieldLeak")
    private static Context sHostContext;

    private static NeptuneConfig sGlobalConfig;

    private static Instrumentation mHostInstr;

    private Neptune() {
    }

    /**
     * �?始化Neptune�?�件环境
     *
     * @param application  宿主的Appliction
     * @param config  �?置信�?�
     */
    public static void init(Application application, NeptuneConfig config) {

        sHostContext = application;
        sGlobalConfig = config != null ? config
                : new NeptuneConfig.NeptuneConfigBuilder().build();

        PluginDebugLog.setIsDebug(sGlobalConfig.isDebug());

        boolean hookInstr = VersionUtils.hasPie() || sGlobalConfig.getSdkMode() != NeptuneConfig.LEGACY_MODE;
        if (hookInstr) {
            hookInstrumentation();
        }

        // 调用getInstance()方法会�?始化bindService
        PluginPackageManagerNative.getInstance(sHostContext).setPackageInfoManager(sGlobalConfig.getPluginInfoProvider());
        // 注册�?�载监�?�广播
        //PluginManager.registerUninstallReceiver(sHostContext);
    }

    public static Context getHostContext() {
        return sHostContext;
    }

    public static NeptuneConfig getConfig() {
        if (sGlobalConfig == null) {
            sGlobalConfig = new NeptuneConfig.NeptuneConfigBuilder().build();
        }
        return sGlobalConfig;
    }


    /**
     * �??射替�?�ActivityThread的mInstrumentation
     */
    private static void hookInstrumentation() {

        PluginDebugLog.runtimeLog(TAG, "need to hook Instrumentation for plugin framework");
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        Instrumentation hostInstr = getHostInstrumentation();

        if (hostInstr != null) {
            String hostInstrName = hostInstr.getClass().getName();
            PluginDebugLog.runtimeLog(TAG, "host Instrument name: " + hostInstrName);

            if (hostInstrName.startsWith("com.chaozhuo.superme")
                    || hostInstrName.startsWith("com.lody.virtual")) {
                // warning: 特殊case，VirtualApp环境，暂�?Hook
                PluginDebugLog.runtimeLog(TAG, "reject hook instrument, run in VirtualApp Environment");
            } else if (hostInstr instanceof NeptuneInstrument) {
                // already hooked
                PluginDebugLog.runtimeLog(TAG, "ActivityThread Instrumentation already hooked");
            } else {
                PluginInstrument pluginInstrument = new NeptuneInstrument(hostInstr);
                ReflectionUtils.on(activityThread).set("mInstrumentation", pluginInstrument);
                PluginDebugLog.runtimeLog(TAG, "init hook ActivityThread Instrumentation success");
            }
        } else {
            PluginDebugLog.runtimeLog(TAG, "init hook ActivityThread Instrumentation failed, hostInstr==null");
        }
    }

    /**
     * 获�?�ActivityThread的Instrumentation对象
     */
    public static Instrumentation getHostInstrumentation() {

        if (mHostInstr == null) {
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Instrumentation hostInstr = activityThread.getInstrumentation();
            mHostInstr = PluginInstrument.unwrap(hostInstr);
        }

        return mHostInstr;
    }

    /**
     * 安装sd�?�上的�?�件
     *
     * @param context 宿主的Context
     * @param apkPath �?�件apk路径
     */
    public static void install(Context context, String apkPath) {
        install(context, apkPath, null);
    }

    /**
     * 安装sd上的�?�件
     *
     * @param context  宿主的Context
     * @param apkPath  �?�件apk路径
     * @param callBack 安装回调
     */
    public static void install(Context context, String apkPath, IInstallCallBack callBack) {

        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            return;
        }

        PluginLiteInfo liteInfo = new PluginLiteInfo();
        Context mContext = ensureContext(context);
        PackageInfo packageInfo = mContext.getPackageManager()
                .getPackageArchiveInfo(apkPath, 0);
        if (packageInfo != null) {
            liteInfo.mPath = apkPath;
            liteInfo.packageName = packageInfo.packageName;
            liteInfo.pluginVersion = packageInfo.versionName;
            install(mContext, liteInfo, callBack);
        }
    }

    /**
     * 安装一个�?�件
     *
     * @param context  宿主的Context
     * @param info     �?�件的信�?�，包括包�??，路径等
     * @param callBack 安装回调
     */
    public static void install(Context context, PluginLiteInfo info, IInstallCallBack callBack) {
        // install
        Context mContext = ensureContext(context);
        PluginPackageManagerNative.getInstance(mContext).install(info, callBack);
    }


    /**
     * 根�?�包�??删除�?�件apk，so，dex等数�?�
     *
     * @param context  宿主的Context
     * @param pkgName  待删除�?�件的包�??
     */
    public static void deletePackage(Context context, String pkgName) {
        deletePackage(context, pkgName, null);
    }


    /**
     * 根�?�包�??删除�?�件apk，so，dex等数�?�
     *
     * @param context  宿主的Context
     * @param pkgName  待删除�?�件的包�??
     * @param callBack  �?�载回调
     */
    public static void deletePackage(Context context, String pkgName, IUninstallCallBack callBack) {
        Context mContext = ensureContext(context);
        PluginLiteInfo info = PluginPackageManagerNative.getInstance(mContext).getPackageInfo(pkgName);
        if (info != null) {
            deletePackage(mContext, info, callBack);
        }
    }

    /**
     * 删除�?�件apk，dex，so等数�?�
     *
     * @param context 宿主的Context
     * @param info    待删除�?�件的信�?�，包括包�??，路径等
     * @param callBack �?�载回调
     */
    public static void deletePackage(Context context, PluginLiteInfo info, IUninstallCallBack callBack) {
        // uninstall
        Context mContext = ensureContext(context);
        PluginPackageManagerNative.getInstance(mContext).deletePackage(info, callBack);
    }

    /**
     * 根�?�包�??�?�载一个�?�件
     *
     * @param context  宿主的Context
     * @param pkgName  待�?�载�?�件的包�??
     */
    public static void uninstall(Context context, String pkgName) {
        uninstall(context, pkgName, null);
    }


    /**
     * 根�?�包�??�?�载一个�?�件
     *
     * @param context  宿主的Context
     * @param pkgName  待�?�载�?�件的包�??
     * @param callBack  �?�载回调
     */
    public static void uninstall(Context context, String pkgName, IUninstallCallBack callBack) {
        Context mContext = ensureContext(context);
        PluginLiteInfo info = PluginPackageManagerNative.getInstance(mContext).getPackageInfo(pkgName);
        if (info != null) {
            uninstall(mContext, info, callBack);
        }
    }

    /**
     * �?�载一个�?�件
     *
     * @param context 宿主的Context
     * @param info    待�?�载�?�件的信�?�，包括包�??，路径等
     * @param callBack �?�载回调
     */
    public static void uninstall(Context context, PluginLiteInfo info, IUninstallCallBack callBack) {
        // uninstall
        Context mContext = ensureContext(context);
        PluginPackageManagerNative.getInstance(mContext).uninstall(info, callBack);
    }

    /**
     * �?�动一个�?�件的入�?�类
     *
     * @param mHostContext  宿主的Context
     * @param pkgName  待�?�动�?�件的包�??
     */
    public static void launchPlugin(Context mHostContext, String pkgName) {
        // start plugin
        PluginManager.launchPlugin(mHostContext, pkgName);
    }

    /**
     * 根�?�Intent�?�动一个�?�件
     *
     * @param mHostContext  宿主的Context
     * @param intent  需�?�?�动�?�件的Intent
     */
    public static void launchPlugin(Context mHostContext, Intent intent) {
        // start plugin, 默认选择进程
        PluginManager.launchPlugin(mHostContext, intent, null);
    }

    /**
     * 根�?�Intent�?�动一个�?�件，指定�?行进程的�??称
     *
     * @param mHostContext  宿主的Context
     * @param intent  需�?�?�动�?�件的Intent
     * @param processName  指定�?�动�?�件�?行的进程
     */
    public static void launchPlugin(Context mHostContext, Intent intent, String processName) {
        // start plugin, 指定进程
        PluginManager.launchPlugin(mHostContext, intent, processName);
    }

    /**
     * 根�?�Intent�?�动一个�?�件，支�?ServiceConnection
     *
     * @param mHostContext  宿主的Context
     * @param intent 需�?�?�动的�?�件Intent
     * @param sc  ServiceConnection
     * @param processName �?行的进程�??
     */
    public static void launchPlugin(Context mHostContext, Intent intent, ServiceConnection sc, String processName) {
        // start plugin, 指定ServiceConnection和进程
        PluginManager.launchPlugin(mHostContext, intent, sc, processName);
    }

    /**
     * 判断�?�件是�?�安装
     *
     * @param context  宿主的Context
     * @param pkgName  �?�件的包�??
     * @return  �?�件已安装， 返回true; �?�件未安装，返回false
     */
    public static boolean isPackageInstalled(Context context, String pkgName) {

        Context mContext = ensureContext(context);
        return PluginPackageManagerNative.getInstance(mContext).isPackageInstalled(pkgName);
    }

    /**
     * 判断�?�件是�?��?�用
     *
     * @param context  宿主的Context
     * @param pkgName  �?�件的包�??
     * @return  �?�件是�?�用的，返回true; �?�件�?�?�用，返回false
     */
    public static boolean isPackageAvailable(Context context, String pkgName) {

        Context mContext = ensureContext(context);
        return PluginPackageManagerNative.getInstance(mContext).isPackageAvailable(pkgName);
    }

    /**
     * 获�?��?�件PluginLiteInfo
     *
     * @param context  宿主的Context
     * @param pkgName  �?�件的包�??
     * @return  �?�件的信�?�
     */
    public static PluginLiteInfo getPluginInfo(Context context, String pkgName) {

        Context mContext = ensureContext(context);
        return PluginPackageManagerNative.getInstance(mContext).getPackageInfo(pkgName);
    }

    private static Context ensureContext(Context originContext) {
        if (originContext != null) {
            return originContext;
        }
        return sHostContext;
    }
}
