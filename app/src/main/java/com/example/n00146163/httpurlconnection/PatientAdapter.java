package com.example.n00146163.httpurlconnection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import com.example.n00146163.httpurlconnection.Model.Patient;

/**
 * Created by n00146163 on 21/11/2017.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private List<Patient> mPatients;
    private Context mContext;

    public PatientAdapter(Context context, List<Patient> patients) {
        this.mContext = context;
        this.mPatients = patients;
    }


    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }
    public void onBindViewHolder(PatientAdapter.ViewHolder holder, int position) {

        final Patient patient = mPatients.get(position);


        holder.tvName.setText(patient.getName());
        String imageFile = patient.getPhoto();
        holder.imageView.setImageBitmap(patient.getBitmap());

//// if the user clicks this ViewHolder start a new intent to open the detail activity for the object that was selected.
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // String patientId = patient.getPatientId();
//                Intent intent = new Intent(mContext, MainActivity.class);
//                mContext.startActivity(intent);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itemNameText);
            imageView = itemView.findViewById(R.id.tvPatientImage);
            mView = itemView;
        }
    }
}

