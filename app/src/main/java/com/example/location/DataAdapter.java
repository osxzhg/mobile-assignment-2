package com.example.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
{
    String[] name;
    String[] telephone;
    String[] latitude;
    String[] longitude;
    String home_latitude;
    String home_longitude;


    String[] address;
    Context applicationContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View layout;
        public TextView textView;
        public TextView textViewColour;
        public ViewHolder(View layout) {
            super(layout);
            this.layout = layout;

            textView = layout.findViewById(R.id.textView);
            textViewColour = layout.findViewById(R.id.textViewColour);
        }
    }

    public DataAdapter(Context applicationContext, String[] data, String[] telephone, String[] latitude, String[] longitude, String home_latitude, String home_longitude)
    {
        this.applicationContext = applicationContext;
        this.name = data;
        this.telephone = telephone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.home_latitude= home_latitude;
        this.home_longitude=home_longitude;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationContext,name[position],Toast.LENGTH_LONG).show();

                Intent call_all = new Intent(applicationContext,AllActivity.class);
                call_all.putExtra("latitude",latitude[position]);
                call_all.putExtra("longitude",longitude[position]);
                call_all.putExtra("home_latitude",home_latitude);
                call_all.putExtra("home_longitude",home_longitude);
                call_all.putExtra("name",name[position]);
                applicationContext.startActivity(call_all);


            }
        });
/*
        holder.textView.setText(name[position]);

        holder.textViewColour.setBackgroundColor(
                Color.parseColor(name[position])
        ); */
        holder.textView.setText(name[position]);
        holder.textViewColour.setText(telephone[position]);
        Log.e("tele",telephone[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return name.length;
    }
}

