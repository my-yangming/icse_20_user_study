/*
 * Copyright 2015 Ben Manes. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.benmanes.caffeine.cache;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class provides a skeletal implementation of the {@link LoadingCache} interface to minimize
 * the effort required to implement a {@link LocalCache}.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
interface LocalLoadingCache<K, V> extends LocalManualCache<K, V>, LoadingCache<K, V> {
  Logger logger = Logger.getLogger(LocalLoadingCache.class.getName());

  /** Returns the {@link CacheLoader} used by this cache. */
  CacheLoader<? super K, V> cacheLoader();

  /** Returns the {@link CacheLoader#load} as a mapping function. */
  Function<K, V> mappingFunction();

  /** Returns the {@link CacheLoader#loadAll} as a mapping function, if implemented. */
  @Nullable Function<Iterable<? extends K>, Map<K, V>> bulkMappingFunction();

  @Override
  default @Nullable V get(K key) {
    return cache().computeIfAbsent(key, mappingFunction());
  }

  @Override
  default Map<K, V> getAll(Iterable<? extends K> keys) {
    return (bulkMappingFunction() == null)
        ? loadSequentially(keys)
        : getAll(keys, bulkMappingFunction());
  }

  /** Sequentially loads each missing entry. */
  default Map<K, V> loadSequentially(Iterable<? extends K> keys) {
    Set<K> uniqueKeys = new LinkedHashSet<>();
    for (K key : keys) {
      uniqueKeys.add(key);
    }

    int count = 0;
    Map<K, V> result = new LinkedHashMap<>(uniqueKeys.size());
    try {
      for (K key : uniqueKeys) {
        count++;

        V value = get(key);
        if (value != null) {
          result.put(key, value);
        }
      }
    } catch (Throwable t) {
      cache().statsCounter().recordMisses(uniqueKeys.size() - count);
      throw t;
    }
    return Collections.unmodifiableMap(result);
  }

  @Override
  @SuppressWarnings("FutureReturnValueIgnored")
  default void refresh(K key) {
    requireNonNull(key);

    long[] writeTime = new long[1];
    long startTime = cache().statsTicker().read();
    V oldValue = cache().getIfPresentQuietly(key, writeTime);
    CompletableFuture<V> refreshFuture = (oldValue == null)
        ? cacheLoader().asyncLoad(key, cache().executor())
        : cacheLoader().asyncReload(key, oldValue, cache().executor());
    refreshFuture.whenComplete((newValue, error) -> {
      long loadTime = cache().statsTicker().read() - startTime;
      if (error != null) {
        logger.log(Level.WARNING, "Exception thrown during refresh", error);
        cache().statsCounter().recordLoadFailure(loadTime);
        return;
      }

      boolean[] discard = new boolean[1];
      cache().compute(key, (k, currentValue) -> {
        if (currentValue == null) {
          return newValue;
        } else if (currentValue == oldValue) {
          long expectedWriteTime = writeTime[0];
          if (cache().hasWriteTime()) {
            cache().getIfPresentQuietly(key, writeTime);
          }
          if (writeTime[0] == expectedWriteTime) {
            return newValue;
          }
        }
        discard[0] = true;
        return currentValue;
      }, /* recordMiss */ false, /* recordLoad */ false, /* recordLoadFailure */ true);

      if (discard[0] && cache().hasRemovalListener()) {
        cache().notifyRemoval(key, newValue, RemovalCause.REPLACED);
      }
      if (newValue == null) {
        cache().statsCounter().recordLoadFailure(loadTime);
      } else {
        cache().statsCounter().recordLoadSuccess(loadTime);
      }
    });
  }

  /** Returns a mapping function that adapts to {@link CacheLoader#load}. */
  static <K, V> Function<K, V> newMappingFunction(CacheLoader<? super K, V> cacheLoader) {
    return key -> {
      try {
        return cacheLoader.load(key);
      } catch (RuntimeException e) {
        throw e;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new CompletionException(e);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    };
  }

  /** Returns a mapping function that adapts to {@link CacheLoader#loadAll}, if implemented. */
  static <K, V> @Nullable Function<Iterable<? extends K>, Map<K, V>> newBulkMappingFunction(
      CacheLoader<? super K, V> cacheLoader) {
    if (!hasLoadAll(cacheLoader)) {
      return null;
    }
    return keysToLoad -> {
      try {
        @SuppressWarnings("unchecked")
        Map<K, V> loaded = (Map<K, V>) cacheLoader.loadAll(keysToLoad);
        return loaded;
      } catch (RuntimeException e) {
        throw e;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new CompletionException(e);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    };
  }

  /** Returns whether the supplied cache loader has bulk load functionality. */
  static boolean hasLoadAll(CacheLoader<?, ?> loader) {
    try {
      Method classLoadAll = loader.getClass().getMethod("loadAll", Iterable.class);
      Method defaultLoadAll = CacheLoader.class.getMethod("loadAll", Iterable.class);
      return !classLoadAll.equals(defaultLoadAll);
    } catch (NoSuchMethodException | SecurityException e) {
      logger.log(Level.WARNING, "Cannot determine if CacheLoader can bulk load", e);
      return false;
    }
  }
}
