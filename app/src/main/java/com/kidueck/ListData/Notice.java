package com.kidueck.ListData;

import android.graphics.drawable.Drawable;

/**
 * Created by system777 on 2016-06-27.
 */
public class Notice {
    public int noticeLogId;
    public String logDate;
    public int logCategory;
    public boolean isRead;
    public Drawable logImage;
    public int targetPostingId;

    public Notice(){

    }

    public Notice(int noticeLogId, String logDate, int logCategory, boolean isRead,
                  Drawable logImage, int targetPostingId) {
        this.noticeLogId = noticeLogId;
        this.logDate = logDate;
        this.logCategory = logCategory;
        this.isRead = isRead;
        this.logImage = logImage;
        this.targetPostingId = targetPostingId;
    }

    public int getNoticeLogId() {
        return noticeLogId;
    }

    public void setNoticeLogId(int noticeLogId) {
        this.noticeLogId = noticeLogId;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public int getLogCategory() {
        return logCategory;
    }

    public void setLogCategory(int logCategory) {
        this.logCategory = logCategory;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Drawable getLogImage() {
        return logImage;
    }

    public void setLogImage(Drawable logImage) {
        this.logImage = logImage;
    }

    public int getTargetPostingId() {
        return targetPostingId;
    }

    public void setTargetPostingId(int targetPostingId) {
        this.targetPostingId = targetPostingId;
    }
}
