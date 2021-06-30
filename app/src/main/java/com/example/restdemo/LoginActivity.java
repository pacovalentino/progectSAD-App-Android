package com.example.restdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restdemo.ui.gallery.GalleryFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import response.LoginResponse;
import response.Patient;
import response.Reservation;
import response.Structure;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String emailRESERV = (String) getIntent().getSerializableExtra("email_reserv");

        loginButton = findViewById(R.id.button);
        etMail = findViewById(R.id.emailId);
        etPassword = findViewById(R.id.passId);
        regText=findViewById(R.id.notaccountId);
        imageView=findViewById(R.id.logoid);

        textView=findViewById(R.id.textViewid);
        textView.setVisibility(View.INVISIBLE);

        final Intent form_intent = new Intent(LoginActivity.this,HomeActivity.class);

        if(emailRESERV!=null){

            etMail.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            regText.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);

            etMail.setText(emailRESERV);
            etPassword.setText("test"); //la pass non me la passo quindi la metto a mano dato che è sempre uguale

            //Autoclick
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginButton.performClick();
                }
            }, 0);

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
                            Log.e("Variabile login ",s);

                            loginResponse = g.fromJson(s, LoginResponse.class);

                            if(loginResponse.getLogin().equals("success")){
                                //Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                //Finestra di dialogo

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
                                                    Toast.makeText(getApplicationContext(), "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                                                }
                                            });
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
