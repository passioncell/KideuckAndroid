package com.kidueck.Model;

/**
 * Created by system777 on 2016-06-27.
 */
public class CommentListModel {
    public int commentId;
    public String writeDate;
    public boolean isCommenter;
    public String content;
    public String lat;
    public String lon;
    public boolean isVisible;
    public int deepCommentCnt;

    public CommentListModel(){

    }

    public CommentListModel(int commentId, String writeDate, boolean isCommenter, String content, String lat, String lon, boolean isVisible,
                            int deepCommentCnt) {
        this.commentId = commentId;
        this.writeDate = writeDate;
        this.isCommenter = isCommenter;
        this.content = content;
        this.lat = lat;
        this.lon = lon;
        this.isVisible = isVisible;
        this.deepCommentCnt = deepCommentCnt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public boolean isCommenter() {
        return isCommenter;
    }

    public void setIsCommenter(boolean isCommenter) {
        this.isCommenter = isCommenter;
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getDeepCommentCnt() {
        return deepCommentCnt;
    }

    public void setDeepCommentCnt(int deepCommentCnt) {
        this.deepCommentCnt = deepCommentCnt;
    }
}
