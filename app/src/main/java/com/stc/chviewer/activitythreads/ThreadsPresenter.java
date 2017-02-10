package com.stc.chviewer.activitythreads;

import android.util.Log;

import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadsContentHelper;
import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;

import rx.Observer;

import static android.content.ContentValues.TAG;

public class ThreadsPresenter implements ThreadsContract.Presenter {
    private String board;
    private ThreadsContentHelper contentHelper;
    private ChRetroHelper retroHelper;
    private ThreadsContract.View view;

    public ThreadsPresenter(ThreadsContract.View view) {
        this.view=view;
        view.setPresenter(this);
        board=view.getBoard();
        retroHelper= new ChRetroHelper();
    }

    @Override
    public void start() {
        Log.d(TAG, "start: ");
        retroHelper.getCatalog(board).subscribe(new Observer<Catalog>() {
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
    public void requestThreadContent(String threadId) {
        PlayableItem[] threadItems = contentHelper.getThreadPlayableItems(threadId);
        if (threadItems == null) {
            view.showError("Items not added yet");
        } else if (threadItems.length == 0) {
            view.showError("No playable items in this thread");
        } else {
            view.loadThreadContent(contentHelper.getThread(threadId), threadItems);
        }
    }
}
