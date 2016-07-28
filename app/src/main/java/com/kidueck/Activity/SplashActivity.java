package com.kidueck.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kidueck.R;

/**
 * Created by system777 on 2016-06-24.
 */
public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //최초 진입시 로그인했는지 확인.
        String userId =  checkLogin();

        //사용자가 로그인했는지 안했는지 검사.
        if(userId.equals("")){
            //로그인화면으로 이동
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(0,android.R.anim.fade_in);
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            },SPLASH_TIME_OUT);
        }else{
            //메인화면 진입.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    overridePendingTransition(0, android.R.anim.fade_in);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }//if else ();;

    }

    //프리퍼런스 : 로긴여부 확인 (로긴했을시 login 프리퍼런스에 유저 이메일이 들어가있음)
    private String checkLogin() {
        SharedPreferences pref = getSharedPreferences("userId", MODE_PRIVATE);
        return pref.getString("userId", "");
    }
}