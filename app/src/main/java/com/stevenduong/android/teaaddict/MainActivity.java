package com.stevenduong.android.teaaddict;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Essentially YelpActivity
public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    private static String api_key = "Ube5uJt-BFN-i5P7-mK7cw4r6PB_ahla7ZsFGjW7fItvDQgNxLYuG2i5dJmjyFmveUIDNKHMlPU17o0YNUKE40l-W85AdhoefSXFHytxPVMzDOcNcPL_nGuQCzOoWnYx";
    private String term = "milk tea";
    private String location = "San Jose, CA"; //default location
    private final String yelpUrl = "https://api.yelp.com/v3/businesses/search";

    ArrayList<TeaStore> teaStores = new ArrayList<>();
    String result;
/*

    ListView lst;
    String[] teaStoreName={"Pekoes", "Quicklys", "TenRen", "Soyful", "Teaspoon", "Gong Cha"};
    String[] desc={"This is pekoes", "This is quicklys", "This is TenRen", "This is Soyful", "This is Teaspoon", "This is Gong Cha"};
    Integer[] imgid={R.drawable.pekoes, R.drawable.quicklys, R.drawable.tenren, R.drawable.soyful, R.drawable.teaspoon, R.drawable.gongcha};
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //                            DONT NEED ANYMORE FROM BELOW ***************************************************
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);
        try {
            //run();                                  //create an object of yelpquery which is getTeaStores()
            getTeaStores(location);



        } catch (IOException e) {
            e.printStackTrace();
        }

        //                            DONT NEED ANYMORE FROM ABOVE ***************************************************
      /*  lst = (ListView) findViewById(R.id.listview);
        CustomListview customListview = new CustomListview(this, teaStoreName, desc, imgid);  //constructor calls listview_layout
        lst.setAdapter(customListview);*/
    }

    //                            DONT NEED ANYMORE FROM BELOW ***************************************************       Instead, just create a object of class YelpQuery
/*    void run() throws IOException {
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(yelpUrl).newBuilder();
        urlBuilder.addQueryParameter("term", term);
        urlBuilder.addQueryParameter("location", location);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + api_key)
                .url(url)
                .build();


        //Process the JSON Response Asynchronously
        //Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {    //in background thread
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject yelpJSON = new JSONObject(myResponse);
                                String newTextViewResult;

                                //list of businesses based on yelp search criteria
                                JSONArray businessesJSON = yelpJSON.getJSONArray("businesses");
                                for(int i = 0; i < businessesJSON.length(); i++)  {
                                    JSONObject teaStoreJSON = businessesJSON.getJSONObject(i);
                                    String teaStoreName = teaStoreJSON.getString("name");
                                    String teaStoreLocation = teaStoreJSON.getJSONObject("location").getString("address1");
                                    newTextViewResult = teaStoreName + ", " + teaStoreLocation;
                                    mTextViewResult.append(newTextViewResult + "\n");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

        });
    }
    //                            DONT NEED ANYMORE FROM ABOVE ***************************************************
*/

    private void getTeaStores(String location) throws IOException {

        final YelpQuery yelpQuery = new YelpQuery();                //creates instance of the class that will be used to connect to yelp api

        yelpQuery.findTeaStores(location, new Callback() {          //connect to yelp api to search for milk tea stores in specified location
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


    private void getData() {
        String newTextViewResult;
        for(int i = 0; i < teaStores.size(); i++) {
            String teaStoreName = teaStores.get(i).getName();
            String teaStoreLocation = teaStores.get(i).getAddress();
            String teaStoreRating = teaStores.get(i).getRating();
            newTextViewResult = teaStoreName + ", " + teaStoreLocation + ", Rating: " + teaStoreRating;
            mTextViewResult.append(newTextViewResult + "\n\n");
        }
        mTextViewResult.append("\n\n Code successfully parsed correct data with no errors and reach getData()");
    }
}

    /*
    /*
    // Performs network requests
    public class FetchTeaStoresTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            try {
                run();
            } catch(IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        void run() throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {    //in background thread
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException { //get JSON back
                    if(response.isSuccessful()) {
                        final String myResponse = response.body().string();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextViewResult.setText(myResponse);
                            }
                        });
                    }
                }
            });
        }
    }

     */