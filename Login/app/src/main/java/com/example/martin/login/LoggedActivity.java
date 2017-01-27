package com.example.martin.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoggedActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    String text;
    private GoogleSignInAccount account;
    TextView txtvw;
    EditText addNameTxt;
    GoogleApiClient mGoogleApiClient;
    private static final int SIGN_IN_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        //txt= (TextView) findViewById(R.id.txt);
        txtvw = (TextView) findViewById(R.id.txtvw);
        text = "";

//        findViewById(R.id.sign_out_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent intent = getIntent();
        String nameSurname = intent.getStringExtra("displayname");
        String email = intent.getStringExtra("displaymail");
        String id = intent.getStringExtra("displayid");
        //String imageUrl = intent.getStringExtra("imageurl");

        TextView displayName = (TextView) findViewById(R.id.nameSurnameText);
        displayName.setText("" + nameSurname);
        TextView displayEmail = (TextView) findViewById(R.id.nameEmailText);
        displayEmail.setText("" + email);
        TextView displayId = (TextView) findViewById(R.id.nameIdText);
        displayId.setText("" + id);


        findViewById(R.id.postButton).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);


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

    public void postData(String nameTxt) {
        // Create a new HttpClient and Post Header
        //noinspection deprecation
        HttpClient httpclient = new DefaultHttpClient();
        //noinspection deprecation
        HttpPost httppost = new HttpPost("http://147.175.105.140:8013/~xbachratym/adduser.php");

        try {
            // Add your data
            List nameValuePairs = new ArrayList(1);
            //noinspection deprecation
            nameValuePairs.add(new BasicNameValuePair("meno", nameTxt));
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.postButton:

                String getNameText;
                System.out.println("SIIIIIIIIIIIIIIIIIIII");
                addNameTxt = (EditText) findViewById(R.id.nametxt);
                getNameText = addNameTxt.getText().toString();
                postData(getNameText);

                break;
            case R.id.sign_out_button:

                signOut();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
