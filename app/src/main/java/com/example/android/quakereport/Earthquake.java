package com.example.android.quakereport;

/**
 * Created by meghnabanerjee on 5/18/17.
 */

public class Earthquake {
    private double magnitude; private String location; String webpage; long date;

    public Earthquake(double magnitude, String location, Long date, String webpage)
    {
        this.magnitude = magnitude;
        this.location = location;
        this.date = date;
        this.webpage =  webpage;

    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public String getLocation()
    {
        return  location;
    }

    public long getDate()
    {
        return date;
    }

    public String getWebpage()
    {
        return webpage;
    }
}
