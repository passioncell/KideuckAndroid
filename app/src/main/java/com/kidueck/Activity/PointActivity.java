package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.kidueck.Concrete.MemberRepository;
import com.kidueck.R;

/**
 * Created by Hyeonbae on 2016-07-10.
 */
public class PointActivity extends Activity {

    MemberRepository memberRepository = new MemberRepository();
    int userPoint = 0;
    TextView tv_userPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        init();
        new GetUserPoint().execute();
    }

    private void init(){
        tv_userPoint = (TextView) findViewById(R.id.tv_point_point);
    }

    private class GetUserPoint extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try {
                //Getting data from server
                userPoint = memberRepository.getUserPoint(getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_userPoint.setText("$"+String.valueOf(userPoint));
                }
            });
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.getInstace());
            progressDialog.setMessage("키키 불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId() {
            SharedPreferences pref = MainActivity.getInstace().getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }
}
