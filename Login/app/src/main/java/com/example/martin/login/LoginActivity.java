package com.example.martin.login;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.StrictMode;
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
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    Button btn;
    EditText usr, psd;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private GoogleSignInAccount account;
    Animation shake;
    AccessTokenTracker accessTokenTracker;

    LoginButton loginButton;
    TextView textViewFb;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        if(AccessToken.getCurrentAccessToken()!=null){
          Intent intent = new Intent(  LoginActivity.this,LoggedActivity.class);
            intent.putExtra("loginType","Facebook");
            startActivity(intent);
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

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("loggedWithFb "+loginResult.getAccessToken().getUserId());
                String accessToken= loginResult.getAccessToken().getToken();
               loginResult.getAccessToken().getToken();
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                //Log.e("Token", "--------" + loginResult.getAccessToken().getToken());

               // Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
                Profile profile = Profile.getCurrentProfile();
               // Log.e("ProfileDataNameF", "--" + profile.getFirstName());
               // Log.e("ProfileDataNameL", "--" + profile.getLastName());

                //Log.e("Image URI", "--" + profile.getLinkUri());

                Nextacc();
            }

            @Override
            public void onCancel() {
                System.out.println("cancelledLogginWithFb");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("errorWithFb");
            }
        });

        // [START customize_button]
        // Set the dimensions of the sign-in button.

// basic login begin
        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.psd);
        btn = (Button) findViewById(R.id.btn);
//basic login end
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                if (usr.getText().toString().equals("Admin") && psd.getText().toString().equals("password")) {
                    Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(LoginActivity.this,
                            LoggedActivity.class);
                    myintent.putExtra("loginType","sss");
                    startActivity(myintent);


                } else {
                    findViewById(R.id.usr).startAnimation(shake);
                    findViewById(R.id.psd).startAnimation(shake);
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        //LoginManager.getInstance().logOut();
    }
    public void Nextacc(){
        Intent myintent = new Intent(this,
                LoggedActivity.class);
        myintent.putExtra("loginType","Facebook");
        startActivity(myintent);
    }
    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]


    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

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
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            account = result.getSignInAccount();

            updateUI(true);

            //System.out.println("si");
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            System.out.println("si");
            Intent myintent = new Intent(this,
                    LoggedActivity.class);

            myintent.putExtra("displayname", account.getDisplayName().toString());
            myintent.putExtra("displaymail", account.getEmail());
            myintent.putExtra("displayid", account.getId());
            startActivity(myintent);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.sign_in_button:
                signIn();
                break;
            /*case R.id.disconnect_button:
                revokeAccess();
                break;*/
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
