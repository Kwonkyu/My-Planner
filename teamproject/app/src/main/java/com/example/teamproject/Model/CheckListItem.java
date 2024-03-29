package com.example.teamproject.Model;

import android.annotation.TargetApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckListItem {
    private int id;
    private String itemText;
    private LocalDate itemDate;
    private String itemPlace;
    private boolean isDone;
    // itemText for checklist item's text, itemDate for checklist item's expiration date.

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getItemText() {
        return itemText;
    }
    public void setItemText(String itemText) {this.itemText = itemText;}

    @TargetApi(26)
    public String getItemDateString(){return itemDate.format(DateTimeFormatter.ofPattern("yyyy/M/d"));}
    public LocalDate getItemDate() { return itemDate; }
    public void setItemDate(LocalDate itemDate) {this.itemDate = itemDate;}
    // Getters and Setters for each member.

    public String getItemPlace() { return itemPlace; }
    public void setItemPlace(String itemPlace) { this.itemPlace = itemPlace; }

    public boolean getDone(){ return isDone; }
    public void setDone(boolean isDone){ this.isDone = isDone; }

    public boolean equals(CheckListItem c){
        return id == c.id &&
                itemText.equals(c.itemText) &&
                itemDate.equals(c.itemDate) &&
                itemPlace.equals(c.itemPlace) &&
                isDone == c.isDone;
    }

    public CheckListItem(int id, String text, LocalDate date, String place, boolean isDone){
        setId(id);
        setItemText(text);
        setItemDate(date);
        setItemPlace(place);
        setDone(isDone);
    } // Constructor for String and LocalDate type
    @TargetApi(26)
    public CheckListItem(int id, String text, String date, String place, boolean isDone){
        setId(id);
        setItemText(text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        LocalDate localDate = LocalDate.parse(date, formatter);
        setItemDate(localDate);
        setItemPlace(place);
        setDone(isDone);
    } // Constructor for String and String type(yyyy/mm/dd format)
}
