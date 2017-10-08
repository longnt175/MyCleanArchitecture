package com.photoviewer.data.repository.datastore;

import android.content.Context;

import com.photoviewer.data.entity.PhotoStatisticsEntity;
import com.photoviewer.data.preferences.orm.PreferencesDao;

import javax.inject.Inject;

import io.reactivex.Observable;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class PreferencesPhotoStatisticsEntityStore {

    @Setter
    private PreferencesDao<PhotoStatisticsEntity> mStatisticsDao;

    @Inject
    public PreferencesPhotoStatisticsEntityStore(Context context) {
        setStatisticsDao(new PreferencesDao<>(PhotoStatisticsEntity.class, context));
    }

    public Observable<PhotoStatisticsEntity> readStatistics() {
        return Observable.create(photoStatisticsEntityObservableEmitter -> {
            try {
                PhotoStatisticsEntity statistics = mStatisticsDao.read();
                photoStatisticsEntityObservableEmitter.onNext(statistics);
                photoStatisticsEntityObservableEmitter.onComplete();
            } catch (Throwable e) {
                photoStatisticsEntityObservableEmitter.onError(e);
            }
        });
    }

    public Observable<PhotoStatisticsEntity> updateStatistics(PhotoStatisticsEntity photoStatisticsEntity) {
        return Observable.create(photoStatisticsEntityObservableEmitter -> {
            try {
                PhotoStatisticsEntity oldStatistics = mStatisticsDao.read();
                mStatisticsDao.save(photoStatisticsEntity);
                photoStatisticsEntityObservableEmitter.onNext(oldStatistics);
                photoStatisticsEntityObservableEmitter.onComplete();
            } catch (Throwable e) {
                photoStatisticsEntityObservableEmitter.onError(e);
            }
        });
    }
}
