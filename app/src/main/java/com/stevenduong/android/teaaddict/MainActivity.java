package com.stevenduong.android.teaaddict;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
public class MainActivity extends AppCompatActivity{

    ArrayList<TeaStore> teaStores = new ArrayList<>();
    TeaStoreAdapter adapter;
    ListView mListView;

    GPSTracker gps;
    Context mContext;
    double longitude;
    double latitude;

    TextView dummyIntentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        dummyIntentResult = (TextView) findViewById(R.id.textViewResult);

        gps = new GPSTracker(mContext, MainActivity.this);
       /* if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }*/

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

                   // dummyIntentResult.setText("latitude: " + Double.toString(latitude) + "longitude: " + Double.toString(longitude) + ", " + searchRadiusFromIntent + ", " + sortByFromIntent);
                } else {
                    getTeaStoresCustomLocation(locationFromIntent, Integer.parseInt(searchRadiusFromIntent), sortByFromIntent);
                    //dummyIntentResult.setText(locationFromIntent + ", " + searchRadiusFromIntent + ", " + sortByFromIntent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //might not need spinner anymore. WORK ON FILTER MENU BUTTON
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_filter, menu);
      // MenuItem item = menu.findItem(R.id.spinner);
        //Spinner spinner = (Spinner) item.getActionView();

      // ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.filter_spinner_array, android.R.layout.simple_spinner_dropdown_item);
    //   spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // spinner.setAdapter(spinnerAdapter);

      // spinner.setOnItemSelectedListener(this);

       return true;
    }

    //WORK ON THIS ONE
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
                    });     //call getData() below

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

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        try {
        switch(position) {
            case 0: //Based on current location
                getTeaStoresCurrentLocation(longitude, latitude, 16093, "best_match");
                break;
            case 1: //Best Match
                //sort by review count
                getTeaStoresCustomLocation("San Francisco", 16093, "best_match");
                break;
            case 2: //Distance
                //sort by review count
                getTeaStoresCustomLocation("San Francisco", 16093, "distance");
                break;
            case 3: //Ratings
                //sort by review count
                getTeaStoresCustomLocation("San Francisco", 16093, "rating");
                break;
            case 4: //Review Count
                //sort by review count
                getTeaStoresCustomLocation("San Francisco", 16093, "review_count");
                break;
        }} catch (IOException e) {
                e.printStackTrace();
            }

        Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString() + " Selected",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}