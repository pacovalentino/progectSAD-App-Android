package com.example.restdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import response.LoginResponse;
import response.Patient;
import response.Reservation;

public class LoginActivity extends AppCompatActivity {

    private EditText etMail, etPassword;
    Button loginButton;
    String URL="http://10.0.2.2:8000/api/login";
    private LoginResponse loginResponse;
    Gson g = new Gson();
    TextView regText,textView;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialogBuilder;
    ImageView imageView;
    String Token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String tokenRESERV = (String) getIntent().getSerializableExtra("token_reserv");
        final Patient patientRESERV = (Patient) getIntent().getSerializableExtra("patient_reserv");

        loginButton = findViewById(R.id.button);
        etMail = findViewById(R.id.emailId);
        etPassword = findViewById(R.id.passId);
        regText=findViewById(R.id.notaccountId);
        imageView=findViewById(R.id.logoid);

        textView=findViewById(R.id.textViewid);
        textView.setVisibility(View.INVISIBLE);

        final Intent form_intent = new Intent(LoginActivity.this,HomeActivity.class);

        String code = getIntent().getStringExtra("code");
        Log.e("CODE:", code != null ? code : "codice non presente");
        if (code != null) {
            if (code.equals("401")) {
                Toast.makeText(
                        getApplicationContext(),
                        "Token scaduto, effettuare nuovamente il login",
                        Toast.LENGTH_LONG
                ).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Si è verificato un errore dal server",
                        Toast.LENGTH_LONG
                ).show();
            }
        }

        if(patientRESERV!=null){

            Log.e("debug email:", patientRESERV.getEmail());
            etMail.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            regText.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);

            StringRequest request1 = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/get-login/"+patientRESERV.getEmail(), new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    try {
                        Log.e("login RESERVE:", s);
                        JSONObject jsonObject = new JSONObject(s);
                        loginResponse = g.fromJson(s, LoginResponse.class);
                        if(s.equals("{\"login\":\"success\",\"code\":200}")){

                            progressDialog = new ProgressDialog(LoginActivity.this, R.style.DialogTheme);
                            progressDialog.setMessage("Loading..."); // Setting Message
                            //progressDialog.setTitle("ProgressDialog"); // Setting Title
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                        Intent form_intent1 = new Intent(LoginActivity.this,HomeActivity.class);
                                        form_intent1.putExtra("patient",patientRESERV);
                                        form_intent1.putExtra("token",tokenRESERV);
                                        Log.e("debug yoyo:", patientRESERV.getEmail());
                                        if (tokenRESERV != null) {
                                            Log.e("debug bla bla:", tokenRESERV);
                                        }
                                        startActivity(form_intent1);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                        }else {
                            Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Integer code = volleyError.networkResponse.statusCode;
                    if (code ==401){
                        Toast.makeText(getApplicationContext(), "Token non valido", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    //parameters.put("email", etMail.getText().toString());
                    return parameters;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("authorization", "Bearer "+tokenRESERV);
                    params.put("Accept", "application/json");
                    return params;
                }
            };
            RequestQueue rQueue1 = Volley.newRequestQueue(getApplicationContext());
            rQueue1.add(request1);


        }



        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle= new Bundle();
                bundle.putString("email",etMail.getText().toString());
                bundle.putString("password",etPassword.getText().toString());
                Intent form_intent = new Intent(getApplicationContext(), RegisterActivity.class);
                form_intent.putExtras(bundle);
                startActivity(form_intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMail.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Controlla se ogni campo è completo", Toast.LENGTH_LONG).show();
                }else{
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Log.e("Variabile login + token",s);

                            loginResponse = g.fromJson(s, LoginResponse.class);


                            if(loginResponse.getLogin().equals("success")){
                                //Token
                                Token = loginResponse.getToken();

                                Log.e("Token",Token);

                                StringRequest request = new StringRequest(Request.Method.POST, "http://10.0.2.2:8000/api/patient", new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            Log.e("Variabile login ", s);
                                            JSONObject jsonObject = new JSONObject(s);
                                            final Patient patient = new Patient(jsonObject.getString("email"), null, jsonObject.getString("first_name"),
                                                    jsonObject.getString("last_name"), jsonObject.getString("date_of_birth"),
                                                    jsonObject.getString("gender"), jsonObject.getString("fiscal_code"),
                                                    jsonObject.getString("city"), jsonObject.getString("cap"),
                                                    jsonObject.getString("mobile_phone"), jsonObject.getString("heart_disease"),
                                                    jsonObject.getString("allergy"), jsonObject.getString("immunosuppression"),
                                                    jsonObject.getString("anticoagulants"), jsonObject.getString("covid"), jsonObject.getString("id"));

                                            Log.e("login response:", jsonObject.getString("email"));
                                            StringRequest request1 = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/get-reservations-by-email/"+jsonObject.getString("email"), new Response.Listener<String>(){
                                                @Override
                                                public void onResponse(String s) {
                                                    try {
                                                        Log.e("login response:", s);

                                                        String variabile="ciao";
                                                        JSONObject jsonObject = new JSONObject(s);
                                                        Log.e("var null",variabile);
                                                        //Log.e("var null",);
                                                        if (jsonObject.isNull("reservation")) {
                                                            Log.e("var null","sono dentro");
                                                            form_intent.putExtra("var", variabile);
                                                        }else {
                                                            Log.e("json object:", jsonObject.toString());
                                                            JSONObject jsonObject2 = jsonObject.getJSONObject("reservation");
                                                            Log.e("json object2:", jsonObject2.toString());

                                                            final Reservation reservation = new Reservation(
                                                                    jsonObject2.getString("structure_name"),
                                                                    jsonObject2.getString("date"),
                                                                    jsonObject2.getString("time"),
                                                                    jsonObject2.getString("batch_id"),
                                                                    jsonObject2.getString("name"),
                                                                    jsonObject2.getString("state"),
                                                                    jsonObject2.getString("phone_number")
                                                            );
                                                            Log.e("reservation stru:", reservation.getStruttura());

                                                            form_intent.putExtra("get_reservation_by_mail", reservation);
                                                            variabile="addio";
                                                            form_intent.putExtra("var",variabile);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },new Response.ErrorListener(){
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    Integer code = volleyError.networkResponse.statusCode;
                                                    if (code ==401){
                                                        Toast.makeText(getApplicationContext(), "Token non valido", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                                                        finish();
                                                    }
                                                    Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                                                }
                                            }){

                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> parameters = new HashMap<>();
                                                    //parameters.put("email", etMail.getText().toString());
                                                    return parameters;
                                                }
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    Map<String, String>  params = new HashMap<String, String>();
                                                    params.put("authorization", "Bearer "+Token);
                                                    params.put("Accept", "application/json");
                                                    return params;
                                                }
                                            };
                                            RequestQueue rQueue1 = Volley.newRequestQueue(getApplicationContext());
                                            rQueue1.add(request1);



                                            progressDialog = new ProgressDialog(LoginActivity.this, R.style.DialogTheme);
                                            progressDialog.setMessage("Loading..."); // Setting Message
                                            //progressDialog.setTitle("ProgressDialog"); // Setting Title
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                            progressDialog.show(); // Display Progress Dialog
                                            progressDialog.setCancelable(false);
                                            new Thread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        Thread.sleep(2800);
                                                        //Intent form_intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                        form_intent.putExtra("patient",patient);
                                                        form_intent.putExtra("token",Token);
                                                        startActivity(form_intent);
                                                        finish();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    progressDialog.dismiss();
                                                }
                                            }).start();



                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Integer code = volleyError.networkResponse.statusCode;
                                        if (code ==401){
                                            Toast.makeText(getApplicationContext(), "Token non valido", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                                            finish();
                                        }
                                        Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                                    }
                                }) {

                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("email", etMail.getText().toString());

                                        return parameters;
                                    }

                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("authorization", "Bearer "+Token);
                                        params.put("Accept", "application/json");
                                        return params;
                                    }
                                };
                                RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                                rQueue.add(request);

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_LONG).show();

                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Integer code = volleyError.networkResponse.statusCode;
                            if (code ==401){
                                Toast.makeText(getApplicationContext(), "Token non valido", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                                finish();
                            }
                            Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("email", etMail.getText().toString());
                            parameters.put("password", etPassword.getText().toString());

                            return parameters;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("authorization", "Bearer "+Token);
                            params.put("Accept", "application/json");
                            return params;
                        }
                    };
                    RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                    rQueue.add(request);
                }
            }
        });
}


    @Override
    public void onBackPressed() {
        //Toast.makeText(getApplicationContext(), "Gestione chiusura app\nDa implementare", Toast.LENGTH_LONG).show();

        alertDialogBuilder = new AlertDialog.Builder(this,R.style.DialogThemeExit);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Exit..!!!").setMessage("Are you sure,You want to exit?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
