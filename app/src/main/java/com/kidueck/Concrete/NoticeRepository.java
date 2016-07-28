package com.kidueck.Concrete;

import android.util.Log;

import com.kidueck.Abstract.INoticeRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;
import com.kidueck.Model.NoticeListModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-27.
 */
public class NoticeRepository implements INoticeRepository {
    private static final String TAG = "NoticeRepository";
    private URL url;
    private HttpMessage httpMessage;

    private NoticeListModel noticeListModel;
    private Vector<NoticeListModel> vector = new Vector<>();

    org.json.JSONObject Obj;

    @Override
    public Vector<NoticeListModel> getNoticeList(int userId, int pageNumber) {
        try {
            url = new URL(URLInfo.Notice_GetNoticeList2);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                noticeListModel = new NoticeListModel(
                        Integer.parseInt(Obj.get("noticeLogId").toString()),
                        Boolean.parseBoolean(Obj.get("isRead").toString()),
                        Obj.get("logDate").toString(),
                        Integer.parseInt(Obj.get("logCategory").toString()),
                        Integer.parseInt(Obj.get("targetPostingId").toString())
                );
                vector.add(noticeListModel);
            }

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return vector;
    }

    @Override
    public String getNewNoticeCnt(int userId) {
        String result;

        try {
            url = new URL(URLInfo.Notice_GetNewNoticeCnt);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));

            result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        }

        return result;
    }
}
