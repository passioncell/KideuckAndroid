package com.kidueck.Abstract;

import com.kidueck.Model.JoinModel;

/**
 * Created by system777 on 2016-06-25.
 */
public interface IMemberRepository {
    int checkJoin(String userSerial);

    String join(JoinModel joinModel);
    String login(String id, String pw);
    String getUserPrimaryKey(String id);
    int getUserPoint(int userId);
}
