package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class hospitalbookingdata extends AppCompatActivity {
    TextInputEditText date_enter;
    TextView total_vaccine, comp_vaccine,total_covaxine,comp_covaxine, total_covishield,comp_covishield,total_sputnik,comp_sputnik;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalbookingdata);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        total_vaccine = findViewById(R.id.textView24);
        comp_vaccine = findViewById(R.id.textView25);
        total_covaxine=findViewById(R.id.textView34);
        total_covishield=findViewById(R.id.textView35);
        total_sputnik=findViewById(R.id.textView36);
        comp_covaxine=findViewById(R.id.textView37);
        comp_covishield=findViewById(R.id.textView38);
        comp_sputnik = findViewById(R.id.textView39);
        date_enter=findViewById(R.id.fullname);


        final Calendar[] calendar = {Calendar.getInstance()};
        int year= calendar[0].get(Calendar.YEAR);
        int day= calendar[0].get(Calendar.DAY_OF_MONTH);
        int month= calendar[0].get(Calendar.MONTH);

        SharedPreferences preferences =getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String get_email = preferences.getString("adminemial","");

        final String[] state = new String[1];
        final String[] district = new String[1];
        final String[] hospital = new String[1];

        DatabaseReference admindetail = FirebaseDatabase.getInstance().getReference("admindetails");
        admindetail.child(get_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                state[0] = Objects.requireNonNull(snapshot.child("state").getValue()).toString();
                district[0] = Objects.requireNonNull(snapshot.child("district").getValue()).toString();
                hospital[0] = Objects.requireNonNull(snapshot.child("hospital_key").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        date_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(getApplicationContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }

        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date="";
                if(dayOfMonth<10)
                {
                    if(month<10)
                    {
                        date= "0"+dayOfMonth+"/0"+month+"/"+year;
                    }
                    else
                    {
                        date= "0"+dayOfMonth+"/"+month+"/"+year;
                    }
                }
                else
                {
                    if(month<10)
                    {
                        date= dayOfMonth+"/0"+month+"/"+year;
                    }
                    else
                    {
                        date= dayOfMonth+"/"+month+"/"+year;
                    }
                }

                date_enter.setText(date);
            }
        };
        String datE = Objects.requireNonNull(date_enter.getText()).toString();


        DatabaseReference vbd = FirebaseDatabase.getInstance().getReference("vaccinebookingdata");

        vbd.child(datE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long covaxine_count_book = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("covaxine").child("Booked").getChildrenCount();
                    long covaxine_count_done = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("covaxine").child("Done").getChildrenCount();
                    long covishield_count_book = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("covishield").child("Booked").getChildrenCount();
                    long covishield_count_done = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("covishield").child("Done").getChildrenCount();
                    long sputnik_count_book = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("sputnikv").child("Booked").getChildrenCount();
                    long sputnik_count_done = snapshot.child(state[0]).child(district[0]).child(hospital[0]).child("sputnikv").child("Done").getChildrenCount();

                    long tot_vacc=covaxine_count_book+covishield_count_book+sputnik_count_book;
                    long comp_vac = covaxine_count_done+covishield_count_done+sputnik_count_done;

                    total_vaccine.setText((int) tot_vacc);
                    comp_vaccine.setText((int) comp_vac);
                    total_covaxine.setText((int) covaxine_count_book);
                    comp_covaxine.setText((int) covaxine_count_done);
                    total_covishield.setText((int) covishield_count_book);
                    comp_covishield.setText((int) covishield_count_done);
                    total_sputnik.setText((int) sputnik_count_book);
                    comp_sputnik.setText((int) sputnik_count_done);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}