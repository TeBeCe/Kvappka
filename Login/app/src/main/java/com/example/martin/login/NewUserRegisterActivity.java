package com.example.martin.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewUserRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEdit,emailEdit,setPasswordEdit,repeatPasswordEdit,birthDateEdit;
    private Spinner BloodGroupSpinner;
    private Button registerBtn;
    private Boolean isAllOk;
    private SharedPreferences getPreference;
    private SharedPreferences.Editor mEditor;
    private Animation shake;
    private Calendar myCalendar,date;
    final SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.YYYY");
    private RadioGroup genderRadioGroups;
    private String gender;
    private Spinner bloodGroupSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_register);

        registerBtn = (Button)findViewById(R.id.newUserRegisterButton);
        registerBtn.setOnClickListener(this);
        nameEdit = (EditText) findViewById(R.id.newUserNameReg);
        emailEdit = (EditText) findViewById(R.id.newEmailEditTextReg);
        setPasswordEdit = (EditText) findViewById(R.id.setPasswordEdit);
        repeatPasswordEdit = (EditText) findViewById(R.id.repeatPasswordEdit);
        BloodGroupSpinner = (Spinner) findViewById(R.id.spinnerBloodGroupsReg);
        getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        birthDateEdit = (EditText) findViewById(R.id.newUserBirthdayReg);
        genderRadioGroups = (RadioGroup)findViewById(R.id.genderRadioGroupsReg);
        mEditor = getPreference.edit();
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        myCalendar = Calendar.getInstance();
        bloodGroupSpinner = (Spinner) findViewById(R.id.spinnerBloodGroupsReg);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                birthDateEdit.setText(format1.format(myCalendar.getTime()));
            }

        };

        birthDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewUserRegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void validate(){
        isAllOk = true;
        if(!setPasswordEdit.getText().toString().equals(repeatPasswordEdit.getText().toString())){
            isAllOk = false;
            repeatPasswordEdit.setError("Passwords didn't match");
            repeatPasswordEdit.startAnimation(shake);

        }
        if (!emailEdit.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            isAllOk = false;
            emailEdit.setError("Invalid Email Address");
        }
        if (setPasswordEdit.getText().toString().length()<8){
            isAllOk = false;
            setPasswordEdit.setError("Password must be at least 8 characters long");

        }
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
                gender = "male";}
            else{
                gender = "female";}
        }
        //if(setPasswordEdit.getText().toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$"))
        if(isAllOk){
            volleyVerifyCustomLogin(emailEdit.getText().toString(),repeatPasswordEdit.getText().toString()
                    ,nameEdit.getText().toString(),String.valueOf(myCalendar.getTimeInMillis())
                    ,bloodGroupSpinner.getSelectedItem().toString(),gender);
        }
    }
    public  void  volleyVerifyCustomLogin(final String email,final String password
            ,final String name,final String birthdate,final String bloodgroup, final String gender) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/user/register/custom";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.has("duplicite")){
                                emailEdit.startAnimation(shake);
                                emailEdit.setError("Email already taken");
                                Toast.makeText(getApplicationContext(),"Email already taken" ,
                                        Toast.LENGTH_LONG).show();
                                emailEdit.setTextColor(getResources().getColor(R.color.mainRed));
                            }
                            else if(jsonObject.length()==6){
                                mEditor.putBoolean("loggedIn",true);
                                mEditor.putString("userId",jsonObject.getString("id"));
                                mEditor.putString("userName",jsonObject.getString("first_name"));
                                mEditor.putString("email",jsonObject.getString("email"));
                                mEditor.putString("gender",jsonObject.getString("gender"));
                                mEditor.putString("birthDate",jsonObject.getString("birth_day"));
                                mEditor.putString("bloodGroup",jsonObject.getString("blood_group"));
                                mEditor.apply();
                                Intent myintent = new Intent(NewUserRegisterActivity.this,
                                        ProfileActivity.class);
                                startActivity(myintent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Something wrong happening :("
                                        , Toast.LENGTH_LONG).show();
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
                params.put("name",name);
                params.put("birthdate",birthdate);
                params.put("bloodgroup",bloodgroup);
                params.put("gender",gender);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newUserRegisterButton:{
                validate();
                break;
            }
        }
    }
}
