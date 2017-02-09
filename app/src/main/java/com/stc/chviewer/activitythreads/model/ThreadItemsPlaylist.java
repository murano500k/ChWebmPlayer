package com.stc.chviewer.activitythreads.model;

/**
 * Created by artem on 2/9/17.
 */

public class ThreadItemsPlaylist {
    private String threadId;
    private String threadTitle;
    private boolean hasItems;
    private boolean isLoaded;

    public ThreadItemsPlaylist(String threadId, String threadTitle) {
        this.threadId = threadId;
        this.threadTitle = threadTitle;
        hasItems=false;
        isLoaded=false;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getThreadTitle() {
        return threadTitle;
    }
    public boolean isLoaded(){
        return  isLoaded;
    }
    public boolean hasItems(){
        return  hasItems;
    }

    public void setLoaded(boolean b){
        isLoaded=b;
    }
public void setHasItems(boolean b){
    hasItems=b;
    }


}
