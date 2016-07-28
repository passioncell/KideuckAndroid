package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Common.PointReceiver;
import com.kidueck.Concrete.PostingRepository;
import com.kidueck.Fragment.FeedFragment;
import com.kidueck.R;

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
    String imageSubmitResult;

    final int PICK_IMAGE_REQUEST=100;
    ImageView selectedImage;

    //이미지 첨부 관련
    boolean isImage = false;
    int requestCode, resultCode;
    Intent data;

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
        selectedImage = (ImageView) findViewById(R.id.iv_write_selected);

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
                    postContent.setText("");
                    new SubmitPost().execute();
                }

                break;

            case R.id.bt_write_image:
                Intent clsIntent = new Intent(Intent.ACTION_PICK);
                clsIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                clsIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(clsIntent, 100);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                isImage = true;
                this.requestCode = requestCode;
                this.resultCode = resultCode;
                this.data = data;

//                postingRepository.uploadPostingImage(data, getContentResolver(), getUserId());
                Bitmap clsBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                selectedImage.setImageBitmap(clsBitmap);

            } catch (Exception e) {
                Log.e("Picture", e.toString());
            }
        }

    }


    public int getUserId(){
        SharedPreferences pref = getSharedPreferences("userId", Context.MODE_PRIVATE);
        return Integer.parseInt(pref.getString("userId", ""));
    }




    public class SubmitPost extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server

                if(isImage){
                    imageSubmitResult = postingRepository.writePostWithImage(getUserId(), inputContent, data, getContentResolver());
                }else{
                    submitResult = postingRepository.writePost(getUserId(), inputContent);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if(isImage){//사진 첨부한경우
                if(imageSubmitResult != null){
                   // Toast.makeText(getApplicationContext(), "imageSubmitResult"+imageSubmitResult, Toast.LENGTH_SHORT).show();
                    Log.e("zzz", "imageSubmitResult"+imageSubmitResult);

                    //포인트 갱신
                    sendBroadcast(new Intent(WriteActivity.this, PointReceiver.class));

                    finish();
                    feedFrag = FeedFragment.newInstance();
                    MainActivity.getInstace().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commitAllowingStateLoss();

                }else{
                    Toast.makeText(getApplicationContext(), "사진 게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }

            }else{ //사진첨부 안한경우
                if(submitResult){
                    Toast.makeText(getApplicationContext(), "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    //포인트 갱신
                    sendBroadcast(new Intent(WriteActivity.this, PointReceiver.class));

                    finish();
                    feedFrag = FeedFragment.newInstance();
                    MainActivity.getInstace().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commitAllowingStateLoss();

                }else{
                    Toast.makeText(getApplicationContext(), "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
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
