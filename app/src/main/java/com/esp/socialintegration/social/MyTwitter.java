package com.esp.socialintegration.social;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import io.fabric.sdk.android.Fabric;
import retrofit.mime.TypedFile;

/**
 * Created by acespritech on 4/12/15.
 */
public class MyTwitter {

    public static TwitterAuthClient client;
    private Activity activity;
    private Context context;
    public static boolean isTwitterInit = false;


    private static final String TWITTER_KEY = "pQGsbuviNpF97BjuGNXZNOmAH";
    private static final String TWITTER_SECRET = "ZOddMaqjC44WCyZBAzGyZBafh4AWgrXf5ymPDVal2WtomRFOG4";
//    private static final String TWITTER_KEY = "aa9o4UHteF32AssOt706oqIdG";
//    private static final String TWITTER_SECRET = "aNjEUtUIXbGeWqcpm6j7ibl5VHX0pziRa5PBkkahKSSQi6o7jj";
//    public static final String TWITTER_KEY = "XkyldoVomQCw9aGuyjYGJqKNo";
//    public static final String TWITTER_SECRET = "PSgrvasz2rf9Hoja8le8EpHSUrY3orjMABqnud5oKRCSZTJn2q";

    public MyTwitter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        if (!isTwitterInit) {
            TwitterSDK(this.activity);
        }
        Log.print("MyTwitter", "Contructro of TE fabric done");
    }


    public void login(final ResponseListener responseListenerTW) {

        client = new TwitterAuthClient();
        Log.print("MyTwitter", "login with Twitter");
        client.authorize(activity, new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                Log.print("MyTwitter", "Success");
                Log.print("MyTwitter Twitter Session ", result.data.toString());
                Pref.setValue(activity, Config.PREF_TWLOGGEDIN, true);
                Pref.setValue(activity, Config.PREF_TWSESSION, result.data);
                responseListenerTW.onResponce(Config.TWITTER_LOGIN_TAG, Config.RESULT_OK, "Success");
            }

            @Override
            public void failure(TwitterException e) {
                responseListenerTW.onResponce(Config.TWITTER_LOGIN_TAG, Config.RESULT_FAIL, e.getMessage());
                e.printStackTrace();
            }
        });
        Log.print("MyTwitter", "login complete");
    }

    public void sendTweet(final String Msg, final Bitmap bitmapImage, final ResponseListener responseListener) {

        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        if (session != null) {
            Log.print("MyTwitter", "before Async");

            new AsyncTask() {
                File photo;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Log.print("MyTwitter", "Before progress");
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        Date dt = new Date();
                        String filename = dt.getDate() + "_" + dt.getMonth() + "_" + dt.getYear() +
                                "_" + dt.getHours() + "_" + dt.getMinutes() + "_" + dt.getSeconds() + ".png";
                        Log.print("MyTwitter file Name ", filename);

                        photo = writeInPng(filename, bitmapImage);

                    } catch (Exception e) {
                        Log.print("MyTwitter Exception ", e.toString());
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                    TypedFile typedFile = new TypedFile("application/octet-stream", photo);
                    MediaService ms = twitterApiClient.getMediaService();
                    ms.upload(typedFile, null, null, new Callback<Media>() {
                        @Override
                        public void success(Result<Media> mediaResult) {
                            StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
                            statusesService.update(Msg, null, false, null, null, null, true, false, mediaResult.data.mediaIdString, new Callback<Tweet>() {
                                @Override
                                public void success(Result<Tweet> tweetResult) {
                                    if (photo != null) {
                                        photo.delete();
                                    }
                                    responseListener.onResponce(Config.TWITTER_TWEET_TAG, Config.RESULT_OK, "Success");
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    responseListener.onResponce(Config.TWITTER_TWEET_TAG, Config.RESULT_FAIL, e.toString());
                                }
                            });
                        }

                        @Override
                        public void failure(TwitterException e) {
                            responseListener.onResponce(Config.TWITTER_TWEET_TAG, Config.RESULT_FAIL, e.toString());
                        }
                    });
                }
            }.execute(null, null, null);
        } else {
            Toast.makeText(context, "Your Are Not Login", Toast.LENGTH_LONG).show();
        }
    }

    public boolean logout() {
        try {
            Log.print("MyTwitter Twitter ", Twitter.getInstance().toString());
//            Log.print("MyTwitter TW Client ", TwitterCore.getInstance().getApiClient().toString());
//            Log.print("MyTwitter TW Session ", TwitterCore.getInstance().getSessionManager().getActiveSession().toString());
            TwitterCore.getInstance().logOut();
            //Log.print("MyTwitter TW Session ", TwitterCore.getInstance().getSessionManager().getActiveSession().toString());
//                 TwitterCore.getInstance().getSessionManager().clearActiveSession();
            //objLoginUtl.setTwitterLogin(false);
            Pref.setValue(activity, Config.PREF_TWLOGGEDIN, false);
            client = null;
            return true;
        } catch (Exception e) {
            Log.print("MyTwitter Exception " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private File writeInPng(String name, Bitmap bitmap) {
        File f = new File(context.getExternalFilesDir(null), name);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void TwitterSDK(Activity activity) {
        try {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(activity, new Twitter(authConfig), new TweetComposer());//
        } catch (Exception e) {
            Log.print("MyTwitter Exception ", e.getMessage());
            e.printStackTrace();
        }
        isTwitterInit = true;
    }

    public void getProfile(final ResponseListener responseListenerTW) {
        Log.print("MyTwitter Get Profile");
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        twitterApiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                ProfileBean profileBean = new ProfileBean();
                profileBean.id = String.valueOf(userResult.data.id);
                profileBean.name = userResult.data.name;
                profileBean.email = userResult.data.email;
                profileBean.profile_pic = userResult.data.profileImageUrl;

                responseListenerTW.onResponce(Config.TWITTER_PROFILE_TAG, Config.RESULT_OK, profileBean);
            }

            @Override
            public void failure(TwitterException e) {
                Log.print("TwitterKit Verify Credentials Failure", e.toString());
                responseListenerTW.onResponce(Config.TWITTER_PROFILE_TAG, Config.RESULT_FAIL, e.getMessage());
            }
        });
    }

    public void checkSession() {

        String activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession().toString();
        Log.print("MyTwitter activeSession", activeSession);
        //Twitter.sharedInstance().sessionStore.session();

        String UNKNOWN_USER_NAME = TwitterSession.UNKNOWN_USER_NAME;
        Log.print("MyTwitter UNKNOWN_USER_NAME", UNKNOWN_USER_NAME);

    }
}
