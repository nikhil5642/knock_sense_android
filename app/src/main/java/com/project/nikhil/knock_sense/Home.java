package com.project.nikhil.knock_sense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    Spinner location_head;
    DatabaseReference mdatabase;
    Button admin;
    ArrayList<restaurant_object> list=new ArrayList<restaurant_object>();
    private int lastVisibleItem, totalItemCount;
    private boolean loading=false;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        admin = (Button) findViewById(R.id.admin);
        location_head=(Spinner)findViewById(R.id.location_head);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Admin_home.class);
                startActivity(intent);
            }
        });
        mdatabase=FirebaseDatabase.getInstance().getReference().child("restaurants").child("West-Lucknow");
        final DatabaseReference location=FirebaseDatabase.getInstance().getReference().child("locations");
        location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String locations[]=(""+dataSnapshot.getValue()).split(",");
                List<String> stringList = new ArrayList<String>(Arrays.asList(locations)); //new ArrayList is only needed if you absolutely need an ArrayList for()
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,stringList);
                location_head.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        location_head.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list.clear();
                mdatabase=FirebaseDatabase.getInstance().getReference().child("restaurants").child(location_head.getSelectedItem().toString().trim());
                mdatabase.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot notesnapshot:dataSnapshot.getChildren()){
                            restaurant_object obj=(restaurant_object)notesnapshot.child("info").getValue(restaurant_object.class);
                            list.add(obj);
                        }
                        set(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }


                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount=list.size();
                        lastVisibleItem = mLayoutManager
                                .findLastVisibleItemPosition();
                        if ((!loading)  && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            loading=true;
                            mdatabase.orderByKey().startAt(list.get(totalItemCount-1).getKey()).limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot notesnapshot:dataSnapshot.getChildren()){
                                        restaurant_object obj=(restaurant_object)notesnapshot.child("info").getValue(restaurant_object.class);
                                        list.add(obj);
                                        Log.e("scroller_active","working");
                                        }
                                    if(list.size()>totalItemCount){
                                    set(list);}
                                    loading=false;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });      }    };
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
    public void set(ArrayList<restaurant_object> list){
         mAdapter = new restaurant_recycler_adapter(list,restaurant_display_user.class);
        recyclerView.setAdapter(mAdapter);
     }

}