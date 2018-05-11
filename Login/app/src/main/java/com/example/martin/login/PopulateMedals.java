package com.example.martin.login;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.YEAR;

/**
 * Created by Martin on 17. 4. 2018.
 */

public class PopulateMedals extends ProfileActivity {

    private SharedPreferences sharedPreferences;
    private int[] manBadges, womanBadges;
    private List<Calendar> donationList = new ArrayList<>();
    private Context context;
    private TextView profileTest;
    private Long lastDonate;
    private final String LASTDONATIONDATE = "lastDonationDate";
    private String test;
    private Calendar calendar = new GregorianCalendar().getInstance();
    private Calendar calendarLast = new GregorianCalendar().getInstance();

    public PopulateMedals(Context context) {
        this.context = context;

    }

    public void populateMedals(TextView badgeLvl1Num, TextView badgeLvl2Num, TextView badgeLvl3Num
            , TextView badgeLvl4Num, TextView badgeLvl5Num, TextView badgeLvl1Date
            , TextView badgeLvl2Date, TextView badgeLvl3Date, TextView badgeLvl4Date
            , TextView badgeLvl5Date, TextView daysToDonate, TextView lastDonation
            , TextView countDonations, List<Calendar> donationList) {

        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(context);
        //Long lastDonate = getPreference.getLong(LASTDONATIONDATE, 0);
        if(donationList.size()>0){
            lastDonate = donationList.get(donationList.size() - 1).getTimeInMillis();
        }
        else{
            lastDonate = new GregorianCalendar().getInstance().getTimeInMillis();
        }

        int numberOfDonations = donationList.size();

        String gender = getPreference.getString("gender", "null");
        Collections.sort(donationList);
        calendar.setTimeInMillis(lastDonate);
        calendarLast.setTimeInMillis(lastDonate);
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatShort = new SimpleDateFormat("dd.MM.yy");


        manBadges = new int[]{10, 20, 40, 80, 100};
        womanBadges = new int[]{10, 20, 30, 60, 80};

        int months = 0;

        if (gender.equals("male")) {

            Calendar lastDonationDateOffset = new GregorianCalendar().getInstance();
            //lastDonationDateOffset.setTimeInMillis(getPreference.getLong(LASTDONATIONDATE,0));
            if(numberOfDonations == 0){
                lastDonationDateOffset.setTimeInMillis(lastDonate);
                countDonations.setText("0");
                lastDonation.setText("--.--.--");
                donationList.add(lastDonationDateOffset);
            }
            else{
                lastDonationDateOffset.setTimeInMillis(donationList.get(donationList.size() - 1).getTimeInMillis());
                countDonations.setText(String.valueOf(numberOfDonations));
                lastDonation.setText(formatShort.format(calendar.getTime()));

            }
            System.out.println(lastDonationDateOffset.getTime().toString());
            lastDonationDateOffset.add(Calendar.MONTH, 3);
            lastDonationDateOffset.set(Calendar.HOUR_OF_DAY, 8);
            System.out.println(lastDonationDateOffset.getTime().toString());
            if (Calendar.getInstance().before(lastDonationDateOffset)) {


                Calendar today = Calendar.getInstance();
                calendar.set(Calendar.HOUR, 8);
                long diff = lastDonationDateOffset.getTimeInMillis() - today.getTimeInMillis();
                daysToDonate.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                //toast.show();
            } else {
                daysToDonate.setText("0");
            }

            if (numberOfDonations >= manBadges[0]) {
                badgeLvl1Date.setText(format1.format(donationList.get(9).getTime()));
            } else {
                int monthsToAdd = (manBadges[0] - numberOfDonations) * 3;
                Toast toast = Toast.makeText(context, String.valueOf(numberOfDonations)
                        , Toast.LENGTH_LONG);
                toast.show();
                months = manBadges[0] - numberOfDonations;
                Calendar closestWithOffset = calendar;
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                System.out.println("mnths: "+ months+"monthstoadd: "+monthsToAdd + " lastoffset: " +lastDonationDateOffset.getTime().toString());
                badgeLvl1Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= manBadges[1]) {
                badgeLvl2Date.setText(format1.format(donationList.get(19).getTime()));
            } else {
                int monthsToAdd = (manBadges[1] - numberOfDonations) * 3;
                months = manBadges[1] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                System.out.println("mnths: "+ months+"monthstoadd: "+monthsToAdd + " lastoffset: " +lastDonationDateOffset.getTime().toString());
                badgeLvl2Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= manBadges[2]) {
                badgeLvl3Date.setText(format1.format(donationList.get(39).getTime()));
            } else {
                int monthsToAdd = (manBadges[2] - numberOfDonations - months) * 3;
                months = manBadges[2] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl3Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= manBadges[3]) {
                badgeLvl4Date.setText(format1.format(donationList.get(79).getTime()));
            } else {
                int monthsToAdd = (manBadges[3] - numberOfDonations - months) * 3;
                months = manBadges[3] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl4Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= manBadges[4]) {
                badgeLvl5Date.setText(format1.format(donationList.get(99).getTime()));
            } else {
                int monthsToAdd = (manBadges[4] - numberOfDonations - months) * 3;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl5Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            badgeLvl1Num.setText(String.valueOf(manBadges[0]));
            badgeLvl2Num.setText(String.valueOf(manBadges[1]));
            badgeLvl3Num.setText(String.valueOf(manBadges[2]));
            badgeLvl4Num.setText(String.valueOf(manBadges[3]));
            badgeLvl5Num.setText(String.valueOf(manBadges[4]));

        } else if (gender.equals("female")) {
            Calendar lastDonationDateOffset = new GregorianCalendar().getInstance();
            //lastDonationDateOffset.setTimeInMillis(getPreference.getLong(LASTDONATIONDATE,0));
            if(numberOfDonations == 0){
                lastDonationDateOffset.setTimeInMillis(lastDonate);
                countDonations.setText("0");
                lastDonation.setText("--.--.--");
                donationList.add(lastDonationDateOffset);
            }
            else{
                lastDonationDateOffset.setTimeInMillis(donationList.get(donationList.size() - 1).getTimeInMillis());
                countDonations.setText(String.valueOf(numberOfDonations));
                lastDonation.setText(formatShort.format(calendar.getTime()));

            }
            System.out.println(lastDonationDateOffset.getTime().toString());
            lastDonationDateOffset.add(Calendar.MONTH, 4);
            lastDonationDateOffset.set(Calendar.HOUR_OF_DAY, 8);
            System.out.println(lastDonationDateOffset.getTime().toString());
            if (Calendar.getInstance().before(lastDonationDateOffset)) {


                Calendar today = Calendar.getInstance();
                calendar.set(Calendar.HOUR, 8);
                long diff = lastDonationDateOffset.getTimeInMillis() - today.getTimeInMillis();
                daysToDonate.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                //toast.show();
            } else {
                daysToDonate.setText("0");
            }

            if (Calendar.getInstance().before(lastDonationDateOffset)) {

                Calendar today = Calendar.getInstance();
                calendar.set(Calendar.HOUR, 8);
                long diff = lastDonationDateOffset.getTimeInMillis() - today.getTimeInMillis();
                daysToDonate.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
            } else {
                daysToDonate.setText("0");
            }

            if (numberOfDonations >= womanBadges[0]) {
                badgeLvl1Date.setText(format1.format(donationList.get(9).getTime()));
            } else {
                int monthsToAdd = (womanBadges[0] - numberOfDonations) * 4;
                months = womanBadges[0] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl1Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= womanBadges[1]) {
                badgeLvl2Date.setText(format1.format(donationList.get(19).getTime()));
            } else {
                int monthsToAdd = (womanBadges[1] - numberOfDonations - months) * 4;
                months = womanBadges[1] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl2Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= womanBadges[2]) {
                badgeLvl3Date.setText(format1.format(donationList.get(29).getTime()));
            } else {
                int monthsToAdd = (womanBadges[2] - numberOfDonations - months) * 4;
                months = womanBadges[2] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl3Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= womanBadges[3]) {
                badgeLvl4Date.setText(format1.format(donationList.get(59).getTime()));
            } else {
                int monthsToAdd = (womanBadges[3] - numberOfDonations - months) * 4;
                months = womanBadges[3] - numberOfDonations;
                Calendar closestWithOffset = donationList.get(0);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl4Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            if (numberOfDonations >= womanBadges[4]) {
                badgeLvl5Date.setText(format1.format(donationList.get(79).getTime()));
            } else {
                int monthsToAdd = (womanBadges[4] - numberOfDonations - months) * 4;

                Calendar closestWithOffset = donationList.get(donationList.size() - 1);
                closestWithOffset.add(Calendar.MONTH, monthsToAdd);
                badgeLvl5Date.setText("> " + format1.format(closestWithOffset.getTime()));
            }

            badgeLvl1Num.setText(String.valueOf(womanBadges[0]));
            badgeLvl2Num.setText(String.valueOf(womanBadges[1]));
            badgeLvl3Num.setText(String.valueOf(womanBadges[2]));
            badgeLvl4Num.setText(String.valueOf(womanBadges[3]));
            badgeLvl5Num.setText(String.valueOf(womanBadges[4]));

        }
    }
    public Calendar getLastDonate(){


        return calendarLast;
    }


}
