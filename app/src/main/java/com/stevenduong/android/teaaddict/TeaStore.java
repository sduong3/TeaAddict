package com.stevenduong.android.teaaddict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Steven on 3/15/2018.
 */

/**
 *
 */
public class TeaStore implements Serializable {
    String id;
    String name;
    String address;
    String imageUrl;
    String reviewCount;
    String rating;
    String distance;
    double distanceInMiles;
    Boolean openNow;

    public String getName() {
        return name;
    }

    public String getId() { return id;}
    public String getAddress() {
        return address;
    }

    public String getImageUrl() {return imageUrl;}

    public String getReviewCount() {return reviewCount;}

    public String getRating() {
        return rating;
    }

    public String getDistance() {return distance;}

    public Boolean getOpenNow() { return openNow;}

    public Double getDistanceInMiles() {return Double.parseDouble(distance) *0.000621371192;}

    public void setId(String id) { this.id = id;}
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setReviewCount(String reviewCount) { this.reviewCount = reviewCount;}
    public void setDistance(String distance) {this.distance = distance;}

    public void setOpenNow(Boolean openNow) { this.openNow = openNow;}

    //Parse the jsonObject and extract data to be stored into its TeaStore properties
    public TeaStore(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");       //to be used to determine whether store is currently open
            this.name = jsonObject.getString("name");
            this.imageUrl = jsonObject.getString("image_url");
            this.rating = jsonObject.getString("rating");
            this.reviewCount = jsonObject.getString("review_count") + " Reviews";
            this.distance = jsonObject.getString("distance");   //in meters
       //     this.openNow = jsonObject.getBoolean("open_now");

            if(jsonObject.getJSONObject("location").getString("address1") != "") {
                this.address = jsonObject.getJSONObject("location").getString("address1");
            } else {
                this.address = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //For each element in the jsonArray, create a new TeaStore object with its extracted properties. Then add the object into the arraylist resultsTeaS
    public static ArrayList<TeaStore> fromJsonArray(JSONArray array) {
        ArrayList<TeaStore> results = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                results.add(new TeaStore(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


}
