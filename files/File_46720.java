package com.codingapi.txlcn.tracing;

import com.codingapi.txlcn.common.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Description: DTX Tracing 工具
 * Date: 19-2-11 下�?�1:02
 *
 * @author ujued
 */
@Slf4j
public class Tracings {

    /**
     * �?有构造器
     */
    private Tracings() {
    }

    /**
     * 传输Tracing信�?�
     *
     * @param tracingSetter Tracing信�?�设置器
     */
    public static void transmit(TracingSetter tracingSetter) {
        if (TracingContext.tracing().hasGroup()) {
            log.debug("tracing transmit group:{}", TracingContext.tracing().groupId());
            tracingSetter.set(TracingConstants.HEADER_KEY_GROUP_ID, TracingContext.tracing().groupId());
            tracingSetter.set(TracingConstants.HEADER_KEY_APP_MAP,
                    Base64Utils.encodeToString(TracingContext.tracing().appMapString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * 获�?�传输的Tracing信�?�
     *
     * @param tracingGetter Tracing信�?�获�?�器
     */
    public static void apply(TracingGetter tracingGetter) {
        String groupId = Optional.ofNullable(tracingGetter.get(TracingConstants.HEADER_KEY_GROUP_ID)).orElse("");
        String appList = Optional.ofNullable(tracingGetter.get(TracingConstants.HEADER_KEY_APP_MAP)).orElse("");
        TracingContext.init(Maps.newHashMap(TracingConstants.GROUP_ID, groupId, TracingConstants.APP_MAP,
                StringUtils.isEmpty(appList) ? appList : new String(Base64Utils.decodeFromString(appList), StandardCharsets.UTF_8)));
        if (TracingContext.tracing().hasGroup()) {
            log.debug("tracing apply group:{}, app map:{}", groupId, appList);
        }
    }

    /**
     * Tracing信�?�设置器
     */
    public interface TracingSetter {
        /**
         * 设置tracing属性
         *
         * @param key   key
         * @param value value
         */
        void set(String key, String value);
    }

    /**
     * Tracing信�?�获�?�器
     */
    public interface TracingGetter {
        /**
         * 获�?�tracing属性
         *
         * @param key key
         * @return tracing value
         */
        String get(String key);
    }
}
