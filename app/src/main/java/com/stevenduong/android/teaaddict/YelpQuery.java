package com.stevenduong.android.teaaddict;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Steven on 3/15/2018.
 */

//Essentially YelpService, should be called yelpConnection or something instead
    //Makes a connection online to yelp api according to parameters/headers
public class YelpQuery {
    private static String API_KEY = "Ube5uJt-BFN-i5P7-mK7cw4r6PB_ahla7ZsFGjW7fItvDQgNxLYuG2i5dJmjyFmveUIDNKHMlPU17o0YNUKE40l-W85AdhoefSXFHytxPVMzDOcNcPL_nGuQCzOoWnYx";
    private static String YELP_BASE_URL = "https://api.yelp.com/v3/businesses/search";

    private static String TERM_QUERY_PARAMETER = "term";
    private static String LOCATION_QUERY_PARAMETER = "location";
    private static String TERM = "milk tea";
    private static String LOCATION = "San Jose, CA"; //default location



    public static void findTeaStores(String location, Callback callback) throws IOException{
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient.Builder().build();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(TERM_QUERY_PARAMETER, TERM);
        urlBuilder.addQueryParameter(LOCATION_QUERY_PARAMETER, location);             //only including location here to test the default location; user should need to pass in a location after though
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + API_KEY)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }



}
