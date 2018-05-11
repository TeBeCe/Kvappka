package com.example.martin.login;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private String userId, firstName,lastName,email, birthday,gender;
    private Button btn;
    private EditText usr, psd;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private GoogleSignInAccount account;
    private Animation shake;
    private AccessTokenTracker accessTokenTracker;
    private String profilePicture;
    private TextView registerText;
    private LoginButton loginButton;
    private TextView textViewFb;
    private CallbackManager callbackManager;
    private SharedPreferences getPreference;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = getPreference.edit();

        if(getPreference.getBoolean("loggedIn",false)){
            Intent intent= new Intent(LoginActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        if(AccessToken.getCurrentAccessToken()!=null){
            Intent intent = new Intent(  LoginActivity.this,ProfileActivity.class);
            intent.putExtra("loginType","Facebook");
            startActivity(intent);
            finish();
        }

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        loginButton = (LoginButton) findViewById(R.id.fb_login_id) ;
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        //accessToken = AccessToken.getCurrentAccessToken();
        registerText = (TextView) findViewById(R.id.textViewRegister);
        registerText.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("loggedWithFb "+loginResult.getAccessToken().getUserId());
                String accessToken= loginResult.getAccessToken().getToken();
               loginResult.getAccessToken().getToken();
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Profile profile = Profile.getCurrentProfile();
               // Log.e("ProfileDataNameF", "--" + profile.getFirstName());
               // Log.e("ProfileDataNameL", "--" + profile.getLastName());
                //Log.e("Image URI", "--" + profile.getLinkUri());

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                        try {
                            userId = object.getString("id");
                            profilePicture = "https://graph.facebook.com/" + userId + "/picture?width=250&height=250";
                            if(object.has("first_name"))
                                firstName = object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");
                            new LoginActivity.DownloadImageTask().execute(profilePicture);
                            mEditor.putString("fbId","null");
                            mEditor.apply();
                            if(getPreference.getString("fbId","null").equals("null")){

                                mEditor.putString("fbId",userId);
                                mEditor.putString("userName",firstName+" " + lastName);
                                mEditor.putString("email",email);
                                mEditor.putString("gender",gender);
                                mEditor.putString("birthDate",birthday);
                                mEditor.apply();

                                volleyGetFacebookAlreadyVerified(userId);

                            }
                            else {
                                oldUserActivity("Facebook");
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }


        });

        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.psd);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                volleyVerifyCustomLogin(usr.getText().toString(),psd.getText().toString());
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        //LoginManager.getInstance().logOut();
    }
    public void oldUserActivity(String loginType){
        Intent myintent = new Intent(this,
                ProfileActivity.class);
        myintent.putExtra("loginType",loginType);
        startActivity(myintent);
    }
    public void newUserActivity(String loginType){
        Intent myintent = new Intent(this,
                NewUserActivity.class);
        myintent.putExtra("loginType",loginType);
        startActivity(myintent);

    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            account = result.getSignInAccount();

            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myProfile";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            System.out.println("si");
            Intent myintent = new Intent(this,
                    LoggedActivity.class);

            myintent.putExtra("displayname", account.getDisplayName().toString());
            myintent.putExtra("displaymail", account.getEmail());
            myintent.putExtra("displayid", account.getId());
            startActivity(myintent);
        }
    }
    public  void  volleyVerifyCustomLogin(final String email,final String password) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/user/logincustom";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                            try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.length()==6){
                                mEditor.putBoolean("loggedIn",true);
                                mEditor.putString("userId",jsonObject.getString("id"));
                                mEditor.putString("userName",jsonObject.getString("first_name"));
                                mEditor.putString("email",jsonObject.getString("email"));
                                mEditor.putString("gender",jsonObject.getString("gender"));
                                mEditor.putString("birthDate",jsonObject.getString("birth_day"));
                                mEditor.putString("bloodGroup",jsonObject.getString("blood_group"));
                                mEditor.apply();

                                Intent myintent = new Intent(LoginActivity.this,
                                              ProfileActivity.class);
                                        startActivity(myintent);
                            }
                            else{
                                findViewById(R.id.usr).startAnimation(shake);
                                findViewById(R.id.psd).startAnimation(shake);
                                btn.startAnimation(shake);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
    public  void  volleyGetFacebookAlreadyVerified(final String facebook_id) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/user/login/"+facebook_id;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONArray jArray = new JSONArray(response);
                            for (int i=0; i < jArray.length(); i++)
                            {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(0);
                                    if(oneObject.has("facebook_id")){
                                        mEditor.putString("fbId",userId);
                                        mEditor.putBoolean("loggedIn",true);
                                        mEditor.putString("userName",oneObject.getString("first_name"));
                                        mEditor.putString("email",oneObject.getString("email"));
                                        mEditor.putString("gender",oneObject.getString("gender"));
                                        mEditor.putString("birthDate",oneObject.getString("birth_day"));
                                        mEditor.putString("userId",oneObject.getString("id"));
                                        mEditor.putString("bloodGroup",oneObject.getString("blood_group"));
                                        mEditor.apply();
                                        Toast.makeText(getApplicationContext(),"loggedAsOldFBUser" , Toast.LENGTH_LONG).show();
                                        oldUserActivity("Facebook");
                                        finish();
                                    }
                                    else {newUserActivity("Facebook");
                                        finish();}
                                    // Pulling items from the array
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewRegister:
                Intent newUserIntent = new Intent(LoginActivity.this,NewUserRegisterActivity.class);
                startActivity(newUserIntent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask() {

            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            createImageFromBitmap(result);
        }
    }
}
