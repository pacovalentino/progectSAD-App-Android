package com.example.restdemo.ui.slideshow;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restdemo.R;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import response.Patient;
import response.RegisterResponse;
import response.Structure;

public class ReservationFragment extends Fragment implements NumberPicker.OnValueChangeListener {

    TextView t1,t2,t3,t4,testStrutt;
    Button button,buttonStr,btReserve;
    @SuppressLint("StaticFieldLeak")
    static EditText editText,editTextStru;

    String URL="http://10.0.2.2:8000/api/reservation";


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        final Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("patient");

        testStrutt=root.findViewById(R.id.textViewIdStruttura);

        t1=root.findViewById(R.id.t1);
        t1.setText(patient.getFirst_name()+" "+patient.getLast_name());

        t2=root.findViewById(R.id.t2);
        t2.setText(patient.getEmail());

        t3=root.findViewById(R.id.t3);
        t3.setText(patient.getFiscal_code());

        t4=root.findViewById(R.id.t4);
        t4.setText(patient.getMobile_phone());

        //****************Codice


        editText=root.findViewById(R.id.editdata);
        editTextStru=root.findViewById(R.id.editStructure);
        button=root.findViewById(R.id.buttonData);
        btReserve=root.findViewById(R.id.buttonReserve);
        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextStru.getText().toString().equals("") || editText.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Controlla se ogni campo è completo", Toast.LENGTH_LONG).show();
                } else{
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.e("Variabile reservation ", s);
                            if (s.equals("{\"reservation\":\"error\"}")) {
                                Toast.makeText(getContext(), "Reservation exists\nEnter another date", Toast.LENGTH_LONG).show();
                            } else if (s.equals("{\"reservation\":\"success\"}")) {
                                Toast.makeText(getContext(), "Reservation Successful", Toast.LENGTH_LONG).show();

                            } else
                                Toast.makeText(getContext(), "Incorrect Details", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("patient_id", patient.getId());
                            parameters.put("date", editText.getText().toString());
                            parameters.put("structure_id",testStrutt.getText().toString());
                            return parameters;
                        }
                    };
                    RequestQueue rQueue = Volley.newRequestQueue(getContext());
                    rQueue.add(request);
                }
             }
        });
        buttonStr=root.findViewById(R.id.buttonStructure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });
        buttonStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                show();
            }
        });

        return root;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }


    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {



            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            populateSetDate(yy, mm+1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            Log.e("Data",year +"/"+month +"/"+day);



            editText.setText(year+"-"+month+"-"+day);



        }


    }


    public void show()
    {
        final Structure s1 = (Structure) getActivity().getIntent().getSerializableExtra("structure1");
        final Structure s2 = (Structure) getActivity().getIntent().getSerializableExtra("structure2");

        final String[] strut  = {s1.getNome(), s2.getNome()};
        final Dialog d = new Dialog(getContext());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        final Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(strut.length-1); // max value 100 //sono tre parole
        np.setMinValue(0);   // min value 0
        np.setDisplayedValues(strut);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                editTextStru.setText(strut[np.getValue()]); //set the value to textview
                if(editTextStru.getText().toString().equals(s1.getNome())){
                    testStrutt.setText(s1.getId());
                } else if (editTextStru.getText().toString().equals(s2.getNome())){
                    testStrutt.setText(s2.getId());
                }

                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

}

/*

StringRequest request = new StringRequest(Request.Method.POST, "http://10.0.2.2:8000/api/data", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.e("Variabile data ", s);
                    dataResponse = g.fromJson(s, DataResponse.class);
                    if (dataResponse.getData().equals("busy")) {
                        Toast.makeText(getContext(), "Data Occupata", Toast.LENGTH_LONG).show();
                    } else if (dataResponse.getData().equals("free")) {

                        Toast.makeText(getContext(), "Data Libera", Toast.LENGTH_LONG).show();


                    } else
                        Toast.makeText(getContext(), "Incorrect Details", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    return parameters;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(getContext());
            rQueue.add(request);

*/