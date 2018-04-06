package com.example.martin.login;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class DonationsActivity extends AppCompatActivity implements View.OnClickListener{
    Calendar mCalendar;
    int year,month,day;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);

        mCalendar = Calendar.getInstance();
        btn = (Button)findViewById(R.id.btncalendar);
        btn.setOnClickListener(this);

        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        btn.setText(year + "/" + month + "/" + day);

    }

    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(DonationsActivity.this,
                                                                    new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;

                btn.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
            }
        },year,month,day);

        datePickerDialog.getWindow();
        datePickerDialog.show();
        datePickerDialog.setTitle("AddDate");

    }
}
