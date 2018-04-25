package com.example.martin.login;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by Martin on 24. 4. 2018.
 */

public class DonationsEntity implements Comparable<DonationsEntity> {
    private Calendar calendar;
    private String place;

    DonationsEntity(){
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int compareTo(@NonNull DonationsEntity donationsEntity) {
        return getCalendar().compareTo(donationsEntity.getCalendar());
    }
}