package com.stc.chviewer.activitythreads;

import android.util.Log;

import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;
import com.stc.chviewer.activitythreads.model.ThreadsContentHelper;
import com.stc.chviewer.ChRetroHelper;
import com.stc.chviewer.model.Catalog;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class ThreadsPresenter implements ThreadsContract.Presenter {
    private static final String TAG = "ThreadsPresenter";
    private String board;


    private ThreadsContentHelper contentHelper;
    private ChRetroHelper retroHelper;
    private ThreadsContract.View view;

    public ThreadsPresenter(ThreadsContract.View view) {

        this.view=view;
        view.setPresenter(this);
        board=view.getBoard();
        retroHelper= view.getRetroHelper();
        start();
    }

    @Override
    public void start() {
        Log.d(TAG, "start: ");
        retroHelper.getCatalog(board)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Catalog>() {
            @Override
            public void onCompleted() {
                view.showBaseInfo(contentHelper.getThreads(), contentHelper.getThreads()==null);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.getMessage());

            }

            @Override
            public void onNext(Catalog catalog) {
                contentHelper=new ThreadsContentHelper(board, catalog);
                contentHelper.loadContent(retroHelper);
            }
        });
    }

    @Override
    public void requestStartPlayer(String threadId, int itemIndex) {
        if(contentHelper.getDownloadStatus()>0){
            view.startPlayer(board, contentHelper.getThread(threadId), contentHelper.getAllPlayableItems(), itemIndex);
        }else {
            view.showError("Items not added yet");
        }
    }

    @Override
    public void requestGetThreadContent(String threadId) {
        if(contentHelper.getDownloadStatus()!=1) {
            view.showError("Not ready yet");
            return;
        }
        PlayableItem[] threadItems = contentHelper.getThreadPlayableItems(threadId);
        if (threadItems == null) {
            view.showError("Items not added yet");
        } else if (threadItems.length == 0) {
            view.showError("No playable items in this thread");
        } else {
            view.loadThreadContent(contentHelper.getThread(threadId), threadItems);
        }
    }

    @Override
    public void requestSearchThreadContent(String query) {
        List<ThreadItemsPlaylist> matched = new ArrayList<>();
        for(ThreadItemsPlaylist threadItemsPlaylist : contentHelper.getThreads()){
            if(threadItemsPlaylist.getThreadTitle().contains(query)) matched.add(threadItemsPlaylist);
        }
        if(matched.size()>0) view.showBaseInfo(matched, false);
        else view.showError("No matches found");
    }
}
