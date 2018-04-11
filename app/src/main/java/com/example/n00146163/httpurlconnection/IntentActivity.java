package com.example.n00146163.httpurlconnection;

import android.content.Intent;
import android.net.Uri;
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
    TextView output;
    String data;
    List<Patient> patientsList = new ArrayList<>();
    Patient[] patientlistJSON;
    RecyclerView recyclerView;
    PatientAdapter adapter;
    public static final String XML_URL = "http://172.18.15.71/Patient.xml";

    public static final String JSON_URL = "http://172.18.15.71/Patient.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

    }

    public void JSONClickHandler(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        intent.setData(Uri.parse(JSON_URL));
        startService(intent);
    }

}
