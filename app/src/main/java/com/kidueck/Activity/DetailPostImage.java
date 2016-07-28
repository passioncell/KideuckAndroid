package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kidueck.R;

import java.io.IOException;
import java.io.InputStream;

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

        try {
            getattachedImg();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAttacher = new PhotoViewAttacher(attachedImg);

    }

    public void getattachedImg() throws IOException {
        attachedImg.setVisibility(View.VISIBLE);
        pd = new ProgressDialog(this);
        pd.setMessage("Downloading Image");

        new DownloadImageTask(attachedImg).execute("http://dayot.seobuchurch.or.kr/upload/post/395/1.jpg");


    }


    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pd.dismiss();
            bmImage.setImageBitmap(result);
        }
    }
}
