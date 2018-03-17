package com.stevenduong.android.teaaddict;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Steven on 3/13/2018.
 */

public class CustomListview extends ArrayAdapter<String> {

    private String[] teaStoreName;
    private String[] desc;
    private Integer[] imgid;
    private Activity context;

    public CustomListview(Activity context, String[] teaStoreName, String[] desc, Integer[] imgid) {
        super(context, R.layout.listview_layout, teaStoreName);

        this.context = context;
        this.teaStoreName = teaStoreName;
        this.desc = desc;
        this.imgid = imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.ivw.setImageResource(imgid[position]);
        viewHolder.tvw1.setText(teaStoreName[position]);
        viewHolder.tvw2.setText(desc[position]);

        return r;
    }


    class ViewHolder {
        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        ImageView ivw;

        ViewHolder(View v) {
            tvw1 = (TextView) v.findViewById(R.id.tv_teaStoreName);
            tvw2 = (TextView) v.findViewById(R.id.tv_address);
            tvw3 = (TextView) v.findViewById(R.id.tv_rating);
            ivw = (ImageView) v.findViewById(R.id.iv_teaPicture);
        }
    }


}
