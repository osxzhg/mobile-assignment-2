package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ThirdActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // list is the recycler view widget in activity_third.xml
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // setup a linear layout manager to use with the recycler view
        mLayoutManager = new LinearLayoutManager(this);

        // assign the layout manager to the view
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] output = new String[2];
        // specify an adapter (see also next example)
        output[0]="black";
        output[1]="gray";
        //mAdapter = new DataAdapter(getApplicationContext(),new String[]{"black","darkgray", "gray","lightgray","white", "red", "green", "blue", "yellow", "cyan", "magenta", "aqua", "fuchsia","darkgrey", "grey", "lightgrey", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"},new String[]{"black","darkgray", "gray","lightgray","white", "red", "green", "blue", "yellow", "cyan", "magenta", "aqua", "fuchsia","darkgrey", "grey", "lightgrey", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"});
        mAdapter = new DataAdapter(getApplicationContext(),output,new String[]{"black","gray"});
        mRecyclerView.setAdapter(mAdapter);
    }
}
