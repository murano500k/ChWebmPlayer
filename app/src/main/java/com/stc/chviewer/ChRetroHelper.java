package com.stc.chviewer;

import android.util.Log;

import com.stc.chviewer.model.Catalog;
import com.stc.chviewer.model.ChThread;
import com.stc.chviewer.model.ChThreadContent;
import com.stc.chviewer.model.File;
import com.stc.chviewer.model.Post;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;
import static com.stc.chviewer.Constants.BASE_URL_2CH;

/**
 * Created by artem on 2/8/17.
 */

public class ChRetroHelper {

    private ChApi api;
    public ChRetroHelper(Retrofit retrofit) {
        this.api=retrofit.create(ChApi.class);
    }
    public Observable<Catalog> getCatalog(String boardName){
        Log.d(TAG, "getThreads: "+BASE_URL_2CH+boardName);
        return api.getThreads(boardName);
    }
    public Observable<List<ChThread>> getThreads(String board){
        return getCatalog(board).flatMap(catalog -> {
            return Observable.just(catalog.getThreads());
        });
    }
    public Observable<String> getNextThreadId(String board, String threadId){
        if (threadId == null) {
            return null;
        }
        return getThreads(board).flatMap(chThreads -> {
            for(ChThread thread: chThreads){
                if(threadId.equals(thread.getNum())) return Observable.just(thread.getNum()) ;
            }
            return Observable.empty();
        });
    }
    public Observable<ChThread> getThreadWithAllPosts(String board, String threadId) {
        if (threadId == null) {
            return Observable.empty();
        }
        return getThreads(board).flatMap(chThreads -> {
            for(ChThread thread: chThreads){
                if(threadId.equals(thread.getNum())) return Observable.just(thread) ;
            }
            return Observable.empty();
        });
    }

    public Observable<ChThreadContent> getThreadContent(String boardName, String threadId){
        Log.d(TAG, "getPosts: "+BASE_URL_2CH+boardName);
        Log.d(TAG, "getPosts: "+threadId);
        return api.getThreadContent(boardName, threadId);
    }





        public Observable<ChThreadContent> getPosts(String boardName, String threadId){
        Log.d(TAG, "getPosts: "+BASE_URL_2CH+boardName);
        Log.d(TAG, "getPosts: "+threadId);
        return api.getThreadContent(boardName, threadId);
    }
    public Observable<File> getFiles(String board , String threadId){
        Observable<ChThreadContent> catalog = api.getThreadContent(board, threadId);
        return catalog.flatMapIterable(chThreadContent -> {
            List<File> files =new ArrayList<>();
            for(Post post : chThreadContent.getThreads().get(0).getPosts()){
                if (post.getFiles() != null) {
                    files.addAll(post.getFiles());
                }
            }

            return files;
        });
    }

}
