package com.stevenduong.android.teaaddict;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Essentially YelpActivity
public class MainActivity extends AppCompatActivity{

    ArrayList<TeaStore> teaStores = new ArrayList<>();
    TeaStoreAdapter adapter;
    ListView mListView;

    GPSTracker gps;
    Context mContext;
    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        gps = new GPSTracker(mContext, MainActivity.this);

        Intent intentFromFilter = getIntent();
        Bundle bundleFromFilter = intentFromFilter.getExtras();

        try {
            if(bundleFromFilter != null) {
                String locationFromIntent = (String) bundleFromFilter.get("location");
                String searchRadiusFromIntent = (String) bundleFromFilter.get("radius");
                String sortByFromIntent = (String) bundleFromFilter.get("sort_by");

                if(locationFromIntent.equals("current")) {
                    if(gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                    } else {
                        gps.showSettingsAlert();
                    }
                    getTeaStoresCurrentLocation(longitude, latitude, Integer.parseInt(searchRadiusFromIntent), sortByFromIntent);

                } else {
                    getTeaStoresCustomLocation(locationFromIntent, Integer.parseInt(searchRadiusFromIntent), sortByFromIntent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_filter, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), FilterActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void getTeaStoresCurrentLocation(double longitude, double latitude, int radius, String sort_by) throws IOException {
        teaStores.clear();          //wipe previous data of listview

        final YelpConnect yelpConnect = new YelpConnect();                //creates instance of the class that will be used to connect to yelp api

        yelpConnect.buildYelpUrlCurrentLocation(longitude, latitude, radius, sort_by, new Callback() {          //connect to yelp api to search for milk tea stores in specified location
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {   //if connection is successful...
                try {
                    String jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonResults = jsonObject.getJSONArray("businesses");      //retrieved all milk tea stores based on location (this includes all fields of each business)

                    teaStores.addAll(TeaStore.fromJsonArray(jsonResults));                    //ArrayList teaStores will then add all tea stores objects from the returned arraylist

                    runOnUiThread(new Runnable() {                  //now back on main thread
                        @Override
                        public void run() {
                            getData();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTeaStoresCustomLocation(String location, int radius, String sort_by) throws IOException {
        teaStores.clear();          //wipe previous data of listview

        final YelpConnect yelpConnect = new YelpConnect();                //creates instance of the class that will be used to connect to yelp api

        yelpConnect.buildYelpUrlCustomLocation(location, radius, sort_by, new Callback() {          //connect to yelp api to search for milk tea stores in specified location
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {   //if connection is successful...
                try {
                    String jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonResults = jsonObject.getJSONArray("businesses");      //retrieved all milk tea stores based on location (this includes all fields of each business)

                    teaStores.addAll(TeaStore.fromJsonArray(jsonResults));                    //ArrayList teaStores will then add all tea stores objects from the returned arraylist

                    runOnUiThread(new Runnable() {                  //now back on main thread
                        @Override
                        public void run() {
                            getData();
                        }
                    });     //call getData() below

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData() {
        mListView = (ListView) findViewById(R.id.listview);
        adapter = new TeaStoreAdapter(this, teaStores);
        mListView.setAdapter(adapter);

    }
}