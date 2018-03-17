package com.stevenduong.android.teaaddict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Essentially YelpActivity
public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    private static String api_key = "Ube5uJt-BFN-i5P7-mK7cw4r6PB_ahla7ZsFGjW7fItvDQgNxLYuG2i5dJmjyFmveUIDNKHMlPU17o0YNUKE40l-W85AdhoefSXFHytxPVMzDOcNcPL_nGuQCzOoWnYx";
    private String term = "milk tea";
    private String location = "San Jose, CA"; //default location
    private final String yelpUrl = "https://api.yelp.com/v3/businesses/search";

    ArrayList<TeaStore> teaStores = new ArrayList<>();
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = (TextView) findViewById(R.id.textViewResult);

        try {
            getTeaStores(location);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //                            DONT NEED ANYMORE FROM ABOVE ***************************************************
      /*  lst = (ListView) findViewById(R.id.listview);
        CustomListview customListview = new CustomListview(this, teaStoreName, desc, imgid);  //constructor calls listview_layout
        lst.setAdapter(customListview);*/
    }


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
                    });     //call getData() below

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getData() {
        mListView = (ListView) findViewById(R.id.listview);
        TeaStoreAdapter adapter = new TeaStoreAdapter(this, teaStores);
        mListView.setAdapter(adapter);

        /*
        String newTextViewResult;
        for(int i = 0; i < teaStores.size(); i++) {
            String teaStoreName = teaStores.get(i).getName();
            String teaStoreLocation = teaStores.get(i).getAddress();
            String teaStoreRating = teaStores.get(i).getRating();
            String teaStoreImageUrl = teaStores.get(i).getImageUrl();
            newTextViewResult = teaStoreName + ", " + teaStoreLocation + ", Rating: " + teaStoreRating + ", " + teaStoreImageUrl;
            mTextViewResult.append(newTextViewResult + "\n\n");
        }
        mTextViewResult.append("\n\n Code successfully parsed correct data with no errors and reach getData()");*/
    }
}