/*
 * Copyright (C) 2017-2018 Manbang Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wlqq.phantom.library.pool;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.util.ArrayMap;

import com.wlqq.phantom.library.utils.VLog;

import java.util.ArrayList;
import java.util.Set;

/**
 * �?�标准�?�动模�? {@link ActivityInfo#LAUNCH_MULTIPLE} �?��? {@link Activity} �?用池
 * <p/>
 * 若系统回收 {@link Activity}，则 {@link Activity#isFinishing()} 为 false；
 * 若是 {@link Activity} 退出，则 {@link Activity#isFinishing()} 为
 * true
 * <p/>
 * 通知�?�?�动的 {@link Activity} 需�?�?�外处�?�，
 */

class ActivityPool {
    private final ArrayList<String> mUnusedActivities;
    private final ArrayMap<String, ActivityRecord> mUsedActivities;

    /**
     * @param size            �?��?activity个数
     * @param modePrefix      �?��?activity�?缀，比如ActivityProxySingleInstance0的�?缀是ActivityProxySingleInstance。
     *                        �?��?activity编�?�从0开始
     * @param fixedActivities 被分�?给PendingIntent的Activity
     */
    ActivityPool(int size, String modePrefix, Set<String> fixedActivities) {
        this.mUnusedActivities = new ArrayList<>(size);
        this.mUsedActivities = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            this.mUnusedActivities.add(modePrefix + i);
        }

        for (String fixedActivityStr : fixedActivities) {
            FixedActivity fixedActivity = FixedActivity.parseFormString(fixedActivityStr);
            this.mUsedActivities.put(fixedActivity.pluginActivity,
                    new ActivityRecord(fixedActivity.proxyActivity, fixedActivity.pluginActivity, true, 0));
            this.mUnusedActivities.remove(fixedActivity.proxyActivity);
        }

    }

    String resolveActivity(String targetActivity) {
        return resolveActivity(targetActivity, false);
    }

    String resolveFixedActivity(String targetActivity) {
        return resolveActivity(targetActivity, true);
    }

    private String resolveActivity(String targetActivity, boolean isFixed) {
        //该activity已�?�?�动过
        ActivityRecord record = this.mUsedActivities.get(targetActivity);
        if (null != record) {
            VLog.d("resolveActivity %s has record, record isFixed=%s", targetActivity, record.isFixed());
            record.addRef();
            if (isFixed) {
                record.setFixed();
            }
            return record.mProxyActivity;
        }

        if (this.mUnusedActivities.size() > 0) {
            String activity = this.mUnusedActivities.remove(0);
            this.mUsedActivities.put(targetActivity, new ActivityRecord(activity, targetActivity, isFixed, 0));
            return activity;
        }

        return null;
    }


    void unrefActivity(String targetActivity) {
        ActivityRecord record = this.mUsedActivities.get(targetActivity);
        if (null == record) {
            return;
        }

        int ref = record.reduceRef();
        VLog.d("unrefActivity %s ref is %d isFixed %s", targetActivity, ref, record.isFixed());
        if (ref < 0 && !record.isFixed()) {
            //回收�?��?activity
            this.mUsedActivities.remove(targetActivity);
            this.mUnusedActivities.add(record.mProxyActivity);
            VLog.d("recycle proxy activity %s for %s", record.mProxyActivity, targetActivity);
        }
    }

    /**
     * 查找�?�件Activity对应的�?��?Activity
     *
     * @param pluginActivity �?�件Activity全�??，格�?为：packageName/activityName
     * @return 如果�?�件Activity还没�?�动或是标准�?�动模�?返回null
     */
    String findProxyActivity(String pluginActivity) {
        ActivityRecord record = mUsedActivities.get(pluginActivity);
        return null == record ? null : record.mProxyActivity;
    }

    /**
     * 打�?�Activity映射关系
     */
    void dump() {
        for (ActivityRecord record : mUsedActivities.values()) {
            VLog.w("%s  -->  %s", record.mProxyActivity, record.mPluginActivity);
        }

        for (String unusedActivity : mUnusedActivities) {
            VLog.w("%s  -->", unusedActivity);
        }
    }

    private static class ActivityRecord {
        String mProxyActivity;
        String mPluginActivity;
        int mActivityRef;
        //PendingIntent是由系统处�?�的，对这类Intent使用Activity分�?固定的代�?�activity
        boolean mIsFixed;

        ActivityRecord(String proxyActivity, String pluginActivity, boolean isFixed, int ref) {
            this.mProxyActivity = proxyActivity;
            this.mActivityRef = ref;
            this.mIsFixed = isFixed;
            this.mPluginActivity = pluginActivity;
        }

        void setFixed() {
            this.mIsFixed = true;
        }

        boolean isFixed() {
            return this.mIsFixed;
        }

        /**
         * 增加引用计数
         *
         * @return 当�?引用数
         */
        int addRef() {
            return ++this.mActivityRef;
        }

        /**
         * �?少引用计数
         *
         * @return 当�?引用数
         */
        int reduceRef() {
            return --this.mActivityRef;
        }

        @Override
        public String toString() {
            return String.format("%s, use host %s activity", mPluginActivity, mProxyActivity);
        }
    }
}
