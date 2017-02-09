
package com.stc.chviewer.retro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Top {

    @SerializedName("board")
    @Expose
    private String board;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("name")
    @Expose
    private String name;

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
