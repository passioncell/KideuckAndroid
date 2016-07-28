package com.kidueck.Abstract;

/**
 * Created by system777 on 2016-06-29.
 */
public interface ISettingRepository {
    String getLatestVersionName();
    boolean submitInquiry(String content, int userId);
}
