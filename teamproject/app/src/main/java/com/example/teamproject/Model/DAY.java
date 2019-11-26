package com.example.teamproject.Model;

public enum DAY {
    MON{
        @Override
        public String getDay() {
            final String str = "월";
            return str;
        }
    }, TUE{
        @Override
        public String getDay(){
            final String str = "화";
            return str;
        }
    }, WED{
        @Override
        public String getDay(){
            final String str = "수";
            return str;
        }
    }, THU{
        @Override
        public String getDay(){
            final String str = "목";
            return str;
        }
    }, FRI{
        @Override
        public String getDay(){
            final String str = "금";
            return str;
        }
    }, SAT{
        @Override
        public String getDay(){
            final String str = "토";
            return str;
        }
    }, SUN{
        @Override
        public String getDay(){
            final String str = "일";
            return str;
        }
    };

    public String getDay() {
        return null;
    }
}
