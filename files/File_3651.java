/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 20:20</create-date>
 *
 * <copyright file="NLPTokenizer.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.other.CharTable;
import com.hankcs.hanlp.dictionary.ts.SimplifiedChineseDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.utility.SentencesUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * �?体中文分�?器
 *
 * @author hankcs
 */
public class TraditionalChineseTokenizer
{
    /**
     * 预置分�?器
     */
    public static Segment SEGMENT = HanLP.newSegment();

    private static List<Term> segSentence(String text)
    {
        String sText = CharTable.convert(text);
        List<Term> termList = SEGMENT.seg(sText);
        int offset = 0;
        for (Term term : termList)
        {
            term.offset = offset;
            term.word = text.substring(offset, offset + term.length());
            offset += term.length();
        }

        return termList;
    }

    public static List<Term> segment(String text)
    {
        List<Term> termList = new LinkedList<Term>();
        for (String sentence : SentencesUtil.toSentenceList(text))
        {
            termList.addAll(segSentence(sentence));
        }

        return termList;
    }

    /**
     * 分�?
     *
     * @param text 文本
     * @return 分�?结果
     */
    public static List<Term> segment(char[] text)
    {
        return segment(CharTable.convert(text));
    }

    /**
     * 切分为�?��?形�?
     *
     * @param text 文本
     * @return �?��?列表
     */
    public static List<List<Term>> seg2sentence(String text)
    {
        List<List<Term>> resultList = new LinkedList<List<Term>>();
        {
            for (String sentence : SentencesUtil.toSentenceList(text))
            {
                resultList.add(segment(sentence));
            }
        }

        return resultList;
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
