package com.ritvi.kaajneeti.activity.loginregistration;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.Constants;
import com.ritvi.kaajneeti.Util.FileUtils;
import com.ritvi.kaajneeti.Util.Pref;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.Util.ToastClass;
import com.ritvi.kaajneeti.activity.home.HomeActivity;
import com.ritvi.kaajneeti.pojo.user.UserProfilePOJO;
import com.ritvi.kaajneeti.webservice.WebServiceBase;
import com.ritvi.kaajneeti.webservice.WebServicesCallBack;
import com.ritvi.kaajneeti.webservice.WebServicesUrls;
import com.ritvi.kaajneeti.webservice.WebUploadService;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.tv_login_otp)
    TextView tv_login_otp;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.ll_facebook)
    LinearLayout ll_facebook;
    @BindView(R.id.ll_google)
    LinearLayout ll_google;
    @BindView(R.id.ll_twitter)
    LinearLayout ll_twitter;
    @BindView(R.id.login_button)
    TwitterLoginButton twitterLoginButton;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_mpin)
    EditText et_mpin;

    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    TwitterAuthClient mTwitterAuthClient= new TwitterAuthClient();
    private int RC_SIGN_IN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        RequestData();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initializeGoogleSignIn();

        tv_signup.setText(Html.fromHtml("<u>Sign up</u>"));
        tv_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,EnterMobileNumberActivity.class).putExtra("type",Constants.ENTER_MOBILE_LOGIN_WITH_OTP_TYPE));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,EnterMobileNumberActivity.class).putExtra("type",Constants.ENTER_MOBILE_REGISTRATION_TYPE));
            }
        });

        ll_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FbIntegration();
            }
        });
        ll_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        ll_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        ll_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                twitterLoginButton.getCallback();
//                twitterLoginButton.callOnClick();
                mTwitterAuthClient.authorize(LoginActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Success
                        login(twitterSessionResult);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginAPI();
            }
        });
    }

    public void callLoginAPI() {
        if (et_phone_number.getText().toString().length() > 0 && et_mpin.getText().toString().length() > 0) {
            if(et_phone_number.getText().toString().length()==10) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("mobile", "+91" + et_phone_number.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("mpin", et_mpin.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("login_type", "1"));
                new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
                    @Override
                    public void onGetMsg(String apicall, String response) {
                        parseLoginResponse(response);
                    }
                }, Constants.CALL_LOGIN_API, true).execute(WebServicesUrls.LOGIN_MPIN);
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Invalid phone number");
            }
        } else {
            ToastClass.showShortToast(getApplicationContext(), "Please Enter required fields properly");
        }
    }


    public void login(Result<TwitterSession> result) {

        final TwitterSession session = result.data;
        final String username = session.getUserName();
        final TwitterAuthClient authClient = new TwitterAuthClient();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        AccountService accountService = twitterApiClient.getAccountService();
        Call<User> call = accountService.verifyCredentials(true, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                //here we go User details
                Log.d(TagUtils.getTag(), "result user " + result);

                final String id=String.valueOf(result.data.id);
                final String name=result.data.name;
                String email=result.data.email;
                String description=result.data.description;
                final String profileImageUrl=result.data.profileImageUrl;
                Log.d(TagUtils.getTag(), "id:-" + id);
                Log.d(TagUtils.getTag(), "name:-" + name);
                Log.d(TagUtils.getTag(), "email:-" + email);
                Log.d(TagUtils.getTag(), "description:-" + description);
                Log.d(TagUtils.getTag(), "image url:-" + profileImageUrl);

                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        // Do something with the result, which provides the email address
                        String email = result.data;
                        Log.d(TagUtils.getTag(), "email:-" + email);

                        saveImageFromUrl("twitter", String.valueOf(id), name, email, profileImageUrl, "");

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.d(TagUtils.getTag(), "twitter login failure");
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });

//        saveImageFromUrl("twitter", String.valueOf(user.id), user.name, email, profileImage, "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            try {

                Log.d(TagUtils.getTag(), "name:-" + acct.getDisplayName());
                Log.d(TagUtils.getTag(), "email:-" + acct.getEmail());
                String image="";
                if(acct.getPhotoUrl()!=null) {
                    Log.d(TagUtils.getTag(), "image:-" + acct.getPhotoUrl().toString());
                    image=acct.getPhotoUrl().toString();
                }
                Log.d(TagUtils.getTag(), "id:-" + acct.getId());
                saveImageFromUrl("google", acct.getId(), acct.getDisplayName(), acct.getEmail(), image, "");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Your Google account is not configured with google plus account", Toast.LENGTH_SHORT).show();
            }

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void FbIntegration() {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public void initializeGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d(TagUtils.getTag(), "facebook response:-" + response.toString());
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        String id = "";
                        String name = "";
                        String email = "";
                        String photo = "";
                        String phone = "";

                        if (json.has("id")) {
                            id = json.optString("id");
                        }
                        if (json.has("name")) {
                            name = json.optString("name");
                        }
                        if (json.has("email")) {
                            email = json.optString("email");
                        }
                        if (json.has("phone")) {
                            phone = json.optString("phone");
                        }

                        String profile_url = "https://graph.facebook.com/" + json.getString("id") + "/picture?type=large";
                        Log.d(TagUtils.getTag(), "facebook profile url:-" + profile_url);

                        saveImageFromUrl("facebook", id, name, email, "", phone);

                    }

                } catch (JSONException e) {
                    Log.d("profile", e.toString());

                    try {
                        Log.d(TagUtils.getTag(), e.toString());
                        Log.d(TagUtils.getTag(), "id:-" + json.getString("id"));
                        Log.d(TagUtils.getTag(), "name:-" + json.getString("name"));
//                        String email="temp_"+json.getString("id")+"@bjain.com";
//                        FacebookLoginAPI(email);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void saveImageFromUrl(final String social_type, final String id, final String name, final String email, final String picture, final String mobile) {
        new AsyncTask<Void, Void, Void>() {
            File file = new File(FileUtils.getSocialDir() + File.separator + social_type + "_" + System.currentTimeMillis() + ".png");

            @Override
            protected Void doInBackground(Void... voids) {
                InputStream input = null;
                OutputStream output = null;
                try {

                    URL url = new URL(picture);
                    input = url.openStream();
                    output = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                sendDataToServer(social_type, id, name, email, file.toString(), mobile);
            }
        }.execute();
    }

    public void sendDataToServer(String social_type, String id, String name, String email, String picture, String mobile) {

        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (picture.length() > 0 && new File(picture).exists()) {
                FileBody bin1 = new FileBody(new File(picture));
                reqEntity.addPart("photo", bin1);
            } else {
                reqEntity.addPart("photo", new StringBody(""));
            }
            reqEntity.addPart("request_action", new StringBody("LOGIN_WITH_SOCIAL"));
            reqEntity.addPart("social_type", new StringBody(social_type));
            reqEntity.addPart("id", new StringBody(id, Charset.forName(HTTP.UTF_8)));
            reqEntity.addPart("name", new StringBody(name, Charset.forName(HTTP.UTF_8)));
            reqEntity.addPart("email", new StringBody(email, Charset.forName(HTTP.UTF_8)));
            reqEntity.addPart("mobile", new StringBody(mobile, Charset.forName(HTTP.UTF_8)));
            reqEntity.addPart("login_type", new StringBody(Constants.LOGIN_TYPE, Charset.forName(HTTP.UTF_8)));
            new WebUploadService(reqEntity, this, new WebServicesCallBack() {
                @Override
                public void onGetMsg(String apicall, String response) {
                    parseLoginResponse(response);
                }
            }, Constants.CALL_UPLOAD_SOCIAL_DATA, false).execute(WebServicesUrls.LOGIN_WITH_SOCIAL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void parseLoginResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString(Constants.API_STATUS).equals(Constants.API_SUCCESS)) {
                String user_profile = jsonObject.optJSONObject("result").toString();
                Gson gson = new Gson();
                UserProfilePOJO userProfilePOJO = gson.fromJson(user_profile, UserProfilePOJO.class);
                Constants.userProfilePOJO = userProfilePOJO;
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, true);
                Pref.SetStringPref(getApplicationContext(), StringUtils.USER_PROFILE, user_profile);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finishAffinity();
            }else{
                ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TagUtils.getTag(), "error:-" + e.toString());
        } finally {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastClass.showShortToast(getApplicationContext(),"Connection Failed");
    }
}
