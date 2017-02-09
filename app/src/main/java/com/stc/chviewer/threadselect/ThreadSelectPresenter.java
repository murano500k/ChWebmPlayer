/*
package com.stc.chviewer.threadselect;

import android.util.Log;

import com.stc.chviewer.Constants;
import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;
import com.stc.chviewer.retro.model.File;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

*/
/**
 * Created by artem on 2/8/17.
 *//*


public class ThreadSelectPresenter implements ThreadSelectContract.Presenter {
    ThreadSelectContract.View mThreadSelectView;
    ChRetroHelper retroHelper;
    private String board;
    private static final String TAG = "ThreadSelectPresenter";


    public ThreadSelectPresenter(ThreadSelectContract.View mThreadSelectView) {
        this.mThreadSelectView = mThreadSelectView;
        mThreadSelectView.setPresenter(this);


    }


    @Override
    public void start() {
        if (mThreadSelectView == null) return;
        board = mThreadSelectView.getBoard();
        if (board == null) {
            mThreadSelectView.showThreads(null, true);
            return;
        }
        retroHelper = new ChRetroHelper();
    }
    public void getThreads2(String board){
        Observable<Catalog> call = retroHelper.getCatalog(board);

        Subscription subscription = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Catalog>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e(TAG, "onError: ",e );
                        }
                        mThreadSelectView.showThreads(null, false);

                    }

                    @Override
                    public void onNext(Catalog catalog) {
                        Log.d(TAG, "onCompleted: "+catalog);

                        mThreadSelectView.showThreads(catalog.getThreads(), false);
                    }

                });
    }
    public void getUris(long threadId){
        retroHelper.getFiles(board,threadId).filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                if(file.getType()==6)return true;
                else return false;
            }
        }).flatMap(new Func1<File, Observable<String>>() {
            @Override
            public Observable<String> call(File file) {
                return Observable.just(Constants.BASE_URL_2CH+ file.getPath());
            }
        }).toList().subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mThreadSelectView.showError(e.getMessage());

            }

            @Override
            public void onNext(List<String>uris) {
                if(uris==null || uris.size()==0)                 mThreadSelectView.showError("null");
                else {

                    String[] extL = new String[uris.size()];
                    String[] uriL = new String[uris.size()];
                    int i = 0;
                    for(String uri: uris){
                        extL[i]="webm";
                        uriL[i]=uri;
                        i++;

                        mThreadSelectView.startPlayer(board,String.valueOf(threadId) ,extL,uriL );
                    }
                }
            }
        });
    }
}
*/
