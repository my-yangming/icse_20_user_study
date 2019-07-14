package com.hankcs.hanlp.classification.features;

/**
 * TF-IDF�?��?计算
 */
public class TfIdfFeatureWeighter implements IFeatureWeighter
{
    int numDocs;
    int df[];

    public TfIdfFeatureWeighter(int numDocs, int[] df)
    {
        this.numDocs = numDocs;
        this.df = df;
    }

    public double weight(int feature, int tf)
    {
        if (feature >= df.length) System.err.println(feature);
        return Math.log10(tf + 1) * (Math.log10((double) numDocs / df[feature] + 1));    // 一�?改进的tf*idf计算方�?;
    }
}
