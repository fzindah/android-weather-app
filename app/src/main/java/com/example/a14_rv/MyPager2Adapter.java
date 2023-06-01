package com.example.a14_rv;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPager2Adapter extends FragmentStateAdapter {
    Context cv_context;

    ArrayList<MyRecyclerViewData> data;

    public MyPager2Adapter(FragmentActivity fa, ArrayList<MyRecyclerViewData> data) {
        super(fa);
        cv_context = fa.getApplicationContext();
        this.data = data;
    }

    @Override
    public Fragment createFragment(int position) {
        // [] index from 0, "Page 1-4 is selected"
        return MyPlaceholderFragment.cf_newInstance(
                position, data);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}