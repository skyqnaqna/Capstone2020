package com.cs2020.capstone;

public class su_Category
{
    String name;
    int count;

    public su_Category(String name)
    {
        this.name = name;
        this.count = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCount()
    {
        return count;
    }

    public void addCount()
    {
        this.count++;
    }
}
