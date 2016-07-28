package com.kidueck.Concrete;

import android.util.Log;

import com.kidueck.Abstract.ISettingRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by system777 on 2016-06-29.
 */
public class SettingRepository implements ISettingRepository {

    private static final String TAG = "SettingRepository";
    private URL url;
    private HttpMessage httpMessage;

    @Override
    public String getLatestVersionName() {

        String result;

        try {
            url = new URL(URLInfo.Setting_GetLatestVersionName);
            httpMessage = new HttpMessage(url);

            result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage());
            return result;

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        }
    }

    @Override
    public boolean submitInquiry(String content, int userId) {
        try {
            url = new URL(URLInfo.Setting_WriteInquiry);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("content", content);
            prop.setProperty("userId", String.valueOf(userId));
            return Boolean.parseBoolean(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return false;
        }
    }
}
