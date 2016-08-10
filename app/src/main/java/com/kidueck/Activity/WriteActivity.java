package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Common.URLInfo;
import com.kidueck.Concrete.PostingRepository;
import com.kidueck.Fragment.FeedFragment;
import com.kidueck.R;
import com.kidueck.Util.MultipartUtility;
import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;
import com.yongbeam.y_photopicker.util.photopicker.utils.YPhotoPickerIntent;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hyeonbae on 2016-07-10.
 */
public class WriteActivity extends Activity implements View.OnClickListener {

    FeedFragment feedFrag;

    EditText postContent;
    Button submitPost, selectImageButton;
    TextView nowTextCnt;

    PostingRepository postingRepository = new PostingRepository();
    String inputContent;
    boolean submitResult;


    //이미지 첨부 관련
    final int PICK_IMAGE_REQUEST=100;
    boolean isImage = false;
    int requestCode, resultCode;
    Intent data;
    List<String> photos = null;
    LinearLayout selectedImgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        init();

    }



    private void init(){
        postContent = (EditText) findViewById(R.id.et_write_content);
        nowTextCnt = (TextView) findViewById(R.id.tv_write_text_cnt);
        postContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nowTextCnt.setText(String.valueOf(postContent.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submitPost = (Button) findViewById(R.id.bt_write_submit);
        selectImageButton = (Button) findViewById(R.id.bt_write_image);
        selectedImgLayout = (LinearLayout) findViewById(R.id.ll_write_selected_img);

        submitPost.setOnClickListener(this);
        selectImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_write_submit:
                inputContent = postContent.getText().toString().trim();
                if(inputContent.length() == 0){
                    Toast.makeText(getApplicationContext(), "글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if(isImage){ //이미지 첨부한경우
                        new SubmitPost().execute();
                        new UploadImages().execute();
                    }else{ //이미지 안첨부한경우
                        new SubmitPost().execute();
                    }

                }

                break;

            case R.id.bt_write_image:
//                Intent clsIntent = new Intent(Intent.ACTION_PICK);
//                clsIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//                clsIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(clsIntent, 100);

                YPhotoPickerIntent intent = new YPhotoPickerIntent(this);
                intent.setMaxSelectCount(20);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                intent.setSelectCheckBox(false);
                intent.setMaxGrideItemCount(3);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectedImgLayout.removeAllViews();
        photos = null;
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if (data != null) {

                this.requestCode = requestCode;
                this.resultCode = resultCode;
                this.data = data;
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                isImage = true;

                for(int i=0; i< photos.size(); i++){
                    try {
                        ImageView image = new ImageView(WriteActivity.this);
                        selectedImgLayout.addView(image);
                        Bitmap orgImage = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file://"+photos.get(i)));
                        Bitmap resize = Bitmap.createScaledBitmap(orgImage, 300, 400, true);
                        image.setImageBitmap(resize);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"이미지 첨부에 실패하였습니다."+e.toString(),Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }


    public int getUserId(){
        SharedPreferences pref = getSharedPreferences("userId", Context.MODE_PRIVATE);
        return Integer.parseInt(pref.getString("userId", ""));
    }


    public class UploadImages extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;
        boolean uploadResult = false;
        String exeptionStr;


        @Override
        protected Void doInBackground(Boolean... params) {
            String charset = "UTF-8";
            int attachedImgCnt = photos.size();
            String requestURL = URLInfo.Posting_MultiUpload;


            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                for(int i=0; i<attachedImgCnt; i++){
                    multipart.addFilePart("uploadedfile",new File(photos.get(i)));
                }

                multipart.addFormField("userId", String.valueOf(getUserId()));

                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                for (String line : response) {
                    System.out.println(line);
                }

                uploadResult = true;
            } catch (IOException ex) {
                System.err.println(ex);
                exeptionStr = ex.toString();
                uploadResult = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(uploadResult){
                Toast.makeText(getApplicationContext(), "작성완료", Toast.LENGTH_SHORT).show();
                finish();
                feedFrag = FeedFragment.newInstance();
                MainActivity.getInstace().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commitAllowingStateLoss();
            }else{
                Toast.makeText(getApplicationContext(), "오류 : "+exeptionStr, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(WriteActivity.this);
            progressDialog.setMessage("사진등록중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }

    }//UploadImages Class();;


    public class SubmitPost extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                submitResult = postingRepository.writePost0(getUserId(), inputContent, isImage);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if(!submitResult){ //게시글 등록실패
                Toast.makeText(getApplicationContext(), "게시글 등록중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }

            if(!isImage){ //사진 첨부안한경우
                finish();
                feedFrag = FeedFragment.newInstance();
                MainActivity.getInstace().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commitAllowingStateLoss();
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(WriteActivity.this);
            progressDialog.setMessage("게시글 등록중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SubmitPost Class();;


}
