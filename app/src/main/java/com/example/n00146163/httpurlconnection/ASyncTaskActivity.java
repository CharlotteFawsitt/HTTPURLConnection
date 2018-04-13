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

    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/" ;
    List<Patient> patientsList = new ArrayList<>();
    RecyclerView recyclerView;
    PatientAdapter adapter;
    String type = "";
    private static final String XML_URL = "http://172.18.15.71/Patient.xml";
    private static final String JSON_URL = "http://172.18.15.71/Patient.json";

    public void onJSONClicked(View view) {
        MyThread thread = new MyThread();
        type = "JSON";
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, JSON_URL);
    }

    public void onXMLClicked(View view) {
        MyThread thread = new MyThread();
        type = "XML";
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, XML_URL);
    }

    public void onClearClicked(View view) {
        patientsList.clear();
        updateDisplay();

    }

    public void updateDisplay() {
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        adapter = new PatientAdapter(this, patientsList);
        recyclerView.setAdapter(adapter);
    }


    private class MyThread extends AsyncTask<String, String, List<Patient>> {

        @Override
        protected List<Patient> doInBackground(String... params) {

            if (type.equals("JSON")) {
                String content = HttpManagerImports.getData(params[0]);
                Gson gson = new Gson();
                Patient[] patientlist = gson.fromJson(content, Patient[].class);
                patientsList = new ArrayList<>(Arrays.asList(patientlist));
            } else if (type.equals("XML")) {
                String content = HttpManagerImports.getData(params[0]);
                patientsList = XMLParser.parseFeed(content);
            }


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
