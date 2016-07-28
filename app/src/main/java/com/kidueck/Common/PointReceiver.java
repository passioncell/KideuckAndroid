package com.kidueck.Common;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.kidueck.Activity.MainActivity;
import com.kidueck.Concrete.MemberRepository;

public class PointReceiver extends BroadcastReceiver {

    MemberRepository memberRepository = new MemberRepository();
    int userPoint = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        new GetUserPoint().execute();

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

            MainActivity.getInstace().setKikiPoint("$"+String.valueOf(userPoint));
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