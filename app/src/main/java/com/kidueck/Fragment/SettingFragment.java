package com.kidueck.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Activity.InquiryActivity;
import com.kidueck.Activity.LoginActivity;
import com.kidueck.Activity.MainActivity;
import com.kidueck.Concrete.SettingRepository;
import com.kidueck.R;

/**
 * Created by system777 on 2016-06-25.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {


    LinearLayout latestVersionLayout, annonceLayout, inquiryLayout, logoutLayout;
    TextView userVersion, lateVersion;

    SettingRepository settingRepository = new SettingRepository();
//    ResponseHandler handler = new ResponseHandler();
    String latestVersionName;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        init(view);


        return view;
    }

    private void init(View view){
        latestVersionLayout = (LinearLayout) view.findViewById(R.id.ll_setting_latest_version);
        annonceLayout = (LinearLayout) view.findViewById(R.id.ll_setting_annonce);
        inquiryLayout = (LinearLayout) view.findViewById(R.id.ll_setting_inquiry);
        logoutLayout = (LinearLayout) view.findViewById(R.id.ll_setting_logout);
        latestVersionLayout.setOnClickListener(this);
        annonceLayout.setOnClickListener(this);
        inquiryLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);

        userVersion = (TextView) view.findViewById(R.id.tv_setting_user_version);
        lateVersion = (TextView) view.findViewById(R.id.tv_setting_late_version);
        compareVersion();
    }

    private void compareVersion(){
        final PackageInfo info;
        try {
            info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            userVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetLatestVersionName().execute();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_setting_annonce:
                Toast.makeText(getContext(),"준비중",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setting_inquiry:
                Intent intent = new Intent(getActivity(),InquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting_latest_version:
                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                marketLaunch.setData(Uri.parse("market://details?id=com.kideuck"));
                getActivity().startActivity(marketLaunch);
                break;
            case R.id.ll_setting_logout:
                new AlertDialog.Builder(getActivity())
                        .setTitle("키득 로그아웃")
                        .setMessage("정말 로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // LOG OUT
                                logout();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                break;
        }
    }

    //로그아웃
    private void logout() {
        //프리퍼런스 userEmail 삭제
        SharedPreferences pref = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE);
        SharedPreferences pref2 = getActivity().getSharedPreferences("userNickName", getActivity().MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences.Editor editor2 = pref2.edit();

        editor.remove("userId");
        editor2.remove("userNickName");

        editor.commit();
        editor2.commit();

        getActivity().finish(); //지금현재 엑티비티 종료
        MainActivity.getInstace().stopTimer();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private class GetLatestVersionName extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                latestVersionName = settingRepository.getLatestVersionName();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lateVersion.setText(latestVersionName.trim());
                }
            });
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("최신버전 코드 불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }//GetLatestVersionName Class();;
}
