package com.stc.chviewer.activitythreads.model;

import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;
import com.stc.chviewer.retro.model.ChThread;
import com.stc.chviewer.retro.model.ChThreadContent;
import com.stc.chviewer.retro.model.File;
import com.stc.chviewer.retro.model.Post;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by artem on 2/9/17.
 */

public class ThreadsContentHelper {
    private static final String TAG = "ThreadsContentHelper";
    private List<PlayableItem> items;
    private List<ThreadItemsPlaylist> threads;
    private String board;
    private int downloadStatus=0;

    public ThreadsContentHelper(String board, Catalog catalog) {
        this.board = board;
        threads=new ArrayList<>();
        items=new ArrayList<>();
        initThreads(catalog);

    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    private void initThreads(Catalog catalog) {
        for(ChThread thread : catalog.getThreads()){
            threads.add(new ThreadItemsPlaylist(thread.getNum(), thread.getSubject()+"\nposts: "+thread.getPostsCount()+ "\n"+thread.getDate()));
        }
    }
    private int addItemsToThread(ChThreadContent threadContent){
        int i=0;
        for(Post post: threadContent.getThreads().get(0).getPosts()){
            if(post.getFiles()!=null && post.getFiles().size()!=0){
                for(File file : post.getFiles()){
                    if(file.getType()==6){
                        PlayableItem item = new PlayableItem(items.size(),file);
                        items.add(item);
                        i++;

                    }
                }
            }
        }
        return i;
    }

    public void loadContent(ChRetroHelper retroHelper){
        for(ThreadItemsPlaylist pls : threads){
            retroHelper.getPosts(board, pls.getThreadId()).subscribe(new Observer<ChThreadContent>() {
                @Override
                public void onCompleted() {
                    downloadStatus=1;
                }

                @Override
                public void onError(Throwable e) {
                    downloadStatus=-1;
                }

                @Override
                public void onNext(ChThreadContent chThreadContent) {
                    pls.setItemsCount(addItemsToThread(chThreadContent));
                    pls.setLoaded(true);
                }
            });
        }
    }

    public List<ThreadItemsPlaylist> getThreads(){
        return threads;
    }

    public PlayableItem[] getThreadPlayableItems(String threadId){
        if(!getThread(threadId).isLoaded()) return null;
        if(items==null || items.size()==0)return  null;
        List<PlayableItem> threadItems=new ArrayList<>();
        for(PlayableItem item : items){
            if(item!=null && threadId.equals(item.getThreadId())) threadItems.add(item);
        }
        return threadItems.toArray(new PlayableItem[threadItems.size()]);
    }
    public PlayableItem[] getAllPlayableItems(){
        if(items==null)return  null;
        return items.toArray(new PlayableItem[items.size()]);
    }
    public ThreadItemsPlaylist getThread(String threadId){
        for(ThreadItemsPlaylist threadItemsPlaylist : threads){
            if(threadId.equals(threadItemsPlaylist.getThreadId()))return threadItemsPlaylist;
        }
        return null;
    }


}
