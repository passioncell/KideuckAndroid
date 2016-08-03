package com.kidueck.Concrete;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.kidueck.Abstract.IPostingRepository;
import com.kidueck.Common.HttpMessage;
import com.kidueck.Common.URLInfo;
import com.kidueck.Model.DetailPostingModel;
import com.kidueck.Model.PostingListModel;
import com.kidueck.Util.FileUpload;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-25.
 */
public class PostingRepository implements IPostingRepository {

    private static final String TAG = "PostingRepository";
    private URL url;
    private HttpMessage httpMessage;

    private PostingListModel postingListModel;
    private Vector<PostingListModel> vector = new Vector<>();

    private DetailPostingModel detailPostingModel;
    private Vector<DetailPostingModel> detailVector = new Vector<>();

    org.json.JSONObject Obj;

    //upload
    private static Context mContext;
    private static Activity mActivity;
    private FileUpload fileUpload;
    private File file;
    public static Uri mImageCaptureUri;
    public static List<Uri> mImageCaptureUris;
    private static Map<String, String> mParameters = new HashMap<String, String>();

    public PostingRepository() {

    }

    public PostingRepository(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    @Override
    public Vector<PostingListModel> getPostingList(int userId, int pageNumber) {
        try {
            url = new URL(URLInfo.Posting_GetPostingList);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                postingListModel = new PostingListModel(
                        Obj.get("postingId").toString(), Obj.get("content").toString(),
                        Obj.get("writtenDate").toString(), Boolean.parseBoolean(Obj.get("isWriter").toString()),
                        Obj.get("commentCnt").toString(), Integer.parseInt(Obj.get("totalVoteCnt").toString()),
                        Integer.parseInt(Obj.get("isUpDown").toString()),Obj.get("lat").toString(),Obj.get("lon").toString(),
                        Boolean.parseBoolean(Obj.get("isImage").toString())
                );
                vector.add(postingListModel);
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
    public Vector<PostingListModel> getHotPostingList(int userId, int pageNumber) {
        try {
            url = new URL(URLInfo.Posting_GetHotPostingList);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                postingListModel = new PostingListModel(
                        Obj.get("postingId").toString(), Obj.get("content").toString(),
                        Obj.get("writtenDate").toString(), Boolean.parseBoolean(Obj.get("isWriter").toString()),
                        Obj.get("commentCnt").toString(), Integer.parseInt(Obj.get("totalVoteCnt").toString()),
                        Integer.parseInt(Obj.get("isUpDown").toString()),Obj.get("lat").toString(),Obj.get("lon").toString(),
                        Boolean.parseBoolean(Obj.get("isImage").toString()));
                vector.add(postingListModel);
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
    public Vector<PostingListModel> getMyPostingList(int userId, int pageNumber) {
        try {
            url = new URL(URLInfo.Posting_GetMyPostingList);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("pageNumber", String.valueOf(pageNumber));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                postingListModel = new PostingListModel(
                        Obj.get("postingId").toString(), Obj.get("content").toString(),
                        Obj.get("writtenDate").toString(), Boolean.parseBoolean(Obj.get("isWriter").toString()),
                        Obj.get("commentCnt").toString(), Integer.parseInt(Obj.get("totalVoteCnt").toString()),
                        Integer.parseInt(Obj.get("isUpDown").toString()),Obj.get("lat").toString(),Obj.get("lon").toString(),
                        Boolean.parseBoolean(Obj.get("isImage").toString()));
                vector.add(postingListModel);
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
    public boolean votePosting(int userId, int postingId, int type) {
        try {
            url = new URL(URLInfo.Posting_VotePosting2);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("postingId", String.valueOf(postingId));
            prop.setProperty("type", String.valueOf(type));
            return Boolean.parseBoolean(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));


        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return false;
        }
    }

    @Override
    public Vector<DetailPostingModel> getDetailPosting(int postingId, int userId) {
        try {
            url = new URL(URLInfo.Posting_GetDetailPosting);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("postingId", String.valueOf(postingId));
            prop.setProperty("userId", String.valueOf(userId));

            String result = HttpMessage.convertStreamToString(httpMessage.sendGetMessage(prop));

            JSONArray array = new JSONArray(result);

            for(int i = 0; i < array.length(); i++) {
                Obj = (org.json.JSONObject) array.get(i);
                detailPostingModel = new DetailPostingModel(
                        Obj.get("writtenDate").toString(),
                        Obj.get("content").toString(),
                        Integer.parseInt(Obj.get("isUpDown").toString()),
                        Boolean.parseBoolean(Obj.get("isImage").toString())
                );
                detailVector.add(detailPostingModel);
            }

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return detailVector;
    }

    @Override
    public boolean writePost(int userId, String content) {
        try {
            url = new URL(URLInfo.Posting_WritePosting2);
            httpMessage = new HttpMessage(url);
            Properties prop = new Properties();
            prop.setProperty("userId", String.valueOf(userId));
            prop.setProperty("content", content);
            return Boolean.parseBoolean(HttpMessage.getWebContentText(httpMessage.sendGetMessage(prop)));

        } catch (IOException e1) {

            Log.d(TAG, e1.getMessage());

            return false;
        }
    }


    @Override
    public String writePostWithImage(int userId, String content, Intent data, ContentResolver contentResolver) {
        file = null;
        mParameters.put("userId", String.valueOf(userId));
        mParameters.put("content", content);

        fileUpload = new FileUpload(mContext, mActivity, mParameters);

        mImageCaptureUri = data.getData();
        file = getImageFile(mImageCaptureUri, contentResolver);
        String full_path = file.getAbsolutePath();

        new FileUploadAsyncTask().execute(full_path);

        return full_path;
    }

    @Override
    public String multiUpload(Intent data, ContentResolver contentResolver) {
        file = null;
        fileUpload = new FileUpload(mContext, mActivity, mParameters);

        mImageCaptureUri = data.getData();
        file = getImageFile(mImageCaptureUri, contentResolver);
        String full_path = file.getAbsolutePath();

        new FileUploadAsyncTask().execute(full_path);

        return full_path;

    }


    private File getImageFile(Uri uri, ContentResolver contentResolver) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = contentResolver.query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");


        if(mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        //path가 널이 나온다....
        String path = mCursor.getString(column_index);

        if (mCursor !=null ) {
            mCursor.close();
            mCursor = null;
        }


        return new File(path);
    }

    private class FileUploadAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            fileUpload.HttpFileUpload(mContext, "", params[0], URLInfo.Posting_MultiUpload);
            return null;
        }
    }


}
