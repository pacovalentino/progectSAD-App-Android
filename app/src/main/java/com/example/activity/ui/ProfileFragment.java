package com.example.activity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.activity.R;
import entity.Patient;


public class ProfileFragment extends Fragment {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("patient");

        t1=root.findViewById(R.id.t1);
        assert patient != null;
        t1.setText(patient.getFirst_name());

        t2=root.findViewById(R.id.t2);
        t2.setText(patient.getLast_name());

        t3=root.findViewById(R.id.t3);
        t3.setText(patient.getEmail());

        t4=root.findViewById(R.id.t4);
        t4.setText(patient.getDate_of_birth());

        t5=root.findViewById(R.id.t5);
        if(patient.getGender().equals("0")){
            t5.setText("Maschio");
        } else t5.setText("Femmina");

        t6=root.findViewById(R.id.t6);
        t6.setText(patient.getFiscal_code());

        t7=root.findViewById(R.id.t7);
        t7.setText(patient.getCity());

        t8=root.findViewById(R.id.t8);
        t8.setText(patient.getCap());

        t9=root.findViewById(R.id.t9);
        t9.setText(patient.getMobile_phone());

        t10=root.findViewById(R.id.t10);
        if(patient.getHeart_disease().equals("false")){
            t10.setText("Negativo");
        } else t10.setText("Positivo");

        t11=root.findViewById(R.id.t11);
        if(patient.getAllergy().equals("false")){
            t11.setText("Negativo");
        } else t11.setText("Positivo");

        t12=root.findViewById(R.id.t12);
        if(patient.getImmunosuppression().equals("false")){
            t12.setText("Negativo");
        } else t12.setText("Positivo");

        t13=root.findViewById(R.id.t13);
        if(patient.getAnticoagulants().equals("false")){
            t13.setText("Negativo");
        } else t13.setText("Positivo");

        t14=root.findViewById(R.id.t14);
        if(patient.getCovid().equals("false")){
            t14.setText("Negativo");
        } else t14.setText("Positivo");

        return root;
    }

}