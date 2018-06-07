package com.project.nikhil.knock_sense;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class add_restaurant extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    EditText title, tag_line, description,location_description;
    private DatabaseReference mDatabase;
    Spinner location_head;
    Button add,add_location;
    private LatLng data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) ){
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        title = (EditText) findViewById(R.id.title);
        tag_line = (EditText) findViewById(R.id.tag_line);
        description = (EditText) findViewById(R.id.description);
        location_description=(EditText)findViewById(R.id.location_description);
        location_head=(Spinner)findViewById(R.id.location_head);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        add=(Button)findViewById(R.id.save);
        add_location=(Button)findViewById(R.id.add_location);

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(add_restaurant.this,add_location.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data==null){
                    Toast.makeText(add_restaurant.this, "Select your restaurant in Map", Toast.LENGTH_LONG).show();
                    return;
                }
                restaurant_object object=new restaurant_object(
                        "kklj"
                        ,title.getText().toString()
                        ,tag_line.getText().toString()
                        ,description.getText().toString()
                        ,""
                        ,""
                        ,location_head.getSelectedItem().toString().trim()
                        ,location_description.getText().toString().trim()
                        ,""+data.latitude
                        ,""+data.longitude
                        ,"",0,0, FirebaseAuth.getInstance().getCurrentUser().getUid()
                );
                    DatabaseReference rest = mDatabase.child("restaurants").child(location_head.getSelectedItem().toString().trim()).push();
                    object.setKey(rest.getKey());
                    rest.child("info").setValue(object);
                   Toast.makeText(add_restaurant.this, "SuccessFully added", Toast.LENGTH_LONG).show();
                Intent i=new Intent(add_restaurant.this,Admin_home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        final DatabaseReference location=mDatabase.child("locations");
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

           mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    data=latLng;
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your Restaurant"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
        }
}
