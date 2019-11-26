package com.example.teamproject;

import android.annotation.TargetApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckListItem {
    private String itemText;
    private LocalDate itemDate;
    // itemText for checklist item's text, itemDate for checklist item's expiration date.

    public String getItemText() {
        return itemText;
    }
    public void setItemText(String itemText) {this.itemText = itemText;}

    @TargetApi(26)
    public String getItemDateString(){return itemDate.format(DateTimeFormatter.ofPattern("yyyy/M/d"));}
    public LocalDate getItemDate() { return itemDate; }
    public void setItemDate(LocalDate itemDate) {this.itemDate = itemDate;}
    // Getters and Setters for each member.

    public CheckListItem(String text, LocalDate date){
        setItemText(text);
        setItemDate(date);
    } // Constructor for String and LocalDate type
    @TargetApi(26)
    public CheckListItem(String text, String date){
        setItemText(text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        LocalDate localDate = LocalDate.parse(date, formatter);
        setItemDate(localDate);
    } // Constructor for String and String type(yyyy/mm/dd format)
}