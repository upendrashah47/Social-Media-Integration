package com.esp.socialintegration.social;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.esp.socialintegration.backend.ResponseListener;
import com.esp.socialintegration.bean.ProfileBean;
import com.esp.socialintegration.utils.Config;
import com.esp.socialintegration.utils.Log;
import com.esp.socialintegration.utils.Pref;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

/**
 * Created by user on 4/1/17.
 */

public class MyLinkedin {

    private Activity activity;
    private Context context;
    public static boolean isLinkedInInit = false;
    public static LISessionManager liSessionManager;
    private static final String host = "api.linkedin.com";
    private static final String profileUrl = "https://" + host + "/v1/people/~:(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";


    public MyLinkedin(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        if (!isLinkedInInit) {
            LinkedInSDK(this.activity);
        }
    }

    public void login(final ResponseListener responseListener) {
        liSessionManager.init(activity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                //login_linkedin_btn.setVisibility(View.GONE);
                Log.print("MyLinkedin login success");
                Pref.setValue(activity, Config.PREF_LILOGGEDIN, true);
                responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_OK, "Success");
            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(context, "failed " + error.toString(), Toast.LENGTH_LONG).show();
                Log.print("MyLinkedin login failed", error.toString());
                responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_FAIL, error.toString());
            }
        }, true);
    }

    public void weblogin(final ResponseListener responseListener) {
        APIHelper apiHelper = APIHelper.getInstance(activity);
        apiHelper.getRequest(activity, "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=8105vu6dt4nuj0&redirect_uri=https%3A%2F%2Fwww.example.com%2Fauth%2Flinkedin&state=987654321&scope=r_basicprofile", new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    Log.print("web", result.toString());
                    Log.print("MyLinkedin login success");
                    Pref.setValue(activity, Config.PREF_LILOGGEDIN, true);
                    responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_OK, "Success");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.print("web exce", e.toString());
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
                Log.print("web execpi", error.toString());
                Toast.makeText(context, "failed " + error.toString(), Toast.LENGTH_LONG).show();
                Log.print("MyLinkedin login failed", error.toString());
                responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_FAIL, error.toString());

            }
        });
    }
    public void getTok(final ResponseListener responseListener) {
        String str = "{\"grant_type\":\"authorization_code\",\n" +
                "\"code\":\"987654321\",\n" +
                "\"redirect_uri\":\"https%3A%2F%2Fwww.myapp.com%2Fauth%2Flinkedin\",\n" +
                "\"client_id\":\"123456789\",\n" +
                "\"client_secret\":\"shhdonottell\"}";

        APIHelper apiHelper = APIHelper.getInstance(activity);
        apiHelper.postRequest(activity, "https://www.linkedin.com/oauth/v2/accessToken", "grant_type=authorization_code&code=987654321&redirect_uri=https%3A%2F%2Fwww.myapp.com%2Fauth%2Flinkedin&client_id=123456789&client_secret=shhdonottell", new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    Log.print("web", result.toString());
                    Log.print("MyLinkedin login success");
                    Pref.setValue(activity, Config.PREF_LILOGGEDIN, true);
                    responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_OK, "Success");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.print("web exce", e.toString());
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
                Log.print("web execpi", error.toString());
                Toast.makeText(context, "failed " + error.toString(), Toast.LENGTH_LONG).show();
                Log.print("MyLinkedin login failed", error.toString());
                responseListener.onResponce(Config.LINKEDIN_LOGIN_TAG, Config.RESULT_FAIL, error.toString());

            }
        });
    }

    public void getUserData(final ResponseListener responseListener) {
        APIHelper apiHelper = APIHelper.getInstance(activity);
        apiHelper.getRequest(activity, profileUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

//                    setUserProfile(result.getResponseDataAsJson());
//                    progress.dismiss();
                    JSONObject jsonObject = result.getResponseDataAsJson();
                    ProfileBean profileBean = new ProfileBean();
                    profileBean.name = jsonObject.getString("emailAddress").toString();
                    profileBean.email = jsonObject.getString("formattedName").toString();
                    profileBean.profile_pic = jsonObject.getString("pictureUrl").toString();

                    responseListener.onResponce(Config.LINKEDIN_PROFILE_TAG, Config.RESULT_OK, profileBean);


                } catch (Exception e) {
                    e.printStackTrace();
                    responseListener.onResponce(Config.LINKEDIN_PROFILE_TAG, Config.RESULT_FAIL, e.toString());
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
                responseListener.onResponce(Config.LINKEDIN_PROFILE_TAG, Config.RESULT_FAIL, error.toString());

            }
        });
    }

    public void post(String message, String imageUrl, final ResponseListener responseListener) {
        String url = "https://api.linkedin.com/v1/people/~/shares";

        String payload = "{" +
                "\"comment\":\"Check out developer.linkedin.com! " + "http://linkd.in/1FC2PyG\"," +
                "\"visibility\":{" +
                "    \"code\":\"anyone\"}" +
                "}";
        String comment, title, description, submitted_url, submitted_image_url;
        comment = "comment";
        title = "title";
        description = "description";
        submitted_url = "submitted_url";
        submitted_image_url = "submitted_image_url";

        String payload1 = "{" +
                "\"comment\":\"" + message + "\"," +
                "\"content\":{" +
                "\"title\":\"" + message + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"submitted-url\":\"" + submitted_url + "\"," +
                "\"submitted-image-url\":\"" + imageUrl + "\"}," +
                "\"visibility\":{" +
                "\"code\":\"anyone\"}" +
                "}";

        String payload2 = "{\n" +
                "  \"comment\": \"Check out developer.linkedin.com!\",\n" +
                "  \"content\": {\n" +
                "    \"title\": \"LinkedIn Developers Resources\",\n" +
                "    \"description\": \"Leverage LinkedIn's APIs to maximize engagement\",\n" +
                "    \"submitted-url\": \"https://developer.linkedin.com\",  \n" +
                "    \"submitted-image-url\": \"" + imageUrl + "\"\n" +
                "  },\n" +
                "  \"visibility\": {\n" +
                "    \"code\": \"anyone\"\n" +
                "  }  \n" +
                "}";

        Log.print("MyLinkedin payload", payload1);

        APIHelper apiHelper = APIHelper.getInstance(activity);
        apiHelper.postRequest(activity, url, payload2, new

                ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {
                        // Success!
                        Log.print("post success");
                        Log.print(apiResponse.toString());
                        responseListener.onResponce(Config.LINKEDIN_POST_TAG, Config.RESULT_OK, "Success");
                    }

                    @Override
                    public void onApiError(LIApiError liApiError) {
                        // Error making POST request!
                        Log.print("Post fail");
                        responseListener.onResponce(Config.LINKEDIN_POST_TAG, Config.RESULT_FAIL, liApiError.toString());
                    }
                }

        );
    }

    public void logout() {
        try {
            liSessionManager.clearSession();
            Pref.setValue(activity, Config.PREF_LILOGGEDIN, false);
        } catch (Exception e) {
            Log.print("myLinkedin logout", e.toString());
        }
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }

    public static void LinkedInSDK(Activity activity) {
        try {
            liSessionManager = LISessionManager.getInstance(activity);

        } catch (Exception e) {
            Log.print("MyLinkedin Exception ", e.getMessage());
            e.printStackTrace();
        }
        isLinkedInInit = true;
    }

}
