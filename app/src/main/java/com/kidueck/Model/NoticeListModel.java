package com.kidueck.Model;

/**
 * Created by system777 on 2016-06-27.
 */
public class NoticeListModel {
    public int noticeLogId;
    public boolean isRead;
    public String logDate;
    public int logCategory;
    public int targetPostingId;

    public NoticeListModel(int noticeLogId, boolean isRead, String logDate, int logCategory, int targetPostingId) {
        this.noticeLogId = noticeLogId;
        this.isRead = isRead;
        this.logDate = logDate;
        this.logCategory = logCategory;
        this.targetPostingId = targetPostingId;
    }

    public int getNoticeLogId() {
        return noticeLogId;
    }

    public void setNoticeLogId(int noticeLogId) {
        this.noticeLogId = noticeLogId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
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

    public int getTargetPostingId() {
        return targetPostingId;
    }

    public void setTargetPostingId(int targetPostingId) {
        this.targetPostingId = targetPostingId;
    }
}
