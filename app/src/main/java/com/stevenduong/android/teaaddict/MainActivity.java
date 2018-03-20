package com.stevenduong.android.teaaddict;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Essentially YelpActivity
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<TeaStore> teaStores = new ArrayList<>();
    TeaStoreAdapter adapter;
    ListView mListView;
    TextView mResult;

    GPSTracker gps;
    Context mContext;
    double longitude;
    double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mResult = (TextView) findViewById(R.id.textViewResult);

        gps = new GPSTracker(mContext, MainActivity.this);
        if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.filter_menu, menu);
       MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

       ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.filter_spinner_array, android.R.layout.simple_spinner_dropdown_item);
       spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(spinnerAdapter);

       spinner.setOnItemSelectedListener(this);

       return true;
    }

    private void getTeaStores(double longitude, double latitude) throws IOException {
        teaStores.clear();          //wipe previous data of listview

        final YelpConnect yelpConnect = new YelpConnect();                //creates instance of the class that will be used to connect to yelp api

        yelpConnect.buildYelpUrl(longitude, latitude, new Callback() {          //connect to yelp api to search for milk tea stores in specified location
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

    private void getTeaStoresBySorting(String location, String sort_by) throws IOException {
        teaStores.clear();          //wipe previous data of listview

        final YelpConnect yelpConnect = new YelpConnect();                //creates instance of the class that will be used to connect to yelp api

        yelpConnect.buildYelpUrl(location, sort_by, new Callback() {          //connect to yelp api to search for milk tea stores in specified location
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch(position) {
            case 0: //Based on current location
                try {
                    getTeaStores(longitude, latitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1: //Best Match
                try {
                    //sort by review count
                    getTeaStoresBySorting("San Francisco", "best_match");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2: //Distance
                try {
                    //sort by review count
                    getTeaStoresBySorting("San Francisco", "distance");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3: //Ratings
                try {
                    //sort by review count
                    getTeaStoresBySorting("San Francisco", "rating");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4: //Review Count
                try {
                    //sort by review count
                    getTeaStoresBySorting("San Francisco", "review_count");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString() + " Selected",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





}