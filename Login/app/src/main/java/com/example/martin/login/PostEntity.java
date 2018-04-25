package com.example.martin.login;

import java.util.Calendar;

/**
 * Created by Martin on 25. 4. 2018.
 */

public class PostEntity {
    private String name;
    private Calendar date;
    private int id;
    private String content;

    public PostEntity(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
