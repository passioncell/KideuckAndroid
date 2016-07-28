package com.kidueck.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.equals("인터넷 연결이 해제되어 있습니다.")){
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }

    }
}