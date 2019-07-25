package com.xiaojinzi.component.cache;

import android.support.annotation.NonNull;

import com.xiaojinzi.component.Component;

/**
 * <pre>
 * @author : zbb 33775
 * @Date: 2019/2/25 13:50
 * </pre>
 */
public class DefaultCacheFactory implements Cache.Factory{

    private DefaultCacheFactory() {
    }

    public static final DefaultCacheFactory INSTANCE = new DefaultCacheFactory();

    @NonNull
    @Override
    public Cache build(CacheType type) {
        return new LruCache(type.calculateCacheSize(Component.getApplication()));
    }

}
