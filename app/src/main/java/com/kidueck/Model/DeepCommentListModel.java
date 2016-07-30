package com.kidueck.Model;

/**
 * Created by system777 on 2016-07-29.
 */
public class DeepCommentListModel {
    public int deepCommentId;
    public String writeDate;
    public boolean isDeepCommenter;
    public String content;
    public String lat;
    public String lon;

    public DeepCommentListModel(int deepCommentId, String writeDate, boolean isDeepCommenter, String content, String lat, String lon) {
        this.deepCommentId = deepCommentId;
        this.writeDate = writeDate;
        this.isDeepCommenter = isDeepCommenter;
        this.content = content;
        this.lat = lat;
        this.lon = lon;
    }

    public int getDeepCommentId() {
        return deepCommentId;
    }

    public void setDeepCommentId(int deepCommentId) {
        this.deepCommentId = deepCommentId;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public boolean isDeepCommenter() {
        return isDeepCommenter;
    }

    public void setIsDeepCommenter(boolean isDeepCommenter) {
        this.isDeepCommenter = isDeepCommenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


}
