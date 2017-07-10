package com.esp.socialintegration.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;

import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;

import java.io.File;

/**
 * Created by user on 5/1/17.
 */

public class MyGoogleplus implements GoogleApiClient.OnConnectionFailedListener {

    private static Activity activity;
    private Context context;
    public static GoogleSignInAccount googleSignInAccount;
    public static GoogleApiClient mGoogleApiClient;
    public static boolean isGooglePlusinit = false;
    private FragmentActivity fragmentActivity;
    public Intent profileIntent;


    public MyGoogleplus(Activity activity, FragmentActivity fragmentActivity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.fragmentActivity = fragmentActivity;

        if (!isGooglePlusinit) {
            GooglePlusInit(activity, fragmentActivity);
        }
    }


    public void login() {

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        Log.print("MyGoogleplus", "client connected");
        Log.print("MyGoogleplus", "google plus intent");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, Config.GOOGLEPLUS_REQUEST);

    }

    public static void sendGP(String msg, double lat, double lng) {

        if (!MyGoogleplus.mGoogleApiClient.isConnected()) {
            Log.print("MyGoogleplus", "Google not Connected");
        }
//        if (MyGooglePlus.mGoogleApiClient.hasConnectedApi(Plus.API)) {
        Intent shareIntent = new PlusShare.Builder(MyGoogleplus.activity)
                .setRecipients(Plus.PeopleApi.getCurrentPerson(MyGoogleplus.mGoogleApiClient), null)
                .setType("text/plain")
                .setText(msg)
                .setContentUrl(Uri.parse("http://www.google.com/maps/place/" + lat + "," + lng + "?center=true"))
                .getIntent();
        MyGoogleplus.activity.startActivityForResult(shareIntent, 0);
//         } else {
//            Toast.makeText(MyGooglePlus.activity.getApplicationContext(), "Not Connected with Google+", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.print("MyGoogleplus", " on connection faild");
    }


    public void logout() {
        Log.print("MyGoogleplus", "GP logout");
        if (Pref.getValue(activity, Config.PREF_GPLOGGEDIN, false)) {
//            Auth.GoogleSignInApi.signOut(MyGooglePlus.mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(MyGoogleplus.mGoogleApiClient);
            Plus.AccountApi.clearDefaultAccount(MyGoogleplus.mGoogleApiClient);
            Auth.GoogleSignInApi.revokeAccess(MyGoogleplus.mGoogleApiClient);
            Auth.GoogleSignInApi.signOut(MyGoogleplus.mGoogleApiClient);
            MyGoogleplus.mGoogleApiClient.disconnect();
            MyGoogleplus.mGoogleApiClient.connect();

            Pref.setValue(activity, Config.PREF_GPLOGGEDIN, false);
            Log.print("MyGoogleplus", "GP logout clear defulat a/c");
        } else {
            Log.print("MyGoogleplus", "in Else");
//            objLoginUtl.setGooglePlusLogin(false);
        }

    }

    /**
     * Method to resolve any signin errors
     */

    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(activity, Config.GOOGLEPLUS_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void loginCheck(Intent data, ResponseListener responseListener) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        profileIntent = data;

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            MyGoogleplus.googleSignInAccount = result.getSignInAccount();
            Pref.setValue(activity, Config.PREF_GPLOGGEDIN, true);
            Log.print("MyGoogleplus login check", Config.PREF_GPLOGGEDIN.toString());
            responseListener.onResponce(Config.GOOGLEPLUS_LOGIN_TAG, Config.RESULT_OK, "Success");

        } else {
            // Signed out, show unauthenticated UI.
            Log.print("MyGoogleplus", "sign Acccount : false");
            responseListener.onResponce(Config.GOOGLEPLUS_LOGIN_TAG, Config.RESULT_FAIL, "Fail");
        }
    }

    //from main activity's onActivity for result
   /* public void getProfile(ResponseListener responseListener) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(profileIntent);

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            MyGoogleplus.googleSignInAccount = result.getSignInAccount();

            Log.print("MyGoogleplus", "sign Acccount");
            Log.print("MyGoogleplus", "Name  : " + MyGoogleplus.googleSignInAccount.getDisplayName());
            Log.print("MyGoogleplus", "Email : " + MyGoogleplus.googleSignInAccount.getEmail());
            Log.print("MyGoogleplus", "photo : " + MyGoogleplus.googleSignInAccount.getPhotoUrl());
            Log.print("MyGoogleplus", "S code: " + MyGoogleplus.googleSignInAccount.getServerAuthCode());
            Log.print("MyGoogleplus", "Scope : " + MyGoogleplus.googleSignInAccount.getGrantedScopes());

            ProfileBean profileBean = new ProfileBean();
            profileBean.id = MyGoogleplus.googleSignInAccount.getId();
            profileBean.name = MyGoogleplus.googleSignInAccount.getDisplayName();
            profileBean.email = MyGoogleplus.googleSignInAccount.getEmail();
            profileBean.profile_pic = MyGoogleplus.googleSignInAccount.getPhotoUrl().toString();

            responseListener.onResponce(Config.GOOGLEPLUS_PROFILE_TAG, Config.RESULT_OK, profileBean);

        } else {
            // Signed out, show unauthenticated UI.
            Log.print("MyGoogleplus", "sign Acccount : false");
            responseListener.onResponce(Config.GOOGLEPLUS_PROFILE_TAG, Config.RESULT_FAIL, "Fail");
        }

    }*/

    public static void GooglePlusInit(Activity activity, FragmentActivity fragmentActivity) {
        Log.print("MyGoogleplus", "GooglePlusInit");
        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                    .requestEmail()
                    .requestProfile()
                    .requestScopes(Plus.SCOPE_PLUS_LOGIN)
                    .requestScopes(Plus.SCOPE_PLUS_PROFILE)
//                    .requestServerAuthCode("1041249556553-63hto2gf5ud3e7kupvate2gbeioj1pbd.apps.googleusercontent.com")
                    .build();

            MyGoogleplus.activity = activity;


            GoogleApiClient.OnConnectionFailedListener callbacks = new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.print("MyGoogleplus", " on connection faild");
                }
            };

            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .enableAutoManage(fragmentActivity /* FragmentActivity */, callbacks /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .build();

            isGooglePlusinit = true;
        }
    }

    public void getProfile(ResponseListener responseListener) {
        if (mGoogleApiClient != null) {

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            Log.print("MyGoogleplus", "Google connected : " + mGoogleApiClient.isConnected());

            try {
                ProfileBean profileBean = new ProfileBean();
                profileBean.id = googleSignInAccount.getId();
                profileBean.name = googleSignInAccount.getDisplayName();
                profileBean.email = googleSignInAccount.getEmail();
                profileBean.profile_pic = googleSignInAccount.getPhotoUrl().toString();

                responseListener.onResponce(Config.GOOGLEPLUS_PROFILE_TAG, Config.RESULT_OK, profileBean);

            } catch (Exception e) {
                Log.print("MyGoogleplus", "Exception : " + e.getMessage() + "::");
                e.printStackTrace();
                responseListener.onResponce(Config.GOOGLEPLUS_PROFILE_TAG, Config.RESULT_FAIL, e.getMessage());
            }
        } else {
            Log.print("MyGoogleplus", "return Google client null");
        }
    }


    public void sharePost(String message, Uri imageUri) {
        File pictureFile;

//        try {
//            File rootSdDirectory = Environment.getExternalStorageDirectory();
//
//            pictureFile = new File(rootSdDirectory, "attachment.jpg");
//            if (pictureFile.exists()) {
//                pictureFile.delete();
//            }
//            pictureFile.createNewFile();
//
//            FileOutputStream fos = new FileOutputStream(pictureFile);
//
//            URL url = new URL("http://img.youtube.com/vi/AxeOPU6n1_M/0.jpg");
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//            connection.connect();
//            InputStream in = connection.getInputStream();
//
//            byte[] buffer = new byte[1024];
//            int size = 0;
//            while ((size = in.read(buffer)) > 0) {
//                fos.write(buffer, 0, size);
//            }
//            fos.close();
//
//        } catch (Exception e) {
//
//            Log.print("MyGoogleplus share exception", e.toString());
//            // e.printStackTrace();
//            return;
//        }
//
//        Uri pictureUri = Uri.fromFile(pictureFile);
        try {
//            Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
//                    .setText(message)
//                    .setType("image/jpeg")
//                    .setStream(imageUri).getIntent()
//                    .setPackage("com.google.android.apps.plus");
            Intent shareIntent = new PlusShare.Builder(MyGoogleplus.activity)
                    .setRecipients(Plus.PeopleApi.getCurrentPerson(MyGoogleplus.mGoogleApiClient), null)
                    .setType("text/plain")
                    .setText(message)
                    .setStream(imageUri)
                    .getIntent();
            activity.startActivityForResult(shareIntent, Config.GOOGLEPLUS_POST);
        } catch (Exception e) {

        }
    }
}


