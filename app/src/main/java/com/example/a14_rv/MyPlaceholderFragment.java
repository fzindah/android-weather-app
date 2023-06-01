package com.example.a14_rv;


import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;

import com.example.a14_rv.R;
import com.example.a14_rv.databinding.FragmentMainBinding;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

import android.util.TypedValue;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyPlaceholderFragment extends Fragment {

    // for 'passing intent/fragment parameter' to bundle
    private static final String ARG_TAB_NUMBER = "tab_number+1";
    private static final String ARG_BG_COLOR = "tab_bgcolor";
    private FragmentMainBinding cv_binding;
    private MyPageViewModel cv_pageViewModel;
    private View lv_root;
    private static HashMap<String,String> weatherData;
    char tempUnit = 'C';

    // return MyPlaceholderFragment.newInstance(position + 1);
    public static MyPlaceholderFragment cf_newInstance(int index, ArrayList<MyRecyclerViewData> data) {
        MyPlaceholderFragment lv_fragment = new MyPlaceholderFragment();
        Bundle lv_bundle = new Bundle();
        lv_bundle.putInt(ARG_TAB_NUMBER, index);
        lv_bundle.putSerializable("data", data);
        lv_fragment.setArguments(lv_bundle);
        return lv_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cv_pageViewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
        int lv_index = 1;
        String lv_title = "color";
        if (getArguments() != null) {
            lv_index = getArguments().getInt(ARG_TAB_NUMBER);
            lv_title = getArguments().getString("Weather");
            Log.i("index", lv_index + "");
            weatherData =
                    ((ArrayList<MyRecyclerViewData>) getArguments().getSerializable("data")).get(lv_index).getData();
            Log.i("indexData", getArguments().getSerializable("data").toString());
        }
        cv_pageViewModel.mf_setIndexTitle(lv_index, lv_title);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        cv_binding = FragmentMainBinding.inflate(inflater, container, false);
        lv_root = cv_binding.getRoot();

        cv_pageViewModel.mf_getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // tricky -- get color string from MyPager2Adapter->cf_getTabTitle()
                if (getArguments() != null) {
                    // fetch data from api and display
                    showAppData();
                }
            }
        });

        cv_binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        cv_binding.toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tempUnit = isChecked ? 'F' : 'C';
            cv_binding.vvTemp.setText(convertUnit(weatherData.get("temp")) + "°");
            cv_binding.cvDailyExtremes.setText("H " + convertUnit(weatherData.get("max")) +
                    "   L " + convertUnit(weatherData.get("min")));
        });
        return lv_root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cv_binding = null;
    }

    private void showAppData() {
        Typeface lv_customFont = ResourcesCompat.getFont(getActivity(), R.font.climacons);
        Typeface customFont2 = ResourcesCompat.getFont(getActivity(), R.font.helvetica);

        cv_binding.icon.setTypeface(lv_customFont);
        cv_binding.vvIcon1.setTypeface(lv_customFont);
        cv_binding.vvIcon2.setTypeface(lv_customFont);
        cv_binding.vvIcon3.setTypeface(lv_customFont);
        cv_binding.vvIcon4.setTypeface(lv_customFont);
        cv_binding.vvIcon5.setTypeface(lv_customFont);
        cv_binding.icon.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);

        cv_binding.vvCity.setTypeface(customFont2);
        cv_binding.weather.setTypeface(customFont2);
        cv_binding.vvTemp.setTypeface(customFont2);
        cv_binding.cvDailyExtremes.setTypeface(customFont2);
        cv_binding.vvDay1.setTypeface(customFont2);
        cv_binding.vvDay2.setTypeface(customFont2);
        cv_binding.vvDay3.setTypeface(customFont2);
        cv_binding.vvDay4.setTypeface(customFont2);
        cv_binding.vvDay5.setTypeface(customFont2);


        cv_binding.vvCity.setText(weatherData.get("city"));
        cv_binding.icon.setText(getIcon(weatherData.get("weather")));
        cv_binding.weather.setText(weatherData.get("weather"));
        cv_binding.vvTemp.setText(weatherData.get("temp") + "°");
        cv_binding.cvDailyExtremes.setText("H " + weatherData.get("max") +
                "   L " + weatherData.get("min"));
        cv_binding.vvDay1.setText(weatherData.get("day1"));
        cv_binding.vvDay2.setText(weatherData.get("day2"));
        cv_binding.vvDay3.setText(weatherData.get("day3"));
        cv_binding.vvDay4.setText(weatherData.get("day4"));
        cv_binding.vvDay5.setText(weatherData.get("day5"));

        cv_binding.vvIcon1.setText(getIcon(weatherData.get("day1Icon")));
        cv_binding.vvIcon2.setText(getIcon(weatherData.get("day2Icon")));
        cv_binding.vvIcon3.setText(getIcon(weatherData.get("day3Icon")));
        cv_binding.vvIcon4.setText(getIcon(weatherData.get("day4Icon")));
        cv_binding.vvIcon5.setText(getIcon(weatherData.get("day5Icon")));

        cv_binding.image1.setBackgroundResource(getImg(weatherData.get("weather")));
        cv_binding.backgroundColor.setBackgroundDrawable(setBackgroundTemp(weatherData.get("temp"), lv_root.getHeight()));
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

    private String convertUnit(String temp) {
        double t = Double.parseDouble(temp);
        if (tempUnit == 'F')
            return "" + (Math.round(t * 9 / 5) + 32);
        else
            return "" +Math.round(t);
    }

    private ShapeDrawable setBackgroundTemp(String temp, int h) {
        double tempNum = Double.parseDouble(temp);
        String [] color1 = {"44B2FC", "AADFEE", "6dd5fa", "6dd5fa", "194BFF",
                "0F96EF", "BC22DB", "4AC8F6", "62C0FF"};
        String [] color2 = {"0EF6F0", "46C9EE", "0F96EF", "9A50B4", "9A50B4",
                "A668E0", "836CC2", "FF0CC2", "FC0713"};
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
                Color.parseColor("#50"+color1[i]), Color.parseColor("#50"+color2[i]), Shader.TileMode.REPEAT));
        return mDrawable;
    }


    private int getImg(String weather) {
        int icon = 0;

        switch(weather.toLowerCase()) {
            case "clouds": icon = R.drawable.clouds; break;
            case "clear": icon = R.drawable.clear; break;
            case "tornado": icon = R.drawable.tornado; break;
            case "squall":
            case "snow": icon = R.drawable.flake; break;
            case "fog":
            case "haze":
            case "mist":
            case "smoke": icon = R.drawable.fog; break;
            case "rain":
            case "drizzle": icon = R.drawable.rain; break;
            case "thunderstorm": icon = R.drawable.storm;
        }
        return icon;
    }
}