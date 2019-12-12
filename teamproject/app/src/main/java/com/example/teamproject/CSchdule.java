package com.example.teamproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CSchdule implements Serializable {
    private HashMap<String, ArrayList<ScheduleInfo>> schedulesHashMap;

    public CSchdule() {
        schedulesHashMap = new HashMap<>();
    }

    public ArrayList<ScheduleInfo> getScheduleInfos(String date) {
        if (schedulesHashMap != null && schedulesHashMap.containsKey(date))
            return schedulesHashMap.get(date);
        else
            return null;
    }

    public void addSchedule(String date, ScheduleInfo scheduleInfo) {
        ArrayList<ScheduleInfo> schedulesList = null;
        if (this.schedulesHashMap != null && date != null && !date.isEmpty()) {
            if (schedulesHashMap != null && schedulesHashMap.containsKey(date)) {
                schedulesList = schedulesHashMap.get(date);
                schedulesList.add(scheduleInfo);
                schedulesHashMap.remove(date);
                schedulesHashMap.put(date, schedulesList);
            } else {
                schedulesList = new ArrayList<>();
                schedulesList.add(scheduleInfo);
                schedulesHashMap.put(date, schedulesList);
            }
        }
    }

    public void removeSchedule(String date, int position) {
        if (date != null && schedulesHashMap.containsKey(date)) {
            schedulesHashMap.get(date).remove(position);
        }
    }

    public void removeSchedule(String date, String title) {
        if (date != null && schedulesHashMap.containsKey(date)) {
            int position = -1;
            for (int i = 0; i < schedulesHashMap.get(date).size(); i++) {
                if (schedulesHashMap.get(date).get(i).title.equals(title)) {
                    position = i;
                    break;
                }
            }
            if(position != -1)
            schedulesHashMap.get(date).remove(position);
        }
    }

    public void removeSchedule(String date) {
        if (date != null && schedulesHashMap.containsKey(date))
            schedulesHashMap.remove(date);
    }


    public static class ScheduleInfo implements Serializable {
        String title;
        String work;
        Calendar startTime;
        Calendar endTime;
        int time_value;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public void setStartTIme(Calendar startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(Calendar endTime) {
            this.endTime = endTime;
        }

        public String getTitle() {
            return title;
        }

        public String getWork() {
            return work;
        }

        public Calendar getStartTime() {
            return startTime;
        }

        public Calendar getEndTime() {
            return endTime;
        }


        public int getStartTimeYear() {
            return startTime.get(startTime.YEAR);
        }

        public int getStartTimeMonth() {
            return startTime.get(startTime.MONTH);
        }

        public int getStartTImeDay() {
            return startTime.get(startTime.DAY_OF_MONTH);
        }

        public int getStartTimeHour() {
            return startTime.get(startTime.HOUR_OF_DAY);
        }

        public int getStartTimeMinute() {
            return startTime.get(startTime.MINUTE);
        }

        public int getTime_value() {
            time_value = startTime.get(startTime.HOUR_OF_DAY) * 60 + startTime.get(startTime.MINUTE);
            return time_value;
        }


        public int getEndTimeYear() {
            return endTime.get(endTime.YEAR);
        }

        public int getEndTimeMonth() {
            return endTime.get(endTime.MONTH);
        }

        public int getEndTImeDay() {
            return endTime.get(endTime.DAY_OF_MONTH);
        }

        public int getEndTimeHour() {
            return endTime.get(startTime.HOUR_OF_DAY);
        }

        public int getEndTimeMinute() {
            return endTime.get(startTime.MINUTE);
        }
    }
}
