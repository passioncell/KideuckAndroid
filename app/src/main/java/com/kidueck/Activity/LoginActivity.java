package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kidueck.Concrete.MemberRepository;
import com.kidueck.R;

/**
 * Created by system777 on 2016-07-05.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    EditText etId, etPw;
    Button btLogin, btJoin;


    MemberRepository memberRepository = new MemberRepository();
    String inputId, inputPw;
    String submitResult;
    String userPrimaryKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isNetwork()){
            init();
        }else{
            Toast.makeText(getApplicationContext(), "인터넷 연결이 안되어있습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private void init(){
        etId = (EditText) findViewById(R.id.et_login_id);
        etPw = (EditText) findViewById(R.id.et_login_pw);
        btLogin = (Button) findViewById(R.id.bt_login_login);
        btJoin = (Button) findViewById(R.id.bt_login_join);

        btLogin.setOnClickListener(this);
        btJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login_login:
                if (isInvalid()) {
                    new SubmitLogin().execute();
                }
                break;
            case R.id.bt_login_join:
                startActivity(new Intent(LoginActivity.this,JoinActivity.class));
                break;
        }
    }

    private boolean isInvalid(){

        if(etId.getText().toString().trim().equals("") || etPw.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"ID와 PASSWORD를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            inputId = etId.getText().toString().trim();
            inputPw = etPw.getText().toString().trim();
        }


        return true;

    }

    private boolean isNetwork() { // network 연결 상태 확인
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()){
            Log.d("NETWORK", "Network connect success");
            return true;
        }else{
            Log.d("NETWORK", "Network connect fail");
            return false;
        }
    }

    private void setUserSharedPreferences(){
        SharedPreferences pref = getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("userNickName", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences.Editor editor2 = pref2.edit();

        editor.putString("userId", userPrimaryKey);
        editor2.putString("userNickName", inputId);

        editor.commit();
        editor2.commit();
    }



    private class SubmitLogin extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                submitResult = memberRepository.login(inputId, inputPw);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(submitResult.equals("success")){
                new GetUserPrimaryKey().execute();
            }else{
                Toast.makeText(getApplicationContext(), submitResult, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("로그인중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

    }//Submit Class();;

    private class GetUserPrimaryKey extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                userPrimaryKey = memberRepository.getUserPrimaryKey(inputId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            setUserSharedPreferences();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "환영합니다♥", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("회원정보 가져오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

    }//Submit Class();;
}
