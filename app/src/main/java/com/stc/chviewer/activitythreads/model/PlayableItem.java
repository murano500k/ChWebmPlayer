package com.stc.chviewer.activitythreads.model;


import com.stc.chviewer.retro.model.File;

import static com.stc.chviewer.Constants.BASE_URL_2CH;

/**
 * Created by artem on 2/9/17.
 */

public class PlayableItem {
    private int itemIndex;
    private String thumbUrl;
    private String webmUrl;

    public PlayableItem(int index ,File file) {
        itemIndex=index;
        webmUrl=BASE_URL_2CH + file.getPath();
        thumbUrl=BASE_URL_2CH + file.getThumbnail();
    }
    public String getThreadId(){
        String temp=webmUrl.substring(webmUrl.indexOf("src/")+4);
        return temp.substring(0, temp.indexOf("/"));
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getWebmUrl() {
        return webmUrl;
    }
}
