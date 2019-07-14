/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/19 18:33</create-date>
 *
 * <copyright file="IndexTokenizer.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * 索引分�?器
 * @author hankcs
 */
public class IndexTokenizer
{
    /**
     * 预置分�?器
     */
    public static final Segment SEGMENT = HanLP.newSegment().enableIndexMode(true);
    public static List<Term> segment(String text)
    {
        return SEGMENT.seg(text);
    }

    /**
     * 分�?
     * @param text 文本
     * @return 分�?结果
     */
    public static List<Term> segment(char[] text)
    {
        return SEGMENT.seg(text);
    }

    /**
     * 切分为�?��?形�?
     * @param text 文本
     * @return �?��?列表
     */
    public static List<List<Term>> seg2sentence(String text)
    {
        return SEGMENT.seg2sentence(text);
    }

    /**
     * 分�?断�?� 输出�?��?形�?
     *
     * @param text     待分�?�?��?
     * @param shortest 是�?�断�?�为最细的�?�?�（将逗�?�也视作分隔符）
     * @return �?��?列表，�?个�?��?由一个�?��?列表组�?
     */
    public static List<List<Term>> seg2sentence(String text, boolean shortest)
    {
        return SEGMENT.seg2sentence(text, shortest);
    }
}
