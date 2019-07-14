/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/5 15:49</create-date>
 *
 * <copyright file="IdVectorScorer.java" company="上海林原信�?�科技有�?公�?�">
 * Copyright (c) 2003-2014, 上海林原信�?�科技有�?公�?�. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信�?�科技有�?公�?� to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.suggest.scorer.lexeme;

import com.hankcs.hanlp.suggest.scorer.BaseScorer;

/**
 * �?��?语义�?��?打分器
 * @author hankcs
 */
public class IdVectorScorer extends BaseScorer<IdVector>
{
    @Override
    protected IdVector generateKey(String sentence)
    {
        IdVector idVector = new IdVector(sentence);
        if (idVector.idArrayList.size() == 0) return null;
        return idVector;
    }
}
