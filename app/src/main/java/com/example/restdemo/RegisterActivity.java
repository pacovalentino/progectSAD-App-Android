package com.example.restdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import response.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {
    TextView regText;
    Button registerButton;
    private EditText etMail, etPass, etNome, etCogn, etData, etCF, etCit, etCap, etCall; //etAddr
    private RadioGroup radioGroup;
    private RadioButton male, female;
    String URL="http://10.0.2.2:8000/api/register";
    private RegisterResponse registerResponse;
    Gson g = new Gson();
    private CheckBox c1,c2,c3,c4,c5;
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
        c1=(CheckBox)findViewById(R.id.checkBox1);
        c2=(CheckBox)findViewById(R.id.checkBox2);
        c3=(CheckBox)findViewById(R.id.checkBox3);
        c4=(CheckBox)findViewById(R.id.checkBox4);
        c5=(CheckBox)findViewById(R.id.checkBox5);

        Bundle bundle= this.getIntent().getExtras();
        etMail.setText(bundle.getString("email"));
        etPass.setText(bundle.getString("password"));

        //Resetta i tasti male e female
        //radioGroup.clearCheck();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMail.getText().toString().equals("")
                        || etPass.getText().toString().equals("")
                        || etNome.getText().toString().equals("")
                        || etCogn.getText().toString().equals("")
                        || etData.getText().toString().equals("")
                        || etCF.getText().toString().equals("")
                        || etCit.getText().toString().equals("")
                      //  || etAddr.getText().toString().equals("")
                        || etCap.getText().toString().equals("")
                        || etCall.getText().toString().equals("")
                        || radioGroup.getCheckedRadioButtonId()==-1 ){
                    Toast.makeText(getApplicationContext(), "Controlla se ogni campo è completo", Toast.LENGTH_LONG).show();
                }else {
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.e("Variabile register ", s);
                            registerResponse = g.fromJson(s, RegisterResponse.class);

                            if (registerResponse.getRegister().equals("account_exists")) {
                                Toast.makeText(getApplicationContext(), "Account exists", Toast.LENGTH_LONG).show();
                            } else if (registerResponse.getRegister().equals("success")) {

                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                finish();

                                //Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("email", etMail.getText().toString());
                            parameters.put("password", etPass.getText().toString());
                            parameters.put("first_name", etNome.getText().toString());
                            parameters.put("last_name", etCogn.getText().toString());
                            parameters.put("date_of_birth", etData.getText().toString());
                            if(male.isChecked()) parameters.put("gender","0");
                            if(female.isChecked()) parameters.put("gender","1");
                            
                            parameters.put("fiscal_code", etCF.getText().toString());
                            parameters.put("city", etCit.getText().toString());
                            //parameters.put("", etAddr.getText().toString());
                            parameters.put("cap", etCap.getText().toString());
                            parameters.put("mobile_phone", etCall.getText().toString());
                            if(c1.isChecked()) parameters.put("heart_disease","1");
                                else parameters.put("heart_disease","0");
                            if(c2.isChecked()) parameters.put("allergy","1");
                                else parameters.put("allergy","0");
                            if(c3.isChecked()) parameters.put("immunosuppression","1");
                                else parameters.put("immunosuppression","0");
                            if(c4.isChecked()) parameters.put("anticoagulants","1");
                                else parameters.put("anticoagulants","0");
                            if(c5.isChecked()) parameters.put("covid","1");
                                else parameters.put("covid","0");

                            return parameters;
                        }
                    };
                    RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                    rQueue.add(request);
                }
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