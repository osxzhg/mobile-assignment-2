package com.example.location;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
{
    String[] name;
    String[] telephone;
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

    public DataAdapter(Context applicationContext, String[] data, String[] telephone)
    {
        this.applicationContext = applicationContext;
        this.name = data;
        this.telephone = telephone;

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
            }
        });

        holder.textView.setText(name[position]);

        holder.textViewColour.setBackgroundColor(
                Color.parseColor(name[position])
        );
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return name.length;
    }
}

