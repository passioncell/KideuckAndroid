package com.kidueck.Abstract;

import android.content.ContentResolver;
import android.content.Intent;

import com.kidueck.Model.DetailPostingModel;
import com.kidueck.Model.PostingListModel;

import java.util.Vector;

/**
 * Created by system777 on 2016-06-25.
 */
public interface IPostingRepository {
    Vector<PostingListModel> getPostingList(int userId, int pageNumber);
    Vector<PostingListModel> getHotPostingList(int userId, int pageNumber);
    Vector<PostingListModel> getMyPostingList(int userId, int pageNumber);
    boolean votePosting(int userId, int postingId, int type);
    Vector<DetailPostingModel> getDetailPosting(int postingId, int userId);
    boolean writePost(int userId, String content);
    String writePostWithImage(int userId, String content, Intent data, ContentResolver contentResolver);

}
