package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class adminhomescreen extends AppCompatActivity {
    Button hospitalbutton, bookingbutton,vaccinatebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhomescreen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        hospitalbutton =findViewById(R.id.hospitalbutton);
        bookingbutton=findViewById(R.id.bookingbutton);
        vaccinatebutton=findViewById(R.id.vaccinatebutton);
        bookingbutton.setEnabled(false);
        vaccinatebutton.setEnabled(false);

        DatabaseReference admindetail = FirebaseDatabase.getInstance().getReference("admindetails");
        SharedPreferences preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String get_adminemail = preferences.getString("adminemail","");
        hospitalbutton = findViewById(R.id.hospitalbutton);

        admindetail.child(get_adminemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child("verified").exists()) {
                    if (Objects.requireNonNull(snapshot.child("verified").getValue()).toString().equals("verified")) {
                        bookingbutton.setEnabled(true);
                        vaccinatebutton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        hospitalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),hospitaldata.class);
                startActivity(intent);
            }
        });

        vaccinatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),doctorcheck.class);
                startActivity(intent);
            }
        });
    }
}