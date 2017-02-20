
package com.stc.chviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsLatest {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("num")
    @Expose
    private Integer num;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("views")
    @Expose
    private Integer views;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

}
