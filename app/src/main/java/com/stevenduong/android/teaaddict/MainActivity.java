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

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    private static String api_key = "Ube5uJt-BFN-i5P7-mK7cw4r6PB_ahla7ZsFGjW7fItvDQgNxLYuG2i5dJmjyFmveUIDNKHMlPU17o0YNUKE40l-W85AdhoefSXFHytxPVMzDOcNcPL_nGuQCzOoWnYx";
    private String term = "milk tea";
    private String location = "San Jose, CA"; //default location
    private final String yelpUrl = "https://api.yelp.com/v3/businesses/search?term=" + term;
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

        mTextViewResult = (TextView) findViewById(R.id.textViewResult);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
      /*  lst = (ListView) findViewById(R.id.listview);
        CustomListview customListview = new CustomListview(this, teaStoreName, desc, imgid);
        lst.setAdapter(customListview);*/
    }

    void run() throws IOException {
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(yelpUrl).newBuilder();
        urlBuilder.addQueryParameter("location", location);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + api_key)
                .url(url)
                .build();


        //Process the JSON Response Asynchronously
        client.newCall(request).enqueue(new Callback() {    //in background thread
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
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
            }
        });
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