package com.kidueck.Abstract;

import com.kidueck.Model.CommentListModel;
import com.kidueck.Model.DeepCommentListModel;

import java.util.Vector;

/**
 * Created by system777 on 2016-06-27.
 */
public interface ICommentRepository {
    Vector<CommentListModel> getCommentList(int userId, int postingId, int pageNumber);
    boolean writeComment(int userId, int postingId, String content);
    Vector<DeepCommentListModel> getDeepCommentList(int userId, int commentId, int pageNumber);
    boolean writeDeepComment(int userId, int commentId, String content);
    DeepCommentListModel getDeepCommentDetail(int userId, int commentId);
}
