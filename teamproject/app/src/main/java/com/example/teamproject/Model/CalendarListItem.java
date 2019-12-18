package com.example.teamproject.Model;

import android.annotation.TargetApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarListItem {
    private String itemTitle;
    private String itemWork;
    private String itemTime;

    public String getTitleText() {
        return itemTitle;
    }
    public void setTitleText(String itemtitle) {this.itemTitle = itemtitle;}

    public String getWorkText() {
        return itemWork;
    }
    public void setWorkText(String itemwork) {this.itemWork = itemwork;}

    public String getTimeText() {
        return itemTime;
    }
    public void setTimeText(String itemtime) {this.itemTime = itemtime;}

    public CalendarListItem(String title, String work, String time){
        setTitleText(title);
        setWorkText(work);
        setTimeText(time);
    } // Constructor for String and LocalDate type

}
