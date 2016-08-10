package com.kidueck.Model;

/**
 * Created by system777 on 2016-06-25.
 */
public class PostingListModel {
    public String postingId;
    public String content;
    public String writtenDate;
    public boolean isWriter;
    public String commentCnt;
    public int totalVoteCnt;
    public int isUpDown;
    public String lat;
    public String lon;
    public boolean isImage;
    public int imageCnt;

    public PostingListModel(String postingId, String content, String writtenDate, boolean isWriter, String commentCnt, int totalVoteCnt, int isUpDown,
                            String lat, String lon, boolean isImage, int imageCnt) {
        this.postingId = postingId;
        this.content = content;
        this.writtenDate = writtenDate;
        this.isWriter = isWriter;
        this.commentCnt = commentCnt;
        this.totalVoteCnt = totalVoteCnt;
        this.isUpDown = isUpDown;
        this.lat = lat;
        this.lon = lon;
        this.isImage = isImage;
        this.imageCnt = imageCnt;
    }

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWrittenDate() {
        return writtenDate;
    }

    public void setWrittenDate(String writtenDate) {
        this.writtenDate = writtenDate;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    public String getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(String commentCnt) {
        this.commentCnt = commentCnt;
    }

    public int getTotalVoteCnt() {
        return totalVoteCnt;
    }

    public void setTotalVoteCnt(int totalVoteCnt) {
        this.totalVoteCnt = totalVoteCnt;
    }

    public int getIsUpDown() {
        return isUpDown;
    }

    public void setIsUpDown(int isUpDown) {
        this.isUpDown = isUpDown;
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

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public int getImageCnt() {
        return imageCnt;
    }

    public void setImageCnt(int imageCnt) {
        this.imageCnt = imageCnt;
    }
}
