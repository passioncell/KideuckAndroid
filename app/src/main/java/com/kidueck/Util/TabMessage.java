package com.kidueck.Util;

import com.kidueck.R;

public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.bottom_menu_feed:
                message += "처음으로";
                break;
            case R.id.bottom_menu_notice:
                message += "알림";
                break;
            case R.id.bottom_menu_setting:
                message += "설정";
                break;
        }


        return message;
    }
}