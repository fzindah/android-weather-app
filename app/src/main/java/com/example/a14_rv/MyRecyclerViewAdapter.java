package com.example.a14_rv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    //// HERE
    private final ArrayList<MyRecyclerViewData> mv_data;
    Activity cv_activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView cv_tvModel, cv_tvOS, cv_tvPrice;
        private final View layout;

        public MyViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            cv_tvModel = view.findViewById(R.id.vv_tvModel);
            cv_tvOS = view.findViewById(R.id.vv_tvOS);
            cv_tvPrice = view.findViewById(R.id.vv_tvPrice);
            layout = view.findViewById(R.id.layout);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "position : " + getLayoutPosition()
                            + " text : " + cv_tvModel.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //// HERE
    public MyRecyclerViewAdapter(ArrayList<MyRecyclerViewData> data, Activity activity) {
        mv_data = data;
        cv_activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View lv_view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);

        MyViewHolder lv_holder = new MyViewHolder(lv_view);
        lv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "position : " + lv_holder.getLayoutPosition()
                        + " text : " + lv_holder.cv_tvModel.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return lv_holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.MyViewHolder holder, int position) {
        //// HERE
        Typeface lv_customFont = ResourcesCompat.getFont(cv_activity, R.font.climacons);
        holder.cv_tvModel.setTypeface(lv_customFont);

        lv_customFont = ResourcesCompat.getFont(cv_activity, R.font.helvetica);
        holder.cv_tvOS.setTypeface(lv_customFont);
        holder.cv_tvPrice.setTypeface(lv_customFont);

        holder.cv_tvModel.setText(getIcon(mv_data.get(position).getWeather()));
        holder.cv_tvOS.setText(mv_data.get(position).getCity());
        holder.cv_tvPrice.setText(mv_data.get(position).getTemp() + "°");
        holder.layout.setBackgroundResource(getImg(mv_data.get(position).getWeather()));

//// Method 3: itemView is hidden defined
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent lv_it = new Intent(cv_activity, MyListDetailsActivity.class);
                    lv_it.putExtra("data", mv_data.get(position));
                    lv_it.putExtra("cities", mv_data.get(position).getCities());
                    lv_it.putExtra("city", mv_data.get(position).getCity());
                    lv_it.putExtra("allData", mv_data);
                    lv_it.putExtra("position", position);
                    cv_activity.startActivity(lv_it);
                    cv_activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
            });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //// HERE
        return mv_data.size();
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