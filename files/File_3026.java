/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 17:24</create-date>
 *
 * <copyright file="WordNatureDependencyParser.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.dependency;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.dependency.common.Edge;
import com.hankcs.hanlp.dependency.common.Node;
import com.hankcs.hanlp.model.bigram.WordNatureDependencyModel;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.utility.GlobalObjectPool;

import java.util.List;

/**
 * 一个简�?�的�?�法分�?器
 *
 * @author hankcs
 */
public class WordNatureDependencyParser extends MinimumSpanningTreeParser
{
    private WordNatureDependencyModel model;

    public WordNatureDependencyParser(WordNatureDependencyModel model)
    {
        this.model = model;
    }

    public WordNatureDependencyParser(String modelPath)
    {
        model = GlobalObjectPool.get(modelPath);
        if (model != null) return;
        model = new WordNatureDependencyModel(modelPath);
        GlobalObjectPool.put(modelPath, model);
    }

    public WordNatureDependencyParser()
    {
        this(HanLP.Config.WordNatureModelPath);
    }

    /**
     * 分�?�?��?的�?存�?�法
     *
     * @param termList �?��?，�?�以是任何具有�?性标注功能的分�?器的分�?结果
     * @return CoNLL格�?的�?存�?�法树
     */
    public static CoNLLSentence compute(List<Term> termList)
    {
        return new WordNatureDependencyParser().parse(termList);
    }

    /**
     * 分�?�?��?的�?存�?�法
     *
     * @param sentence �?��?
     * @return CoNLL格�?的�?存�?�法树
     */
    public static CoNLLSentence compute(String sentence)
    {
        return new WordNatureDependencyParser().parse(sentence);
    }

    @Override
    protected Edge makeEdge(Node[] nodeArray, int from, int to)
    {
        return model.getEdge(nodeArray[from], nodeArray[to]);
    }
}
