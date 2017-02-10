package com.stc.chviewer.activitythreads.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by artem on 2/9/17.
 */

public class ThreadItemsPlaylist implements Parcelable {
    private String threadId;
    private String threadTitle;
    private int itemsCount;
    private boolean isLoaded;

    public ThreadItemsPlaylist(String threadId, String threadTitle) {
        this.threadId = threadId;
        this.threadTitle = threadTitle;
        itemsCount=0;
        isLoaded=false;
    }

    protected ThreadItemsPlaylist(Parcel in) {
        threadId = in.readString();
        threadTitle = in.readString();
        itemsCount = in.readInt();
        isLoaded = in.readByte() != 0;
    }

    public static final Creator<ThreadItemsPlaylist> CREATOR = new Creator<ThreadItemsPlaylist>() {
        @Override
        public ThreadItemsPlaylist createFromParcel(Parcel in) {
            return new ThreadItemsPlaylist(in);
        }

        @Override
        public ThreadItemsPlaylist[] newArray(int size) {
            return new ThreadItemsPlaylist[size];
        }
    };

    public String getThreadId() {
        return threadId;
    }

    public String getThreadTitle() {
        if(getItemsCount()>0) return threadTitle+"\n playable: "+getItemsCount();
        else return threadTitle;
    }
    public boolean isLoaded(){
        return  isLoaded;
    }
    public int getItemsCount(){
        return  itemsCount;
    }

    public void setLoaded(boolean b){
        isLoaded=b;
    }
    public void setItemsCount(int v){
        itemsCount=v;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(threadId);
        dest.writeString(threadTitle);
        dest.writeInt(itemsCount);
        dest.writeByte((byte) (isLoaded ? 1 : 0));
    }
}
