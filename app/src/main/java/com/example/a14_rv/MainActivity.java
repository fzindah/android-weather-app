package com.example.a14_rv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Typeface;

import android.util.TypedValue;
import android.widget.EditText;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //// HERE
    MyRecyclerViewAdapter lv_adapter;
    ArrayList<MyRecyclerViewData> lv_data;
    SwipeRefreshLayout cv_refresh;

    private char tempUnit = 'C';
    ArrayList<String> allZips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_refresh = (SwipeRefreshLayout) findViewById(R.id.vv_refresh);
        cv_refresh.setOnRefreshListener(this);
        cv_refresh.setColorSchemeColors(Color.GREEN);

        RecyclerView lv_recyclerView = findViewById(R.id.vv_rvList);
        lv_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //// HERE

        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                allZips = extras.getStringArrayList("cities");
            else
                allZips = new ArrayList<>(Arrays.asList("48197", "99301", "99703", "85365", "33101"));
        }
        else
            allZips = new ArrayList<>(Arrays.asList("48197", "99301", "99703", "85365", "33101"));

        lv_data = new ArrayList<>();

        httpInThread(allZips, allZips);

        lv_adapter = new MyRecyclerViewAdapter(lv_data, MainActivity.this);
        lv_recyclerView.setAdapter(lv_adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.vv_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = findViewById(R.id.editTextTextPersonName2);
                allZips.add(String.valueOf(edit.getText()));
                httpInThread(new ArrayList<String>(Arrays.asList(String.valueOf(edit.getText()))),
                        allZips);
                lv_adapter.notifyDataSetChanged();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //// HERE
                allZips.remove(allZips.size() - 1);
                lv_data.remove(allZips.size());
                lv_adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        cv_refresh.setRefreshing(true);
        for (int i=0; i < lv_data.size(); i++) {
        }

        lv_adapter.notifyDataSetChanged();
        // This line is important as it explicitly refreshes only once
        // If "true" it implicitly refreshes forever
        cv_refresh.setRefreshing(false);
    }

    private void httpInThread(ArrayList<String> cities, ArrayList<String> zips) {
        new Thread(new Runnable() {
            String stringUrl = "HTTPS unable to get";
            String stringForecast = "Error";
            @Override
            public void run() {
                for (int i=0; i < cities.size(); i++ ) {
                    HashMap<String,String> weatherData = new HashMap<>();

                    // HTTPS request to API
                    int zip = cities.size() == 1 ? zips.size() - 1 : i;
                    Log.i("weatherData1", zip + "");
                    Log.i("weatherData1", cities.size() + "");


                    String stringUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + zips.get(zip) +
                            ",us&APPID=a99372a163f005acd620cd59690582b0";
                    String stringForecast = "https://api.openweathermap.org/data/2.5/forecast?zip=" + zips.get(zip)
                            + ",us&appid=a99372a163f005acd620cd59690582b0";
                    // do your stuff
                    try {
                        String url = downloadUrl(stringUrl);
                        String forecast = downloadUrl(stringForecast);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                // do onPostExecute stuff
                                parseJson(url, weatherData);
                                parseForecast(forecast, weatherData);
                                Log.i("WeatherData", weatherData.toString());
                                lv_data.add(new MyRecyclerViewData(weatherData, allZips));
                                lv_adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }).start();
    }

    private String downloadUrl(String urlString) throws IOException {
        String result = "Download https error";
        InputStream in = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            in = conn.getInputStream();

            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                    reader.close();
                } catch (IOException e) {
                }
        }
        return result;
    }


    public void parseJson(String input, HashMap<String, String> weatherData) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonRootObject = (JSONObject) parser.parse(input);
            String city = jsonRootObject.get("name").toString();
            JSONArray jsonWeatherArray = (JSONArray) jsonRootObject.get("weather");
            String cond = ((JSONObject) jsonWeatherArray.get(0)).get("main").toString();
            JSONObject jsonMainObject = (JSONObject) jsonRootObject.get("main");
            double temp = Double.parseDouble(jsonMainObject.get("temp").toString());
            double min = Double.parseDouble(jsonMainObject.get("temp_min").toString());
            double max = Double.parseDouble(jsonMainObject.get("temp_max").toString());
                synchronized (weatherData) {
                    weatherData.put("city", city);
                    weatherData.put("weather", cond);
                    weatherData.put("temp", convertUnit(String.valueOf(temp - 273.15)));
                    weatherData.put("min", convertUnit(String.valueOf(min - 273.15)));
                    weatherData.put("max", convertUnit(String.valueOf(max - 273.15)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseForecast(String input, HashMap<String, String> weatherData) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonRootObject = (JSONObject) parser.parse(input);
            JSONArray jsonListObject = (JSONArray) jsonRootObject.get("list");
            JSONArray jsonWeatherObject = (JSONArray) ((JSONObject) jsonListObject.get(7)).get("weather");

            long day1Date = (long) ((JSONObject) jsonListObject.get(7)).get("dt");
            long day2Date = (long) ((JSONObject) jsonListObject.get(15)).get("dt");
            long day3Date = (long) ((JSONObject) jsonListObject.get(23)).get("dt");
            long day4Date = (long) ((JSONObject) jsonListObject.get(31)).get("dt");
            long day5Date = (long) ((JSONObject) jsonListObject.get(39)).get("dt");

            String day1Icon = ((JSONObject) jsonWeatherObject.get(0)).get("main").toString();
            String day2Icon = ((JSONObject) ((JSONArray) ((JSONObject)
                    jsonListObject.get(15)).get("weather")).get(0)).get("main").toString();
            String day3Icon = ((JSONObject) ((JSONArray) ((JSONObject)
                    jsonListObject.get(23)).get("weather")).get(0)).get("main").toString();
            String day4Icon = ((JSONObject) ((JSONArray) ((JSONObject)
                    jsonListObject.get(31)).get("weather")).get(0)).get("main").toString();
            String day5Icon = ((JSONObject) ((JSONArray) ((JSONObject)
                    jsonListObject.get(39)).get("weather")).get(0)).get("main").toString();

            SimpleDateFormat ft = new SimpleDateFormat("E");
            String day1 = ft.format(new Date(day1Date*1000));
            String day2 = ft.format(new Date(day2Date*1000));
            String day3 = ft.format(new Date(day3Date*1000));
            String day4 = ft.format(new Date(day4Date*1000));
            String day5 = ft.format(new Date(day5Date*1000));

            synchronized (weatherData) {
                weatherData.put("day1", day1);
                weatherData.put("day2", day2);
                weatherData.put("day3", day3);
                weatherData.put("day4", day4);
                weatherData.put("day5", day5);
                weatherData.put("day1Icon", day1Icon);
                weatherData.put("day2Icon", day2Icon);
                weatherData.put("day3Icon", day3Icon);
                weatherData.put("day4Icon", day4Icon);
                weatherData.put("day5Icon", day5Icon);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String convertUnit(String temp) {
        double t = Double.parseDouble(temp);
        if (tempUnit == 'F')
            return "" + (Math.round(t * 9 / 5) + 32);
        else
            return "" +Math.round(t);
    }

    private ShapeDrawable setBackgroundTemp(String temp, int h) {
        double tempNum = Double.parseDouble(temp);
        String [] color1 = {"#44B2FC", "#AADFEE", "#6dd5fa", "#6dd5fa", "#194BFF",
                "#0F96EF", "#BC22DB", "#4AC8F6", "#62C0FF"};
        String [] color2 = {"#0EF6F0", "#46C9EE", "#0F96EF", "#9A50B4", "#9A50B4",
                "#A668E0", "#836CC2", "#FF0CC2", "#FC0713"};
        tempNum = (tempNum * 9 / 5) + 32;

        int i;
        if (tempNum <= 10) i = 0;
        else if (tempNum <= 20) i = 1;
        else if (tempNum <= 30) i = 2;
        else if (tempNum <= 40) i = 3;
        else if (tempNum <= 50) i = 4;
        else if (tempNum <= 60) i = 5;
        else if (tempNum <= 70) i = 6;
        else if (tempNum <= 80) i = 7;
        else i = 8;

        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setShader(new LinearGradient(0, 0, 0, h,
                Color.parseColor(color1[i]), Color.parseColor(color2[i]), Shader.TileMode.REPEAT));
        return mDrawable;
    }
}