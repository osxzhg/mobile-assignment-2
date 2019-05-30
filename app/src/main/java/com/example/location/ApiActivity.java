package com.example.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ApiActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private String longtitude_str;
    private String latitude_str;
    private StringBuilder output = new StringBuilder();
    public static final String PAYLOAD = "PAYLOAD";
    public static final String RESULT = "RESULT";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        mTextMessage = (TextView) findViewById(R.id.apimsg);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://catalogue.data.govt.nz/api/3/action/datastore_search?resource_id=26f44973-b06d-479d-b697-8d7943c97c57";
        //String url ="https://catalogue.data.govt.nz/api/3/action/datastore_search?resource_id=26f44973-b06d-479d-b697-8d7943c97c57&limit=2";

        Bundle extras = getIntent().getExtras();
        longtitude_str =extras.getString("longtitude");
        latitude_str = extras.getString("latitude");

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //StringBuilder output = new StringBuilder();
                try {
                    JSONObject result = response.getJSONObject("result");
//                    String include = result.getString("include_total");
//                    mTextMessage.setText(include);

                    JSONArray schoolArray = result.getJSONArray("records");
                    for (int i = 0; i < schoolArray.length(); i++)  {
                        JSONObject currentSchool = schoolArray.getJSONObject(i);
                        String org_type = currentSchool.getString("Org_Type");
                        //output.append(org_type);
                        /*
                        if(Objects.equals(org_type,"Free Kindergarten")) {
                            String latitude = currentSchool.getString("Latitude");
                            String longitude = currentSchool.getString("Longitude");
                            String orgName = currentSchool.getString("Org_Name");
                            output.append(orgName + "," + latitude + "," + longitude + ",");
                        }*/
                        String latitude = currentSchool.getString("Latitude");
                        String longitude = currentSchool.getString("Longitude");
                        String orgName = currentSchool.getString("Org_Name");
                        output.append(orgName + "," + latitude + "," + longitude + ",");
                    }
                    mTextMessage.setText(output.toString());
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextMessage.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
