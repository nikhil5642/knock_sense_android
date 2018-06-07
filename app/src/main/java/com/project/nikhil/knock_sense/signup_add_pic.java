package com.project.nikhil.knock_sense;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class signup_add_pic extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
     private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;
    String imageurl=null;
    Button cont,skip;
    Uri public_uri=null;
    ImageView scanner_image;
    TextView abc;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    current_user current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_add_pic);
        isStoragePermissionGranted();
        progressDialog=new ProgressDialog(signup_add_pic.this);

        sharedpreferences=getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_file),0);
        editor=sharedpreferences.edit();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user=dataSnapshot.getValue(current_user.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        scanner_image=(ImageView)findViewById(R.id.scanner_image);
        abc=(TextView)findViewById(R.id.abc);
        cont=(Button)findViewById(R.id.button_scan);
        skip=(Button)findViewById(R.id.button_skip);


        scanner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isStoragePermissionGranted()){
                    return;
                }
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        REQUEST_SELECT_IMAGE_IN_ALBUM);
            }
        });



        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_image(public_uri);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(signup_add_pic.this,Home.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void upload_image(Uri public_uri) {

        if(imageurl!=null){
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StorageReference ref=storageReference.child("images").child(current_user.getEmail());
        ref.putFile(public_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid()).child("info");
               Uri download_link=taskSnapshot.getDownloadUrl();
               mDatabase.child("photo").setValue(download_link.toString());
               Toast.makeText(signup_add_pic.this,"Upload Done",Toast.LENGTH_SHORT).show();
               Intent i=new Intent(signup_add_pic.this,Home.class);
                startActivity(i);
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 progressDialog.dismiss();
                Toast.makeText(signup_add_pic.this,"Upload Failed",Toast.LENGTH_SHORT).show();;
            }
        });
    }else {
        Toast.makeText(this,"Please Select an image and proceed",Toast.LENGTH_SHORT);
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE_IN_ALBUM:
                if (resultCode == RESULT_OK) {
                    Uri targetUri = data.getData();

                    Log.e("problem", "working");
                     imageurl = targetUri.toString();

                    CropImage.activity(targetUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);
                    break;
                }
            default:
                break;
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Uri resultUri = result.getUri();
                    File file=new File(resultUri.getPath());
                    Bitmap bitmap= new Compressor(signup_add_pic.this)
                            .setMaxHeight(128)
                            .setMaxWidth(128)
                            .setQuality(50)
                            .compressToBitmap(file);
                    public_uri = getImageUri(this,bitmap);
                    scanner_image.setImageURI(resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

}
