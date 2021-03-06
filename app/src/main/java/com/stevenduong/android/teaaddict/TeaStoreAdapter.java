package com.stevenduong.android.teaaddict;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Steven on 3/16/2018.
 */

public class TeaStoreAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<TeaStore> mDataSource;

    public TeaStoreAdapter(Context context, ArrayList<TeaStore> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.layout_listview, parent, false);

        // Get title element
        TextView nameTextView =
                (TextView) rowView.findViewById(R.id.tv_teaStoreName);

// Get subtitle element
        TextView addressTextView =
                (TextView) rowView.findViewById(R.id.tv_address);

// Get detail element
        TextView reviewCountTextView =
                (TextView) rowView.findViewById(R.id.tv_reviewCount);

        RatingBar ratingBarView =
                (RatingBar) rowView.findViewById(R.id.myRatingBar);

        TextView distanceTextView =
                (TextView) rowView.findViewById(R.id.tv_distance);

        TextView openNowTextView =
                (TextView) rowView.findViewById(R.id.tv_openNow);

// Get thumbnail element
        ImageView pictureImageView =
                (ImageView) rowView.findViewById(R.id.iv_teaPicture);


        // 1
        TeaStore teaStore = (TeaStore) getItem(position);

// 2
        nameTextView.setText(teaStore.name);
        addressTextView.setText(teaStore.address);
        reviewCountTextView.setText(teaStore.reviewCount);
        float rating = Float.parseFloat(teaStore.rating);
        ratingBarView.setRating(rating);
        distanceTextView.setText(String.format("%.2f miles",teaStore.getDistanceInMiles()));
        openNowTextView.setText(teaStore.id);


// 3
        Picasso.with(mContext).load(teaStore.imageUrl).placeholder(R.mipmap.ic_launcher).into(pictureImageView);
        return rowView;
    }

}
