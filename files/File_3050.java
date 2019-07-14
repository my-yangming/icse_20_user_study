/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/13 13:12</create-date>
 *
 * <copyright file="CoreSynonymDictionary.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.dictionary;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.algorithm.EditDistance;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.List;
import static com.hankcs.hanlp.utility.Predefine.logger;
/**
 * 核心�?�义�?�?典
 *
 * @author hankcs
 */
public class CoreSynonymDictionary
{
    static CommonSynonymDictionary dictionary;

    static
    {
        try
        {
            long start = System.currentTimeMillis();
            dictionary = CommonSynonymDictionary.create(IOUtil.newInputStream(HanLP.Config.CoreSynonymDictionaryDictionaryPath));
            logger.info("载入核心�?�义�?�?典�?功，耗时 " + (System.currentTimeMillis() - start) + " ms");
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("载入核心�?�义�?�?典失败" + e);
        }
    }

    /**
     * 获�?�一个�?的�?�义�?（�?义完全相�?�的，�?�{@link com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary.SynonymItem#type}
     * == {@link com.hankcs.hanlp.corpus.synonym.Synonym.Type#EQUAL}的）列表
     * @param key
     * @return
     */
    public static CommonSynonymDictionary.SynonymItem get(String key)
    {
        return dictionary.get(key);
    }

    /**
     * �?分�?直接转�?�
     * @param text
     * @return
     */
    public static String rewriteQuickly(String text)
    {
        return dictionary.rewriteQuickly(text);
    }

    public static String rewrite(String text)
    {
        return dictionary.rewrite(text);
    }

    /**
     * 语义�?离
     * @param itemA
     * @param itemB
     * @return
     */
    public static long distance(CommonSynonymDictionary.SynonymItem itemA, CommonSynonymDictionary.SynonymItem itemB)
    {
        return itemA.distance(itemB);
    }

    /**
     * 判断两个�?��?之间的语义�?离
     * @param A
     * @param B
     * @return
     */
    public static long distance(String A, String B)
    {
        CommonSynonymDictionary.SynonymItem itemA = get(A);
        CommonSynonymDictionary.SynonymItem itemB = get(B);
        if (itemA == null || itemB == null) return Long.MAX_VALUE;

        return distance(itemA, itemB);
    }

    /**
     * 计算两个�?��?之间的相似度，0表示�?相似，1表示完全相似
     * @param A
     * @param B
     * @return
     */
    public static double similarity(String A, String B)
    {
        long distance = distance(A, B);
        if (distance > dictionary.getMaxSynonymItemIdDistance()) return 0.0;

        return (dictionary.getMaxSynonymItemIdDistance() - distance) / (double) dictionary.getMaxSynonymItemIdDistance();
    }

    /**
     * 将分�?结果转�?�为�?�义�?列表
     * @param sentence �?��?
     * @param withUndefinedItem 是�?��?留�?典中没有的�?语
     * @return
     */
    public static List<CommonSynonymDictionary.SynonymItem> convert(List<Term> sentence, boolean withUndefinedItem)
    {
        List<CommonSynonymDictionary.SynonymItem> synonymItemList = new ArrayList<CommonSynonymDictionary.SynonymItem>(sentence.size());
        for (Term term : sentence)
        {
            CommonSynonymDictionary.SynonymItem item = get(term.word);
            if (item == null)
            {
                if (withUndefinedItem)
                {
                    item = CommonSynonymDictionary.SynonymItem.createUndefined(term.word);
                    synonymItemList.add(item);
                }

            }
            else
            {
                synonymItemList.add(item);
            }
        }

        return synonymItemList;
    }

    /**
     * 获�?�语义标签
     * @return
     */
    public static long[] getLexemeArray(List<CommonSynonymDictionary.SynonymItem> synonymItemList)
    {
        long[] array = new long[synonymItemList.size()];
        int i = 0;
        for (CommonSynonymDictionary.SynonymItem item : synonymItemList)
        {
            array[i++] = item.entry.id;
        }
        return array;
    }


    public long distance(List<CommonSynonymDictionary.SynonymItem> synonymItemListA, List<CommonSynonymDictionary.SynonymItem> synonymItemListB)
    {
        return EditDistance.compute(synonymItemListA, synonymItemListB);
    }

    public long distance(long[] arrayA, long[] arrayB)
    {
        return EditDistance.compute(arrayA, arrayB);
    }
}
