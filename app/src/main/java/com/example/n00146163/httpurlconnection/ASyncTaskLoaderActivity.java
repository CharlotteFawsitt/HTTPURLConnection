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

    //Constants for the xml, json files and the photo location.
    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/";
    private static final String XML_URL = "http://172.18.15.71/Patient.xml";
    private static final String JSON_URL = "http://172.18.15.71/Patient.json";
    //String to be used to set the data type when a button is clicked, and another string for the url.
    String url, type = "";
    //Arraylist to be used to hold the patients.
    List<Patient> patientsList = new ArrayList<>();
    //Instance of the recyclerview and the adapter.
    RecyclerView recyclerView;
    PatientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_loader);
    }

    //Click handler for the JSON button, Sets the type to "JSON" and starts the loader.
    public void JSONClickHandler(View view) {
        type = "JSON";
        url = JSON_URL;
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    //Click handler for the XML button, Sets the type to "XML" and starts the loader.
    public void XMLClickHandler(View view) {
        type = "XML";
        url = XML_URL;
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    public void onClearClicked(View view) {
        patientsList.clear();
        updateDisplay();

    }

    //CLick handler for the clear button, clears the patientlist and updates the display.
    public void updateDisplay() {
        if (patientsList != null) {
            recyclerView = (RecyclerView) findViewById(R.id.rvItems);
            adapter = new PatientAdapter(this, patientsList);
            recyclerView.setAdapter(adapter);
        }
    }

    //These were implemented by the loadermanager
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

    //innerclass for the loader
    private static class MyTaskLoader extends AsyncTaskLoader<List<Patient>> {

        //strings for the type, url, xmldata and jsondata, these are needed as the class cannot use the variables
        //from the main body.
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
