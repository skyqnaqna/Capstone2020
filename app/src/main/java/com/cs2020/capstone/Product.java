package com.cs2020.capstone;

class Product
{
    String image_src;
    String name;
    String category;
    int year, month, day;
    // 알림 추가
    String company;

    public Product(String name, String category, String company)
    {
        this.name = name;
        this.category = category;
        this.company = company;
    }

    public Product(String name, String category, String company, int year, int month, int day)
    {
        this.name = name;
        this.category = category;
        this.company = company;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getName() {return name;}
    public String getCategory() {return category;}
    public Integer getYear() {return year;}
    public Integer getMonth() {return month;}
    public Integer getDay() {return day;}

    public void setName(String name) { this.name = name;}
    public void setCategory(String category) {this.category = category;}
    public void setCompany(String company) {this.company = company;}

}
