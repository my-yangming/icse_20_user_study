package com.sankuai.waimai.router.common;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.sankuai.waimai.router.annotation.RouterPage;
import com.sankuai.waimai.router.components.RouterComponents;
import com.sankuai.waimai.router.core.UriCallback;
import com.sankuai.waimai.router.core.UriRequest;
import com.sankuai.waimai.router.core.UriResult;
import com.sankuai.waimai.router.utils.LazyInitHelper;
import com.sankuai.waimai.router.utils.RouterUtils;

/**
 * 内部页�?�跳转，由注解 {@link RouterPage} �?置。
 * {@link PageAnnotationHandler} 处�?�且�?�处�?�所有格�?为 wm_router://page/* 的URI，根�?�path匹�?，
 * 匹�?�?到的分�?�给 {@link NotFoundHandler} ，返回 {@link UriResult#CODE_NOT_FOUND}
 *
 * Created by jzj on 2018/3/23.
 */

public class PageAnnotationHandler extends PathHandler {

    public static final String SCHEME = "wm_router";
    public static final String HOST = "page";
    public static final String SCHEME_HOST = RouterUtils.schemeHost(SCHEME, HOST);

    public static boolean isPageJump(Intent intent) {
        return intent != null && SCHEME_HOST.equals(RouterUtils.schemeHost(intent.getData()));
    }

    private final LazyInitHelper mInitHelper = new LazyInitHelper("PageAnnotationHandler") {
        @Override
        protected void doInit() {
            initAnnotationConfig();
        }
    };

    public PageAnnotationHandler() {
        addInterceptor(NotExportedInterceptor.INSTANCE); // exported全为false
        setDefaultChildHandler(NotFoundHandler.INSTANCE); // 找�?到直接终止分�?�
    }

    /**
     * @see LazyInitHelper#lazyInit()
     */
    public void lazyInit() {
        mInitHelper.lazyInit();
    }

    protected void initAnnotationConfig() {
        RouterComponents.loadAnnotation(this, IPageAnnotationInit.class);
    }

    @Override
    public void handle(@NonNull UriRequest request, @NonNull UriCallback callback) {
        mInitHelper.ensureInit();
        super.handle(request, callback);
    }

    @Override
    protected boolean shouldHandle(@NonNull UriRequest request) {
        return SCHEME_HOST.matches(request.schemeHost());
    }

    @Override
    public String toString() {
        return "PageAnnotationHandler";
    }
}
