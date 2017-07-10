package com.esp.socialintegration.utils;

import android.os.Environment;

public class Config {
    public static String TAG = "SocialIntegration";
    public static String DB_NAME = "SocialIntegration.db";
    // Create a directory in SD CARD
    public static String APP_HOME = Environment.getExternalStorageDirectory().getPath() + "/" + TAG;
    // A directory to store logs
    public static String DIR_LOG = APP_HOME + "/log";
    // preference file name
    public static final String PREF_FILE = TAG + "_PREF";
    public static String DIR_USERDATA = APP_HOME + "/userdata";


    // LOCAL
    public static String HOST = "http://192.168.1.42/work/zebra";
    // Live
    //public static String HOST = "http://zebra.esprit-apps.com";
    public static String HOST_IMAGE = HOST + "/public/media/";
    public static String IMAGE_PATH_WEB_AVATARS = HOST_IMAGE + "avatar/";
    public static String IMAGE_PATH_WEB_WALLPAPERS = HOST_IMAGE + "wallpaper/";
    public static String IMAGE_PATH_WEB_BANNER = HOST_IMAGE + "banner/";
    public static String IMAGE_PATH_ICON = HOST_IMAGE + "feed_style/";


    public static boolean isOpen = false;
    // LIVE
//    public static String HOST = "http://esprit.co.in/inkskill";


    //for signup
    public static final String API_REGISTRATION = "/users/signup";
    public static String TAG_REGISTRATION = "TAG_REGISTRATION";


    // 1 = Android, 2 = Ios
    public static int DEVICE_TYPE = 1;

    // connection timeout is set to 20 seconds
    public static int TIMEOUT_CONNECTION = 20000;

    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 60000;
    /*
     * Cookie and SESSION
     */
    public static String PREF_SESSION_COOKIE = "sessionid";
    public static String SET_COOKIE_KEY = "Set-Cookie";
    public static String COOKIE_KEY = "Cookie";
    public static String SESSION_COOKIE = "sessionid";


    public static final String TWITTER_KEY = "XkyldoVomQCw9aGuyjYGJqKNo";
    public static final String TWITTER_SECRET = "PSgrvasz2rf9Hoja8le8EpHSUrY3orjMABqnud5oKRCSZTJn2q";

    /*
    * Pref variables
    * */
    public static String PREF_UDID = "PREF_UDID";
    public static String PREF_CODE = "PREF_CODE";
    public static String PREF_MESG = "PREF_MESG";

    public static final String PREF_FBLOGGEDIN = "PREF_FBLOGGEDIN";
    public static final String PREF_FBLOGINSTATE = "PREF_FBLOGINSTATE";
    public static final String PREF_FBACCESSTOKEN = "PREF_FBACCESSTOKEN";

    public static final String PREF_TWLOGGEDIN = "PREF_TWLOGGEDIN";
    public static final String PREF_TWLOGINSTATE = "PREF_TWLOGINSTATE";
    public static final String PREF_TWSESSION = "PREF_TWSESSION";

    public static final String PREF_GPLOGGEDIN = "PREF_GPLOGGEDIN";
    public static final String PREF_GPLOGINSTATE = "PREF_GPLOGINSTATE";
    public static final String PREF_GPACCESSTOKEN = "PREF_GPACCESSTOKEN";

    public static final String PREF_PILOGGEDIN = "PREF_PILOGGEDIN";
    public static final String PREF_PILOGINSTATE = "PREF_PILOGINSTATE";
    public static final String PREF_PIACCESSTOKEN = "PREF_PIACCESSTOKEN";

    public static final String PREF_LILOGGEDIN = "PREF_LILOGGEDIN";
    public static final String PREF_LILOGINSTATE = "PREF_LILOGINSTATE";
    public static final String PREF_LIACCESSTOKEN = "PREF_LIACCESSTOKEN";

    public static String FACEBOOK_LOGIN_TAG = "FACEBOOK_LOGIN_TAG";
    public static String FACEBOOK_POST_TAG = "FACEBOOK_POST_TAG";
    public static String FACEBOOK_PROFILE_TAG = "FACEBOOK_PROFILE_TAG";

    public static String TWITTER_LOGIN_TAG = "TWITTER_LOGIN_TAG";
    public static String TWITTER_TWEET_TAG = "TWITTER_TWEET_TAG";
    public static String TWITTER_PROFILE_TAG = "TWITTER_PROFILE_TAG";

    public static String PINTEREST_LOGIN_TAG = "PINTEREST_LOGIN_TAG";
    public static String PINTEREST_PIN_TAG = "PINTEREST_PIN_TAG";
    public static String PINTEREST_PROFILE_TAG = "PINTEREST_PROFILE_TAG";

    public static String GOOGLEPLUS_LOGIN_TAG = "GOOGLEPLUS_LOGIN_TAG";
    public static String GOOGLEPLUS_POST_TAG = "GOOGLEPLUS_POST_TAG";
    public static String GOOGLEPLUS_PROFILE_TAG = "GOOGLEPLUS_PROFILE_TAG";

    public static String LINKEDIN_LOGIN_TAG = "LINKEDIN_LOGIN_TAG";
    public static String LINKEDIN_POST_TAG = "LINKEDIN_POST_TAG";
    public static String LINKEDIN_PROFILE_TAG = "LINKEDIN_PROFILE_TAG";

    public static int FACEBOOK_REQUEST = 130;
    public static int TWITTER_REQUEST = 140;
    public static int PINTEREST_REQUEST = 8772;
    public static int GOOGLEPLUS_REQUEST = 150;
    public static int GOOGLEPLUS_POST = 151;
    public static int LINKEDIN_REQUEST = 3672;

    public static int RESULT_OK = 0;
    public static int RESULT_FAIL = 1;
    public static int ALERT_LOGIN = 100;
    public static int ALERT_LOGOUT = 100;


    public static int GALLARY_REQUEST = 11;
    public static int CAMERA_REQUEST = 22;

    public static int PERMISSION_REQUEST_CODE = 500;

    public static int API_SUCCESS = 0;
    public static int API_FAIL = 1;
    public static int VIEWPOST_CODE = 450;


}