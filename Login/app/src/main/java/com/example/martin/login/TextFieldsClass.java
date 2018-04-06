package com.example.martin.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Martin on 6. 4. 2018.
 */

public class TextFieldsClass extends AppCompatActivity {

    private TextView nameView, emailView, profileName, profileBornView, profileBloodView, profileEmailView;
    private ImageView headerImg;
    private ArrayList<HashMap<String, String>> contactList;
    String Json;
    Context context;

    public void setNames(View header,Context context, int idMenu, NavigationView navigationView){

        contactList = new ArrayList<>();
        Json = postData("");
        contactList = decodeJson(Json);

        navigationView.getMenu()
                .getItem(idMenu)
                .setChecked(true);


        nameView = (TextView) header.findViewById(R.id.nameDrawer1);
        emailView = (TextView) header.findViewById(R.id.emailDrawer1);
        headerImg = (ImageView) header.findViewById(R.id.circularImage);

        nameView.setText(contactList.get(0).get("first_name") + " " + contactList.get(0).get("last_name"));
        emailView.setText(contactList.get(0).get("email"));

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(context.openFileInput("myProfile"));
            headerImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public String postData(String nameTxt) {
        // Create a new HttpClient and Post Header
        //noinspection deprecation
        HttpClient httpclient = new DefaultHttpClient();
        //noinspection deprecation
        HttpPost httppost = new HttpPost("http://147.175.105.140:8013/~xbachratym/friends.php");

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
            Json = new String(baf.toByteArray());
            System.out.println(Json);

            //noinspection deprecation
        } catch (@SuppressWarnings("deprecation") ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return Json;
    }
    public ArrayList<HashMap<String, String>> decodeJson(String Json) {
        if (Json != null) {
            try {
                JSONObject jsonObj = new JSONObject(Json);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("users");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String username = c.getString("username");
                    String first_name = c.getString("first_name");
                    String last_name = c.getString("last_name");
                    String email = c.getString("email");
                    String birth = c.getString("birth_day");
                    String bType = c.getString("blood_group");

                    // Phone node is JSON Object
                   /* JSONObject phone = c.getJSONObject("phone");
*/

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("username", username);
                    contact.put("first_name", first_name);
                    contact.put("last_name", last_name);
                    contact.put("email", email);
                    contact.put("birth_day", birth);
                    contact.put("blood_group", bType);
                    // adding contact to contact list
                    contactList.add(contact);
                }
            } catch (final JSONException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
        return contactList;
    }


}
