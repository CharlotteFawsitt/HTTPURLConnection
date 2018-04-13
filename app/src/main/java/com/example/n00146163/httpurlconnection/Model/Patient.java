package com.example.n00146163.httpurlconnection.Model;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.UUID;

/**
 * Created by n00146163 on 21/11/2017.
 */

//class for patient
public class Patient implements Parcelable {
    //variables for the class
    private String patientId;
    private String name;
    private String gender;
    private String phoneNumber;
    private String nextApp;
    private String photo;
    private Bitmap bitmap;

    //constructor that takes no parameters
    public Patient() {
    }

    //constructor that takes in parameters and assigns the variables of the constructor to the class
    public Patient(String pId, String n, String g, String pN, String nA, String photo) {

        if (pId == null) {
            pId = UUID.randomUUID().toString();
        }
        this.patientId = pId;
        this.name = n;
        this.gender = g;
        this.phoneNumber = pN;
        this.nextApp = nA;
        this.photo = photo;
    }

    //set and get methods for the class
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNextApp() {
        return nextApp;
    }

    public void setNextApp(String nextApp) {
        this.nextApp = nextApp;
    }


    //These functions ware a part of the parceable implementation.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.patientId);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.nextApp);
        dest.writeString(this.photo);
    }

    protected Patient(Parcel in) {
        this.patientId = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.phoneNumber = in.readString();
        this.nextApp = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        public Patient createFromParcel(Parcel source) {
            return new Patient(source);
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
