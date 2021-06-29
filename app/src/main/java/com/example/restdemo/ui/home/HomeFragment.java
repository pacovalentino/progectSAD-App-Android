package com.example.restdemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restdemo.R;

import response.Patient;
import response.Reservation;

public class HomeFragment extends Fragment {

    TextView tV1, tV2,tV3,tV4,tV5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

    /*    Reservation reservation = (Reservation) getActivity().getIntent().getSerializableExtra("get_reservation_by_mail");
        tV1=root.findViewById(R.id.t1);
        tV1.setText(reservation.getStruttura());
        tV2=root.findViewById(R.id.t2);
        tV2.setText(reservation.getData());
        tV3=root.findViewById(R.id.t3);
        tV3.setText(reservation.getTime());
        tV4=root.findViewById(R.id.t4);
        tV4.setText(reservation.getStock_vaccino()+" "+reservation.getVaccino());
        tV5=root.findViewById(R.id.t5);
        tV5.setText(reservation.getStato());
*/




        return root;
    }

}