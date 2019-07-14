package com.nytimes.android.external.fs3;


import com.nytimes.android.external.fs3.filesystem.FileSystem;
import com.nytimes.android.external.store3.base.Persister;
import com.nytimes.android.external.store3.base.impl.BarCode;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.BufferedSource;

/**
 * Persister to be used when storing something to persister from a BufferedSource
 * example usage:
 * ParsingStoreBuilder.<BufferedSource, BookResults>builder()
 * .fetcher(fetcher)
 * .persister(new SourcePersister(fileSystem))
 * .parser(new GsonSourceParser<>(gson, BookResults.class))
 * .open();
 */
public class SourcePersister implements Persister<BufferedSource, BarCode> {

    @Nonnull
    final SourceFileReader sourceFileReader;
    @Nonnull
    final SourceFileWriter sourceFileWriter;

    @Inject
    public SourcePersister(FileSystem fileSystem) {
        sourceFileReader = new SourceFileReader(fileSystem);
        sourceFileWriter = new SourceFileWriter(fileSystem);
    }

    public static SourcePersister create(FileSystem fileSystem) {
        return new SourcePersister(fileSystem);
    }

    @Nonnull
    static String pathForBarcode(@Nonnull BarCode barCode) {
        return barCode.getType() + barCode.getKey();
    }

    @Nonnull
    @Override
    public Maybe<BufferedSource> read(@Nonnull final BarCode barCode) {
        return sourceFileReader.read(barCode);
    }

    @Nonnull
    @Override
    public Single<Boolean> write(@Nonnull final BarCode barCode, @Nonnull final BufferedSource data) {
        return sourceFileWriter.write(barCode, data);
    }

}
