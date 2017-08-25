package com.esp.socialintegration.social;

import android.content.Context;

import com.esp.socialintegration.social.MyInstagram.InstagramApp;

/**
 * Created by Upen on 24/8/17 in SocialIntegration.
 */

public class MyInstagaram implements InstagramApp.OAuthAuthenticationListener {

    private Context context;
    public static boolean isInstagramInit = false;

    private InstagramApp instagramApp;

    public static final String CLIENT_ID = "5c10c208ce1c4c84ad8bfda3c91f641f";
    public static final String CLIENT_SECRET = "bffbaeea81404371b4427cb454218339";
    public static final String CALLBACK_URL = "http://yourcallback.com/";

    public MyInstagaram(Context context) {
        this.context = context;

        if (!isInstagramInit) {
            InstagaramInit(context);
        }
    }

    private void InstagaramInit(Context context) {
        instagramApp = new InstagramApp(context, CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
        instagramApp.setListener(this);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail(String error) {

    }
}
