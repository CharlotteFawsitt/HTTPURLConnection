package com.example.n00146163.httpurlconnection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onASyncClicked(View view) {
        Intent intent= new Intent(this, ASyncTaskActivity.class);
        startActivity(intent);
    }

    public void onIntentClicked(View view) {
        Intent intent= new Intent(this, IntentActivity.class);
        startActivity(intent);
    }
    public void onASyncTaskLoaderClicked(View view) {
        Intent intent= new Intent(this, ASyncTaskLoaderActivity.class);
        startActivity(intent);
    }

}
