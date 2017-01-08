package com.amigosphire_poc.demo.utils;

/**
 * Created by skaribasappa on 1/8/17.
 */

public class Notifications {
    private String title;
    private String date;

    public Notifications(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
