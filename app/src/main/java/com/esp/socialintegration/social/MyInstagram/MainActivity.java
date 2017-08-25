package com.esp.socialintegration.social.MyInstagram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.esp.socialintegration.R;
import com.esp.socialintegration.utils.Config;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, InstagramApp.OAuthAuthenticationListener {

    private Context context;
    private InstagramApp mApp;
    private Button btnLogin, btnViewInfo;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

//        btnLogin = (Button) findViewById(R.id.btnLogin);

//        mApp = new InstagramApp(context, Config.CLIENT_ID, Config.CLIENT_SECRET, Config.CALLBACK_URL);
        mApp.setListener(this);

        if (mApp.hasAccessToken()) {
            btnLogin.setText("Logout");
            mApp.fetchUserName(handler);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btnLogin:
//                connectOrDisconnectUser();
//                break;
//
//            case R.id.btnViewInfo:
//                displayInfoDialogView();
//                break;
        }
    }

    @Override
    public void onSuccess() {
//        ((Button) findViewById(R.id.btnLogin)).setText("Logout");
        mApp.fetchUserName(handler);
    }

    @Override
    public void onFail(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
// btnConnect.setVisibility(View.VISIBLE);
                                    btnLogin.setText("Login");
// tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }

//    private void displayInfoDialogView() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//        alertDialog.setTitle("Profile Info");
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.profile_view, null);
//        alertDialog.setView(view);
//
//        ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfileImage);
//        TextView tvName = (TextView) view.findViewById(R.id.tvUserName);
//        TextView tvNoOfFollwers = (TextView) view.findViewById(R.id.tvNoOfFollowers);
//        TextView tvNoOfFollowing = (TextView) view.findViewById(R.id.tvNoOfFollowing);
//
////        new ImageLoader(MainActivity.this).DisplayImage(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE), ivProfile);
//
//        tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
//        tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
//        tvNoOfFollwers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
//        alertDialog.create().show();
//    }
}

