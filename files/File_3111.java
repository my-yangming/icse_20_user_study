/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/15 19:39</create-date>
 *
 * <copyright file="CoreStopwordDictionary.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.dictionary.stopword;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.ByteArray;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.hanlp.utility.TextUtility;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.ListIterator;
import static com.hankcs.hanlp.utility.Predefine.logger;


/**
 * 核心�?�用�?�?典
 * @author hankcs
 */
public class CoreStopWordDictionary
{
    static StopWordDictionary dictionary;
    static
    {
        load(HanLP.Config.CoreStopWordDictionaryPath, true);
    }

    /**
     * �?新加载{@link HanLP.Config#CoreStopWordDictionaryPath}所指定的�?�用�?�?典，并且生�?新缓存。
     */
    public static void reload()
    {
        load(HanLP.Config.CoreStopWordDictionaryPath, false);
    }

    /**
     * 加载�?�一部�?�用�?�?典
     * @param coreStopWordDictionaryPath �?典路径
     * @param loadCacheIfPossible 是�?�优先加载缓存（速度更快）
     */
    public static void load(String coreStopWordDictionaryPath, boolean loadCacheIfPossible)
    {
        ByteArray byteArray = loadCacheIfPossible ? ByteArray.createByteArray(coreStopWordDictionaryPath + Predefine.BIN_EXT) : null;
        if (byteArray == null)
        {
            try
            {
                dictionary = new StopWordDictionary(HanLP.Config.CoreStopWordDictionaryPath);
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(IOUtil.newOutputStream(HanLP.Config.CoreStopWordDictionaryPath + Predefine.BIN_EXT)));
                dictionary.save(out);
                out.close();
            }
            catch (Exception e)
            {
                logger.severe("载入�?�用�?�?典" + HanLP.Config.CoreStopWordDictionaryPath + "失败"  + TextUtility.exceptionToString(e));
                throw new RuntimeException("载入�?�用�?�?典" + HanLP.Config.CoreStopWordDictionaryPath + "失败");
            }
        }
        else
        {
            dictionary = new StopWordDictionary();
            dictionary.load(byteArray);
        }
    }

    public static boolean contains(String key)
    {
        return dictionary.contains(key);
    }

    /**
     * 核心�?�用�?典的核心过滤器，�?性属于�??�?�?动�?�?副�?�?形容�?，并且�?在�?�用�?表中�?�?会被过滤
     */
    public static Filter FILTER = new Filter()
    {
        @Override
        public boolean shouldInclude(Term term)
        {
            // 除掉�?�用�?
            String nature = term.nature != null ? term.nature.toString() : "空";
            char firstChar = nature.charAt(0);
            switch (firstChar)
            {
                case 'm':
                case 'b':
                case 'c':
                case 'e':
                case 'o':
                case 'p':
                case 'q':
                case 'u':
                case 'y':
                case 'z':
                case 'r':
                case 'w':
                {
                    return false;
                }
                default:
                {
                    if (!CoreStopWordDictionary.contains(term.word))
                    {
                        return true;
                    }
                }
                break;
            }

            return false;
        }
    };

    /**
     * 是�?�应当将这个term纳入计算
     *
     * @param term
     * @return 是�?�应当
     */
    public static boolean shouldInclude(Term term)
    {
        return FILTER.shouldInclude(term);
    }

    /**
     * 是�?�应当去掉这个�?
     * @param term �?
     * @return 是�?�应当去掉
     */
    public static boolean shouldRemove(Term term)
    {
        return !shouldInclude(term);
    }

    /**
     * 加入�?�用�?到�?�用�?�?典中
     * @param stopWord �?�用�?
     * @return �?典是�?��?�生了改�?�
     */
    public static boolean add(String stopWord)
    {
        return dictionary.add(stopWord);
    }

    /**
     * 从�?�用�?�?典中删除�?�用�?
     * @param stopWord �?�用�?
     * @return �?典是�?��?�生了改�?�
     */
    public static boolean remove(String stopWord)
    {
        return dictionary.remove(stopWord);
    }

    /**
     * 对分�?结果应用过滤
     * @param termList
     */
    public static void apply(List<Term> termList)
    {
        ListIterator<Term> listIterator = termList.listIterator();
        while (listIterator.hasNext())
        {
            if (shouldRemove(listIterator.next())) listIterator.remove();
        }
    }
}
