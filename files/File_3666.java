/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/7/14 11:01</create-date>
 *
 * <copyright file="WordNatureUtil.java" company="�?农场">
 * Copyright (c) 2008-2015, �?农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.utility;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * 跟�?语与�?性有关的工具类，�?�以全局动�?修改HanLP内部�?库
 *
 * @author hankcs
 */
public class LexiconUtility
{
    /**
     * 从HanLP的�?库中�??�?��?个�?��?的属性（包括核心�?典和用户�?典）
     *
     * @param word �?��?
     * @return 包�?��?性与频次的信�?�
     */
    public static CoreDictionary.Attribute getAttribute(String word)
    {
        CoreDictionary.Attribute attribute = CoreDictionary.get(word);
        if (attribute != null) return attribute;
        return CustomDictionary.get(word);
    }

    /**
     * �?库是�?�收录了�?语（查询核心�?典和用户�?典）
     * @param word
     * @return
     */
    public static boolean contains(String word)
    {
        return getAttribute(word) != null;
    }

    /**
     * 从HanLP的�?库中�??�?��?个�?��?的属性（包括核心�?典和用户�?典）
     *
     * @param term �?��?
     * @return 包�?��?性与频次的信�?�
     */
    public static CoreDictionary.Attribute getAttribute(Term term)
    {
        return getAttribute(term.word);
    }

    /**
     * 获�?��?个�?��?的�?频
     * @param word
     * @return
     */
    public static int getFrequency(String word)
    {
        CoreDictionary.Attribute attribute = getAttribute(word);
        if (attribute == null) return 0;
        return attribute.totalFrequency;
    }

    /**
     * 设置�?个�?��?的属性
     * @param word
     * @param attribute
     * @return
     */
    public static boolean setAttribute(String word, CoreDictionary.Attribute attribute)
    {
        if (attribute == null) return false;

        if (CoreDictionary.trie.set(word, attribute)) return true;
        if (CustomDictionary.dat.set(word, attribute)) return true;
        if (CustomDictionary.trie == null)
        {
            CustomDictionary.add(word);
        }
        CustomDictionary.trie.put(word, attribute);
        return true;
    }

    /**
     * 设置�?个�?��?的属性
     * @param word
     * @param natures
     * @return
     */
    public static boolean setAttribute(String word, Nature... natures)
    {
        if (natures == null) return false;

        CoreDictionary.Attribute attribute = new CoreDictionary.Attribute(natures, new int[natures.length]);
        Arrays.fill(attribute.frequency, 1);

        return setAttribute(word, attribute);
    }

    /**
     * 设置�?个�?��?的属性
     * @param word
     * @param natures
     * @return
     */
    public static boolean setAttribute(String word, String... natures)
    {
        if (natures == null) return false;

        Nature[] natureArray = new Nature[natures.length];
        for (int i = 0; i < natureArray.length; i++)
        {
            natureArray[i] = Nature.create(natures[i]);
        }

        return setAttribute(word, natureArray);
    }


    /**
     * 设置�?个�?��?的属性
     * @param word
     * @param natureWithFrequency
     * @return
     */
    public static boolean setAttribute(String word, String natureWithFrequency)
    {
        CoreDictionary.Attribute attribute = CoreDictionary.Attribute.create(natureWithFrequency);
        return setAttribute(word, attribute);
    }

    /**
     * 将字符串�?性转为Enum�?性
     * @param name �?性�??称
     * @param customNatureCollector 一个收集集�?�
     * @return 转�?�结果
     */
    public static Nature convertStringToNature(String name, LinkedHashSet<Nature> customNatureCollector)
    {
        Nature nature = Nature.fromString(name);
        if (nature == null)
        {
            nature = Nature.create(name);
            if (customNatureCollector != null) customNatureCollector.add(nature);
        }
        return nature;
    }

    /**
     * 将字符串�?性转为Enum�?性
     * @param name �?性�??称
     * @return 转�?�结果
     */
    public static Nature convertStringToNature(String name)
    {
        return convertStringToNature(name, null);
    }
}
