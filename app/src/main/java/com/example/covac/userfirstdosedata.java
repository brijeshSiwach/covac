package com.example.covac;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class userfirstdosedata extends AppCompatActivity {
    TextView name,aadhar,gender,dob,agegroup,slotdate,state,district,hospital,doctor,status,corstate,cordistrict,corhospital,corslotdate,cordoctor;
    Button generatebtn;
    Bitmap bmp,scaledbmp;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfirstdosedata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.healthministry);
        scaledbmp = Bitmap.createScaledBitmap(bmp,1200, 2010,false);
//        scaledbmp = Bitmap.;



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
        generatebtn=findViewById(R.id.button5);

        generatebtn.setEnabled(false);

        corslotdate.setVisibility(View.INVISIBLE);
//        corstatus.setVisibility(View.INVISIBLE);
        cordistrict.setVisibility(View.INVISIBLE);
        corhospital.setVisibility(View.INVISIBLE);
        corstate.setVisibility(View.INVISIBLE);
//        status.setVisibility(View.INVISIBLE);
        cordoctor.setVisibility(View.INVISIBLE);


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
                if(snapshot.child("vaccine1status").exists()){
                    if(Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Booked")){
                        slotdate.setText(Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                        corslotdate.setVisibility(View.VISIBLE);
                        state.setText(Objects.requireNonNull(snapshot.child("vaccine1state").getValue()).toString());
                        corstate.setVisibility(View.VISIBLE);
                        district.setText(Objects.requireNonNull(snapshot.child("vaccine1district").getValue()).toString());
                        cordistrict.setVisibility(View.VISIBLE);
                        hospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                        corhospital.setVisibility(View.VISIBLE);
                        status.setText("Booked");
                    status.setTextColor(Color.rgb(255,192,12));
                        status.setVisibility(View.VISIBLE);
                    }
                    if(Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Done")) {
                        slotdate.setText(Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                        corslotdate.setVisibility(View.VISIBLE);
                        state.setText(Objects.requireNonNull(snapshot.child("vaccine1state").getValue()).toString());
                        corstate.setVisibility(View.VISIBLE);
                        district.setText(Objects.requireNonNull(snapshot.child("vaccine1district").getValue()).toString());
                        cordistrict.setVisibility(View.VISIBLE);
                        hospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                        corhospital.setVisibility(View.VISIBLE);
                        status.setText("Done");
                    status.setTextColor(Color.GREEN);
//                    status.setVisibility(View.VISIBLE);
                        cordoctor.setVisibility(View.VISIBLE);
                        doctor.setText(Objects.requireNonNull(snapshot.child("doctor1name").getValue()).toString());
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


       

        userdetail.child(get_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child("vaccine1status").exists()){
                    if(Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Booked") || Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Done")) {
                        generatebtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        generatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PdfDocument mypdf = new PdfDocument();
                Paint mypaint = new Paint();
                Paint titlepaint = new Paint();

                PdfDocument.PageInfo mypageinfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
                PdfDocument.Page mypage1 = mypdf.startPage(mypageinfo1);
                Canvas canvas = mypage1.getCanvas();
                canvas.drawBitmap(scaledbmp,0,0,mypaint);
                titlepaint.setTextAlign(Paint.Align.CENTER);
                titlepaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                canvas.drawText("Provisional Certificate for COVID-19 Vaccination - 1st Dose", 500, 500, mypaint);
                mypdf.finishPage(mypage1);

                File file = new File(Environment.getExternalStorageDirectory(),"/Dose-1.pdf");
            }
        });





    }

}