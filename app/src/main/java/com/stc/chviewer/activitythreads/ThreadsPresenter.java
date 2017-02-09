package com.stc.chviewer.activitythreads;

import android.util.Log;

import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;
import com.stc.chviewer.activitythreads.model.ThreadsContentHelper;
import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;

import rx.Observer;

import static android.content.ContentValues.TAG;

public class ThreadsPresenter implements ThreadsContract.Presenter {
    String board;
    ThreadsContentHelper contentHelper;
    ChRetroHelper retroHelper;
    ThreadsContract.View view;

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
                view.showBaseInfo(contentHelper.getThreadsInfos(), contentHelper.getThreadsInfos()!=null);
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
        String threadText=null;
        String[] uris=null;
        for(ThreadItemsPlaylist pls : contentHelper.getThreadsInfos()){
            if(pls.getThreadId().equals(threadId)) {
                threadText=pls.getThreadTitle();
                if(!pls.isLoaded()) {
                    view.showError("thread "+threadText+"is not ready yet");
                    return;
                }
                if(!pls.hasItems()){
                    view.showError("thread "+threadText+" don't have playable items");
                    return;
                }
                uris=contentHelper.getUris(threadId);
                String[] extentions=new String[uris.length];
                for (int i = 0; i <uris.length; i++) {
                    extentions[i]="webm";
                }
                view.startPlayer(board, threadId, threadText, uris, extentions);
            }
        }

    }

    @Override
    public void requestThreadContent(String threadId) {
        String threadText=null;
        String[] uris=null;
        for(ThreadItemsPlaylist pls : contentHelper.getThreadsInfos()){
            if(pls.getThreadId().equals(threadId)) {
                threadText=pls.getThreadTitle();
                if(!pls.isLoaded()) {
                    view.showError("thread "+threadText+"is not ready yet");
                    return;
                }
                if(!pls.hasItems()){
                    view.showError("thread "+threadText+" don't have playable items");
                    return;
                }
                view.loadThreadContent(pls , contentHelper.getPlaylistItems(threadId));
            }
        }
    }
}
