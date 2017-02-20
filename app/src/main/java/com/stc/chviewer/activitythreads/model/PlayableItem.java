package com.stc.chviewer.activitythreads.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.stc.chviewer.model.File;

import static com.stc.chviewer.Constants.BASE_URL_2CH;

/**
 * Created by artem on 2/9/17.
 */

public class PlayableItem implements Parcelable{
    private int itemIndex;
    private String thumbUrl;
    private String webmUrl;

    public PlayableItem(int index ,File file) {
        itemIndex=index;
        webmUrl=BASE_URL_2CH + file.getPath();
        thumbUrl=BASE_URL_2CH + file.getThumbnail();
    }

    protected PlayableItem(Parcel in) {
        itemIndex = in.readInt();
        thumbUrl = in.readString();
        webmUrl = in.readString();
    }

    public static final Creator<PlayableItem> CREATOR = new Creator<PlayableItem>() {
        @Override
        public PlayableItem createFromParcel(Parcel in) {
            return new PlayableItem(in);
        }

        @Override
        public PlayableItem[] newArray(int size) {
            return new PlayableItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemIndex);
        dest.writeString(thumbUrl);
        dest.writeString(webmUrl);
    }
}
