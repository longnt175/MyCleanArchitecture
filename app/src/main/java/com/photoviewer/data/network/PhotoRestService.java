package com.photoviewer.data.network;

import com.photoviewer.data.entity.PhotoEntity;

import java.util.List;

import retrofit2.http.GET;
import io.reactivex.Observable;

/**
 * RestApi for retrieving data from the network.
 */
public interface PhotoRestService {

    /**
     * Retrieves an {@link io.reactivex.Observable} which will emit a List of {@link PhotoEntity}.
     */
    @GET("/photos")
    Observable<List<PhotoEntity>> photoEntityList();
}
