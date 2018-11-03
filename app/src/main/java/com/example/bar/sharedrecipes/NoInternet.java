package com.example.bar.sharedrecipes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class NoInternet extends AppCompatActivity {
    private Button myRecipes;
    private ImageView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        myRecipes = (Button) findViewById(R.id.No_Internet_myRecipesBtn);
        retry = (ImageView) findViewById(R.id.No_Internet_retryBtn);
        myRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoInternet.this,MyRecipes.class);
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline() == true){
                    Intent intent = new Intent(NoInternet.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NoInternet.this, "You are not connected to the Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this,MyRecipes.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
