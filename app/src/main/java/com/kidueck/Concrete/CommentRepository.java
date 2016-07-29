package com.kidueck.Concrete;

import android.util.Log;

import com.kidueck.Abstract.ICommentRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;
import com.kidueck.Model.CommentListModel;
import com.kidueck.Model.DeepCommentListModel;

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

    //댓글
    private CommentListModel commentListModel;
    private Vector<CommentListModel> vector = new Vector<>();
    org.json.JSONObject Obj;

    //댓글의 댓글
    private DeepCommentListModel deepCommentListModel;
    private Vector<DeepCommentListModel> deepVector = new Vector<>();

    //댓글의 댓글 디테일
    private DeepCommentListModel deepCommentDetail;


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
                        Boolean.parseBoolean(Obj.get("isVisible").toString()),
                        Integer.parseInt(Obj.get("deepCommentCnt").toString())
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

    @Override
    public Vector<DeepCommentListModel> getDeepCommentList(int userId, int commentId, int pageNumber) {
        try {
            url = new URL(URLInfo.Comment_GetDeepCommentList);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("commentId", String.valueOf(commentId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                deepCommentListModel = new DeepCommentListModel(
                        Integer.parseInt(Obj.get("deepCommentId").toString()),
                        Obj.get("writeDate").toString(),
                        Boolean.parseBoolean(Obj.get("isDeepCommenter").toString()),
                        Obj.get("content").toString(),
                        Obj.get("lat").toString(),  Obj.get("lon").toString()
                );

                deepVector.add(deepCommentListModel);

            }

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return deepVector;
    }

    @Override
    public boolean writeDeepComment(int userId, int commentId, String content) {
        try {
            url = new URL(URLInfo.Comment_WriteDeepComment);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("commentId", String.valueOf(commentId));
            prop.setProperty("content", content);
            return Boolean.parseBoolean(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG,  e1.getMessage());

            return false;
        }
    }

    @Override
    public DeepCommentListModel getDeepCommentDetail(int userId, int commentId) {
        try {
            url = new URL(URLInfo.Comment_GetDeepCommentDetail);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("commentId", String.valueOf(commentId));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                deepCommentDetail = new DeepCommentListModel(
                        Integer.parseInt(Obj.get("deepCommentId").toString()),
                        Obj.get("writeDate").toString(),
                        Boolean.parseBoolean(Obj.get("isDeepCommenter").toString()),
                        Obj.get("content").toString(),
                        Obj.get("lat").toString(),  Obj.get("lon").toString()
                );


            }

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return deepCommentDetail;
    }
}
