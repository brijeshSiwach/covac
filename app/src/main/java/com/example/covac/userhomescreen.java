package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class userhomescreen extends AppCompatActivity {
    Button bookmyslot_btn,logout_btn, mybookings;
    TextView firstdose,seconddose,firstdosestatus,seconddosestatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhomescreen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        bookmyslot_btn=findViewById(R.id.bookmyslotbutton);
        logout_btn=findViewById(R.id.logoutbutton);
        firstdose = findViewById(R.id.textView3);
        firstdosestatus=findViewById(R.id.textView4);
        seconddose = findViewById(R.id.textView5);
        seconddosestatus = findViewById(R.id.textView6);
        mybookings=findViewById(R.id.button6);
        seconddosestatus.setText("Not Booked Yet");

        DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences=getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String user_aadhar=preferences.getString("user_aadharnumber","");
        //Toast.makeText(userhomescreen.this,user_aadhar, Toast.LENGTH_SHORT).show();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();

        dref5.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.child("vaccine1status").exists())
                {
//                    firstdosestatus.setTextColor(android.R.color.holo_red_dark);
                   firstdosestatus.setText("Not Booked Yet");

                }
                else
                {
                    if(snapshot.child("vaccine1status").getValue().toString().equals("Booked"))
//                    {
                        firstdosestatus.setText("Booked");
//                        firstdosestatus.setTextColor(android.R.color.holo_orange_light);
//                    }
                    else
                    {
                        if(snapshot.child("vaccine1status").getValue().toString().equals("Done"))
                        {
                            firstdosestatus.setText("Done");
//                            firstdosestatus.setTextColor(android.R.color.holo_green_light);
                            if(!snapshot.child("vaccine2status").exists())
                            {
//                                seconddosestatus.setTextColor(Integer.parseInt("red"));
                                seconddosestatus.setText("Not Booked yet");
                            }
                            else
                            {
                                if(Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Booked"))
                                {
//                                    seconddosestatus.setTextColor(android.R.color.holo_orange_light);
                                    seconddosestatus.setText("Booked");
                                }
                                else
                                {
                                    if(snapshot.child("vaccine2status").getValue().toString().equals("Done"))
                                    {
//                                        seconddosestatus.setTextColor(android.R.color.holo_green_light);
                                        seconddosestatus.setText("Done");
                                    }
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });






        bookmyslot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
                SharedPreferences preferences=getSharedPreferences("CovacPreferences",MODE_PRIVATE);
                String user_aadhar=preferences.getString("user_aadharnumber","");
                //Toast.makeText(userhomescreen.this,user_aadhar, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=preferences.edit();

                dref5.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!snapshot.child("vaccine1status").exists())
                        {
                            editor.putString("vaccinenumber","vaccine1status");
                            editor.apply();
                            Intent intent=new Intent(getApplicationContext(),location.class);
                            startActivity(intent);

                        }
                        else
                        {
                            if(snapshot.child("vaccine1status").getValue().toString().equals("Booked"))
                            {
                                Toast.makeText(userhomescreen.this,"You have already booked for Vaccine Dose 1", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(snapshot.child("vaccine1status").getValue().toString().equals("Done"))
                                {
                                    if(!snapshot.child("vaccine2status").exists())
                                    {
                                        editor.putString("vaccine1date", Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                        editor.putString("vaccinenumber","vaccine2status");
                                        editor.apply();
                                        Intent intent=new Intent(getApplicationContext(),location.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        if(snapshot.child("vaccine2status").getValue().toString().equals("Booked"))
                                        {
                                            Toast.makeText(userhomescreen.this,"You have already booked for Vaccine Dose 2", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            if(snapshot.child("vaccine2status").getValue().toString().equals("Done"))
                                            {
                                                Toast.makeText(userhomescreen.this,"You have Been Vaccinated Twice!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });



            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("alreadyloggedin", false);
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),adminuserlogin.class);
                startActivity(intent);
                finish();
            }
        });

        mybookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),mybookingsdata.class);
                startActivity(intent);
            }
        });


    }
    public void onBackPressed(){

        finishAffinity();
    }
}