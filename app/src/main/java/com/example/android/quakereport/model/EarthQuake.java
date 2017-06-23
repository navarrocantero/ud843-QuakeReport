package com.example.android.quakereport.model;

/**
 * Created by driftineo on 2/6/17.
 */

public class EarthQuake {
    private String cityName;
    private String magnitude;
    private String date;

    public EarthQuake(String cityName, String magnitude, String date) {
        this.cityName = cityName;
        this.magnitude = magnitude;
        this.date = date;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {



        this.date = date;
    }


}
