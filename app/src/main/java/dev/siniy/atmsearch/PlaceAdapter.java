package dev.siniy.atmsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ryou on 5/29/2016.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {

    public PlaceAdapter(Context context, int resource, List<Place> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.activity_place, null);
        }
        Place p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tvPlaceName = (TextView) view.findViewById(R.id.tvPlaceName);
            tvPlaceName.setText(p.getName());

            TextView tvPlaceAddress = (TextView) view.findViewById(R.id.tvPlaceAddress);
            tvPlaceAddress.setText(p.getAddress());

            ImageView imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            String imageLink = "http://192.168.1.71:81/atmsearch/public/images/" + p.getLogo();
            Picasso.with(getContext()).load(imageLink).into(imgLogo);


        }
        return view;
    }

}