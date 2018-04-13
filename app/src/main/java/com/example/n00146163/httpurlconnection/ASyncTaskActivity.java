package com.example.n00146163.httpurlconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.n00146163.httpurlconnection.Model.Patient;
import com.example.n00146163.httpurlconnection.Parsers.XMLParser;

public class ASyncTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
    }

    //Constants for the xml, json files and the photo location.
    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/";
    private static final String XML_URL = "http://172.18.15.71/Patient.xml";
    private static final String JSON_URL = "http://172.18.15.71/Patient.json";
    //Arraylist to be used to hold the patients.
    List<Patient> patientsList = new ArrayList<>();
    //Instance of the recyclerview and the adapter.
    RecyclerView recyclerView;
    PatientAdapter adapter;
    //String to be used to set the data type when a button is clicked.
    String type = "";

    //Click handler for the JSON button, Sets the type to "JSON" and launchs the thread pool executer.
    public void onJSONClicked(View view) {
        MyThread thread = new MyThread();
        type = "JSON";
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, JSON_URL);
    }

    //Click handler for the XML button, Sets the type to "XML" and launchs the thread pool executer.
    public void onXMLClicked(View view) {
        MyThread thread = new MyThread();
        type = "XML";
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, XML_URL);
    }

    //CLick handler for the clear button, clears the patientlist and updates the display.
    public void onClearClicked(View view) {
        patientsList.clear();
        updateDisplay();

    }

    //Fills the recyclerview with the patientlist.
    public void updateDisplay() {
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        adapter = new PatientAdapter(this, patientsList);
        recyclerView.setAdapter(adapter);
    }


    private class MyThread extends AsyncTask<String, String, List<Patient>> {

        @Override
        protected List<Patient> doInBackground(String... params) {

            //Checks to see what type is set to.
            if (type.equals("JSON")) {
                //gets the url of the file.
                String content = HttpManagerImports.getData(params[0]);
                Gson gson = new Gson();
                //Puts the values of the JSON file into an array.
                Patient[] patientlist = gson.fromJson(content, Patient[].class);
                //Fills the arraylist with objects from the Array.
                patientsList = new ArrayList<>(Arrays.asList(patientlist));
            } else if (type.equals("XML")) {
                String content = HttpManagerImports.getData(params[0]);
                //Calls the xml parser to parse the XML file to get the object.
                patientsList = XMLParser.parseFeed(content);
            }


            //Loop to get the image for each patient and assign the image to that patient.
            for (Patient p : patientsList) {
                try {
                    String imageUrl = PHOTOS_BASE_URL + p.getPhoto();

                    InputStream in = (InputStream) new URL(imageUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    p.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return patientsList;
        }

        @Override
        protected void onPreExecute() {
            updateDisplay();
        }

        @Override
        protected void onPostExecute(List<Patient> patientsList) {
            updateDisplay();

        }
    }

}
