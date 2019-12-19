package com.example.teamproject.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Lecture implements Serializable {
    private int id;
    private String name, room, lecturer, color;
    private ArrayList<Time> timeList;

    public static class Time implements Serializable{
        private DAY day;
        private Calendar startTime, endTime;

        public Time(DAY day, String startTime, String endTime) {
            this.day = day;
            this.startTime = Calendar.getInstance();
            setStartTime(startTime);
            this.endTime = Calendar.getInstance();
            setEndTime(endTime);
        }

        public void setDay(DAY day) {
            this.day = day;
        }
        public DAY getDay(){
            return day;
        }

        public void setStartTime(String time) {
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
            startTime.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
            startTime.set(Calendar.SECOND, 0);
            startTime.set(Calendar.MILLISECOND,0);
        }
        public Calendar getStartTime(){
            return startTime;
        }

        public void setEndTime(String time) {
            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
            endTime.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
            endTime.set(Calendar.SECOND, 0);
            endTime.set(Calendar.MILLISECOND,0);
        }
        public Calendar getEndTime(){
            return endTime;
        }

        public String getInfo(String mode) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String str = mode.equals("string") ? day.getDay() : Integer.toString(day.ordinal());
            return str + " " + format.format(startTime.getTime()) + "~" + format.format(endTime.getTime());
        }
    }

    public Lecture() {
        timeList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRoom(){
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public String getLecturer(){
        return lecturer;
    }
    public void setLecturer(String lecturer){
        this.lecturer = lecturer;
    }

    public String getColor(){
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Time> getLectureTime() {
        return timeList;
    }

    public void addTime(DAY day, String startTime, String endTime) {
        timeList.add(new Time(day, startTime, endTime));
    }
    public void removeTime(int index) {
        timeList.remove(index);
    }

    public ArrayList<String> getLectureInfo() {
        ArrayList<String> list = new ArrayList<>();
        for(Time t : timeList) {
            list.add(t.getInfo("number") + "," + id + "," + color + "," + name + "," + room + "," + lecturer + "\n");
        }
        return list;
    }

}
