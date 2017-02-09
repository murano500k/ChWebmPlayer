package com.stc.chviewer.activitythreads.model;

import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;
import com.stc.chviewer.retro.model.ChThread;
import com.stc.chviewer.retro.model.ChThreadContent;
import com.stc.chviewer.retro.model.File;
import com.stc.chviewer.retro.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * Created by artem on 2/9/17.
 */

public class ThreadsContentHelper {
    private HashMap<String , List<PlayableItem>> itemsMap;
    private List<ThreadItemsPlaylist> threads;
    private String board;
    private int downloadStatus=0;

    public ThreadsContentHelper(String board, Catalog catalog) {
        this.board = board;
        threads=new ArrayList<>();
        itemsMap=new HashMap<>();
        initThreads(catalog);

    }

    private void initThreads(Catalog catalog) {
        for(ChThread thread : catalog.getThreads()){
            threads.add(new ThreadItemsPlaylist(thread.getNum(), thread.getSubject()+"\nposts: "+thread.getPostsCount()+ "\n"+thread.getDate()));
        }
    }
    private boolean addItemsToThread(ChThreadContent threadContent){
        int i=0;
        List<PlayableItem> items=new ArrayList<>();
        for(Post post: threadContent.getThreads().get(0).getPosts()){
            if(post.getFiles()!=null && post.getFiles().size()!=0){
                for(File file : post.getFiles()){
                    if(file.getType()==6){
                        PlayableItem item = new PlayableItem(i,file);
                        i++;
                        items.add(item);
                    }
                }
            }
        }
        if(items.size()>0 ) {
            itemsMap.put(items.get(0).getThreadId(), items);
            return true;
        }else return false;
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
                    pls.setHasItems(addItemsToThread(chThreadContent));
                    pls.setLoaded(true);
                }
            });
        }
    }
    public List<ThreadItemsPlaylist> getThreadsInfos(){
        return threads;
    }
    public List<PlayableItem> getPlaylistItems(String threadId){
        return itemsMap.get(threadId);
    }

    public String[] getUris(String threadId){
        if(itemsMap!=null || itemsMap.get(threadId)!=null || itemsMap.get(threadId).isEmpty()) {
            return null;
        }
        String[] uris = new String[itemsMap.get(threadId).size()];
        int i = 0;
        for(PlayableItem item : itemsMap.get(threadId)){
            uris[item.getItemIndex()] = item.getWebmUrl();
        }
        return uris;
    }


}
