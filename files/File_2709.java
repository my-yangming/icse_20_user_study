/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/9 0:04</create-date>
 *
 * <copyright file="DictionaryMaker.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.dictionary;

import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;
import com.hankcs.hanlp.corpus.dictionary.item.Item;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.*;
import java.util.*;

import static com.hankcs.hanlp.HanLP.Config.IOAdapter;
import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * 一个通用的�?典制作工具，�?�?�格�?：�? 标签 频次
 * @author hankcs
 */
public class DictionaryMaker implements ISaveAble
{
    BinTrie<Item> trie;

    public DictionaryMaker()
    {
        trie = new BinTrie<Item>();
    }

    /**
     * �?��?典中加入一个�?语
     *
     * @param word �?语
     */
    public void add(IWord word)
    {
        Item item = trie.get(word.getValue());
        if (item == null)
        {
            item = new Item(word.getValue(), word.getLabel());
            trie.put(item.key, item);
        }
        else
        {
            item.addLabel(word.getLabel());
        }
    }

    public void add(String value, String label)
    {
        add(new Word(value, label));
    }

    /**
     * 删除一个�?�?�
     * @param value
     */
    public void remove(String value)
    {
        trie.remove(value);
    }

    public Item get(String key)
    {
        return trie.get(key);
    }

    public Item get(IWord word)
    {
        return get(word.getValue());
    }

    public TreeSet<String> labelSet()
    {
        TreeSet<String> labelSet = new TreeSet<String>();
        for (Map.Entry<String, Item> entry : entrySet())
        {
            labelSet.addAll(entry.getValue().labelMap.keySet());
        }

        return labelSet;
    }

    /**
     * 读�?�所有�?�目
     *
     * @param path
     * @return
     */
    public static List<Item> loadAsItemList(String path)
    {
        List<Item> itemList = new LinkedList<Item>();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(IOAdapter == null ? new FileInputStream(path) :
                                                                                 IOAdapter.open(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null)
            {
                Item item = Item.create(line);
                if (item == null)
                {
                    logger.warning("使用�?" + line + "】创建Item失败");
                    return null;
//                    continue;
                }
                itemList.add(item);
            }
        }
        catch (Exception e)
        {
            logger.warning("读�?��?典" + path + "�?�生异常" + e);
            return null;
        }

        return itemList;
    }

    /**
     * 从�?盘加载
     * @param path
     * @return
     */
    public static DictionaryMaker load(String path)
    {
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(path));

        return dictionaryMaker;
    }

    /**
     * �?�入全部�?�目
     *
     * @param itemList
     */
    public void addAll(List<Item> itemList)
    {
        for (Item item : itemList)
        {
            add(item);
        }
    }

    /**
     * �?�入新�?�目，�?执行�?�并
     *
     * @param itemList
     */
    public void addAllNotCombine(List<Item> itemList)
    {
        for (Item item : itemList)
        {
            addNotCombine(item);
        }
    }

    /**
     * �?�入�?�目
     *
     * @param item
     */
    public void add(Item item)
    {
        Item innerItem = trie.get(item.key);
        if (innerItem == null)
        {
            innerItem = item;
            trie.put(innerItem.key, innerItem);
        }
        else
        {
            innerItem.combine(item);
        }
    }

    /**
     * �?览所有�?�?�
     * @return
     */
    public Set<Map.Entry<String, Item>> entrySet()
    {
        return trie.entrySet();
    }

    public Set<String> keySet()
    {
        return trie.keySet();
    }

    /**
     * �?�入�?�目，但是�?�?�并，如果已有则忽略
     *
     * @param item
     */
    public void addNotCombine(Item item)
    {
        Item innerItem = trie.get(item.key);
        if (innerItem == null)
        {
            innerItem = item;
            trie.put(innerItem.key, innerItem);
        }
    }

    /**
     * �?�并两部�?典
     *
     * @param pathA
     * @param pathB
     * @return
     */
    public static DictionaryMaker combine(String pathA, String pathB)
    {
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(pathA));
        dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(pathB));

        return dictionaryMaker;
    }

    /**
     * �?�并多部�?典
     *
     * @param pathArray
     * @return
     */
    public static DictionaryMaker combine(String... pathArray)
    {
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        for (String path : pathArray)
        {
            logger.warning("正在处�?�" + path);
            dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(path));
        }
        return dictionaryMaker;
    }

    /**
     * 对除第一个之外的�?典执行标准化，并且�?�并
     *
     * @param pathArray
     * @return
     */
    public static DictionaryMaker combineWithNormalization(String[] pathArray)
    {
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        logger.info("正在处�?�主�?典" + pathArray[0]);
        dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(pathArray[0]));
        for (int i = 1; i < pathArray.length; ++i)
        {
            logger.info("正在处�?�副�?典" + pathArray[i] + "，将执行新�?�?�并模�?");
            dictionaryMaker.addAllNotCombine(DictionaryMaker.loadAsItemList(pathArray[i]));
        }
        return dictionaryMaker;
    }

    /**
     * �?�并，�?�补充除第一个�?典外其他�?典的新�?
     *
     * @param pathArray
     * @return
     */
    public static DictionaryMaker combineWhenNotInclude(String[] pathArray)
    {
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        logger.info("正在处�?�主�?典" + pathArray[0]);
        dictionaryMaker.addAll(DictionaryMaker.loadAsItemList(pathArray[0]));
        for (int i = 1; i < pathArray.length; ++i)
        {
            logger.info("正在处�?�副�?典" + pathArray[i] + "，并且过滤已有�?典");
            dictionaryMaker.addAllNotCombine(DictionaryMaker.normalizeFrequency(DictionaryMaker.loadAsItemList(pathArray[i])));
        }
        return dictionaryMaker;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("�?�?�数�?：");
        sb.append(trie.size());
        return sb.toString();
    }

    @Override
    public boolean saveTxtTo(String path)
    {
        if (trie.size() == 0) return true;  // 如果没有�?�?�，那也算�?功了
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(IOUtil.newOutputStream(path), "UTF-8"));
            Set<Map.Entry<String, Item>> entries = trie.entrySet();
            for (Map.Entry<String, Item> entry : entries)
            {
                bw.write(entry.getValue().toString());
                bw.newLine();
            }
            bw.close();
        }
        catch (Exception e)
        {
            logger.warning("�?存到" + path + "失败" + e);
            return false;
        }

        return true;
    }

    public void add(String param)
    {
        Item item = Item.create(param);
        if (item != null) add(item);
    }

    public static interface Filter
    {
        /**
         * 是�?��?存这个�?�目
         * @param item
         * @return true表示�?存
         */
        boolean onSave(Item item);
    }

    /**
     * �?许�?存之�?对其�?�一些调整
     *
     * @param path
     * @param filter
     * @return
     */
    public boolean saveTxtTo(String path, Filter filter)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(IOUtil.newOutputStream(path), "UTF-8"));
            Set<Map.Entry<String, Item>> entries = trie.entrySet();
            for (Map.Entry<String, Item> entry : entries)
            {
                if (filter.onSave(entry.getValue()))
                {
                    bw.write(entry.getValue().toString());
                    bw.newLine();
                }
            }
            bw.close();
        }
        catch (Exception e)
        {
            logger.warning("�?存到" + path + "失败" + e);
            return false;
        }

        return true;
    }

    /**
     * 调整频次，按排�?�?�的次�?给定频次
     *
     * @param itemList
     * @return 处�?��?�的列表
     */
    public static List<Item> normalizeFrequency(List<Item> itemList)
    {
        for (Item item : itemList)
        {
            ArrayList<Map.Entry<String, Integer>> entryArray = new ArrayList<Map.Entry<String, Integer>>(item.labelMap.entrySet());
            Collections.sort(entryArray, new Comparator<Map.Entry<String, Integer>>()
            {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            int index = 1;
            for (Map.Entry<String, Integer> pair : entryArray)
            {
                item.labelMap.put(pair.getKey(), index);
                ++index;
            }
        }
        return itemList;
    }
}
