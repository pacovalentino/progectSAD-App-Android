package com.example.restdemo.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restdemo.R;

import response.Patient;
import response.Reservation;

public class HomeFragment extends Fragment {

    TextView tV1, tV2,tV3,tV4,tV5,textView;
    LinearLayout linearLayout,linearLayoutinfo;
    Button b1,b2,b3,b4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        tV1 = root.findViewById(R.id.t1);
        textView=root.findViewById(R.id.myreservationID);
        tV2 = root.findViewById(R.id.t2);
        tV3 = root.findViewById(R.id.t3);
        tV4 = root.findViewById(R.id.t4);
        tV5 = root.findViewById(R.id.t5);
        b1=root.findViewById(R.id.bEmail);
        b2=root.findViewById(R.id.bCall);
        b3=root.findViewById(R.id.bWeb);
        b4=root.findViewById(R.id.bFacebook);
        linearLayout=root.findViewById(R.id.idlayout);
        linearLayoutinfo=root.findViewById(R.id.idlayoutbottoni);

        String a = (String) getActivity().getIntent().getSerializableExtra("var");
        final Reservation reservation = (Reservation) getActivity().getIntent().getSerializableExtra("get_reservation_by_mail");

        Log.e("variabile a",a);
        if(a.equals("addio")) {


            tV1.setText(reservation.getStruttura());
            tV2.setText(reservation.getData());
            tV3.setText(reservation.getTime());
            tV4.setText(reservation.getStock_vaccino() + " - " + reservation.getVaccino());
            tV5.setText(reservation.getStato());
            Log.e("if","if");

        } else if(a.equals("ciao")){
            Log.e("else","else");
            textView.setText("Make you Reservation");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height=0;
            params.width=0;
            linearLayout.setLayoutParams(params);
            linearLayout.setVisibility(View.INVISIBLE);
            linearLayoutinfo.setLayoutParams(params);
            linearLayoutinfo.setVisibility(View.INVISIBLE);

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Bottone Email", Toast.LENGTH_LONG).show();

                Intent email= new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:your.eamil@gmail.com"));
                email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                email.putExtra(Intent.EXTRA_TEXT, "My Email message");
                startActivity(email);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Bottone Call", Toast.LENGTH_LONG).show();

                String tel="tel:"+reservation.getTel();
                if(tel.length()<5){
                    tel="tel:0";
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(tel));
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String web="https://www.google.it/search?q=";
                //Toast.makeText(getContext(), "Bottone Web", Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web+reservation.getStruttura()));
                startActivity(browserIntent);
            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Bottone Facebook", Toast.LENGTH_LONG).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(browserIntent);
            }
        });





        return root;
    }

}