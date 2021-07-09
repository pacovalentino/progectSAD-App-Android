package com.example.restdemo.ui.slideshow;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restdemo.LoginActivity;
import com.example.restdemo.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import api.MakeReservationApi;
import entity.Patient;
import api.interfaces.ServerCallback;
import api.utils.VolleyErrorHandler;

public class ReservationFragment extends Fragment implements NumberPicker.OnValueChangeListener {

    TextView testStrutt;
    Button button,buttonStr,btReserve,btRegion;
    @SuppressLint("StaticFieldLeak")
    static EditText editText,editRegion,editStructure;
    ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        final Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("patient");
        final String tok=(String) getActivity().getIntent().getSerializableExtra("token");

        testStrutt=root.findViewById(R.id.textViewIdStruttura);
        editStructure=root.findViewById(R.id.editStructure);
        editRegion=root.findViewById(R.id.editRegion);
        buttonStr=root.findViewById(R.id.buttonStructure);
        btRegion=root.findViewById(R.id.buttonRegion);


        //****************Codice

        editText=root.findViewById(R.id.editdata);
        button=root.findViewById(R.id.buttonData);
        btReserve=root.findViewById(R.id.buttonReserve);

        String a = (String) getActivity().getIntent().getSerializableExtra("var");
        if(a.equals("addio")) {
            //Se è presente la reservation
            editText.setFocusable(false);
            editText.setEnabled(false);
            editRegion.setFocusable(false);
            editRegion.setEnabled(false);
            editStructure.setFocusable(false);
            editStructure.setEnabled(false);
            button.setEnabled(false);
            buttonStr.setEnabled(false);
            btRegion.setEnabled(false);
            btReserve.setEnabled(false);

        } else if(a.equals("ciao")){
            btReserve.setEnabled(true);
        }

        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editStructure.getText().toString().equals("") || editText.getText().toString().equals("") || editRegion.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Controlla se ogni campo è completo", Toast.LENGTH_LONG).show();
                    return;
                }

                MakeReservationApi.call(getContext(), patient, editText.getText().toString(), testStrutt.getText().toString(), tok, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        final Intent form_intent = new Intent(getContext(), LoginActivity.class);

                            progressDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
                            progressDialog.setMessage("Loading..."); // Setting Message
                            //progressDialog.setTitle("ProgressDialog"); // Setting Title
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        form_intent.putExtra("email_reserv", patient.getEmail());
                                        startActivity(form_intent);
                                        getActivity().finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                    }

                    @Override
                    public void onFail(VolleyError volleyError) {
                        StringBuilder toastErrors = VolleyErrorHandler.getToastMessage(volleyError);
                        Toast.makeText(getContext(), toastErrors, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

        btRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegion();
            }
        });
        buttonStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editRegion.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Compilare prima campo Region", Toast.LENGTH_LONG).show();
                }else {
                    show();
                }

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

    public void show(){

        Editable urlComplete=editRegion.getText();
        final String tok=(String) getActivity().getIntent().getSerializableExtra("token");

        StringRequest request = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/get-structures-by-region/"+urlComplete, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    Log.e("Variabile structure ",s);
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray =jsonObject.getJSONArray("structures");

                    final String[] id_strutture = new String [jsonArray.length()];
                    final String[] nome_strutture = new String [jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++){
                        nome_strutture[i]=jsonArray.getJSONObject(i).getString("name");
                        id_strutture[i]=jsonArray.getJSONObject(i).getString("id");
                        Log.e("Struttur1 debug",nome_strutture[i]+" "+id_strutture[i]);

                    }
                    show1(id_strutture,nome_strutture);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Integer code = volleyError.networkResponse.statusCode;
                if (code ==401){
                    Toast.makeText(getContext(), "Token non valido", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext() , LoginActivity.class));
                    getActivity().finish();
                }
                Toast.makeText(getContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", "Bearer "+tok);
                params.put("Accept", "application/json");
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }


    public void show1(final String[] id_stru, final String[] struct)
    {

        final Dialog d = new Dialog(getContext());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        final Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(struct.length-1); // max value 100 //sono tre parole
        np.setMinValue(0);   // min value 0
        np.setDisplayedValues(struct);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                editStructure.setText(struct[np.getValue()]); //set the value to textview
                testStrutt.setText(id_stru[np.getValue()]);
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

    public void showRegion()
    {
        final String[] strut  = {
                "Abruzzo", "Sicilia", "Piemonte", "Marche", "Valle d'Aosta", "Toscana", "Campania",
                "Puglia", "Veneto", "Lombardia", "Emilia-Romagna", "Trentino-Alto Adige", "Sardegna", "Molise", "Calabria",
                "Lazio", "Liguria", "Friuli-Venezia Giulia", "Basilicata", "Umbria"
        };
        final Dialog d = new Dialog(getContext());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        final Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(strut.length-1);
        np.setMinValue(0);   // min value 0
        np.setDisplayedValues(strut);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                editRegion.setText(strut[np.getValue()]); //set the value to textview
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