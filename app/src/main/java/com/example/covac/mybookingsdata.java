package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class mybookingsdata extends AppCompatActivity {

    Button first,second,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybookingsdata);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        first = findViewById(R.id.button2);
        second = findViewById(R.id.button);
        cancel = findViewById(R.id.button7);

//        cancel.setEnabled(false);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), userfirstdosedata.class);
                startActivity(intent);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), seconddosedata.class);
                startActivity(intent);
            }
        });
        DatabaseReference userdetail = FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String get_aadhar = preferences.getString("user_aadharnumber","");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdetail.child(get_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.child("vaccine1status").exists()) {
                            if (Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Booked")) {
//                                cancel.setEnabled(true);
                                String vaccine = Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString();
                                String state = Objects.requireNonNull(snapshot.child("vaccine1state").getValue()).toString();
                                String district = Objects.requireNonNull(snapshot.child("vaccine1district").getValue()).toString();
                                String key = Objects.requireNonNull(snapshot.child("vaccine1key").getValue()).toString();

                                DatabaseReference vbd = FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                                vbd.child(state).child(district).child(key).child(vaccine).child(get_aadhar).removeValue();
                                userdetail.child(get_aadhar).child("vaccine1status").removeValue();

                            }
                            else if (Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Done")) {
                                if (snapshot.child("vaccine2status").exists()) {
                                    if (Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Booked")) {
//                                        cancel.setEnabled(true);
                                        String vaccine = Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString();
                                        String state = Objects.requireNonNull(snapshot.child("vaccine2state").getValue()).toString();
                                        String district = Objects.requireNonNull(snapshot.child("vaccine2district").getValue()).toString();
                                        String key = Objects.requireNonNull(snapshot.child("vaccine2key").getValue()).toString();

                                        DatabaseReference vbd = FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                                        vbd.child(state).child(district).child(key).child(vaccine).child(get_aadhar).removeValue();
                                        userdetail.child(get_aadhar).child("vaccinestatus").removeValue();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"not validate for cancel booking", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else{

                                Toast.makeText(getApplicationContext(),"please book your vaccine", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });


    }
}