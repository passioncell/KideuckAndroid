package com.kidueck.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.kidueck.ListData.Comment;
import com.kidueck.R;

import java.util.ArrayList;

public class TestActivity extends Activity {

    ArrayList<Comment> commentList = new ArrayList<Comment>();
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        Comment item = new Comment();
//        item.setCommentContent("ㅋㅋㅋ내용");
//        item.setWrittenDate("날짜다임마");
//
//        commentList.add(item);
//
//        DeepCommentAdapter adapter = new
//                DeepCommentAdapter(TestActivity.this, commentList);
//        list=(ListView)findViewById(R.id.list);
//        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(TestActivity.this, "You Clicked at " , Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

}