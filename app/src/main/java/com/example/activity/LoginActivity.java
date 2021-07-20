package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

        final String tokenRESERV = (String) getIntent().getSerializableExtra("token");
        final Patient patientRESERV = (Patient) getIntent().getSerializableExtra("patient");

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
            if (code.equals("401") || code.equals("403")) {
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
                            form_intent.putExtra("patient", patient);
                            form_intent.putExtra("token", token);

                            progressDialog = new ProgressDialog(LoginActivity.this, R.style.DialogTheme);
                            progressDialog.setMessage("Caricamento...");
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
        alertDialogBuilder.setTitle("Conferma l'uscita!!!").setMessage("Sei sicuro?");

        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"Hai cliccato NO",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancella", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Hai cliccato Cancella",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
