package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class seconddosedata extends AppCompatActivity {
    TextView name,aadhar,gender,dob,agegroup,slotdate,state,district,hospital,doctor,status,corstate,cordistrict,corhospital,corslotdate,cordoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconddosedata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();



        name = findViewById(R.id.textView39);
        aadhar=findViewById(R.id.textView40);
        gender=findViewById(R.id.textView41);
        dob=findViewById(R.id.textView42);
        agegroup=findViewById(R.id.textView43);
        slotdate=findViewById(R.id.textView48);
        state=findViewById(R.id.textView45);
        district=findViewById(R.id.textView46);
        hospital=findViewById(R.id.textView47);
        doctor=findViewById(R.id.textView49);
        status=findViewById(R.id.textView44);
        cordistrict=findViewById(R.id.cordistrict);
        corslotdate=findViewById(R.id.corslotdate);
        corstate=findViewById(R.id.corstate);
        corhospital=findViewById(R.id.corhospitalname);
        cordoctor=findViewById(R.id.cordoctorname);

        corslotdate.setVisibility(View.INVISIBLE);
//        corstatus.setVisibility(View.INVISIBLE);
        cordistrict.setVisibility(View.INVISIBLE);
        corhospital.setVisibility(View.INVISIBLE);
        corstate.setVisibility(View.INVISIBLE);
//        status.setVisibility(View.INVISIBLE);
        cordoctor.setVisibility(View.INVISIBLE);
//
//        slotdate.setVisibility(View.INVISIBLE);
//        status.setVisibility(View.INVISIBLE);
//        district.setVisibility(View.INVISIBLE);
//        hospital.setVisibility(View.INVISIBLE);
////        status.setVisibility(View.INVISIBLE);
//        doctor.setVisibility(View.INVISIBLE);


        DatabaseReference userdetail = FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String get_aadhar = preferences.getString("user_aadharnumber","");
//        Toast.makeText(getApplicationContext(),)


        userdetail.child(get_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                name.setText(Objects. requireNonNull(snapshot.child("fullname").getValue()).toString());
                aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                gender.setText(Objects.requireNonNull(snapshot.child("gender").getValue()).toString());
                dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                String str = "Not Booked";
                status.setText(str);
                status.setTextColor(Color.RED);
                agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                if(snapshot.child("vaccine2status").exists()){
                    if(Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Booked")){
                        slotdate.setText(Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString());
                        corslotdate.setVisibility(View.VISIBLE);
                        state.setText(Objects.requireNonNull(snapshot.child("vaccine2state").getValue()).toString());
                        corstate.setVisibility(View.VISIBLE);
                        district.setText(Objects.requireNonNull(snapshot.child("vaccine2district").getValue()).toString());
                        cordistrict.setVisibility(View.VISIBLE);
                        hospital.setText(Objects.requireNonNull(snapshot.child("vaccine2hospital").getValue()).toString());
                        corhospital.setVisibility(View.VISIBLE);
                        status.setText("Booked");

                    status.setTextColor(Color.rgb(255,192,12));
                        status.setVisibility(View.VISIBLE);
                    }
                    if(Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Done")) {
                        slotdate.setText(Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString());
                        corslotdate.setVisibility(View.VISIBLE);
                        state.setText(Objects.requireNonNull(snapshot.child("vaccine2state").getValue()).toString());
                        corstate.setVisibility(View.VISIBLE);
                        district.setText(Objects.requireNonNull(snapshot.child("vaccine2district").getValue()).toString());
                        cordistrict.setVisibility(View.VISIBLE);
                        hospital.setText(Objects.requireNonNull(snapshot.child("vaccine2hospital").getValue()).toString());
                        corhospital.setVisibility(View.VISIBLE);
                        status.setText("Done");
                    status.setTextColor(Color.GREEN);
                    status.setVisibility(View.VISIBLE);
                        cordoctor.setVisibility(View.VISIBLE);
                        doctor.setText(Objects.requireNonNull(snapshot.child("doctor2name").getValue()).toString());
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}