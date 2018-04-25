package com.example.martin.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.YEAR;

/**
 * Created by Martin on 17. 4. 2018.
 */

public class PopulateMedals extends ProfileActivity{

    //TextView badgeLvl1Num,badgeLvl2Num,badgeLvl3Num,badgeLvl4Num,badgeLvl5Num;
    //TextView badgeLvl1Date,badgeLvl2Date,badgeLvl3Date,badgeLvl4Date,badgeLvl5Date;
    SharedPreferences sharedPreferences;
    int[] manBadges,womanBadges;
    List<Calendar> calendarList;
    Context context;

    public  PopulateMedals(Context context){

        this.context = context;
    }
    public void populateMedals(TextView badgeLvl1Num,TextView badgeLvl2Num, TextView badgeLvl3Num,TextView badgeLvl4Num
            , TextView badgeLvl5Num,TextView badgeLvl1Date, TextView badgeLvl2Date,TextView badgeLvl3Date
            , TextView badgeLvl4Date,TextView badgeLvl5Date,TextView daysToDonate,TextView lastDonation,TextView countDonations){
        calendarList = new ArrayList<>();
        Calendar c1 = GregorianCalendar.getInstance();
        Calendar c2 = GregorianCalendar.getInstance();
        Calendar c3 = GregorianCalendar.getInstance();
        Calendar c4 = GregorianCalendar.getInstance();
        Calendar c5 = GregorianCalendar.getInstance();
        Calendar c6 = GregorianCalendar.getInstance();
        Calendar c7 = GregorianCalendar.getInstance();
        Calendar c8 = GregorianCalendar.getInstance();
        Calendar c9 = GregorianCalendar.getInstance();
        c1.set(2018,Calendar.JANUARY,15);
        c2.set(2017,Calendar.SEPTEMBER,15);
        c3.set(2012,Calendar.MARCH,28);
        c4.set(2013,Calendar.MARCH,28);
        c5.set(2015,Calendar.MARCH,28);
        c6.set(2014,Calendar.MARCH,28);
        c7.set(2016,Calendar.MARCH,28);
        c8.set(2017,Calendar.MARCH,28);
        c9.set(2012,Calendar.DECEMBER,28);
        calendarList.add(c1);
        calendarList.add(c2);
        calendarList.add(c3);
        calendarList.add(c4);
        calendarList.add(c5);
        calendarList.add(c6);
        calendarList.add(c7);
        calendarList.add(c8);
        calendarList.add(c9);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c9);
        calendarList.add(c5);
        calendarList.add(c5);
        calendarList.add(c5);

        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(context);
        Long lastDonate = getPreference.getLong("lastDonationDate", 0);
        int numberOfDonations = getPreference.getInt("numberOfDonations",0);
        String gender = getPreference.getString("gender","null");
        Collections.sort(calendarList,Collections.reverseOrder());
        Calendar calendar = new GregorianCalendar().getInstance();
        calendar.setTimeInMillis(lastDonate);
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatShort = new SimpleDateFormat("dd.MM.yy");
        int donations = 21;
        Toast toast = Toast.makeText(context,String.valueOf(numberOfDonations),Toast.LENGTH_LONG);
        //toast.show();

        countDonations.setText(String.valueOf(numberOfDonations));
        lastDonation.setText(formatShort.format(calendar.getTime()));
        daysToDonate.setText("xxx");
        manBadges = new int[]{10,20,40,80,100};
        womanBadges = new int[]{10,20,30,60,80};

        int months=0 ;



        if(gender.equals("male")) {
            if (numberOfDonations >= manBadges[0]) {
                badgeLvl1Date.setText(format1.format(calendarList.get(9).getTime()));
            } else {
                int monthsToAdd = (manBadges[0] - numberOfDonations) * 3;
                months = manBadges[0] - numberOfDonations;
                Calendar closestWithOffset = calendar;
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl1Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= manBadges[1]) {
                badgeLvl2Date.setText(format1.format(calendarList.get(19).getTime()));
            }
            else {
                int monthsToAdd = (manBadges[1] - numberOfDonations - months) * 3;
                months = manBadges[1] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl2Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= manBadges[2]) {
                badgeLvl3Date.setText(format1.format(calendarList.get(39).getTime()));
            }
            else {
                int monthsToAdd = (manBadges[2] - numberOfDonations - months) * 3;
                months = manBadges[2] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl3Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= manBadges[3]) {
                badgeLvl4Date.setText(format1.format(calendarList.get(79).getTime()));
            }
            else {
                int monthsToAdd = (manBadges[3] - numberOfDonations - months) * 3;
                months = manBadges[3] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl4Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= manBadges[4]) {
                badgeLvl5Date.setText(format1.format(calendarList.get(99).getTime()));
            }
            else {
                int monthsToAdd = (manBadges[4] - numberOfDonations - months) * 3;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl5Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            badgeLvl1Num.setText(String.valueOf(manBadges[0]));
            badgeLvl2Num.setText(String.valueOf(manBadges[1]));
            badgeLvl3Num.setText(String.valueOf(manBadges[2]));
            badgeLvl4Num.setText(String.valueOf(manBadges[3]));
            badgeLvl5Num.setText(String.valueOf(manBadges[4]));
        }

        else if(gender.equals("female")) {
            if (numberOfDonations >= womanBadges[0]) {
                badgeLvl1Date.setText(format1.format(calendarList.get(9).getTime()));
            } else {
                int monthsToAdd = (womanBadges[0] - numberOfDonations) * 4;
                months = womanBadges[0] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl1Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= womanBadges[1]) {
                badgeLvl2Date.setText(format1.format(calendarList.get(19).getTime()));
            }
            else {
                int monthsToAdd = (womanBadges[1] - numberOfDonations - months) * 4;
                months = womanBadges[1] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl2Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= womanBadges[2]) {
                badgeLvl3Date.setText(format1.format(calendarList.get(29).getTime()));
            }
            else {
                int monthsToAdd = (womanBadges[2] - numberOfDonations - months) * 4;
                months = womanBadges[2] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl3Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= womanBadges[3]) {
                badgeLvl4Date.setText(format1.format(calendarList.get(59).getTime()));
            }
            else {
                int monthsToAdd = (womanBadges[3] - numberOfDonations - months) * 4;
                months = womanBadges[3] - numberOfDonations;
                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl4Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }
            if (numberOfDonations >= womanBadges[4]) {
                badgeLvl5Date.setText(format1.format(calendarList.get(79).getTime()));
            }
            else {
                int monthsToAdd = (womanBadges[4] - numberOfDonations - months) * 4;

                Calendar closestWithOffset = calendarList.get(0);
                closestWithOffset.add(Calendar.MONTH,monthsToAdd);
                badgeLvl5Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            badgeLvl1Num.setText(String.valueOf(womanBadges[0]));
            badgeLvl2Num.setText(String.valueOf(womanBadges[1]));
            badgeLvl3Num.setText(String.valueOf(womanBadges[2]));
            badgeLvl4Num.setText(String.valueOf(womanBadges[3]));
            badgeLvl5Num.setText(String.valueOf(womanBadges[4]));

        }

    }



}
