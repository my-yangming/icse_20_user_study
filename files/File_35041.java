package com.nytimes.android.external.store3.middleware;


import com.google.gson.Gson;
import com.nytimes.android.external.store3.base.Parser;
import com.nytimes.android.external.store3.util.ParserException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import okio.BufferedSource;

import static com.nytimes.android.external.cache3.Preconditions.checkNotNull;


/**
 * Parser to be used when going from a BufferedSource to any Parsed Type
 * example usage:
 * ParsingStoreBuilder.<BufferedSource, BookResults>builder()
 * .fetcher(fetcher)
 * .persister(SourcePersisterFactory.create(getApplicationContext().getCacheDir()))
 * .parser(GsonParserFactory.createSourceParser(new Gson(),BookResult.class)
 * .open();
 */


public class GsonSourceParser<Parsed> implements Parser<BufferedSource, Parsed> {

    private final Gson gson;
    private final Type type;

    @Inject
    public GsonSourceParser(Gson gson, Type type) {
        checkNotNull(gson, "Gson can't be null");
        checkNotNull(type, "Type can't be null");
        this.gson = gson;
        this.type = type;
    }

    @Override
    public Parsed apply(@NonNull BufferedSource bufferedSource) throws ParserException {
        try (InputStreamReader reader = new InputStreamReader(bufferedSource.inputStream(), Charset.forName("UTF-8"))) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
