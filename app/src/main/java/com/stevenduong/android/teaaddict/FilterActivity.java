package com.stevenduong.android.teaaddict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FilterActivity extends AppCompatActivity {
    RadioButton rb_current_location;
    RadioButton rb_custom_location;
    EditText et_custom_location;

    RadioButton rb_1_mile;
    RadioButton rb_5_miles;
    RadioButton rb_10_miles;
    RadioButton rb_20_miles;

    RadioButton rb_best_match;
    RadioButton rb_distance;
    RadioButton rb_rating;
    RadioButton rb_review;

    RadioGroup rg_location;
    RadioGroup rg_search_radius;
    RadioGroup rg_sort_by;

    Button button_apply_filter;

    private String selectedLocation = "";
    private String selectedSearchRadius = "";
    private String selectedSortBy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        rb_current_location = (RadioButton) findViewById(R.id.rb_current_location);
        rb_custom_location = (RadioButton) findViewById(R.id.rb_custom_location);
        et_custom_location = (EditText) findViewById(R.id.et_custom_location);/*
        rb_1_mile = (RadioButton) findViewById(R.id.rb_one_mile);
        rb_5_miles = (RadioButton) findViewById(R.id.rb_five_miles);
        rb_10_miles = (RadioButton) findViewById(R.id.rb_ten_miles);
        rb_20_miles = (RadioButton) findViewById(R.id.rb_twenty_miles);

        rb_best_match = (RadioButton) findViewById(R.id.rb_best_match_sort);
        rb_distance = (RadioButton) findViewById(R.id.rb_distance_sort);
        rb_rating = (RadioButton) findViewById(R.id.rb_rating_sort);
        rb_review = (RadioButton) findViewById(R.id.rb_review_sort);*/

        // might not need those up top?
        rg_location = (RadioGroup) findViewById(R.id.radio_group_location);
        rg_search_radius = (RadioGroup) findViewById(R.id.radio_group_search_radius);
        rg_sort_by = (RadioGroup) findViewById(R.id.radio_group_sort_by);

        button_apply_filter = (Button) findViewById(R.id.button_apply_filter);


        rg_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.rb_current_location:
                        et_custom_location.setEnabled(false);
                        et_custom_location.setInputType(InputType.TYPE_NULL);
                        et_custom_location.setFocusableInTouchMode(false);
                        break;
                    case R.id.rb_custom_location:
                        et_custom_location.setEnabled(true);
                        et_custom_location.setFocusableInTouchMode(true);
                        break;
                }
            }
        });

        button_apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFilter = new Intent(view.getContext(), MainActivity.class);

                int id_location = rg_location.getCheckedRadioButtonId();
                int id_search_radius = rg_search_radius.getCheckedRadioButtonId();
                int id_sort_by = rg_sort_by.getCheckedRadioButtonId();

                selectedLocation = getSelectedLocation(id_location);
                selectedSearchRadius = getSelectedSearchRadius(id_search_radius);
                selectedSortBy = getSelectedSortBy(id_sort_by);

                Bundle extras = new Bundle();
                extras.putString("location", selectedLocation);
                extras.putString("radius", selectedSearchRadius);
                extras.putString("sort_by", selectedSortBy);
                intentFilter.putExtras(extras);

                startActivity(intentFilter);
            }
        });

    }

    private String getSelectedLocation(int id) {
        switch(id) {
            case R.id.rb_current_location:
                selectedLocation = "current";
                break;
            case R.id.rb_custom_location:
                selectedLocation = et_custom_location.getText().toString();
                break;
        }
        return selectedLocation;
    }

    private String getSelectedSearchRadius(int id) {
        switch(id) {            //sent search radius in yelp api is in meters (will be converted into miles after)
            case R.id.rb_one_mile:
                selectedSearchRadius = "1609";
                break;
            case R.id.rb_five_miles:
                selectedSearchRadius = "8046";
                break;
            case R.id.rb_ten_miles:
                selectedSearchRadius = "16093";
                break;
            case R.id.rb_twenty_miles:
                selectedSearchRadius = "32186";
                break;
        }
        return selectedSearchRadius;
    }

    private String getSelectedSortBy(int id) {
        switch(id) {
            case R.id.rb_best_match_sort:
                selectedSortBy = "best_match";
                break;
            case R.id.rb_distance_sort:
                selectedSortBy = "distance";
                break;
            case R.id.rb_rating_sort:
                selectedSortBy = "rating";
                break;
            case R.id.rb_review_sort:
                selectedSortBy = "rating_count";
                break;
            default:
        }
        return selectedSortBy;
    }


}
