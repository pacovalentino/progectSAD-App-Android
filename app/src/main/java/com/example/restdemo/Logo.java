package com.example.restdemo;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Logo extends AppCompatActivity {

    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        logo=(ImageView)findViewById(R.id.logo_id);

        final int t=200;
        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                logo.setAlpha(0.1f);
                Handler handler=new Handler();
                Runnable r=new Runnable() {
                    public void run() {
                        logo.setAlpha(0.2f);
                        Handler handler=new Handler();
                        Runnable r=new Runnable() {
                            public void run() {
                                logo.setAlpha(0.3f);
                                Handler handler=new Handler();
                                Runnable r=new Runnable() {
                                    public void run() {
                                        logo.setAlpha(0.4f);
                                        Handler handler=new Handler();
                                        Runnable r=new Runnable() {
                                            public void run() {
                                                logo.setAlpha(0.5f);
                                                Handler handler=new Handler();
                                                Runnable r=new Runnable() {
                                                    public void run() {
                                                        logo.setAlpha(0.6f);
                                                        Handler handler=new Handler();
                                                        Runnable r=new Runnable() {
                                                            public void run() {
                                                                logo.setAlpha(0.7f);
                                                                Handler handler=new Handler();
                                                                Runnable r=new Runnable() {
                                                                    public void run() {
                                                                        logo.setAlpha(0.8f);
                                                                        Handler handler=new Handler();
                                                                        Runnable r=new Runnable() {
                                                                            public void run() {
                                                                                logo.setAlpha(0.9f);
                                                                                Handler handler=new Handler();
                                                                                Runnable r=new Runnable() {
                                                                                    public void run() {
                                                                                        logo.setAlpha(1f);
                                                                                        Handler handler=new Handler();
                                                                                        Runnable r=new Runnable() {
                                                                                            public void run() {
                                                                                                logo.setAlpha(0.9f);
                                                                                                Handler handler=new Handler();
                                                                                                Runnable r=new Runnable() {
                                                                                                    public void run() {
                                                                                                        logo.setAlpha(0.8f);
                                                                                                        Handler handler=new Handler();
                                                                                                        Runnable r=new Runnable() {
                                                                                                            public void run() {
                                                                                                                logo.setAlpha(0.7f);
                                                                                                                Handler handler=new Handler();
                                                                                                                Runnable r=new Runnable() {
                                                                                                                    public void run() {
                                                                                                                        logo.setAlpha(0.6f);
                                                                                                                        Handler handler=new Handler();
                                                                                                                        Runnable r=new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                logo.setAlpha(0.5f);
                                                                                                                                Handler handler=new Handler();
                                                                                                                                Runnable r=new Runnable() {
                                                                                                                                    public void run() {
                                                                                                                                        logo.setAlpha(0.4f);
                                                                                                                                        Handler handler=new Handler();
                                                                                                                                        Runnable r=new Runnable() {
                                                                                                                                            public void run() {
                                                                                                                                                logo.setAlpha(0.3f);
                                                                                                                                                Handler handler=new Handler();
                                                                                                                                                Runnable r=new Runnable() {
                                                                                                                                                    public void run() {
                                                                                                                                                        logo.setAlpha(0.2f);
                                                                                                                                                        Handler handler=new Handler();
                                                                                                                                                        Runnable r=new Runnable() {
                                                                                                                                                            public void run() {
                                                                                                                                                                logo.setAlpha(0.1f);
                                                                                                                                                                Handler handler=new Handler();
                                                                                                                                                                Runnable r=new Runnable() {
                                                                                                                                                                    public void run() {
                                                                                                                                                                        logo.setAlpha(0.f);
                                                                                                                                                                        Intent openPage1 = new Intent(Logo.this,LoginActivity.class);
                                                                                                                                                                        startActivity(openPage1);
                                                                                                                                                                        finish();
                                                                                                                                                                    }
                                                                                                                                                                };
                                                                                                                                                                handler.postDelayed(r, t);
                                                                                                                                                            }
                                                                                                                                                        };
                                                                                                                                                        handler.postDelayed(r, t);
                                                                                                                                                    }
                                                                                                                                                };
                                                                                                                                                handler.postDelayed(r, t);
                                                                                                                                            }
                                                                                                                                        };
                                                                                                                                        handler.postDelayed(r, t);
                                                                                                                                    }
                                                                                                                                };
                                                                                                                                handler.postDelayed(r, t);
                                                                                                                            }
                                                                                                                        };
                                                                                                                        handler.postDelayed(r, t);
                                                                                                                    }
                                                                                                                };
                                                                                                                handler.postDelayed(r, t);
                                                                                                            }
                                                                                                        };
                                                                                                        handler.postDelayed(r, t);
                                                                                                    }
                                                                                                };
                                                                                                handler.postDelayed(r, t);
                                                                                            }
                                                                                        };
                                                                                        handler.postDelayed(r, t);
                                                                                    }
                                                                                };
                                                                                handler.postDelayed(r, t+1800);
                                                                            }
                                                                        };
                                                                        handler.postDelayed(r, t);
                                                                    }
                                                                };
                                                                handler.postDelayed(r, t);
                                                            }
                                                        };
                                                        handler.postDelayed(r, t);
                                                    }
                                                };
                                                handler.postDelayed(r, t);
                                            }
                                        };
                                        handler.postDelayed(r, t);
                                    }
                                };
                                handler.postDelayed(r, t);
                            }
                        };
                        handler.postDelayed(r, t);
                    }
                };
                handler.postDelayed(r, t);
            }
        };
        handler.postDelayed(r, t);


    }

}
