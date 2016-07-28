package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;

import com.kidueck.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by system777 on 2016-07-28.
 */
public class DetailPostImage extends Activity {

    PhotoViewAttacher mAttacher;
    ImageView attachedImg;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post_img);

        attachedImg = (ImageView) findViewById(R.id.iv_detail_attached_img2);

        Picasso.with(getApplicationContext()).load("http://dayot.seobuchurch.or.kr/upload/post/395/1.jpg").into(attachedImg);

        mAttacher = new PhotoViewAttacher(attachedImg);

    }

}
