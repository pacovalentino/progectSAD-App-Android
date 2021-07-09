package com.example.restdemo.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.restdemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import api.GetLastReservationByPatientEmailApi;
import api.interfaces.ServerCallback;
import api.utils.VolleyErrorHandler;
import entity.Patient;
import entity.Reservation;

public class HomeFragment extends Fragment {

    TextView tV1, tV2,tV3,tV4,tV5,textView;
    LinearLayout linearLayout,linearLayoutinfo;
    Button b1,b2,b3,b4,b5,b6,b7;
    FloatingActionButton refreshButton;

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
        b5=root.findViewById(R.id.bMap);
        b6=root.findViewById(R.id.bCustomer);
        b7=root.findViewById(R.id.bWebAdmin);
        refreshButton = root.findViewById(R.id.refreshButton);
        linearLayout=root.findViewById(R.id.idlayout);
        linearLayoutinfo=root.findViewById(R.id.idlayoutbottoni);

        final Intent form_intent = getActivity().getIntent();

        Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("patient");
        String token = (String) getActivity().getIntent().getStringExtra("token");

        GetLastReservationByPatientEmailApi.call(getContext(), patient.getEmail(), token, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    final Reservation reservation = new Reservation(result.getJSONObject("reservation"));
                    form_intent.putExtra("reserved", "yes");
                    refreshButton.setVisibility(View.VISIBLE);
                    form_intent.putExtra("reservation", reservation);
                    tV1.setText(reservation.getStruttura());
                    tV2.setText(reservation.getData());
                    tV3.setText(reservation.getTime());
                    tV4.setText(reservation.getStock_vaccino() + " - " + reservation.getVaccino());
                    tV5.setText(Reservation.stateToLabel(reservation.getStato()));

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web+reservation.getStruttura()));
                            startActivity(browserIntent);
                        }
                    });
                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String web="https://www.google.it/maps/search/";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web+reservation.getStruttura()));
                            startActivity(browserIntent);
                        }
                    });
                } catch (JSONException e) {
                    form_intent.putExtra("reserved", "no");
                    refreshButton.setVisibility(View.INVISIBLE);
                    textView.setText("Make you Reservation");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                    params.height=0;
                    params.width=0;
                    linearLayout.setLayoutParams(params);
                    linearLayout.setVisibility(View.INVISIBLE);
                    linearLayoutinfo.setLayoutParams(params);
                    linearLayoutinfo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFail(VolleyError volleyError) {
                form_intent.putExtra("reserved", "no");
                refreshButton.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), VolleyErrorHandler.getToastMessage(volleyError), Toast.LENGTH_LONG).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email= new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:your.eamil@gmail.com"));
                email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                email.putExtra(Intent.EXTRA_TEXT, "My Email message");
                startActivity(email);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(browserIntent);
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("patient");
                String token = (String) getActivity().getIntent().getStringExtra("token");

                GetLastReservationByPatientEmailApi.call(getContext(), patient.getEmail(), token, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            final Reservation newReservation = new Reservation(result.getJSONObject("reservation"));

                            Reservation oldReservation = (Reservation) getActivity().getIntent().getSerializableExtra("reservation");
                            if (oldReservation != null) {
                                if (!oldReservation.getStato().equals(newReservation.getStato())) {
                                    Toast.makeText(getContext(), "Prenotazione aggiornata", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Nessun aggiornamento", Toast.LENGTH_LONG).show();
                                }
                            }

                            form_intent.putExtra("reserved", "yes");
                            refreshButton.setVisibility(View.VISIBLE);
                            form_intent.putExtra("reservation", newReservation);
                            tV1.setText(newReservation.getStruttura());
                            tV2.setText(newReservation.getData());
                            tV3.setText(newReservation.getTime());
                            tV4.setText(newReservation.getStock_vaccino() + " - " + newReservation.getVaccino());
                            tV5.setText(Reservation.stateToLabel(newReservation.getStato()));
                        } catch (JSONException e) {
                            form_intent.putExtra("reserved", "no");
                            refreshButton.setVisibility(View.INVISIBLE);
                            textView.setText("Make you Reservation");
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                            params.height=0;
                            params.width=0;
                            linearLayout.setLayoutParams(params);
                            linearLayout.setVisibility(View.INVISIBLE);
                            linearLayoutinfo.setLayoutParams(params);
                            linearLayoutinfo.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFail(VolleyError volleyError) {
                        form_intent.putExtra("reserved", "no");
                        refreshButton.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), VolleyErrorHandler.getToastMessage(volleyError), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return root;
    }
}
