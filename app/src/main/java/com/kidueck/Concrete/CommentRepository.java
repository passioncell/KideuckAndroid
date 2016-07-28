package com.kidueck.Concrete;

import android.util.Log;

import com.kidueck.Abstract.ICommentRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;
import com.kidueck.Model.CommentListModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-27.
 */
public class CommentRepository implements ICommentRepository {

    private static final String TAG = "CommentRepository";
    private URL url;
    private HttpMessage httpMessage;

    private CommentListModel commentListModel;
    private Vector<CommentListModel> vector = new Vector<>();

    org.json.JSONObject Obj;

    @Override
    public Vector<CommentListModel> getCommentList(int userId, int postingId, int pageNumber) {
        try {
            url = new URL(URLInfo.Comment_GetCommentList);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("postingId", String.valueOf(postingId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                commentListModel = new CommentListModel(
                        Integer.parseInt(Obj.get("commentId").toString()),
                        Obj.get("writeDate").toString(),
                        Boolean.parseBoolean(Obj.get("isCommenter").toString()),
                        Obj.get("content").toString(),
                        Obj.get("lat").toString(),  Obj.get("lon").toString(),
                        Boolean.parseBoolean(Obj.get("isVisible").toString())
                );

                vector.add(commentListModel);

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
    public boolean writeComment(int userId, int postingId, String content) {
        try {
            url = new URL(URLInfo.Comment_WriteComment2);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("postingId", String.valueOf(postingId));
            prop.setProperty("content", content);
            return Boolean.parseBoolean(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return false;
        }
    }
}
