package com.zheng.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动�?数�?��?（数�?��?切�?�）
 * Created by ZhangShuzheng on 2017/1/15.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final static Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = getDataSource();
        LOGGER.info("当�?�?作使用的数�?��?：{}", dataSource);
        return dataSource;
    }

    /**
     * 设置数�?��?
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
    }

    /**
     * 获�?�数�?��?
     *
     * @return
     */
    public static String getDataSource() {
        String dataSource = CONTEXT_HOLDER.get();
        // 如果没有指定数�?��?，使用默认数�?��?
        if (null == dataSource) {
            DynamicDataSource.setDataSource(DataSourceEnum.MASTER.getDefault());
        }
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除数�?��?
     */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }

}
