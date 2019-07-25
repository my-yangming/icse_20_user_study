/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deepoove.poi.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.deepoove.poi.policy.DocxRenderPolicy;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.policy.NumbericRenderPolicy;
import com.deepoove.poi.policy.PictureRenderPolicy;
import com.deepoove.poi.policy.ReferenceRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.util.RegexUtils;

/**
 * �?�件化�?置
 * 
 * @author Sayi
 * @version 1.0.0
 */
public class Configure {

    // defalut expression
    private static final String DEFAULT_GRAMER_REGEX = "[\\w\\u4e00-\\u9fa5]+(\\.[\\w\\u4e00-\\u9fa5]+)*";

    // Highest priority
    private Map<String, RenderPolicy> customPolicys = new HashMap<String, RenderPolicy>();
    // Low priority
    private Map<Character, RenderPolicy> defaultPolicys = new HashMap<Character, RenderPolicy>();

    /**
     * 引用渲染策略
     */
    private List<ReferenceRenderPolicy<?>> referencePolicies = new ArrayList<>();

    /**
     * 语法�?缀
     */
    private String gramerPrefix = "{{";
    /**
     * 语法�?�缀
     */
    private String gramerSuffix = "}}";

    /**
     * 默认支�?中文�?字�?�?数字�?下划线的正则
     */
    private String grammerRegex = DEFAULT_GRAMER_REGEX;

    /**
     * 模�?�表达�?模�?，默认为POI_TL_MODE
     */
    private ELMode elMode = ELMode.POI_TL_STANDARD_MODE;

    /**
     * 渲染数�?�为null时，是�?留还是清空模�?�标签
     */
    private boolean nullToBlank = true;

    private Configure() {
        plugin(GramerSymbol.TEXT, new TextRenderPolicy());
        plugin(GramerSymbol.IMAGE, new PictureRenderPolicy());
        plugin(GramerSymbol.TABLE, new MiniTableRenderPolicy());
        plugin(GramerSymbol.NUMBERIC, new NumbericRenderPolicy());
        plugin(GramerSymbol.DOCX_TEMPLATE, new DocxRenderPolicy());
    }

    /**
     * 获�?�默认�?置
     */
    public static Configure createDefault() {
        return newBuilder().build();
    }

    /**
     * 获�?�构建器
     */
    public static ConfigureBuilder newBuilder() {
        return new ConfigureBuilder();
    }

    /**
     * 新增语法�?�件
     * 
     * @param c
     *            模�?�语法
     * @param policy
     *            策略
     */
    public Configure plugin(char c, RenderPolicy policy) {
        defaultPolicys.put(Character.valueOf(c), policy);
        return this;
    }

    Configure plugin(GramerSymbol symbol, RenderPolicy policy) {
        defaultPolicys.put(symbol.getSymbol(), policy);
        return this;
    }

    /**
     * 自定义模�?�
     * 
     * @param tagName
     *            模�?��??称
     * @param policy
     *            策略
     */
    public void customPolicy(String tagName, RenderPolicy policy) {
        customPolicys.put(tagName, policy);
    }

    /**
     * 新增引用渲染策略
     * 
     * @param policy
     */
    public void referencePolicy(ReferenceRenderPolicy<?> policy) {
        referencePolicies.add(policy);
    }

    /**
     * 获�?�标签策略
     * 
     * @param tagName
     *            模�?��??称
     * @param sign
     *            语法
     */
    public RenderPolicy getPolicy(String tagName, Character sign) {
        RenderPolicy policy = getCustomPolicy(tagName);
        return null == policy ? getDefaultPolicy(sign) : policy;
    }

    public List<ReferenceRenderPolicy<?>> getReferencePolicies() {
        return referencePolicies;
    }

    public Map<Character, RenderPolicy> getDefaultPolicys() {
        return defaultPolicys;
    }

    public Map<String, RenderPolicy> getCustomPolicys() {
        return customPolicys;
    }

    public Set<Character> getGramerChars() {
        return defaultPolicys.keySet();
    }

    public String getGramerPrefix() {
        return gramerPrefix;
    }

    public String getGramerSuffix() {
        return gramerSuffix;
    }

    private RenderPolicy getCustomPolicy(String tagName) {
        return customPolicys.get(tagName);
    }

    private RenderPolicy getDefaultPolicy(Character sign) {
        return defaultPolicys.get(sign);
    }

    public String getGrammerRegex() {
        return grammerRegex;
    }

    public ELMode getElMode() {
        return elMode;
    }

    public boolean isNullToBlank() {
        return nullToBlank;
    }

    public static class ConfigureBuilder {
        private static String regexForAllPattern = "((?!{0})(?!{1}).)*";
        private boolean regexForAll;
        private Configure config = new Configure();

        public ConfigureBuilder() {}

        public ConfigureBuilder buildGramer(String prefix, String suffix) {
            config.gramerPrefix = prefix;
            config.gramerSuffix = suffix;
            return this;
        }

        /**
         * 设置模�?�表达�?模�?
         * 
         * @return
         */
        public ConfigureBuilder setElMode(ELMode mode) {
            config.elMode = mode;
            return this;
        }

        public ConfigureBuilder buildGrammerRegex(String reg) {
            config.grammerRegex = reg;
            return this;
        }

        public ConfigureBuilder supportGrammerRegexForAll() {
            this.regexForAll = true;
            return this;
        }

        public ConfigureBuilder supportNullToBlank(boolean nullToBlank) {
            config.nullToBlank = nullToBlank;
            return this;
        }

        public ConfigureBuilder addPlugin(char c, RenderPolicy policy) {
            config.plugin(c, policy);
            return this;
        }

        public ConfigureBuilder customPolicy(String tagName, RenderPolicy policy) {
            config.customPolicy(tagName, policy);
            return this;
        }

        public ConfigureBuilder referencePolicy(ReferenceRenderPolicy<?> policy) {
            config.referencePolicy(policy);
            return this;
        }

        public Configure build() {
            if (config.elMode == ELMode.SPEL_MODE) {
                supportGrammerRegexForAll();
            }
            if (regexForAll) {
                buildGrammerRegex(MessageFormat.format(regexForAllPattern,
                        RegexUtils.escapeExprSpecialWord(config.gramerPrefix),
                        RegexUtils.escapeExprSpecialWord(config.gramerSuffix)));
            }
            return config;
        }

    }

}
