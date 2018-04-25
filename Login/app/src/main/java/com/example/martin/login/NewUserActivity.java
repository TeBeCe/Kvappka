package com.example.martin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText newUserName,newUserBirthday,newUserMail;
    private Spinner bloodGroupSpinner;
    private RadioGroup genderRadioGroups;
    private RadioButton maleRadio,femaleRadio;
    SharedPreferences getPreference;
    private Button validateButton;
    Calendar date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor mEditor = getPreference.edit();
        newUserName = (EditText) findViewById(R.id.newUserName);
        newUserBirthday =(EditText) findViewById(R.id.newUserBirthday);
        genderRadioGroups = (RadioGroup) findViewById(R.id.genderRadioGroups);
        newUserMail = (EditText) findViewById(R.id.newEmailEditText);
        validateButton = (Button)findViewById(R.id.newUserUpdateButton);
        validateButton.setOnClickListener(this);

        //initialize data
        newUserName.setText(getPreference.getString("userName", "null"));
        newUserMail.setText(getPreference.getString("email","null"));
        String gender = getPreference.getString("gender", "null");
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

        String birthday = getPreference.getString("birthday", "null");

        date = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.YYYY");

        if (!birthday.equals("null")) {
            try{
                date.setTime(sdf.parse(birthday));
                //newUserBirthday.setText(date.getTime().toString());
                int year =  date.get(Calendar.DAY_OF_MONTH);
                Toast toast = Toast.makeText(getApplicationContext(), date.getTime().toString(),Toast.LENGTH_LONG);
                toast.show();
                newUserBirthday.setText(format1.format(date.getTime()));
            } catch(ParseException e){
            // Handle the exception
            }
        }
        else{
            newUserBirthday.setHint("dd.mm.rrrr");
        }


    }
    public void validateAndSaveData(){
        Boolean isAllOk = true;
        SharedPreferences.Editor mEditor = getPreference.edit();
        if(genderRadioGroups.getCheckedRadioButtonId()==-1){
            Toast toast = Toast.makeText(getApplicationContext(),"Choose Gender",Toast.LENGTH_LONG);
            //toast.show();
            isAllOk = false;
        }
        else {
            int indexRadio = genderRadioGroups.getCheckedRadioButtonId();

            RadioButton selectedRadioButton = (RadioButton) findViewById(indexRadio);
            String selectedRadioButtonText = selectedRadioButton.getText().toString();
            if(selectedRadioButtonText.equals("Male") || selectedRadioButtonText.equals("Muž"))
                mEditor.putString("gender", "male");
            else
                mEditor.putString("gender", "female");

            mEditor.apply();
        }
        if(newUserBirthday.getText().length()!=10){
            Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_birth_date,Toast.LENGTH_LONG);
            toast.show();
            isAllOk = false;
        }
        else
        {
            mEditor.putString("birthDate", newUserBirthday.getText().toString());

            SimpleDateFormat format1 = new SimpleDateFormat("mm.DD.YYYY");
                mEditor.putLong("birthDateLong",date.getTimeInMillis());
                Toast toast = Toast.makeText(getApplicationContext(), date.getTime().toString(),Toast.LENGTH_LONG);
                //toast.show();
                mEditor.apply();
        }


            bloodGroupSpinner = (Spinner) findViewById(R.id.spinnerBloodGroups);
            mEditor.putString("bloodGroup", bloodGroupSpinner.getSelectedItem().toString());
            mEditor.putString("userName", newUserName.getText().toString());
            mEditor.apply();
        if (isAllOk){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newUserUpdateButton:
                validateAndSaveData();
        }
    }
}
