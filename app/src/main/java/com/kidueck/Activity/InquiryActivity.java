package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kidueck.Concrete.SettingRepository;
import com.kidueck.R;

/**
 * Created by system777 on 2016-06-29.
 */
public class InquiryActivity extends Activity {

    SettingRepository settingRepository = new SettingRepository();

    Button submitInquiryButton;
    EditText contentEditText;
    String inputContent;
    boolean submitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        submitInquiryButton = (Button) findViewById(R.id.bt_inquiry_submit);
        contentEditText = (EditText) findViewById(R.id.et_inquiry_content);

        submitInquiryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputContent =  contentEditText.getText().toString();
                if(inputContent.length() < 5){
                    Toast.makeText(getApplicationContext(), "5자이상 써주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    submitInquiryButton.setEnabled(false);
                    contentEditText.setText("");
                    new SubmitInquiry().execute();
                    submitInquiryButton.setEnabled(true);
                }
            }
        });
    }


    private class SubmitInquiry extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                submitResult = settingRepository.submitInquiry(inputContent, getUserId());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(submitResult){
                Toast.makeText(getApplicationContext(), "문의사항이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "문의사항 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(InquiryActivity.this);
            progressDialog.setMessage("문의사항 전송중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SubmitPost Class();;

}
