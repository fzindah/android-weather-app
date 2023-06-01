package com.example.a14_rv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MyListDetailsActivity extends AppCompatActivity {
    private MyListDetailsActivity binding;
    private ArrayList<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_details);

        if (getIntent() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Back");

            Bundle extras = getIntent().getExtras();
            ArrayList<MyRecyclerViewData> allData = (ArrayList<MyRecyclerViewData>)
                    extras.getSerializable("allData");
            cities =  (ArrayList<String>) extras.get("cities");
            MyPager2Adapter lv_pagerAdapter = new MyPager2Adapter(this, allData);

            ViewPager2 lv_viewPager2 = findViewById(R.id.vv_vpViewpager2);
            lv_viewPager2.setAdapter(lv_pagerAdapter);
            lv_viewPager2.setCurrentItem(extras.getInt("position"));

            SpringDotsIndicator lv_springDotsIndicator = findViewById(R.id.vv_spring_dots_indicator);
            lv_springDotsIndicator.setViewPager2(lv_viewPager2);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent lv_it = new Intent(MyListDetailsActivity.this, MainActivity.class);
        lv_it.putExtra("cities", cities);
        this.startActivity(lv_it);
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        return true;
    }
}