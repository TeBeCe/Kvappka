package com.example.martin.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewUserRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEdit,emailEdit,setPasswordEdit,repeatPasswordEdit,birthDateEdit;
    private Spinner BloodGroupSpinner;
    private Button registerBtn;
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
    }

    private void validate(){

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
