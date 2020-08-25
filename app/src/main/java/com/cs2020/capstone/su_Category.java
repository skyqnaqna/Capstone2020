package com.cs2020.capstone;

class su_Category
{
    String name;
    int count;

    public su_Category(String name)
    {
        this.name = name;
        this.count = 0;
    }

    public su_Category(String name, int count)
    {
        this.name = name;
        this.count = count;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCount()
    {
        return this.count;
    }

    public void addCount(String namme)
    {
        if (name.equals(this.name))
            this.count++;
        else
            return;
    }

    public void setCount(String name, int count)
    {
        if (name.equals(this.name))
            this.count = count;
        else
            return;
    }
}
