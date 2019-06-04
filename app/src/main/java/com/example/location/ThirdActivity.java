package com.example.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ThirdActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String longitude_str;
    private String latitude_str;
    private String city_str;

    public static final String PAYLOAD = "PAYLOAD";
    public static final String RESULT = "RESULT";
    StringBuilder data = new StringBuilder();

    ArrayList<String> output_name = new ArrayList<String>();
    ArrayList<String> output_tele = new ArrayList<String>();
    ArrayList<String> output_latitude = new ArrayList<String>();
    ArrayList<String> output_longitude = new ArrayList<String>();
    ArrayList<String> output_authority = new ArrayList<String>();
    ArrayList<String> output_students = new ArrayList<String>();

    private static final  double EARTH_RADIUS = 6378137;//
    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        return s;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Bundle extras = getIntent().getExtras();
        longitude_str =extras.getString("longitude");
        latitude_str = extras.getString("latitude");
        city_str = extras.getString("cityName");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://catalogue.data.govt.nz/api/3/action/datastore_search?resource_id=26f44973-b06d-479d-b697-8d7943c97c57";
        //String url ="https://catalogue.data.govt.nz/api/3/action/datastore_search?resource_id=26f44973-b06d-479d-b697-8d7943c97c57&limit=2";


        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // setup a linear layout manager to use with the recycler view
        mLayoutManager = new LinearLayoutManager(this);

        // assign the layout manager to the view
        mRecyclerView.setLayoutManager(mLayoutManager);

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
                        //String org_type = currentSchool.getString("Org_Type");
                        //output.append(org_type);
                        /*
                        if(Objects.equals(org_type,"Free Kindergarten")) {
                            String latitude = currentSchool.getString("Latitude");
                            String longitude = currentSchool.getString("Longitude");
                            String orgName = currentSchool.getString("Org_Name");
                            output.append(orgName + "," + latitude + "," + longitude + ",");
                        }*/

                        String cityName = currentSchool.getString("Education_Region");
                        String latitude = currentSchool.getString("Latitude");
                        String longitude = currentSchool.getString("Longitude");
                        //Objects.equals(cityName,city_str);
                        if(true){
                            //Total Authority
                            if(GetDistance( Double.parseDouble(longitude_str),
                                            Double.parseDouble(latitude_str),
                                            Double.parseDouble(longitude),
                                            Double.parseDouble(latitude))<1000000){
                                String orgName = currentSchool.getString("Org_Name");
                                String telephone = currentSchool.getString("Telephone");
                                String authority = currentSchool.getString("Authority");
                                String students = currentSchool.getString("All_Children");
                                output_latitude.add(latitude);
                                output_longitude.add(longitude);
                                output_name.add(orgName);
                                output_tele.add(telephone);
                                output_authority.add(authority);
                                output_students.add(students);
                            }






                        }



                        //output.append(orgName + "," + latitude + "," + longitude + ",");
                    }
                    // list is the recycler view widget in activity_third.xml


                    String[] name = output_name.toArray(new String[0]);
                    String[] tele = output_tele.toArray(new String[0]);
                    String[] latitude = output_latitude.toArray(new String[0]);
                    String[] longitude = output_longitude.toArray(new String[0]);
                    String[] authority = output_authority.toArray(new String[0]);
                    String[] students = output_students.toArray(new String[0]);
                    // specify an adapter (see also next example)

                    //mAdapter = new DataAdapter(getApplicationContext(),new String[]{"black","darkgray", "gray","lightgray","white", "red", "green", "blue", "yellow", "cyan", "magenta", "aqua", "fuchsia","darkgrey", "grey", "lightgrey", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"},new String[]{"black","darkgray", "gray","lightgray","white", "red", "green", "blue", "yellow", "cyan", "magenta", "aqua", "fuchsia","darkgrey", "grey", "lightgrey", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"});
                    mAdapter = new DataAdapter(getApplicationContext(),name,tele,latitude,longitude,latitude_str,longitude_str,authority,students);
                    mRecyclerView.setAdapter(mAdapter);
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley","That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }
}
