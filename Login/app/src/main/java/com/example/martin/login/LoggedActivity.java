package com.example.martin.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.net.URL;

public class LoggedActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int SIGN_IN_CODE = 9001;
    String text,nameSurname,email,id;
    TextView txtvw;
    EditText addNameTxt;
    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;
    String picUrlString;
    private GoogleSignInAccount account;

    public void changeLang(Context context, String lang) {
        Locale myLocale;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logged);

        //AccessToken.getCurrentAccessToken().getToken();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        txtvw = (TextView) findViewById(R.id.txtvw);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        findViewById(R.id.postButton).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.btnen).setOnClickListener(this);
        findViewById(R.id.btnsk).setOnClickListener(this);


       // AccessToken.getCurrentAccessToken().getToken();
        Intent intent = getIntent();
        if(intent.getStringExtra("loginType").equals("Facebook"))
        {
            id = accessToken.getUserId();
            /* make the API call */
            Bundle params = new Bundle();
            params.putBoolean("redirect", false);
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me?fields=id,name,email,birthday,picture.type(large)",
                    params,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

            /* handle the result */
                            try {
                                picUrlString = (String) response.getJSONObject().getJSONObject("picture").getJSONObject("data").get("url");
                                nameSurname = (String ) response.getJSONObject().get("name");
                                String birthday = (String) response.getJSONObject().get("birthday");
                                email = (String) response.getJSONObject().get("email");
                              //  System.out.println("sss");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setCredentials();
                            Intent intent = getIntent();
                            new DownloadImageTask().execute(picUrlString);
                        }
                    }
            ).executeAsync();


        }
        else if(intent.getStringExtra("loginType").equals("Google")) {
             nameSurname = intent.getStringExtra("displayname");
             email = intent.getStringExtra("displaymail");
             id = intent.getStringExtra("displayid");
            //String imageUrl = intent.getStringExtra("imageurl");
            setCredentials();
        }
        else {

            nameSurname = "bbb";
            email  = "null";
            id = "null";
            setCredentials();
        }


    }

    public void setCredentials(){

        TextView displayName = (TextView) findViewById(R.id.nameSurnameText);
        displayName.setText("" + nameSurname);
        TextView displayEmail = (TextView) findViewById(R.id.nameEmailText);
        displayEmail.setText("" + email);
        TextView displayId = (TextView) findViewById(R.id.nameIdText);
        displayId.setText("" + id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        System.out.println("So");
                        Intent intentLogOut = new Intent(LoggedActivity.this, LoginActivity.class);
                        //updateUI(false);
                        // [END_EXCLUDE]
                        startActivity(intentLogOut);
                    }
                });
    }

    public void postData(String nameTxt ) {
        // Create a new HttpClient and Post Header
        //noinspection deprecation
        HttpClient httpclient = new DefaultHttpClient();
        //noinspection deprecation
        HttpPost httppost = new HttpPost("http://147.175.105.140:8013/~xbachratym/adduser.php");

        try {
            // Add your data
            List nameValuePairs = new ArrayList(2);
            //noinspection deprecation
            nameValuePairs.add(new BasicNameValuePair("meno", nameTxt));
            nameValuePairs.add(new BasicNameValuePair("priezvisko", "skuska"));
            //noinspection deprecation
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            //noinspection deprecation
            HttpResponse response = httpclient.execute(httppost);

            InputStream is = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(is);
            //noinspection deprecation
            ByteArrayBuffer baf = new ByteArrayBuffer(20);

            int current = 0;

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            /* Convert the Bytes read to a String. */
            text = new String(baf.toByteArray());
            txtvw.setText(text);
            //noinspection deprecation
        } catch (@SuppressWarnings("deprecation") ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.postButton:

                String getNameText;
                addNameTxt = (EditText) findViewById(R.id.nametxt);
                getNameText = addNameTxt.getText().toString();
                postData(getNameText);

                break;
            case R.id.sign_out_button:
                LoginManager.getInstance().logOut();
                signOut();
                break;

            case R.id.button2:
                Intent myintent = new Intent(this,
                        ProfileActivity.class);
                myintent.putExtra("image",picUrlString);
                startActivity(myintent);
                break;

            case R.id.btnen:
                changeLang(this,"en");
                super.recreate();
                break;

            case R.id.btnsk:
                changeLang(this,"sk");
                super.recreate();
                break;

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
