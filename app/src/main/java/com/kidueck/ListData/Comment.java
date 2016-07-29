package com.kidueck.ListData;

/**
 * Created by system777 on 2016-06-27.
 */
public class Comment {
    public int commentId;
    public String commentContent;
    public String writtenDate;
    public boolean isCommenter;
    public String lat;
    public String lon;
    public boolean isVisible;
    public int deepCommentcnt;

    public Comment(){

    }

    public Comment(int commentId, String commentContent, String writtenDate, boolean isCommenter, String lat, String lon, boolean isVisible,
                   int deepCommentcnt) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.writtenDate = writtenDate;
        this.isCommenter = isCommenter;
        this.lat = lat;
        this.lon = lon;
        this.isVisible = isVisible;
        this.deepCommentcnt = deepCommentcnt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getWrittenDate() {
        return writtenDate;
    }

    public void setWrittenDate(String writtenDate) {
        this.writtenDate = writtenDate;
    }

    public boolean isCommenter() {
        return isCommenter;
    }

    public void setIsCommenter(boolean isCommenter) {
        this.isCommenter = isCommenter;
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

    public int getDeepCommentcnt() {
        return deepCommentcnt;
    }

    public void setDeepCommentcnt(int deepCommentcnt) {
        this.deepCommentcnt = deepCommentcnt;
    }
}
