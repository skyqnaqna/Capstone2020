package com.cs2020.capstone;

import java.util.Locale;

class Product
{
    Integer primaryKey;
    Integer image_src; // TODO : String으로 바꾸기
    String name;
    String category;
    int end_year, end_month, end_day; // 유통기한
    int alarm_year, alarm_month, alarm_day; // 알람일
    String company;
    String date;
    String alarm;
    Boolean isPassed;

    public Product(String name, String category)
    {
        this.name = name;
        this.category = category;
    }

    public Product(String name, String category, String company, Integer img)
    {
        this.name = name;
        this.category = category;
        this.company = company;
        this.isPassed = false;
        this.image_src = img;
    }

    public Product(String name, String category, String company, int end_year, int end_month, int end_day, Integer img)
    {
        this.name = name;
        this.category = category;
        this.company = company;
        this.end_year = end_year;
        this.end_month = end_month;
        this.end_day = end_day;
        this.isPassed = false;
        this.image_src = img;

        makeDate();
    }

    public String getName() {return name;}
    public String getCategory() {return category;}
    public Integer getEnd_year() {return end_year;}
    public Integer getEnd_month() {return end_month;}
    public Integer getEnd_day() {return end_day;}
    public Boolean getIsPassed() {return isPassed;}
    public String getDate() {return this.date;}

    public void setName(String name) { this.name = name;}
    public void setCategory(String category) {this.category = category;}
    public void setCompany(String company) {this.company = company;}
    public void setIsPassed() {this.isPassed = true;}
    public void setDate(int end_year, int end_month, int end_day)
    {
        this.end_year = end_year;
        this.end_month = end_month;
        this.end_day = end_day;

        makeDate();
    }

    public void setAlarm(int alarm_year, int alarm_month, int alarm_day)
    {
        this.alarm_year = alarm_year;
        this.alarm_month = alarm_month;
        this.alarm_day = alarm_day;

        makeAlarm();
    }

    public void makeDate()
    {
        this.date = String.format(Locale.KOREA,"%d-%d-%d", this.end_year, this.end_month, this.end_day);
    }

    public void makeAlarm()
    {
        this.alarm = String.format(Locale.KOREA, "%d-%d-%d", this.alarm_year, this.alarm_month, this.alarm_day);
    }

}
