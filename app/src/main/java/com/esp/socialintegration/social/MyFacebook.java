package com.esp.socialintegration.social;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.BuildConfig;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by acespritech on 4/12/15.
 */
public class MyFacebook {


    public CallbackManager mCallbackManager;

    private Context context;
    private Activity activity;

    public static boolean isFBSDKinit = false;

    public MyFacebook(Activity activity) {

        this.context = activity.getApplicationContext();
        this.activity = activity;
        mCallbackManager = CallbackManager.Factory.create();

        if (!isFBSDKinit) {
            FBSDKinit(this.context);
        }
    }

    public void login(final ResponseListener responseListener) {

        Log.print("MyFacebook", "LoginFB");

        LoginManager fbLM = LoginManager.getInstance();
        fbLM.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        fbLM.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.print("On success");
                Log.print("MyFacebook FB Access Token", loginResult.getAccessToken().toString());
                Pref.setValue(activity, Config.PREF_FBLOGGEDIN, true);
                Pref.setValue(activity, Config.PREF_FBACCESSTOKEN, loginResult.getAccessToken());

                responseListener.onResponce(Config.FACEBOOK_LOGIN_TAG, Config.RESULT_OK, "Success");

            }

            @Override
            public void onCancel() {
                responseListener.onResponce(Config.FACEBOOK_LOGIN_TAG, Config.RESULT_FAIL, "Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                responseListener.onResponce(Config.FACEBOOK_LOGIN_TAG, Config.RESULT_FAIL, e.getMessage());

            }

        });
        Log.print("MyFacebook", "after register callback");
        fbLM.logInWithReadPermissions(activity, Arrays.asList("user_friends", "user_about_me", "email", "user_birthday", "public_profile"));
//        fbLM.logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));

    }

    public void getProfile(final ResponseListener responseListener) {
        final GraphRequest request = new GraphRequest(
                getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.print("MyFacebook FB Data : " + response.toString());

                        JSONObject jobj = response.getJSONObject();
                        Log.print("MyFacebook FB json object" + response.getJSONObject());

                        if (jobj != null) {
                            try {
                                ProfileBean profileBean = new ProfileBean();
                                profileBean.id = jobj.getString("id");
                                profileBean.name = jobj.getString("name");
                                profileBean.email = jobj.getString("email");
                                profileBean.link = jobj.getString("link");
                                profileBean.last_name = jobj.getString("last_name");
                                profileBean.first_name = jobj.getString("first_name");
                                profileBean.gender = jobj.getString("gender");

                                JSONObject pic = jobj.getJSONObject("picture");
                                JSONObject data = pic.getJSONObject("data");
                                profileBean.profile_pic = data.getString("url");

                                //Log.print("MyFacebook FB Data" + jobj.toString(4));
                                responseListener.onResponce(Config.FACEBOOK_PROFILE_TAG, Config.RESULT_OK, profileBean);
                            } catch (Exception e) {
                                responseListener.onResponce(Config.FACEBOOK_PROFILE_TAG, Config.RESULT_FAIL, e.getMessage());
                            }
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,hometown,first_name,last_name,link,location,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getFriendList() {
        final GraphRequest request = new GraphRequest(
                getCurrentAccessToken(),
                "/me/nonapp_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.print("MyFacebook FB Data : " + response.toString());

                        JSONObject jobj = response.getJSONObject();
                        Log.print("MyFacebook FB json object" + response.getJSONObject());
                        try {
                            Log.print("MyFacebook FB Data" + jobj.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        request.executeAsync();
    }

    public void getTaggableFriend() {
        final GraphRequest request = new GraphRequest(
                getCurrentAccessToken(),
                "/me/taggable_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.print("MyFacebook FB Data : " + response.toString());

                        JSONObject jobj = response.getJSONObject();
                        Log.print("MyFacebook FB json object" + response.getJSONObject());
                        try {
                            Log.print("MyFacebook FB Data" + jobj.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        request.executeAsync();
    }


    public void sendPostDialog(String msg) {
        try {
            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(context, "Send post Completed", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(context, "Send post Canceled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Log.print("MyFacebook getProfile", error.getMessage());
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Share Location on FB")
                        .setContentDescription(msg)
                        .setContentUrl(Uri.parse("http://wallpaper-gallery.net/image.php?pic=/images/image/image-4.jpg"))
                        .build();
                shareDialog.setShouldFailOnDataError(false);
                shareDialog.show(linkContent, ShareDialog.Mode.FEED);
            } else {
                Log.print("MyFacebook post nested else");
            }
        } catch (Exception e) {
            Log.print("MyFacebook post", e.getMessage());
            e.printStackTrace();
        }
    }

    public void postStatusUpdate(final String message, final Bitmap bitmapImage, final ResponseListener responseListener) {

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmapImage)
                .setCaption(message)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        if (getCurrentAccessToken().getPermissions().contains("publish_actions")) {
            ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    responseListener.onResponce(Config.FACEBOOK_POST_TAG, Config.RESULT_OK, "Success");
                }

                @Override
                public void onCancel() {
                    responseListener.onResponce(Config.FACEBOOK_POST_TAG, Config.RESULT_FAIL, "Cancelled");
                }

                @Override
                public void onError(FacebookException e) {
                    responseListener.onResponce(Config.FACEBOOK_POST_TAG, Config.RESULT_FAIL, e.getMessage());
                }
            });
        } else {
            Log.print("getting permission");
            LoginManager.getInstance().logInWithPublishPermissions(activity, Collections.singletonList("publish_actions"));//Arrays.asList("publish_actions"));
        }
    }


    public boolean logout() {
        try {
            LoginManager.getInstance().logOut();
            Pref.setValue(activity, Config.PREF_FBLOGGEDIN, false);
            return true;
        } catch (Exception e) {
            Log.print("MyFacebook logout", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    public static void FBSDKinit(Context context) {
        try {
            FacebookSdk.sdkInitialize(context, Config.FACEBOOK_REQUEST);
            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true);
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
            }
            isFBSDKinit = true;

        } catch (Exception e) {
            Log.print("MyFacebook post", e.toString());
        }
    }

    public void checkAccesstoken() {
        boolean isExpired = AccessToken.getCurrentAccessToken().isExpired();
        Log.print("MyFacebook isExpired", String.valueOf(isExpired));

        AccessToken.refreshCurrentAccessTokenAsync();
        Log.print("MyFacebook accessToken", AccessToken.getCurrentAccessToken().getToken().toString());
        String accessToken = AccessToken.getCurrentAccessToken().getToken().toString();

        Log.print("MyFacebook expirationTime", AccessToken.getCurrentAccessToken().getExpires().toString());
        Date expirationTime = AccessToken.getCurrentAccessToken().getExpires();

        Log.print("MyFacebook declinedPermissions", AccessToken.getCurrentAccessToken().getDeclinedPermissions().toString());
        Set<String> declinedPermissions = AccessToken.getCurrentAccessToken().getDeclinedPermissions();

        Log.print("MyFacebook lastRefreshTime", AccessToken.getCurrentAccessToken().getLastRefresh().toString());
        Date lastRefreshTime = AccessToken.getCurrentAccessToken().getLastRefresh();

        Log.print("MyFacebook permissions", String.valueOf(AccessToken.getCurrentAccessToken().getPermissions().toString()));
        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();

        Log.print("MyFacebook applicationId", AccessToken.getCurrentAccessToken().getApplicationId().toString());
        String applicationId = AccessToken.getCurrentAccessToken().getApplicationId();

        Log.print("MyFacebook userId", AccessToken.getCurrentAccessToken().getUserId().toString());
        String userId = AccessToken.getCurrentAccessToken().getUserId();

        Log.print("MyFacebook accessTokenSource", AccessTokenSource.FACEBOOK_APPLICATION_NATIVE.toString());
        AccessTokenSource accessTokenSource = AccessTokenSource.FACEBOOK_APPLICATION_NATIVE;

        try {
            AccessToken newAccessToken = new AccessToken(accessToken, applicationId, userId, permissions, declinedPermissions, accessTokenSource, expirationTime, lastRefreshTime);
            Log.print("MyFacebook getExpires", newAccessToken.getCurrentAccessToken().getExpires().toString());
            Log.print("MyFacebook getToken", newAccessToken.getCurrentAccessToken().getToken().toString());

        } catch (Exception e) {
            Log.print("MyFacebook Exception", e.toString());

        }
    }

}
