package com.example.n00146163.httpurlconnection;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.n00146163.httpurlconnection.Service.MyIntentService;

import java.util.ArrayList;
import java.util.List;

import com.example.n00146163.httpurlconnection.Model.Patient;

public class IntentActivity extends AppCompatActivity {
    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/" ;
    private static final String XML_URL = "http://172.18.15.71/Patient.xml";
    private static final String JSON_URL = "http://172.18.15.71/Patient.json";
    String data;
    List<Patient> patientsList = new ArrayList<>();
    RecyclerView recyclerView;
    PatientAdapter adapter;
    private BroadcastReceiver mBroadcastRecievier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            patientsList = intent.getParcelableArrayListExtra(MyIntentService.MY_SERVICE_PAYLOAD);
            updateDisplay();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastRecievier,
                        new IntentFilter(MyIntentService.MY_SERVICE_MESSAGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastRecievier);
    }

    public void JSONClickHandler(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        intent.setData(Uri.parse(JSON_URL));
        intent.putExtra("imageUrl", PHOTOS_BASE_URL);
        intent.putExtra("type", "JSON");
        startService(intent);
    }

    public void XMLClickHandler(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        intent.setData(Uri.parse(XML_URL));
        intent.putExtra("imageUrl", PHOTOS_BASE_URL);
        intent.putExtra("type", "XML");
        startService(intent);
    }
    public void onClearClicked(View view) {
        patientsList.clear();
        updateDisplay();

    }
    public void updateDisplay() {
        if(patientsList != null) {
            recyclerView = (RecyclerView) findViewById(R.id.rvItems);
            adapter = new PatientAdapter(this, patientsList);
            recyclerView.setAdapter(adapter);
        }
    }

}
