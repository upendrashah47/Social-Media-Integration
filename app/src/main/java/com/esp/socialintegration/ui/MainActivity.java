package com.esp.socialintegration.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.esp.socialintegration.R;
import com.esp.socialintegration.backend.CustomDialogListener;
import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.social.MyFacebook;
import com.esp.socialintegration.social.MyGoogleplus;
import com.esp.socialintegration.social.MyLinkedin;
import com.esp.socialintegration.social.MyPinterest;
import com.esp.socialintegration.social.MyTwitter;
import com.esp.socialintegration.uc.CustomDialog;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.esp.socialintegration.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.internal.CallbackManagerImpl;
import com.pinterest.android.pdk.PDKClient;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txvFbUser, txvTwUser, txvPiUser, txvGpUser;
    private ImageView imgFbProfile, imgPiProfile, imgTwProfile, imgSelected, imgGpProfile, imgLiProfile;
    //    private Button btnFbLogin, btnTwLogin, btnPiLogin, btnGpLogin;
    private Button btnBrowse, btnPost, btnTest;
    private EditText edtComposeMessage;
    public Bitmap bitmapImage;
    public String message;
    public ProgressDialog progressDialog;
    public Uri imageUri;

    private CustomDialog customDialog;
    private ProfileBean profileBean;
    private MyFacebook myFacebook;
    private MyTwitter myTwitter;
    private MyPinterest myPinterest;
    private MyGoogleplus myGoogleplus;
    private MyLinkedin myLinkedin;
    private PDKClient pdkClient;
    private static final String appID = "4876627695001159652";


    public int GALLERY_PERMISSION_CODE = 2222;
    public long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_home);

        Log.print("MainActivity", "onCreate");

        myFacebook = new MyFacebook(this);
        myTwitter = new MyTwitter(this);
        myPinterest = new MyPinterest(this);
        myGoogleplus = new MyGoogleplus(this, this);
        myLinkedin = new MyLinkedin(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        customDialog = new CustomDialog();

        bindViews();
        isLoggedIn();

        if (!hasPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }

        pdkClient = PDKClient.configureInstance(this, appID);
        pdkClient.onConnect(this);
        pdkClient.setDebugMode(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgFacebook:

                LoginLogout(Config.PREF_FBLOGGEDIN, "Facebook", 1, imgFbProfile, R.drawable.home_facebook);
                break;

            case R.id.imgTwiteer:

                LoginLogout(Config.PREF_TWLOGGEDIN, "Twitter", 2, imgTwProfile, R.drawable.home_twitter);
                break;

            case R.id.imgPintrest:

                LoginLogout(Config.PREF_PILOGGEDIN, "Pinterest", 3, imgPiProfile, R.drawable.home_pingrest);
                break;

            case R.id.imgGoogle:

                LoginLogout(Config.PREF_GPLOGGEDIN, "Google Plus", 4, imgGpProfile, R.drawable.home_google);
                break;

            case R.id.imgLinkedin:

                LoginLogout(Config.PREF_LILOGGEDIN, "LinkedIn", 5, imgLiProfile, R.drawable.home_linkedin);
                break;

            case R.id.btnBrowse:

                Intent intentBrowse = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentBrowse, Config.GALLARY_REQUEST);
                break;

            case R.id.btnPost:

                message = edtComposeMessage.getText().toString();
                Log.print("MainActivity PREF_FBLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_FBLOGGEDIN, false)));
                Log.print("MainActivity PREF_TWLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_TWLOGGEDIN, false)));
                Log.print("MainActivity PREF_PILOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_PILOGGEDIN, false)));
                Log.print("MainActivity PREF_GPLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_GPLOGGEDIN, false)));
                Log.print("MainAvtivity PREF_LILOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_LILOGGEDIN, false)));

                if (Utils.isOnline(this)) {

                    if (Pref.getValue(this, Config.PREF_FBLOGGEDIN, false) || Pref.getValue(this, Config.PREF_TWLOGGEDIN, false) ||
                            Pref.getValue(this, Config.PREF_PILOGGEDIN, false) || Pref.getValue(this, Config.PREF_GPLOGGEDIN, false) ||
                            Pref.getValue(this, Config.PREF_LILOGGEDIN, false)) {

                        if (message == "" || bitmapImage == null) {
                            Toast.makeText(this, "Image or Text cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            if (Pref.getValue(this, Config.PREF_FBLOGGEDIN, false)) {
                                myFacebook.postStatusUpdate(message, bitmapImage, responseListener);
                            }
                            if (Pref.getValue(this, Config.PREF_TWLOGGEDIN, false)) {
                                myTwitter.sendTweet(message, bitmapImage, responseListener);
                            }
                            if (Pref.getValue(this, Config.PREF_PILOGGEDIN, false)) {
                                String img = encodeTobase64(bitmapImage);
                                myPinterest.createPin(message, img, bitmapImage, responseListener);
                            }
                            if (Pref.getValue(this, Config.PREF_GPLOGGEDIN, false)) {
                                myGoogleplus.sharePost(message, imageUri);
                            }
                            if (Pref.getValue(this, Config.PREF_LILOGGEDIN, false)) {
                                String img = encodeTobase64(bitmapImage);
                                myLinkedin.post(message, img, responseListener);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Turn on Internet", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnTest:

//                myFacebook.checkAccesstoken();
                // MyFacebook expirationTime :: Sat Mar 04 14:33:10 GMT+05:30 2017 on 7th jan emu sdk 24
                //myTwitter.checkSession();
//                myPinterest.getBoard();

//                Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
//                startActivity(intent);
                myFacebook.getFriendList();

//                myFacebook.getFriendList();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.print("MainActivity onActivityResult Request Code " + requestCode, "Result Code " + resultCode);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (requestCode == Config.FACEBOOK_REQUEST) {

            if (resultCode == RESULT_OK) {
                Log.print("share", String.valueOf(CallbackManagerImpl.RequestCodeOffset.Share.toRequestCode()));
                Log.print("share", String.valueOf(CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()));
                if (FacebookSdk.isFacebookRequestCode(requestCode)) {
                    if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
                        Log.print("MAinACtivity onActivityResult", "Facebook");
                        if (myFacebook != null) {
                            myFacebook.mCallbackManager.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }

        } else if (requestCode == Config.TWITTER_REQUEST) {

            if (resultCode == RESULT_OK) {
                myTwitter.client.onActivityResult(requestCode, resultCode, data);
            }

        } else if (requestCode == Config.PINTEREST_REQUEST) {

            if (resultCode == RESULT_OK) {
                myPinterest.pdkClient.onOauthResponse(requestCode, resultCode, data);
            }

        } else if (requestCode == Config.GOOGLEPLUS_POST) {

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (resultCode == RESULT_OK) {
                Log.print("Google+ share success");
            } else {
                Log.print("Google+ share fail");
            }

        } else if (requestCode == Config.GOOGLEPLUS_REQUEST) {

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (resultCode == RESULT_OK) {
                myGoogleplus.loginCheck(data, responseListener);
            }

        } else if (requestCode == Config.LINKEDIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                myLinkedin.liSessionManager.onActivityResult(this, requestCode, resultCode, data);
            }
        } else if (requestCode == Config.GALLARY_REQUEST) {

            if (resultCode == RESULT_OK) {
                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(FILE[0]);
                String ImageDecode = cursor.getString(columnIndex);
                cursor.close();

//                imageUrl = ImageDecode;
                imageUri = data.getData();
                bitmapImage = BitmapFactory.decodeFile(ImageDecode);
                imgSelected.setImageURI(imageUri);
            }
        }

    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(final String tag, int result, Object obj) {

            Log.print("MainActivity onResponce ", "Tag :" + tag + " result :" + result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (tag == Config.FACEBOOK_LOGIN_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Facebook Login " + obj, Toast.LENGTH_LONG).show();
                    myFacebook.getProfile(responseListener);
                } else {

                    Log.print("MainActivity FACEBOOK_LOGIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Facebook Login " + obj, Toast.LENGTH_LONG).show();
                }
            } else if (tag == Config.TWITTER_LOGIN_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Twitter Login " + obj, Toast.LENGTH_LONG).show();
                    myTwitter.getProfile(responseListener);
                } else {

                    Log.print("MainActivity TWITTER_LOGIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Twitter Login " + obj, Toast.LENGTH_LONG).show();
                }
            } else if (tag == Config.PINTEREST_LOGIN_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Pinterest Login " + obj, Toast.LENGTH_LONG).show();
                    myPinterest.getUserDetails(responseListener);
                } else {

                    Log.print("MainActivity PINTEREST_LOGIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Pinterest Login " + obj, Toast.LENGTH_LONG).show();
                }
            } else if (tag == Config.GOOGLEPLUS_LOGIN_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Google Plus Login " + obj, Toast.LENGTH_LONG).show();
                    myGoogleplus.getProfile(responseListener);
                } else {

                    Log.print("MainActivity GOOGLEPLUS_LOGIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Google Plus Login " + obj, Toast.LENGTH_LONG).show();
                }
            } else if (tag == Config.LINKEDIN_LOGIN_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Linkedin Login " + obj, Toast.LENGTH_LONG).show();
                    myLinkedin.getUserData(responseListener);
                } else {
                    Log.print("MainActivity LINKEDIN_LOGIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Linkedin Login " + obj, Toast.LENGTH_LONG).show();
                }
            } else if (tag == Config.FACEBOOK_PROFILE_TAG) {
                if (result == Config.RESULT_OK) {
                    profileBean = (ProfileBean) obj;
                    Glide.with(MainActivity.this).load(profileBean.profile_pic).asBitmap().centerCrop().placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new BitmapImageViewTarget(imgFbProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            imgFbProfile.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
                } else {
                    Log.print("MainActivity FACEBOOK_PROFILE_TAG", obj.toString());
                }

            } else if (tag == Config.TWITTER_PROFILE_TAG) {
                if (result == Config.RESULT_OK) {
                    profileBean = (ProfileBean) obj;
                    Glide.with(MainActivity.this).load(profileBean.profile_pic).asBitmap().centerCrop().placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new BitmapImageViewTarget(imgTwProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            imgTwProfile.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
                } else {
                    Log.print("MainActivity TWITTER_PROFILE_TAG", obj.toString());
                }

            } else if (tag == Config.PINTEREST_PROFILE_TAG) {
                if (result == Config.RESULT_OK) {
                    profileBean = (ProfileBean) obj;
                    Glide.with(MainActivity.this).load(profileBean.profile_pic).asBitmap().centerCrop().placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new BitmapImageViewTarget(imgPiProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            imgPiProfile.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
                } else {
                    Log.print("MainActivity PINTEREST_PROFILE_TAG", obj.toString());
                }

            } else if (tag == Config.GOOGLEPLUS_PROFILE_TAG) {
                if (result == Config.RESULT_OK) {
                    profileBean = (ProfileBean) obj;
                    Glide.with(MainActivity.this).load(profileBean.profile_pic).asBitmap().centerCrop().placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new BitmapImageViewTarget(imgGpProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            imgGpProfile.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
                } else {
                    Log.print("MainActivity GOOGLEPLUS_PROFILE_TAG", obj.toString());
                }

            } else if (tag == Config.LINKEDIN_PROFILE_TAG) {
                if (result == Config.RESULT_OK) {
                    profileBean = (ProfileBean) obj;
                    Glide.with(MainActivity.this).load(profileBean.profile_pic).asBitmap().centerCrop().placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new BitmapImageViewTarget(imgLiProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            imgLiProfile.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
                } else {
                    Log.print("MainActivity LINKEDIN_PROFILE_TAG", obj.toString());
                }
            } else if (tag == Config.FACEBOOK_POST_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Facebook Post " + obj, Toast.LENGTH_LONG).show();
                } else {
                    Log.print("MainActivity FACEBOOK_POST_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Facebook Post " + obj, Toast.LENGTH_LONG).show();
                }

            } else if (tag == Config.TWITTER_TWEET_TAG) {
                if (result == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Tweeter Tweet " + obj, Toast.LENGTH_LONG).show();
                } else {
                    Log.print("MainActivity TWITTER_TWEET_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Tweeter Tweet " + obj, Toast.LENGTH_LONG).show();
                }

            } else if (tag == Config.PINTEREST_PIN_TAG) {
                if (result == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Pintrest Pin " + obj, Toast.LENGTH_LONG).show();
                } else {
                    Log.print("MainActivity PINTEREST_PIN_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Pinterest Pin " + obj, Toast.LENGTH_LONG).show();
                }

            } else if (tag == Config.LINKEDIN_POST_TAG) {
                if (result == Config.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Linkedin Post " + obj, Toast.LENGTH_LONG).show();
                } else {
                    Log.print("MainActivity LINKEDIN_POST_TAG", obj.toString());
                    Toast.makeText(MainActivity.this, "Linkedin Post " + obj, Toast.LENGTH_LONG).show();
                }

            }
        }
    };


    public void bindViews() {
        txvFbUser = (TextView) findViewById(R.id.txvFbUser);
        txvTwUser = (TextView) findViewById(R.id.txvTwUser);
        txvPiUser = (TextView) findViewById(R.id.txvPiUser);
        txvGpUser = (TextView) findViewById(R.id.txvGpUser);

        imgFbProfile = (ImageView) findViewById(R.id.imgFacebook);
        imgPiProfile = (ImageView) findViewById(R.id.imgPintrest);
        imgTwProfile = (ImageView) findViewById(R.id.imgTwiteer);
        imgGpProfile = (ImageView) findViewById(R.id.imgGoogle);
        imgLiProfile = (ImageView) findViewById(R.id.imgLinkedin);
        imgSelected = (ImageView) findViewById(R.id.imgSelected);

        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnTest = (Button) findViewById(R.id.btnTest);

        edtComposeMessage = (EditText) findViewById(R.id.edtComposeMessage);

        btnBrowse.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnTest.setOnClickListener(this);
    }

    public void isLoggedIn() {

        Log.print("MainActivity onCreate PREF_FBLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_FBLOGGEDIN, false)));
        progressDialog.show();
        if (Pref.getValue(this, Config.PREF_FBLOGGEDIN, false)) {
            Log.print("onCreate if FB");
            myFacebook.getProfile(responseListener);
        } else {
            Log.print("onCreate Else FB");
            imgFbProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_facebook));
        }
        isTWLoggedIn();

    }

    public void isTWLoggedIn() {

        Log.print("MainActivity onCreate PREF_TWLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_TWLOGGEDIN, false)));
        if (Pref.getValue(this, Config.PREF_TWLOGGEDIN, false)) {
            Log.print("onCreate if TW");
            myTwitter.getProfile(responseListener);
        } else {
            Log.print("onCreate Else TW");
            imgTwProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_twitter));
        }
        isPILoggedIn();

    }

    public void isPILoggedIn() {

        Log.print("MainActivity onCreate PREF_PILOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_PILOGGEDIN, false)));
        if (Pref.getValue(this, Config.PREF_PILOGGEDIN, false)) {
            Log.print("onCreate if PI");
            myPinterest.getUserDetails(responseListener);
        } else {
            Log.print("onCreate Else PI");
            imgPiProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_pingrest));
        }
        isGPLoggedIn();

    }

    public void isGPLoggedIn() {

        Log.print("MainActivity onCreate PREF_GPLOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_GPLOGGEDIN, false)));
        if (Pref.getValue(this, Config.PREF_GPLOGGEDIN, false)) {
            Log.print("onCreate if GP");
            myGoogleplus.getProfile(responseListener);
        } else {
            Log.print("onCreate Else GP");
            imgGpProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_google));
        }
        isLILoggedIn();

    }

    public void isLILoggedIn() {

        Log.print("MainActivity onCreate PREF_LILOGGEDIN", String.valueOf(Pref.getValue(this, Config.PREF_LILOGGEDIN, false)));
        if (Pref.getValue(this, Config.PREF_LILOGGEDIN, false)) {
            Log.print("onCreate if LI");
            myLinkedin.getUserData(responseListener);
        } else {
            Log.print("onCreate Else LI");
            imgLiProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_linkedin));
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void LoginLogout(String key, final String title, int tag, final ImageView imageView, final int defaultPicture) {
        Log.print("MainAvtivity " + key, String.valueOf(Pref.getValue(this, key, false)));

        if (!Pref.getValue(this, key, false)) {
            customDialog.show(this, title, "Are you sure to login?", "Login", "Cancel", tag, new CustomDialogListener() {
                @Override
                public void onAlert(int tag, int code) {
                    if (code == Config.ALERT_LOGIN) {
                        progressDialog.show();
                        if (Utils.isOnline(MainActivity.this)) {
                            Log.print("MainAvtivity ", "Login " + title);
                            if (tag == 1) {
                                if (progressDialog != null) {
                                    progressDialog.hide();
                                }
                                myFacebook.login(responseListener);
                            } else if (tag == 2) {
                                myTwitter.login(responseListener);
                            } else if (tag == 3) {
                                myPinterest.login(responseListener);
                            } else if (tag == 4) {
                                myGoogleplus.login();
                            } else if (tag == 5) {
                                myLinkedin.login(responseListener);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Turn on Internet", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            });
        } else {
            customDialog.show(this, title, "Are you sure to logout?", "Logout", "Cancel", tag, new CustomDialogListener() {
                @Override
                public void onAlert(int tag, int code) {
                    if (code == Config.ALERT_LOGOUT) {
                        Log.print("MainAvtivity ", "Logout " + title);
                        if (tag == 1) {
                            myFacebook.logout();
                        } else if (tag == 2) {
                            myTwitter.logout();
                        } else if (tag == 3) {
                            myPinterest.logout();
                        } else if (tag == 4) {
                            myGoogleplus.logout();
                        } else if (tag == 5) {
                            myLinkedin.logout();
                        }
                        imageView.setImageDrawable(getResources().getDrawable(defaultPicture));
                    }
                }
            });
        }
    }

    public static String encodeTobase64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.NO_WRAP);

    }


    String[] permList = new String[]{INTERNET, GET_ACCOUNTS, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private void requestPermission() {

        if (checkPermission()) {
            Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
            Log.print("Check Permission", "All permission granted");
        } else {
            ActivityCompat.requestPermissions(this, permList, Config.PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permList[1]);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("====Permission read storage");
            }
        }
    }
}
