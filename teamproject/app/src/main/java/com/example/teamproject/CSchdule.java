package com.example.teamproject;

import java.util.Calendar;

public class CSchdule {
    String title;
    String work;
    Calendar startTime;
    Calendar endTime;

    public void setTitle(String title) { this.title = title; }
    public void setWork (String work){  this.work = work; }
    public void setStartTIme (Calendar startTime){ this.startTime = startTime; }
    public void setEndTime (Calendar endTime){ this.endTime = endTime; }

    public String getTitle() { return title;}
    public String getWork() { return work;}
    public Calendar getStartTime() { return startTime;}
    public Calendar getEndTime() { return endTime;}


    public int getStartTimeYear(){ return startTime.get(startTime.YEAR);}
    public int getStartTimeMonth(){ return startTime.get(startTime.MONTH);}
    public int getStartTImeDay(){ return startTime.get(startTime.DAY_OF_MONTH);}
    public int getStartTimeHour() { return startTime.get(startTime.HOUR_OF_DAY);}
    public int getStartTimeMinute() { return startTime.get(startTime.MINUTE);}


    public int getEndTimeYear(){ return endTime.get(endTime.YEAR);}
    public int getEndTimeMonth(){ return endTime.get(endTime.MONTH);}
    public int getEndTImeDay(){ return endTime.get(endTime.DAY_OF_MONTH);}
    public int getEndTimeHour() { return endTime.get(startTime.HOUR_OF_DAY);}
    public int getEndTimeMinute() { return endTime.get(startTime.MINUTE);}

}
