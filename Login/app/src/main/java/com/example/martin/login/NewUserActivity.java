package com.example.martin.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText newUserName,newUserBirthday,newUserMail;
    private Spinner bloodGroupSpinner;
    private RadioGroup genderRadioGroups;
    private RadioButton maleRadio,femaleRadio;
    SharedPreferences getPreference;
    private Button validateButton;
    private Animation shake;
    private String gender,facebookId,email;
    final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    final SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.YYYY");
    private SharedPreferences.Editor mEditor;

    Calendar date, myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = getPreference.edit();
        newUserName = (EditText) findViewById(R.id.newUserName);
        newUserBirthday =(EditText) findViewById(R.id.newUserBirthday);
        genderRadioGroups = (RadioGroup) findViewById(R.id.genderRadioGroups);
        newUserMail = (EditText) findViewById(R.id.newEmailEditText);
        validateButton = (Button)findViewById(R.id.newUserUpdateButton);
        validateButton.setOnClickListener(this);
        shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        //initialize data
        email = getPreference.getString("email","null");
        newUserName.setText(getPreference.getString("userName", "null"));
        newUserMail.setText(getPreference.getString("email","null"));
        facebookId = getPreference.getString("fbId","null");
        gender = getPreference.getString("gender", "null");
        switch (gender) {
            case "male":
                genderRadioGroups.check(R.id.radioButtonMale);
                break;
            case "female":
                genderRadioGroups.check(R.id.radioButtonFemale);
                break;
            default:
                break;
        }

        myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newUserBirthday.setText(format1.format(myCalendar.getTime()));
            }

        };

        newUserBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewUserActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    public void validateAndSaveData(){
        Boolean isAllOk = true;
        mEditor = getPreference.edit();
        if(genderRadioGroups.getCheckedRadioButtonId()==-1){
            Toast toast = Toast.makeText(getApplicationContext(),"Choose Gender",Toast.LENGTH_LONG);
            //toast.show();
            isAllOk = false;
            genderRadioGroups.startAnimation(shake);
        }
        else {
            int indexRadio = genderRadioGroups.getCheckedRadioButtonId();

            RadioButton selectedRadioButton = (RadioButton) findViewById(indexRadio);
            String selectedRadioButtonText = selectedRadioButton.getText().toString();
            if(selectedRadioButtonText.equals("Male") || selectedRadioButtonText.equals("Muž")){
                mEditor.putString("gender", "male");
                gender = "male";}
            else{
                mEditor.putString("gender", "female");
            gender = "male";}

            mEditor.apply();
        }
        if(newUserBirthday.getText().length()!=10){
            Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_birth_date,Toast.LENGTH_LONG);
            toast.show();
            newUserBirthday.startAnimation(shake);
            isAllOk = false;
        }
        else
        {
            mEditor.putString("birthDate", newUserBirthday.getText().toString());

            SimpleDateFormat format1 = new SimpleDateFormat("mm.DD.YYYY");
                mEditor.putLong("birthDateLong",myCalendar.getTimeInMillis());
                //Toast toast = Toast.makeText(getApplicationContext(), date.getTime().toString(),Toast.LENGTH_LONG);
                //toast.show();
                mEditor.apply();
        }


            bloodGroupSpinner = (Spinner) findViewById(R.id.spinnerBloodGroups);
            mEditor.putString("bloodGroup", bloodGroupSpinner.getSelectedItem().toString());
            mEditor.putString("userName", newUserName.getText().toString());
            mEditor.apply();
        if (isAllOk){
            volleyNewFBUser(facebookId,newUserName.getText().toString()
                    ,String.valueOf(myCalendar.getTimeInMillis())
                    ,gender,email,bloodGroupSpinner.getSelectedItem().toString());
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public  void  volleyNewFBUser(final String facebookId,final String name,final String birthDate
            ,final String gender,final String email,final String bloodGroup) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/user/register";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            mEditor.putString("id",jsonObject.getString("id"));
                            mEditor.apply();
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
                params.put("fbid",facebookId);
                params.put("name", name);
                params.put("email", email);
                params.put("bloodgroup", bloodGroup);
                params.put("birthdate", birthDate);
                params.put("gender",gender);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newUserUpdateButton:
                validateAndSaveData();
        }
    }
}
