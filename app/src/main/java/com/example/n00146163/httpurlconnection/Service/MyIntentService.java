package com.example.n00146163.httpurlconnection.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.n00146163.httpurlconnection.HttpManagerImports;
import com.example.n00146163.httpurlconnection.IntentActivity;
import com.example.n00146163.httpurlconnection.Model.Patient;
import com.example.n00146163.httpurlconnection.Parsers.XMLParser;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by n00146163 on 11/04/2018.
 */

public class MyIntentService extends IntentService {
    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";


    String data;
    List<Patient> patientList = new ArrayList<>();
    Patient[] patientlist;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getData().toString();
        String imageUrl = intent.getStringExtra("imageUrl");
        String type = intent.getStringExtra("type");

        if (type.equals("XML")) {
            data = HttpManagerImports.getData(url);
            patientList = XMLParser.parseFeed(data);
        } else if (type.equals("JSON")) {
            data = HttpManagerImports.getData(url);
            Gson gson = new Gson();
            patientlist = gson.fromJson(data, Patient[].class);
            patientList = new ArrayList<Patient>(Arrays.asList(patientlist));
        }

        for (Patient p : patientList) {
            try {
                String patientImage = imageUrl + p.getPhoto();

                InputStream in = (InputStream) new URL(patientImage).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                p.setBitmap(bitmap);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putParcelableArrayListExtra(MY_SERVICE_PAYLOAD, (ArrayList<? extends Parcelable>) patientList);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
