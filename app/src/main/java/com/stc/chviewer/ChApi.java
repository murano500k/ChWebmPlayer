package com.stc.chviewer;


import com.stc.chviewer.model.Catalog;
import com.stc.chviewer.model.ChThreadContent;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by artem on 2/8/17.
 */

public interface ChApi {
    @GET("/{board}/catalog.json")
    Observable<Catalog> getThreads(@Path("board") String board);
    @GET("/{board}/res/{threadId}.json")
    Observable<ChThreadContent> getThreadContent(@Path("board") String board, @Path("threadId") String threadId);



}

