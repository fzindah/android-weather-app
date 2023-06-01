package com.example.a14_rv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRecyclerViewData implements Serializable {

    private HashMap<String,String> data;
    private String city, weather, temp;
    private ArrayList<String> zips;

    public MyRecyclerViewData(){}

    public MyRecyclerViewData(HashMap<String,String> data, ArrayList<String> zips){
        this.data = data;
        city = data.get("city");
        weather = data.get("weather");
        temp = data.get("temp");
        this.zips = zips;
    }

    @Override
    public String toString() {
        return city;
    }
    public HashMap<String, String> getData(){ return data; }

    public ArrayList<String> getCities() { return zips; }

    public String getCity() {
        return city;
    }

    public String getTemp() {
        return temp;
    }

    public String getWeather() {
        return weather;
    }

    private String getIcon(String weather) {
        String icon = "";

        switch(weather.toLowerCase()) {
            case "clouds": icon = "!"; break;
            case "clear": icon = "I"; break;
            case "tornado": icon = "X"; break;
            case "squall": icon = "6"; break;
            case "fog": icon = "<"; break;
            case "haze":
            case "mist":
            case "smoke": icon = "?"; break;
            case "snow": icon = "9"; break;
            case "rain": icon = "\'"; break;
            case "drizzle": icon = "-"; break;
            case "thunderstorm": icon = "F";
        }
        return icon;
    }
}