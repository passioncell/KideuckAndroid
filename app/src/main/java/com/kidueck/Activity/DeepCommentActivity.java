package com.kidueck.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.kidueck.ListData.Comment;
import com.kidueck.R;

import java.util.ArrayList;

/**
 * Created by system777 on 2016-07-22.
 */
public class DeepCommentActivity extends Activity{

    ArrayList<Comment> list = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_comment);
//
//        ListView yourListView = (ListView) findViewById(R.id.lv_deep_comment);
//
//        // get data from the table by the ListAdapter
//        DeepCommentAdapter customAdapter = new DeepCommentAdapter(this, R.layout.listitem_deep_comment, list);
//
//        Comment item = new Comment();
//        item.setCommentContent("ㅋㅋㅋ내용");
//        item.setWrittenDate("날짜다임마");
//        customAdapter.add(item);
//
//        yourListView .setAdapter(customAdapter);
    }
}
