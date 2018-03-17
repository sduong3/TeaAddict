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
    String name;
    String address;
    String imageUrl;
    String rating;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRating() {
        return rating;
    }

    //Parse the jsonObject and extract data to be stored into its TeaStore properties
    public TeaStore(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.imageUrl = jsonObject.getString("image_url");
            this.rating = jsonObject.getString("rating");

            if(jsonObject.getJSONObject("location").getString("address1") != "") {
                this.address = jsonObject.getJSONObject("location").getString("address1")
                        .concat(", ")
                        .concat(jsonObject.getJSONObject("location").getString("city"))
                        .concat(", ")
                        .concat(jsonObject.getJSONObject("location").getString("state"))
                        .concat(", ")
                        .concat(jsonObject.getJSONObject("location").getString("zip_code"));
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
