package com.kidueck.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.kidueck.Common.URLInfo;
import com.kidueck.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by system777 on 2016-07-28.
 */
public class DetailPostImage extends Activity {

    PhotoViewAttacher mAttacher;
    ImageView attachedImg;
    int selectedPostingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post_img);

        Intent intent = getIntent();
        selectedPostingId = Integer.parseInt(intent.getStringExtra("selectedPostingId"));


        attachedImg = (ImageView) findViewById(R.id.iv_detail_attached_img2);
        Picasso.with(getApplicationContext()).load(new URLInfo().getPostImgUploadUrl() +selectedPostingId + "/1.jpg").into(attachedImg);
        mAttacher = new PhotoViewAttacher(attachedImg);

    }

}
