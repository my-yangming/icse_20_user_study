package com.apollographql.apollo.internal;

import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.Subscription;
import com.apollographql.apollo.exception.ApolloCanceledException;
import com.apollographql.apollo.exception.ApolloNetworkException;
import com.apollographql.apollo.internal.subscription.ApolloSubscriptionException;
import com.apollographql.apollo.internal.subscription.SubscriptionManager;

import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.NotNull;

import static com.apollographql.apollo.api.internal.Utils.checkNotNull;
import static com.apollographql.apollo.internal.CallState.ACTIVE;
import static com.apollographql.apollo.internal.CallState.CANCELED;
import static com.apollographql.apollo.internal.CallState.IDLE;
import static com.apollographql.apollo.internal.CallState.TERMINATED;

public class RealApolloSubscriptionCall<T> implements ApolloSubscriptionCall<T> {
  private final Subscription<?, T, ?> subscription;
  private final SubscriptionManager subscriptionManager;
  private final AtomicReference<CallState> state = new AtomicReference<>(IDLE);
  private SubscriptionManagerCallback<T> subscriptionCallback;

  public RealApolloSubscriptionCall(Subscription<?, T, ?> subscription, SubscriptionManager subscriptionManager) {
    this.subscription = subscription;
    this.subscriptionManager = subscriptionManager;
  }

  @Override
  public void execute(@NotNull Callback<T> callback) throws ApolloCanceledException {
    checkNotNull(callback, "callback == null");
    synchronized (this) {
      switch (state.get()) {
        case IDLE: {
          state.set(ACTIVE);
          subscriptionCallback = new SubscriptionManagerCallback<>(callback, this);
          subscriptionManager.subscribe(subscription, subscriptionCallback);
          break;
        }

        case CANCELED:
          throw new ApolloCanceledException("Call is cancelled.");

        case TERMINATED:
        case ACTIVE:
          throw new IllegalStateException("Already Executed");

        default:
          throw new IllegalStateException("Unknown state");
      }
    }
  }

  @Override
  public void cancel() {
    synchronized (this) {
      switch (state.get()) {
        case IDLE: {
          state.set(CANCELED);
          break;
        }

        case ACTIVE: {
          try {
            subscriptionManager.unsubscribe(subscription);
          } finally {
            state.set(CANCELED);
            subscriptionCallback.release();
          }
          break;
        }

        case CANCELED:
        case TERMINATED:
          // These are not illegal states, but cancelling does nothing
          break;

        default:
          throw new IllegalStateException("Unknown state");
      }
    }
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  public ApolloSubscriptionCall<T> clone() {
    return new RealApolloSubscriptionCall<>(subscription, subscriptionManager);
  }

  @Override public boolean isCanceled() {
    return state.get() == CANCELED;
  }

  private void terminate() {
    synchronized (this) {
      switch (state.get()) {
        case ACTIVE: {
          state.set(TERMINATED);
          subscriptionCallback.release();
          break;
        }

        case CANCELED:
          break;

        case IDLE:
        case TERMINATED:
          throw new IllegalStateException(
              CallState.IllegalStateMessage.forCurrentState(state.get()).expected(ACTIVE, CANCELED));

        default:
          throw new IllegalStateException("Unknown state");
      }
    }
  }

  private static final class SubscriptionManagerCallback<T> implements SubscriptionManager.Callback<T> {
    private Callback<T> originalCallback;
    private RealApolloSubscriptionCall<T> delegate;

    SubscriptionManagerCallback(Callback<T> originalCallback, RealApolloSubscriptionCall<T> delegate) {
      this.originalCallback = originalCallback;
      this.delegate = delegate;
    }

    @Override
    public void onResponse(@NotNull Response<T> response) {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onResponse(response);
      }
    }

    @Override
    public void onError(@NotNull ApolloSubscriptionException error) {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onFailure(error);
      }
      terminate();
    }

    @Override
    public void onNetworkError(@NotNull Throwable t) {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onFailure(new ApolloNetworkException("Subscription failed", t));
      }
      terminate();
    }

    @Override
    public void onCompleted() {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onCompleted();
      }
      terminate();
    }

    @Override
    public void onTerminated() {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onTerminated();
      }
      terminate();
    }

    @Override
    public void onConnected() {
      Callback<T> callback = this.originalCallback;
      if (callback != null) {
        callback.onConnected();
      }
    }

    void terminate() {
      RealApolloSubscriptionCall<T> delegate = this.delegate;
      if (delegate != null) {
        delegate.terminate();
      }
    }

    void release() {
      originalCallback = null;
      delegate = null;
    }
  }
}
