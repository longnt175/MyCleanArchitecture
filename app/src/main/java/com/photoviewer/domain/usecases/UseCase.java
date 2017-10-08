package com.photoviewer.domain.usecases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public abstract class UseCase<T> {

    private final Scheduler threadExecutor;
    private final Scheduler postExecutionThread;

    protected Disposable disposable = Disposables.empty();

    protected UseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> call();

    public <S extends Observer<T> & Disposable> void execute(S useCaseDisposable) {
        this.disposable = this.call()
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribeWith(useCaseDisposable);
    }

    public void unsubscribe() {
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }

    public boolean isUnsubscribed() {
        return this.disposable.isDisposed();
    }

}
