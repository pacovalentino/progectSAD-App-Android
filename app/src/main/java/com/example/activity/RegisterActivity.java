package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import api.RegisterApi;
import api.payload.RegisterPayload;
import api.interfaces.ServerCallback;
import api.utils.VolleyErrorHandler;

public class RegisterActivity extends AppCompatActivity {
    TextView regText;
    Button registerButton;
    private EditText etMail, etPass, etNome, etCogn, etData, etCF, etCit, etCap, etCall; //etAddr
    private RadioGroup radioGroup;
    private RadioButton male, female;
    private CheckBox hearthDisease,allergy,immunodepression,anticoagulants,covid;
    AlertDialog.Builder alertDialogBuilder;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regText = findViewById(R.id.hogiaaccountId);
        registerButton = findViewById(R.id.buttonregisterid);
        etMail=(EditText) findViewById(R.id.mailId);
        etPass=(EditText) findViewById(R.id.regpassId);
        etNome=(EditText) findViewById(R.id.nameId);
        etCogn=(EditText) findViewById(R.id.cognId);
        etData=(EditText) findViewById(R.id.dateId);

        etData.setInputType(InputType.TYPE_NULL);
        etData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etData.setText(year  + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        etCF=(EditText) findViewById(R.id.cfId);
        etCit=(EditText) findViewById(R.id.cityId);
        //etAddr=(EditText) findViewById(R.id.addrId);
        etCap=(EditText) findViewById(R.id.capId);
        etCall=(EditText) findViewById(R.id.phoneId);
        radioGroup=findViewById(R.id.radiogroupId);
        male=(RadioButton)findViewById(R.id.maleId);
        female=(RadioButton)findViewById(R.id.femaleId);
        hearthDisease=(CheckBox)findViewById(R.id.checkBox1);
        allergy=(CheckBox)findViewById(R.id.checkBox2);
        immunodepression=(CheckBox)findViewById(R.id.checkBox3);
        anticoagulants=(CheckBox)findViewById(R.id.checkBox4);
        covid=(CheckBox)findViewById(R.id.checkBox5);

        Bundle bundle= this.getIntent().getExtras();
        etMail.setText(bundle.getString("email"));
        etPass.setText(bundle.getString("password"));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterPayload registerPayload = new RegisterPayload(
                        etMail.getText().toString(),
                        etPass.getText().toString(),
                        etNome.getText().toString(),
                        etCogn.getText().toString(),
                        etData.getText().toString(),
                        etCF.getText().toString(),
                        etCit.getText().toString(),
                        etCap.getText().toString(),
                        etCall.getText().toString(),
                        male.isChecked(),
                        hearthDisease.isChecked(),
                        allergy.isChecked(),
                        immunodepression.isChecked(),
                        anticoagulants.isChecked(),
                        covid.isChecked()
                );

                RegisterApi.call(getApplicationContext(), registerPayload, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject registerResponse) {
                        try {
                            if (registerResponse.getString("code").equals("200")) {
                                Toast.makeText(getApplicationContext(), "Registation success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                                
                            } else {
                                Toast.makeText(getApplicationContext(), "Errore durante la registrazione", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Errore durante la registrazione 1" + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(VolleyError volleyError) {
                        StringBuilder toastError = VolleyErrorHandler.getToastMessage(volleyError);
                        Toast.makeText(getApplicationContext(), toastError, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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
                Toast.makeText(RegisterActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
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