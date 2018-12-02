package com.example.bar.sharedrecipes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BiggerPictureActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView iv;
    private String downloadUrl;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigger_picture);
        iv = (ImageView) findViewById(R.id.Activity_Big_Picture_img);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sharedrecipes-65510.appspot.com");
        intent = getIntent();
        downloadUrl = intent.getStringExtra("downloadPic");
        setImagefromFirebase();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    private void setImagefromFirebase(){
        if(isOnline()) {
            storageReference.child(downloadUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(iv);
                }
            });
        }else{
            iv.setImageResource(R.drawable.icons8wifioff100);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);

    }
}
