package com.kidueck.Concrete;

import android.util.Log;

import com.kidueck.Abstract.IMemberRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;
import com.kidueck.Model.JoinModel;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by system777 on 2016-06-25.
 */
public class MemberRepository implements IMemberRepository {
    private static final String TAG = "MemberRepository";
    private URL url;
    private HttpMessage httpMessage;

    @Override
    public int checkJoin(String userSerial) {

        try {
            url = new URL(URLInfo.Member_CheckJoin);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userSerial", userSerial);
            return Integer.parseInt(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return -1;
        }
    }

    @Override
    public String join(JoinModel joinModel) {
        try {
            url = new URL(URLInfo.Member_Join);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("id", joinModel.getId());
            prop.setProperty("password", joinModel.getPassword());
            prop.setProperty("joinRoot", joinModel.getJoinRoot());
            prop.setProperty("sex", String.valueOf(joinModel.getSex()));
            prop.setProperty("birth", joinModel.getBirth());

            return HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return null;
        }
    }

    @Override
    public String login(String id, String pw) {
        try {
            url = new URL(URLInfo.Member_Login);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("id", id);
            prop.setProperty("pw", pw);

            return HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return null;
        }
    }

    @Override
    public String getUserPrimaryKey(String id) {
        try {
            url = new URL(URLInfo.Member_GetUserPrimaryKey);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("id", id);

            return HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return null;
        }
    }

    @Override
    public int getUserPoint(int userId) {
        try {
            url = new URL(URLInfo.Member_GetUserPoint);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));

            return Integer.parseInt(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return 0;
        }
    }
}
