package com.project.nikhil.knock_sense;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class restaurant_info extends Fragment {
    private static final String ARG_PARAM1 = "params";

    private restaurant_object object;


    public restaurant_info() {
        // Required empty public constructor
    }

    TextView title,tag_line,description,location_head,location_description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            object = (restaurant_object) getArguments().getSerializable(ARG_PARAM1);
        }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        title=(TextView)v.findViewById(R.id.restaurant_title);
        tag_line=(TextView)v.findViewById(R.id.restaurant_tag_line);
        description=(TextView)v.findViewById(R.id.restaurant_description);
        location_head=(TextView)v.findViewById(R.id.restaurant_location_head);
        location_description=(TextView)v.findViewById(R.id.restaurant_location_description);

        title.setText(object.getName());
        tag_line.setText(object.getTag_line());
        description.setText(object.getDescription());
        location_head.setText(object.getLocation_head());
        location_description.setText(object.getLocation_description());
        return v;
    }
}



