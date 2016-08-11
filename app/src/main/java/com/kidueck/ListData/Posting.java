package com.kidueck.ListData;

import android.graphics.drawable.Drawable;

/**
 * Created by system777 on 2016-06-25.
 */
public class Posting {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     * 딱화면에 표시되는거랑 대응되게
     */

    public String content;
    public String date;
    public Drawable deleteButton;
    public String commentCnt;
    public int totalVoteCount;
    public Drawable upButton;
    public Drawable downButton;
    public int isUpDown;
    public boolean isWriter;
    public int postingId;
    public Drawable attachedImg;
    public boolean isImage;
    public int imageCnt;


    public Posting() {
    }

    public Posting(String content, String date, Drawable deleteButton, String commentCnt, int totalVoteCount,
                   Drawable upButton, Drawable downButton, int isUpDown, boolean isWriter, int postingId, Drawable attachedImg,
                   boolean isImage, int imageCnt) {
        this.content = content;
        this.date = date;
        this.deleteButton = deleteButton;
        this.commentCnt = commentCnt;
        this.totalVoteCount = totalVoteCount;
        this.upButton = upButton;
        this.downButton = downButton;
        this.isUpDown = isUpDown;
        this.isWriter = isWriter;
        this.postingId = postingId;
        this.attachedImg = attachedImg;
        this.isImage = isImage;
        this.imageCnt = imageCnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Drawable getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Drawable deleteButton) {
        this.deleteButton = deleteButton;
    }

    public String getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(String commentCnt) {
        this.commentCnt = commentCnt;
    }

    public int getTotalVoteCount() {
        return totalVoteCount;
    }

    public void setTotalVoteCount(int totalVoteCount) {
        this.totalVoteCount = totalVoteCount;
    }

    public Drawable getUpButton() {
        return upButton;
    }

    public void setUpButton(Drawable upButton) {
        this.upButton = upButton;
    }

    public Drawable getDownButton() {
        return downButton;
    }

    public void setDownButton(Drawable downButton) {
        this.downButton = downButton;
    }

    public int getIsUpDown() {
        return isUpDown;
    }

    public void setIsUpDown(int isUpDown) {
        this.isUpDown = isUpDown;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    public int getPostingId() {
        return postingId;
    }

    public void setPostingId(int postingId) {
        this.postingId = postingId;
    }

    public Drawable getAttachedImg() {
        return attachedImg;
    }

    public void setAttachedImg(Drawable attachedImg) {
        this.attachedImg = attachedImg;
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
