package com.photoviewer.domain.usecases;

import org.reactivestreams.Subscription;

import io.reactivex.observers.DisposableObserver;

/**
 * Default subscriber base class to be used whenever you want default error handling.
 */
public class SimpleSubscriber<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {
        // no-op by default.
    }

    @Override
    public void onComplete() {
        // no-op by default.
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}
