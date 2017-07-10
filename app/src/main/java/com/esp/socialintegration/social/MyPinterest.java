package com.esp.socialintegration.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 2/1/17.
 */

public class MyPinterest {

    private Context context;
    private Activity activity;
    public static boolean isPinterestPDKinit = false;

    private static final String PINTEREST_APP_ID = "4876627695001159652";
    public PDKClient pdkClient;
    public PDKCallback pdkCallback;
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio";


    public MyPinterest(Activity activity) {

        System.out.println("=====================Constructor");

        this.context = activity.getApplicationContext();
        this.activity = activity;

        if (!isPinterestPDKinit) {
            PinterestPDKinit(this.context);
        }
    }

    public void login(final ResponseListener responseListener) {
        Log.print("MyPinterest", "login()");
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);

        //pdkClient = PDKClient.getInstance();

        pdkClient.login(activity, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.print("MyPinterest", response.getData().toString());
                Pref.setValue(activity, Config.PREF_PILOGGEDIN, true);
                responseListener.onResponce(Config.PINTEREST_LOGIN_TAG, Config.RESULT_OK, "Success");
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.print("MyPinterest On Failure", exception.getDetailMessage());
                responseListener.onResponce(Config.PINTEREST_LOGIN_TAG, Config.RESULT_FAIL, exception.getDetailMessage());
            }
        });
    }

    public void getUserDetails(final ResponseListener responseListener) {

        PDKClient.getInstance().getMe(USER_FIELDS, new PDKCallback() {


            @Override
            public void onSuccess(PDKResponse response) {

                Log.print("Response", String.format("status: %d", response.getStatusCode()));
                ProfileBean profileBean = new ProfileBean();

                profileBean.id = response.getUser().getUid();
                profileBean.name = response.getUser().getFirstName() + response.getUser().getLastName();
                //profileBean.email = response.getUser();
                //profileBean.link = response.getUser();
                profileBean.last_name = response.getUser().getLastName();
                profileBean.first_name = response.getUser().getFirstName();
                //profileBean.gender = response.getUser();
                profileBean.profile_pic = response.getUser().getImageUrl();
                System.out.println("name " + profileBean.name);

                responseListener.onResponce(Config.PINTEREST_PROFILE_TAG, Config.RESULT_OK, profileBean);

            }

            @Override
            public void onFailure(PDKException exception) {
                responseListener.onResponce(Config.PINTEREST_PROFILE_TAG, Config.RESULT_OK, exception.getDetailMessage());
            }
        });
    }

    public void getBoard() {
        PDKClient.getInstance().getMyBoards("id,name,description", new PDKCallback() {

            @Override
            public void onSuccess(PDKResponse response) {

                Log.print("getBoard Response", String.format("status: %d", response.getStatusCode()));
                Log.print("getBoard lenth", String.valueOf(response.getBoardList().toArray().length));

                for (int i = 0; i < response.getBoardList().toArray().length; i++) {
                    Log.print("getBoard response", response.getBoardList().get(i).getName());
                    Log.print("getBoard response", response.getBoardList().get(i).getUid());
                }

            }

            @Override
            public void onFailure(PDKException exception) {
                Log.print("getBoard fail", exception.getDetailMessage());
            }
        });
    }

    public void createPin(final String message, final String imageUrl, final Bitmap bitmapImage, final ResponseListener responseListener) {
        pdkClient.createPin(message, "440227001014933367", imageUrl, "http://google.co.in", new PDKCallback() {
//781374672787760941 name upen
//781374741507230688 board sample upen
// 440227001014933367 dudud espl

            @Override
            public void onSuccess(PDKResponse response) {

                Log.print("MyPinterest PINTEREST_PIN_TAG", response.toString());

                responseListener.onResponce(Config.PINTEREST_PIN_TAG, Config.RESULT_OK, "Success");

            }

            @Override
            public void onFailure(PDKException exception) {
                Log.print("MyPinterest PINTEREST_PIN_TAG", exception.getDetailMessage());
                responseListener.onResponce(Config.PINTEREST_PIN_TAG, Config.RESULT_OK, exception.getDetailMessage());
            }
        },"BASE64");
        //createPin(String note, String boardId, String imageUrl, String link, PDKCallback callback)

    }


    public boolean logout() {

        try {
            pdkClient.logout();
            Pref.setValue(activity, Config.PREF_PILOGGEDIN, false);
            return true;
        } catch (Exception e) {
            Log.print("MyPinterest logout", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }

    }

    public void PinterestPDKinit(Context context) {
        try {
            // Call configureInstance() method with context and App Id
            pdkClient = PDKClient.configureInstance(context, PINTEREST_APP_ID);
            // Call onConnect() method to make link between App id and Pinterest SDK
            pdkClient.onConnect(context);
            pdkClient.setDebugMode(true);
            isPinterestPDKinit = true;
        } catch (Exception e) {
            Log.print("MyPinterest init", e.toString());
        }
    }

    public void createPin1(String message, Uri imageUri) {
//        File imageFileToShare = new File(orgimagefilePath);

//        Uri uri = Uri.fromFile(imageFileToShare);

        Intent sharePintrestIntent = new Intent(Intent.ACTION_SEND);
        sharePintrestIntent.setPackage("com.pinterest");
        sharePintrestIntent.putExtra("com.pinterest.EXTRA_DESCRIPTION", message);
        sharePintrestIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharePintrestIntent.setType("image*//**//*");
        //startActivityForResult(sharePintrestIntent, PINTEREST);
    }
}
