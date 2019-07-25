/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tdunning;

import com.tdunning.math.stats.LogHistogram;
import com.tdunning.math.stats.MergingDigest;
import com.tdunning.math.stats.ScaleFunction;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Explores the value of using a large buffer for the MergingDigest. The rationale is that the internal
 * sort is extremely fast while the merging function in the t-digest can be quite slow, if only because
 * computing the asin function involved in the merge is expensive. This argues for collecting more samples
 * before sorting and merging them into the digest.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@State(Scope.Thread)
public class ApproxLogBench {
    private static final double LOG_2 = Math.log(2);
    private Random gen = new Random();
    private double[] data;

    @Setup
    public void setup() {
        data = new double[10000000];
        for (int i = 0; i < data.length; i++) {
            data[i] = gen.nextDouble();
        }
    }

    @State(Scope.Thread)
    public static class ThreadState {
        int index = 0;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addApprox(ThreadState state) {
        if (state.index >= data.length) {
            state.index = 0;
        }
        double sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += LogHistogram.approxLog2(data[state.index++]);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void addLog(ThreadState state) {
        if (state.index >= data.length) {
            state.index = 0;
        }
        double sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += Math.log(data[state.index++])/LOG_2;
        }

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ApproxLogBench.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opt).run();
    }
}
