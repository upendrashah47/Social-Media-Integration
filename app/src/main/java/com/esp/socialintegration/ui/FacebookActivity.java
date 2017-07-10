package com.esp.socialintegration.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esp.socialintegration.R;
import com.esp.socialintegration.social.MyFacebook;

public class FacebookActivity extends AppCompatActivity {

    private MyFacebook myFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        myFacebook = new MyFacebook(this);

        myFacebook.getFriendList();
    }
}
