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

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.lexical.AbstractLexicalAnalyzer;

import java.io.IOException;
import java.util.List;

/**
 * �?�供自然语言处�?�用的分�?器，更�?视准确率。
 *
 * @author hankcs
 */
public class NLPTokenizer
{
    /**
     * 预置分�?器
     */
    public static AbstractLexicalAnalyzer ANALYZER;

    static
    {
        try
        {
            // 目�?感知机的效果相当�?错，如果能在更大的语料库上训练就更好了
            ANALYZER = new PerceptronLexicalAnalyzer();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<Term> segment(String text)
    {
        return ANALYZER.seg(text);
    }

    /**
     * 分�?
     *
     * @param text 文本
     * @return 分�?结果
     */
    public static List<Term> segment(char[] text)
    {
        return ANALYZER.seg(text);
    }

    /**
     * 切分为�?��?形�?
     *
     * @param text 文本
     * @return �?��?列表
     */
    public static List<List<Term>> seg2sentence(String text)
    {
        return ANALYZER.seg2sentence(text);
    }

    /**
     * �?法分�?
     *
     * @param sentence
     * @return 结构化�?��?
     */
    public static Sentence analyze(final String sentence)
    {
        return ANALYZER.analyze(sentence);
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
        return ANALYZER.seg2sentence(text, shortest);
    }
}
