package com.project.nikhil.knock_sense;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class sign_upActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;
      TextView msg;
    private EditText mOtp,mMobile,mpassword,mpassword_repeat,muser,memail;
    SharedPreferences sharedpreferences;
    private TextView register_already;
    SharedPreferences.Editor editor;
    private Button register;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private String session_id,verify;
    int x=0;
    DataSnapshot snapshot=null;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Set up the login form.
        isStoragePermissionGranted();
        mMobile = (EditText)findViewById(R.id.signup_mobile_no);
        register=(Button)findViewById(R.id.btn_register);
        mpassword=(EditText)findViewById(R.id.signup_password);
        mpassword_repeat=(EditText)findViewById(R.id.signup_password_reenter);
        muser=(EditText)findViewById(R.id.signup_name);
        memail=(EditText)findViewById(R.id.signup_email);
        sharedpreferences=getApplicationContext().getSharedPreferences(getString(R.string.pref_file),0);
        editor=sharedpreferences.edit();
        register_already=(TextView)findViewById(R.id.txt_register);
        mAuth = FirebaseAuth.getInstance();
         setupmenu();

        mDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               snapshot=dataSnapshot;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        register_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(sign_upActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInternetAvailable()){
                    Toast.makeText(sign_upActivity.this, "Check your Internet Connection then Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!memail.getText().toString().contains("@")){
                    Toast.makeText(sign_upActivity.this,"Enter Correct Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(mpassword.getText().toString().trim().contains(mpassword_repeat.getText().toString().trim()))) {
                    Toast.makeText(sign_upActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                return;
                }
                mAuth.createUserWithEmailAndPassword(memail.getText().toString().trim(),mpassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference info=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("info");
                                    current_user current_user=new current_user(uid,muser.getText().toString(),memail.getText().toString(),mMobile.getText().toString(),"lkh");
                                    info.setValue(current_user);
                                    Intent intent=new Intent(sign_upActivity.this,signup_add_pic.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(sign_upActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
        }
        });

     }

  public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void setupmenu() {
        TextView heading=(TextView)findViewById(R.id.app_bar_main_heading);
        Typeface face1 = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/gaurav.ttf");
        Typeface face2 = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/prashant.ttf");
        heading.setText("Sign Up");
        heading.setTypeface(face1);
    }
    public boolean isInternetAvailable(){
        ConnectivityManager cm=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active=cm.getActiveNetworkInfo();
        boolean isconnected=active!=null && active.isConnected();
        return isconnected;
    }
}


