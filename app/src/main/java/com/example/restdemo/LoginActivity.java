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

import java.util.HashMap;
import java.util.Map;

import response.LoginResponse;
import response.Patient;
import response.Structure;

public class LoginActivity extends AppCompatActivity {

    private EditText etMail, etPassword;
    Button loginButton;
    String URL="http://10.0.2.2:8000/api/login";
    private LoginResponse loginResponse;
    Gson g = new Gson();
    TextView regText;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialogBuilder;
    Structure structure1,structure2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.button);
        etMail = findViewById(R.id.emailId);
        etPassword = findViewById(R.id.passId);
        regText=findViewById(R.id.notaccountId);

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
                    Toast.makeText(getApplicationContext(), "Controlla se ogni campo ?? completo", Toast.LENGTH_LONG).show();
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
                                            Log.e("Variabile login ",s);
                                            JSONObject jsonObject=new JSONObject(s);
                                            JSONArray jsonArrayAccount=jsonObject.getJSONArray("contenutiAccount");
                                            JSONArray jsonArrayUser=jsonObject.getJSONArray("contenutiUser");
                                            JSONArray jsonArrayPatient=jsonObject.getJSONArray("contenutiPatient");
                                            JSONObject objAccount = jsonArrayAccount.getJSONObject(0);
                                            JSONObject objPatient = jsonArrayPatient.getJSONObject(0);
                                            JSONObject objUser =jsonArrayUser.getJSONObject(0);
                                            final Patient patient=new Patient(objUser.getString("email"),null,objAccount.getString("first_name"),
                                                                        objAccount.getString("last_name"),objAccount.getString("date_of_birth"),
                                                                        objAccount.getString("gender"),objAccount.getString("fiscal_code"),
                                                                        objAccount.getString("city"),objAccount.getString("cap"),
                                                                        objAccount.getString("mobile_phone"),objPatient.getString("heart_disease"),
                                                                        objPatient.getString("allergy"),objPatient.getString("immunosuppression"),
                                                                        objPatient.getString("anticoagulants"),objPatient.getString("covid"),objUser.getString("id"));


                                            //Per le strutture
                                            StringRequest request = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/structure", new Response.Listener<String>(){
                                                @Override
                                                public void onResponse(String s) {
                                                    try {
                                                        Log.e("Variabile structure ",s);
                                                        JSONObject jsonObject=new JSONObject(s);
                                                        JSONArray jsonArray =jsonObject.getJSONArray("strutture");
                                                        JSONObject obj1 = jsonArray.getJSONObject(0);
                                                        JSONObject obj2 = jsonArray.getJSONObject(1);
                                                        structure1=new Structure(obj1.getString("id"),obj1.getString("name"));
                                                        structure2=new Structure(obj2.getString("id"),obj2.getString("name"));
                                                        Log.e("Struttur1 debug",structure1.getId()+" "+structure1.getNome());
                                                        //Log.e("Struttur2 debug",structure2.getId()+" "+structure2.getNome());


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
                                            RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                                            rQueue.add(request);
                                            //Log.e("Speriamo",vet[0]+" "+vet[1]);






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
                                                        Intent form_intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                        form_intent.putExtra("patient",patient);
                                                        form_intent.putExtra("structure1",structure1);
                                                        form_intent.putExtra("structure2",structure2);
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
