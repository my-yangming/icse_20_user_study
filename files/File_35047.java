package com.nytimes.android.external.store3.middleware.moshi;

import com.nytimes.android.external.fs3.ObjectToSourceTransformer;
import com.nytimes.android.external.store3.annotations.Experimental;
import com.squareup.moshi.Moshi;

import java.lang.reflect.Type;

import javax.annotation.Nonnull;

/**
 * Factory which returns Moshi {@link io.reactivex.SingleTransformer} implementations.
 */
public final class MoshiTransformerFactory {

    private MoshiTransformerFactory() {
    }

    /**
     * Returns a new {@link ObjectToSourceTransformer}, which uses a {@link MoshiBufferedSourceAdapter} to parse from
     * objects of the specified type.
     */
    @Nonnull
    @Experimental
    public static <Parsed> ObjectToSourceTransformer<Parsed> createObjectToSourceTransformer(@Nonnull Type type) {
        return new ObjectToSourceTransformer<>(new MoshiBufferedSourceAdapter<Parsed>(new Moshi.Builder().build(),
                type));
    }

}
