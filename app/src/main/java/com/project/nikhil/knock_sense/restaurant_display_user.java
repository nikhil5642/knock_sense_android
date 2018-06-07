package com.project.nikhil.knock_sense;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class restaurant_display_user extends AppCompatActivity {

      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_display_user);

        restaurant_object object=(restaurant_object) getIntent().getSerializableExtra("data2");

          ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
          tablayout_restaurant_adapter adapter = new tablayout_restaurant_adapter(getSupportFragmentManager(),object);
          viewPager.setAdapter(adapter);

          TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
          tabLayout.setupWithViewPager(viewPager);


    }


}
