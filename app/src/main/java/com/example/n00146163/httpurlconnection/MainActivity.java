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

import Model.Patient;
import Parsers.XMLParser;

public class MainActivity extends AppCompatActivity {
    private static final String PHOTOS_BASE_URL = "http://172.18.15.71/patientPhotos/" ;
    TextView output;
    String data;
    List<Patient> patientsList = new ArrayList<>();
    RecyclerView recyclerView;
    PatientAdapter adapter;
    public static final String XML_URL = "http://172.18.15.71/Patient.xml";

    public static final String JSON_URL = "http://172.18.15.71/Patient.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onJSONClicked(View view) {
        JSONThread thread = new JSONThread();
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, JSON_URL);
    }

    public void onXMLClicked(View view) {
        XMLThread thread = new XMLThread();
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, XML_URL);
    }

    public void onClearClicked(View view) {
        patientsList.clear();
    }

    public void updateDisplay(Patient[] patientlist) {
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        adapter = new PatientAdapter(this, Arrays.asList(patientlist));
        recyclerView.setAdapter(adapter);
    }

    public void updateDisplay2() {
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        adapter = new PatientAdapter(this, patientsList);
        recyclerView.setAdapter(adapter);
    }


    private class JSONThread extends AsyncTask<Object, Object, Patient[]> {

        @Override
        protected Patient[] doInBackground(Object... params) {

            String content = HttpManagerImports.getData((String) params[0]);

            Gson gson = new Gson();
            Patient[] patientlist = gson.fromJson(content, Patient[].class);

            for (Patient p : patientlist) {
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

            return patientlist;
        }

        @Override
        protected void onPreExecute() {

//            updateDisplay();
        }

        @Override
        protected void onPostExecute(Patient[] patientlist) {
            updateDisplay(patientlist);

        }
    }

    private class XMLThread extends AsyncTask<String, String, List<Patient>> {

        @Override
        protected List<Patient> doInBackground(String... params) {

            String content = HttpManagerImports.getData(params[0]);

            patientsList = XMLParser.parseFeed(content);

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
            updateDisplay2();
        }

        @Override
        protected void onPostExecute(List<Patient> patientsList) {
            updateDisplay2();

        }
    }
}
