package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class userbookingshow extends AppCompatActivity {
    TextView firstdose,seconddose,firstdosestatus,seconddosestatus;
    Button firstdosedata,secondosedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbookingshow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        firstdosedata=findViewById(R.id.bookmyslotbutton);
        secondosedata=findViewById(R.id.logoutbutton);
        firstdose = findViewById(R.id.textView3);
        firstdosestatus=findViewById(R.id.textView4);
        seconddose = findViewById(R.id.textView5);
        seconddosestatus = findViewById(R.id.textView6);

        DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences=getSharedPreferences("CovacPreferences",MODE_PRIVATE);
//        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor  = preferences.edit();
        String user_aadhar=preferences.getString("user_aadharnumber","");
        //Toast.makeText(userhomescreen.this,user_aadhar, Toast.LENGTH_SHORT).show();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();

        dref5.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.child("vaccine1status").exists())
                {
                    firstdosestatus.setText("Not Booked");

                }
                else
                {
                    if(snapshot.child("vaccine1status").getValue().toString().equals("Booked"))
                    {
                        firstdosestatus.setText("Booked");
                        editor.putString("check_vaccine1","Booked");
                    }
                    else
                    {
                        if(snapshot.child("vaccine1status").getValue().toString().equals("Done"))
                        {
                            firstdosestatus.setText("Done");
                            editor.putString("check_vaccine1","Done");
                            if(!snapshot.child("vaccine2status").exists())
                            {
                                seconddosestatus.setText("Not Booked");
                            }
                            else
                            {
                                if(Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Booked"))
                                {
                                    editor.putString("check_vaccine2","Booked");
                                    seconddosestatus.setText("Booked");
                                }
                                else
                                {
                                    if(snapshot.child("vaccine2status").getValue().toString().equals("Done"))
                                    {
                                        editor.putString("check_vaccine2","Done");
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
    }
}