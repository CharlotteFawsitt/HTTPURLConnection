package com.example.n00146163.httpurlconnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.n00146163.httpurlconnection.Model.Patient;
import com.example.n00146163.httpurlconnection.Parsers.XMLParser;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASyncTaskLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Patient>> {

    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/" ;
    String url, type = "";
    List<Patient> patientsList = new ArrayList<>();
    RecyclerView recyclerView;
    PatientAdapter adapter;
    public static final String XML_URL = "http://172.18.15.71/Patient.xml";
    public static final String JSON_URL = "http://172.18.15.71/Patient.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_loader);
    }

    public void JSONClickHandler(View view) {
        type = "JSON";
        url = JSON_URL;
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    public void XMLClickHandler(View view) {
        type = "XML";
        url = XML_URL;
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    public void onClearClicked(View view) {
        patientsList.clear();
        updateDisplay();

    }

    public void updateDisplay() {
        if (patientsList != null) {
            recyclerView = (RecyclerView) findViewById(R.id.rvItems);
            adapter = new PatientAdapter(this, patientsList);
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    public Loader<List<Patient>> onCreateLoader(int id, Bundle args) {
        return new MyTaskLoader(this, type, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Patient>> loader, List<Patient> patientData) {
        patientsList = patientData;
        updateDisplay();
    }

    @Override
    public void onLoaderReset(Loader<List<Patient>> loader) {

    }

    private static class MyTaskLoader extends AsyncTaskLoader<List<Patient>>{

        String type, url, xmlData, jsonData;
        List<Patient> patientList = new ArrayList<>();
        Patient[] patientlistJSON;
        public MyTaskLoader(Context context, String type, String url) {
            super(context);
            this.type = type;
            this.url = url;
        }

        @Override
        public List<Patient> loadInBackground() {
            if (type.equals("XML")) {
                xmlData = HttpManagerImports.getData(url);
                patientList = XMLParser.parseFeed(xmlData);
            } else if (type.equals("JSON")) {
                jsonData = HttpManagerImports.getData(url);
                Gson gson = new Gson();
                patientlistJSON = gson.fromJson(jsonData, Patient[].class);
                patientList = new ArrayList<Patient>(Arrays.asList(patientlistJSON));
            }

            for (Patient p : patientList) {
                try {
                    String patientImage = PHOTOS_BASE_URL + p.getPhoto();

                    InputStream in = (InputStream) new URL(patientImage).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    p.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return patientList;
        }
    }
}
