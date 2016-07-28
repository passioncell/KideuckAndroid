package com.kidueck.Abstract;

import com.kidueck.Model.NoticeListModel;

import java.util.Vector;

/**
 * Created by system777 on 2016-06-27.
 */
public interface INoticeRepository {
    Vector<NoticeListModel> getNoticeList(int userId, int pageNumber);
    String getNewNoticeCnt(int userId);
}
