package com.project.nikhil.knock_sense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class add_location extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText location;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("locations");

        location=(EditText)findViewById(R.id.location);
        button=(Button)findViewById(R.id.add_location);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location.getText().toString().trim().length()<2){
                    Toast.makeText(add_location.this,"Please Enter a valid location",Toast.LENGTH_SHORT).show();
                return;
                }
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String locations=(""+dataSnapshot.getValue()+","+location.getText().toString());
                    mDatabase.setValue(locations);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }
        });
    }
}
