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

        String emailRESERV = (String) getIntent().getSerializableExtra("email_reserv");

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
