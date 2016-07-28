package com.kidueck.Activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kidueck.Common.ApplicationController;
import com.kidueck.Common.CaulyAdd;
import com.kidueck.Common.PointReceiver;
import com.kidueck.Concrete.MemberRepository;
import com.kidueck.Concrete.NoticeRepository;
import com.kidueck.Concrete.SettingRepository;
import com.kidueck.Fragment.FeedFragment;
import com.kidueck.Fragment.NoticeFragment;
import com.kidueck.Fragment.SettingFragment;
import com.kidueck.R;
import com.kidueck.Util.ActivityResultEvent;
import com.kidueck.Util.ActivityResultBus;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.security.MessageDigest;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final int redColor = Color.parseColor("#FF0000");

    //프래그먼트
    private Fragment feedFrag, noticeFrag, settingFrag;
    private BottomBar mBottomBar;

    BottomBarBadge noticeBadge;
    MemberRepository memberRepository;
    ResponseHandler handler = new ResponseHandler();
    NoticeRepository noticeRepository = new NoticeRepository();

    //버전
    SettingRepository settingRepository = new SettingRepository();
    String userAppVersion;
    String latestAppVersion;


    private static MainActivity ins;

    //APP BAR
    TextView tv_kiki;
    ImageView iv_logo;
    Button btn_write;

    Timer jobScheduler;

    public MainActivity() {
    }
    //int newNoticeCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;

        if(isNetwork()){
            initComponent(savedInstanceState);

            compareVersion();

            GetNewNoticeCnt job = new GetNewNoticeCnt();
            jobScheduler = new Timer();
            jobScheduler.scheduleAtFixedRate(job, 1000, 30000);

            //애널리스틱
            Tracker t = ((ApplicationController)getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);
            t.setScreenName("Test1Activity");
            t.send(new HitBuilders.AppViewBuilder().build());

            //페이스북 애널리스틱
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);

        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // 제목셋팅
            alertDialogBuilder.setTitle("인터넷이 연결되어있지않습니다.");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("인터넷을 연결하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 네트워크 설정창을 연다.
                                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                                    intent.setComponent(cn);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });

            // 다이얼로그 생성
            AlertDialog alertDialog = alertDialogBuilder.create();

            // 다이얼로그 보여주기
            alertDialog.show();
        }

        getAppKeyHash();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }



    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    public void initComponent(Bundle savedInstanceState){

        memberRepository = new MemberRepository();


        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu_three_items);

        // We want the nearbyBadge to be always shown, except when the Favorites tab is selected.
        noticeBadge = mBottomBar.makeBadgeForTabAt(1, redColor, 0);
        noticeBadge.setAutoShowAfterUnSelection(false);

        mBottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimaryDark));

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //mMessageView.setText(TabMessage.get(menuItemId, false));
                Tracker t = ((ApplicationController) getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);

                switch (menuItemId) {
                    case R.id.bottom_menu_feed:
                        feedFrag = FeedFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commit();
                        t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("Press feed Button").setLabel("FEED Click").build());
                        break;
                    case R.id.bottom_menu_notice:
                        noticeBadge.setCount(0);
                        noticeBadge.hide();
                        updateIconBadgeCount(getApplicationContext(), 0);
                        noticeFrag = NoticeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, noticeFrag).commit();
                        t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("Press notice Button").setLabel("NOTICE Click").build());
                        break;
                    case R.id.bottom_menu_setting:
                        settingFrag = SettingFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, settingFrag).commit();
                        t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("Press setting Button").setLabel("SETTING Click").build());
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                //Toast.makeText(getApplicationContext(), TabMessage.get(menuItemId, true), Toast.LENGTH_SHORT).show();
                onMenuTabSelected(menuItemId);
            }
        });

        tv_kiki = (TextView) findViewById(R.id.actionbar_kiki);
        iv_logo = (ImageView) findViewById(R.id.actionbar_icon);
        btn_write = (Button) findViewById(R.id.actionbar_write);
        tv_kiki.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        btn_write.setOnClickListener(this);

        //포인트 갱신
        getApplicationContext().sendBroadcast(new Intent(MainActivity.this, PointReceiver.class));

    }



    public static MainActivity  getInstace(){
        return ins;
    }


    public void setKikiPoint(String userPoint){
        tv_kiki.setText(userPoint);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        CaulyAdd caulyAdd = new CaulyAdd(this);
        caulyAdd.showFullScreenAd();

        new AlertDialog.Builder(this)
                .setTitle("키득 종료")
                .setMessage("키득을 종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 프로세스 종료.
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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

    public void stopTimer(){
        jobScheduler.cancel();
    }



    private void compareVersion(){
        final PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
            userAppVersion = info.versionName.toString().trim();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetLatestVersionCode().execute();
    }

    //뱃지 카운트 변경
    public void updateIconBadgeCount(Context context, int count) {

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        // Component를 정의
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", getLauncherClassName(context));

        // 카운트를 넣어준다.
        intent.putExtra("badge_count", count);

        // Version이 3.1이상일 경우에는 Flags를 설정하여 준다.
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.setFlags(0x00000020);
        }

        // send
        sendBroadcast(intent);
    }

    private String getLauncherClassName(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(getPackageName());

        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
        if(resolveInfoList != null && resolveInfoList.size() > 0) {
            return resolveInfoList.get(0).activityInfo.name;
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, PointActivity.class);
        switch (v.getId()){
            case R.id.actionbar_write:
//                postFrag = PostFragment.newInstance();
//                getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, postFrag).commit();
                Intent writeIntent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(writeIntent);
                break;
            case R.id.actionbar_icon:
                startActivity(intent);
                break;
            case R.id.actionbar_kiki:
                startActivity(intent);
                break;
        }
    }


    class GetNewNoticeCnt extends TimerTask {

        int getCnt = 0;
        public void run() {

            getCnt  = Integer.parseInt(noticeRepository.getNewNoticeCnt(getUserId()).trim());
            if(getCnt > 0){
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("newNoticeCnt", getCnt);
                message.setData(bundle);

                handler.sendMessage(message);
                getCnt = 0;
            }
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }

    class ResponseHandler extends Handler {

        //이 메서드는 UI쪽 접근가능(메인쓰레드에서 처리됌)
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int newNoticeCnt = bundle.getInt("newNoticeCnt");

            noticeBadge.setCount(newNoticeCnt);
            noticeBadge.show();

            updateIconBadgeCount(getApplicationContext(), newNoticeCnt);
        }

    }

    private class GetLatestVersionCode extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                latestAppVersion = settingRepository.getLatestVersionName().trim();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();


            if(userAppVersion.charAt(0) == latestAppVersion.charAt(0)){ // O.X.X
                //업데이트가 필요함.
                if(userAppVersion.charAt(2) == latestAppVersion.charAt(2)){ // X.O.X
                    //업데이트가 필요함.
                    if(userAppVersion.charAt(4) == latestAppVersion.charAt(4)) { // X.X.O
                        //앱이 최신버전임
                    }else{//1단계 업데이트
                        showUpdateDialog();
                    }
                }else{ //2단계 업데이트
                    showUpdateDialog();
                }
            }else{ //3단계 업데이트
                showUpdateDialog();
            }

        }

        public void showUpdateDialog(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            // 제목셋팅
            alertDialogBuilder.setTitle("업데이트 알림");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("앱을 업데이트 해야합니다.\n" +
                            "설치버전: "+userAppVersion +" 최선버전: "+latestAppVersion)
                    .setCancelable(false)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 앱스토어 이동
                                    Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri.parse("market://details?id=com.kideuck"));
                                    startActivity(marketLaunch);
                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });
            alertDialogBuilder.show();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("최신버전 코드 불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

    }//GetLatestVersionName Class();;





}
