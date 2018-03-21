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
public class YelpConnect {
    private static String API_KEY = "Ube5uJt-BFN-i5P7-mK7cw4r6PB_ahla7ZsFGjW7fItvDQgNxLYuG2i5dJmjyFmveUIDNKHMlPU17o0YNUKE40l-W85AdhoefSXFHytxPVMzDOcNcPL_nGuQCzOoWnYx";
    private static String YELP_BASE_URL = "https://api.yelp.com/v3/businesses/search";
    private static String YELP_STORE_INFO_URL = "https://api.yelp.com/v3/businesses/";

    private static String TERM_QUERY_PARAMETER = "term";
    private static String LOCATION_QUERY_PARAMETER = "location";
    private static String LONGITUDE_QUERY_PARAMETER = "longitude";
    private static String LATITUDE_QUERY_PARAMETER = "latitude";
    private static String RADIUS_QUERY_PARAMETER = "radius";
    private static String SORT_BY_QUERY_PARAMETER = "sort_by";

    private static String TERM = "milk tea";
    private static String LOCATION = "San Jose, CA"; //default location


    public static void buildYelpUrlCurrentLocation(double longitude, double latitude, int radius, String sort_by, Callback callback) throws IOException{
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient.Builder().build();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(TERM_QUERY_PARAMETER, TERM);
        urlBuilder.addQueryParameter(LONGITUDE_QUERY_PARAMETER, Double.toString(longitude));             //only including location here to test the default location; user should need to pass in a location after though
        urlBuilder.addQueryParameter(LATITUDE_QUERY_PARAMETER, Double.toString(latitude));             //only including location here to test the default location; user should need to pass in a location after though
        urlBuilder.addQueryParameter(RADIUS_QUERY_PARAMETER, Integer.toString(radius));
        urlBuilder.addQueryParameter(SORT_BY_QUERY_PARAMETER, sort_by);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + API_KEY)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void buildYelpUrlCustomLocation(String location, int radius, String sort_by, Callback callback) throws IOException{
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient.Builder().build();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(TERM_QUERY_PARAMETER, TERM);
        urlBuilder.addQueryParameter(LOCATION_QUERY_PARAMETER, location);             //only including location here to test the default location; user should need to pass in a location after though
        urlBuilder.addQueryParameter(RADIUS_QUERY_PARAMETER, Integer.toString(radius));
        urlBuilder.addQueryParameter(SORT_BY_QUERY_PARAMETER, sort_by);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + API_KEY)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
/*
    public static void buildStoreInfoUrl(String id, Callback callback) throws IOException{
        //Create HTTP Connection
        OkHttpClient client = new OkHttpClient.Builder().build();

        //Adding Query Parameters and Request Headers
        HttpUrl.Builder urlBuilder = HttpUrl.parse(YELP_BASE_URL).newBuilder();
        urlBuilder.
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization" , "Bearer " + API_KEY)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }*/

}
