package org.hswebframework.web.datasource;

import org.hswebframework.web.datasource.exception.DataSourceNotFoundException;
import org.hswebframework.web.datasource.switcher.*;

/**
 * 用于�?作动�?数�?��?,如获�?�当�?使用的数�?��?,使用switcher切�?�数�?��?等
 *
 * @author zhouhao
 * @since 3.0
 */
public final class DataSourceHolder {

    private static final DataSourceSwitcher defaultSwitcher = new DefaultDataSourceSwitcher();

    /**
     * 动�?数�?��?切�?�器
     */
    static volatile DataSourceSwitcher dataSourceSwitcher = defaultSwitcher;
    /**
     * 动�?数�?��?�?务
     */
    static volatile DynamicDataSourceService dynamicDataSourceService;

    static volatile TableSwitcher tableSwitcher = new DefaultTableSwitcher();

    static volatile DatabaseSwitcher databaseSwitcher = new DefaultDatabaseSwitcher();


    public static void checkDynamicDataSourceReady() {
        if (dynamicDataSourceService == null) {
            throw new UnsupportedOperationException("dataSourceService not ready");
        }
    }

    /**
     * @return 动�?数�?��?切�?�器
     */
    public static DataSourceSwitcher switcher() {
        return dataSourceSwitcher;
    }

    /**
     * @return 表切�?�器, 用于动�?切�?�系统功能表
     */
    public static TableSwitcher tableSwitcher() {
        return tableSwitcher;
    }

    /**
     * @return 数�?�库切�?�器
     * @since 3.0.8
     */
    public static DatabaseSwitcher databaseSwitcher() {
        return databaseSwitcher;
    }


    /**
     * @return 默认数�?��?
     */
    public static DynamicDataSource defaultDataSource() {
        checkDynamicDataSourceReady();
        return dynamicDataSourceService.getDefaultDataSource();
    }

    /**
     * 根�?�指定的数�?��?id获�?�动�?数�?��?
     *
     * @param dataSourceId 数�?��?id
     * @return 动�?数�?��?
     * @throws DataSourceNotFoundException 如果数�?��?�?存在将抛出此异常
     */
    public static DynamicDataSource dataSource(String dataSourceId) {
        checkDynamicDataSourceReady();
        return dynamicDataSourceService.getDataSource(dataSourceId);
    }

    /**
     * @return 当�?使用的数�?��?
     */
    public static DynamicDataSource currentDataSource() {
        String id = dataSourceSwitcher.currentDataSourceId();
        if (id == null) {
            return defaultDataSource();
        }
        checkDynamicDataSourceReady();
        return dynamicDataSourceService.getDataSource(id);
    }

    /**
     * @return 当�?使用的数�?��?是�?�为默认数�?��?
     */
    public static boolean currentIsDefault() {
        return dataSourceSwitcher.currentDataSourceId() == null;
    }

    /**
     * 判断指定id的数�?��?是�?�存在
     *
     * @param id 数�?��?id {@link DynamicDataSource#getId()}
     * @return 数�?��?是�?�存在
     */
    public static boolean existing(String id) {
        try {
            checkDynamicDataSourceReady();
            return dynamicDataSourceService.getDataSource(id) != null;
        } catch (DataSourceNotFoundException e) {
            return false;
        }
    }

    /**
     * @return 当�?使用的数�?��?是�?�存在
     */
    public static boolean currentExisting() {
        if (currentIsDefault()) {
            return true;
        }
        try {
            return currentDataSource() != null;
        } catch (DataSourceNotFoundException e) {
            return false;
        }
    }

    /**
     * @return 当�?数�?�库类型
     */
    public static DatabaseType currentDatabaseType() {
        return currentDataSource().getType();
    }

    /**
     * @return 默认的数�?�库类型
     */
    public static DatabaseType defaultDatabaseType() {
        return defaultDataSource().getType();
    }
}
