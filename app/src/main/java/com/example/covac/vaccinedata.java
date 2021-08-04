package com.example.covac;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class vaccinedata extends AppCompatActivity {
    TextInputEditText covaxin_edttxt,covishield_edttxt,sputnikv_edttxt, slot_date;
    DatePickerDialog.OnDateSetListener setListener2;
    Button uploaddata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinedata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        covaxin_edttxt = findViewById(R.id.covaxin);
        covishield_edttxt=findViewById(R.id.covishield);
        sputnikv_edttxt=findViewById(R.id.sputnikv);
        slot_date=findViewById(R.id.date);
        uploaddata  =findViewById(R.id.uploaddatabtn);



        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);


        slot_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(vaccinedata.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,setListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }

        });
        setListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date ="";
                if(dayOfMonth < 10){
                    if(month < 10){
                        date ="0" + dayOfMonth+"/" +"0" +month+"/"+year;
                    }
                    else {
                        date = "0"+dayOfMonth+"/"+month+"/"+year;
                    }
                }
                else {
                    if (month < 10) {
                        date = dayOfMonth + "/" + "0" + month + "/" + year;
                    } else {
                        date = dayOfMonth + "/" + month + "/" + year;
                    }
                }
                slot_date.setText(date);
            }
        };

        uploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = Objects.requireNonNull(slot_date.getText()).toString();
                String[] str = date.split("/");
                String Date = str[0]+str[1]+str[2];
                String covaxiN = Objects.requireNonNull(covaxin_edttxt.getText()).toString();
                String covishielD = Objects.requireNonNull(covishield_edttxt.getText()).toString();
                String sputnikV = Objects.requireNonNull(sputnikv_edttxt.getText()).toString();

                DatabaseReference vaccinedailydata = FirebaseDatabase.getInstance().getReference("vaccinedata");
                vaccinedailydata.child(Date).child("covaxin").setValue(covaxiN);
                vaccinedailydata.child(Date).child("covishield").setValue(covishielD);
                vaccinedailydata.child(Date).child("suptnikv").setValue(sputnikV);
            }
        });


    }
}