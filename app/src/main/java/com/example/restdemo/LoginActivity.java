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
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import api.LoginApi;
import api.interfaces.ServerCallback;
import entity.Patient;
import entity.Reservation;
import api.utils.VolleyErrorHandler;

public class LoginActivity extends AppCompatActivity {

    private EditText etMail, etPassword;
    Button loginButton;
    TextView regText,textView;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialogBuilder;
    ImageView imageView;


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
            etMail.setText(patientRESERV.getEmail());
            loginButton.setText("Confirm");
            regText.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);

            /*
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

             */

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
                if (etMail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Controlla se ogni campo è completo", Toast.LENGTH_LONG).show();
                    return;
                }

                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                LoginApi.call(getApplicationContext(), email, password, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject loginResponse) {
                        try {
                            String token = loginResponse.getString("token");
                            JSONObject patientObject = new JSONObject(loginResponse.getString("patient"));
                            Patient patient = new Patient(patientObject);
                            Reservation reservation = loginResponse.isNull("reservation")
                                    ? null : new Reservation(new JSONObject(loginResponse.getString("reservation")));
                            form_intent.putExtra("patient", patient);
                            form_intent.putExtra("token", token);
                            form_intent.putExtra("reservation", reservation);
                            form_intent.putExtra("get_reservation_by_mail", reservation);
                            form_intent.putExtra("var", "ciao");

                            progressDialog = new ProgressDialog(LoginActivity.this, R.style.DialogTheme);
                            progressDialog.setMessage("Loading...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        startActivity(form_intent);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Errore nel decifrare la risposta dal server", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(VolleyError volleyError) {
                        StringBuilder error = VolleyErrorHandler.getToastMessage(volleyError);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        volleyError.printStackTrace();
                    }
                });
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
