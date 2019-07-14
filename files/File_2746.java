/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/17 17:24</create-date>
 *
 * <copyright file="SuffixDictionary.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.dictionary;

import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;

import java.util.*;

/**
 * �?�缀树�?典
 * @author hankcs
 */
public class SuffixDictionary
{
    BinTrie<Integer> trie;

    public SuffixDictionary()
    {
        trie = new BinTrie<Integer>();
    }

    /**
     * 添加一个�?语
     * @param word
     */
    public void add(String word)
    {
        word = reverse(word);
        trie.put(word, word.length());
    }

    public void addAll(String total)
    {
        for (int i = 0; i < total.length(); ++i)
        {
            add(String.valueOf(total.charAt(i)));
        }
    }

    public void addAll(String[] total)
    {
        for (String single : total)
        {
            add(single);
        }
    }

    /**
     * 查找是�?�有该�?�缀
     * @param suffix
     * @return
     */
    public int get(String suffix)
    {
        suffix = reverse(suffix);
        Integer length = trie.get(suffix);
        if (length == null) return 0;

        return length;
    }

    /**
     * �?语是�?�以该�?典中的�?个�?��?结尾
     * @param word
     * @return
     */
    public boolean endsWith(String word)
    {
        word = reverse(word);
        return trie.commonPrefixSearchWithValue(word).size() > 0;
    }

    /**
     * 获�?�最长的�?�缀
     * @param word
     * @return
     */
    public int getLongestSuffixLength(String word)
    {
        word = reverse(word);
        LinkedList<Map.Entry<String, Integer>> suffixList = trie.commonPrefixSearchWithValue(word);
        if (suffixList.size() == 0) return 0;
        return suffixList.getLast().getValue();
    }

    private static String reverse(String word)
    {
        return new StringBuilder(word).reverse().toString();
    }

    /**
     * 键值对
     * @return
     */
    public Set<Map.Entry<String, Integer>> entrySet()
    {
        Set<Map.Entry<String, Integer>> treeSet = new LinkedHashSet<Map.Entry<String, Integer>>();
        for (Map.Entry<String, Integer> entry : trie.entrySet())
        {
            treeSet.add(new AbstractMap.SimpleEntry<String, Integer>(reverse(entry.getKey()), entry.getValue()));
        }

        return treeSet;
    }
}
