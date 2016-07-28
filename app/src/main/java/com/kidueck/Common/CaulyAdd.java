package com.kidueck.Common;

import android.app.Activity;
import android.util.Log;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.fsn.cauly.Logger;

/**
 * Created by system777 on 2016-07-10.
 */
public class CaulyAdd implements CaulyAdViewListener, CaulyInterstitialAdListener {

    //카울리
    private static final String APP_CODE = "X4ugHCy7";
    private CaulyInterstitialAd interstitialAd;
    public Activity targetActivity;

    public CaulyAdd(Activity activity){
        targetActivity = activity;
    }

    public void showFullScreenAd(){
        // Cauly 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
        //	LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
        Logger.setLogLevel(Logger.LogLevel.Info);

        //////////////////////
        //
        // Java 기반 전면 광고
        //
        //////////////////////

        // CaulyAdInfo 생성
        // 배너 광고와 동일하게 광고 요청을 설정할 수 있다.
        CaulyAdInfo interstitialAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();

        // 전면 광고 생성
        interstitialAd = new CaulyInterstitialAd();
        interstitialAd.setAdInfo(interstitialAdInfo);
        interstitialAd.setInterstialAdListener(this);
        // 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
        interstitialAd.requestInterstitialAd(targetActivity);

        //////////////////////////////
        //
        // Java 기반 전면 광고 Listener
        //
        //////////////////////////////

        // CaulyInterstitialAdListener
        //	전면 광고의 경우, 광고 수신 후 자동으로 노출되지 않으므로,
        //	반드시 onReceiveInterstitialAd 메소드에서 노출 처리해 주어야 한다.
    }

    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        }
        else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }

        // ad.show();가 없을경우 광고가 노출되지 않는다.
        ad.show();
    }

    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errorCode, String errorMsg) {
        // 전면 광고 수신 실패할 경우 호출됨.
        Log.d("CaulyExample", "failed to receive interstitial AD.");
    }

    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd arg0) {}
    // TODO Auto-generated method stub
    //카울리
    @Override
    public void onReceiveAd(CaulyAdView p1, boolean p2)
    {
        // TODO: Implement this method
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdView p1, int p2, String p3)
    {
        // TODO: Implement this method
    }

    @Override
    public void onShowLandingScreen(CaulyAdView p1)
    {
        // TODO: Implement this method
    }

    @Override
    public void onCloseLandingScreen(CaulyAdView p1)
    {
        // TODO: Implement this method
    }
}
