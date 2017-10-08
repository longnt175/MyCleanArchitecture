package com.photoviewer.data.repository;

import com.photoviewer.data.entity.PhotoEntity;
import com.photoviewer.data.repository.datastore.DatabasePhotoEntityStore;
import com.photoviewer.data.repository.datastore.ServerPhotoEntityStore;
import com.photoviewer.domain.repository.PhotoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class PhotoEntityDataSource implements PhotoRepository {

    private DatabasePhotoEntityStore mDatabasePhotoEntityStore;
    private ServerPhotoEntityStore mServerPhotoEntityStore;

    @Inject
    public PhotoEntityDataSource(DatabasePhotoEntityStore databasePhotoEntityStore, ServerPhotoEntityStore serverPhotoEntityStore) {
        mDatabasePhotoEntityStore = databasePhotoEntityStore;
        mServerPhotoEntityStore = serverPhotoEntityStore;
    }

    @Override
    public Observable<List<PhotoEntity>> photos() {
        return Observable.create(e -> queryDatabaseForAll(e));
    }

    @Override
    public Observable<List<PhotoEntity>> searchPhotosByTitle(String title) {
        return mDatabasePhotoEntityStore.queryForTitle(title);
    }

    @Override
    public Observable<PhotoEntity> photo(int photoId) {
        return mDatabasePhotoEntityStore.queryForId(photoId);
    }

    private void queryDatabaseForAll(final ObservableEmitter<? super List<PhotoEntity>> subscriber) {
        mDatabasePhotoEntityStore.queryForAll().subscribe(new Observer<List<PhotoEntity>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<PhotoEntity> photoEntities) {
                if (photoEntities.size() != 0) {
                    subscriber.onNext(photoEntities);
                    subscriber.onComplete();
                } else {
                    fetchServerForAll(subscriber);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                subscriber.onError(e);
                fetchServerForAll(subscriber);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void fetchServerForAll(ObservableEmitter<? super List<PhotoEntity>> subscriber) {
        mServerPhotoEntityStore.photoEntityList().subscribe(new Observer<List<PhotoEntity>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<PhotoEntity> photoEntities) {
                subscriber.onNext(photoEntities);
                mDatabasePhotoEntityStore.
                        saveAll(photoEntities).
                        subscribe();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        });
    }
}
