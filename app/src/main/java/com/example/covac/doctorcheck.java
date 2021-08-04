package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class doctorcheck extends AppCompatActivity {
    Button  vaccinate_btn, check_btn;
    TextView name,dob,aadhar,phno,vaccinename,agegroup,firstdose,seconddose, firstdosehospital, seconddosehospital;
    TextInputEditText get_aadhar_edttxt;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorcheck);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        //id reference
        vaccinate_btn = findViewById(R.id.vaccinate);
        check_btn=findViewById(R.id.check);

        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        aadhar=findViewById(R.id.aadharnumber);
        phno=findViewById(R.id.phonenumber);
        vaccinename=findViewById(R.id.vaccinename);
        agegroup=findViewById(R.id.agegroup);
        firstdose=findViewById(R.id.firstdose);
        seconddose=findViewById(R.id.seconddose);
        firstdosehospital=findViewById(R.id.firstdosehospital);
        seconddosehospital=findViewById(R.id.seconddosehospital);

        get_aadhar_edttxt =findViewById(R.id.password);

        // database reference


        //String


        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userdetail = FirebaseDatabase.getInstance().getReference("userdetails");
                String get_aadhar = Objects.requireNonNull(get_aadhar_edttxt.getText()).toString().trim();
                userdetail.child(get_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if(snapshot.child("vaccine2status").exists()){
                                name.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                phno.setText(Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                vaccinename.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                                firstdosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                                seconddosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine2hospital").getValue()).toString());
                                firstdose.setText("Done, " +  Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                firstdose.setTextColor(Color.GREEN);
                                seconddose.setText("Booked" + Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString());
                                seconddose.setTextColor(Color.rgb(255,192,12));
                            }
                            else if (snapshot.child("vaccine1status").exists()){
                                if(snapshot.child("vaccine1status").getValue().toString().equals("Booked")){
                                    name.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                    aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                    phno.setText(Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                    dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                    agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                    vaccinename.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                                    firstdosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                                    firstdose.setText("Booked, " +  Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                    seconddosehospital.setText("Not Booked");
                                    seconddose.setText("Not Booked");
                                    firstdose.setTextColor(Color.rgb(255,192,12));
                                    seconddose.setTextColor(Color.RED);
                                }
                                else if (snapshot.child("vaccine1status").getValue().toString().equals("Done")){
                                    name.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                    aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                    phno.setText(Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                    dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                    agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                    vaccinename.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                                    firstdosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                                    firstdose.setText("Done, " +  Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                    seconddosehospital.setText("Not Booked");
                                    seconddose.setTextColor(Color.RED);
                                    firstdose.setTextColor(Color.GREEN);

                                    seconddose.setText("Not Booked");
                                }

                            }
                            else if (!snapshot.child("vaccine1status").exists()){
                                Toast.makeText(getApplicationContext(),"Vaccine Not Booked by Given Entry",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),get_aadhar + " not registered on Covac", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });

        vaccinate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(doctorcheck.this);

                builder.setCancelable(true);
                builder.setTitle("Vaccination");
                builder.setMessage("Are you Sure Vaccination is done!");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference userdetail = FirebaseDatabase.getInstance().getReference("userdetails");
                                String get_aadhar = Objects.requireNonNull(get_aadhar_edttxt.getText()).toString().trim();
                                DatabaseReference admindetail = FirebaseDatabase.getInstance().getReference("admindetails");
                                SharedPreferences preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);
                                String get_adminemail = preferences.getString("adminemail","");
                                userdetail.child(get_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        final String[] state = new String[1];
                                        final String[] district = new String[1];
                                        final String[] hospital = new String[1];

                                        DatabaseReference admindetail = FirebaseDatabase.getInstance().getReference("admindetails");
                                        admindetail.child(get_adminemail).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        if(snapshot.exists()){
                                            if(snapshot.child("vaccine2status").exists()){
                                                name.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                                aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                                phno.setText(Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                                dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                                agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                                vaccinename.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                                                firstdosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                                                seconddosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine2hospital").getValue()).toString());
                                                firstdose.setText("Done, " +  Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                                seconddose.setText("Done" + Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString());
                                                userdetail.child(get_aadhar).child("doctor2name").setValue(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());

                                                userdetail.child(get_aadhar).child("vaccine2status").setValue("Done");
                                                seconddose.setTextColor(Color.GREEN);
                                                firstdose.setTextColor(Color.GREEN);
                                                String Date = Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString();
                                                String vaccine = snapshot.child("vaccine").getValue().toString().toLowerCase();
                                                String[] DatE = Date.split("/");
                                                String user_aadhar = Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString();



                                                DatabaseReference vbd = FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                                                vbd.child(DatE[0]+DatE[1]+DatE[2]).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            vbd.child(DatE[0]+DatE[1]+DatE[2]).child(state[0]).child(district[0]).child(hospital[0]).child(vaccine).child("Done").child(user_aadhar).setValue(user_aadhar);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });
//                                                vbd.child(DatE[0]+DatE[1]+DatE[2]).child(state[0]).child(district[0]).child(hospital[0]).child(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString()).child("Done").child(user_aadhar).setValue(user_aadhar);

                                            }
                                            else if (snapshot.child("vaccine1status").exists()){
                                                if(Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Booked")){
                                                    name.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                                    aadhar.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                                    dob.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                                    phno.setText(Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                                    agegroup.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                                    vaccinename.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                                                    firstdosehospital.setText(Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString());
                                                    firstdose.setText("Done, " +  Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                                    userdetail.child(get_aadhar).child("doctor1name").setValue(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());

                                                    seconddosehospital.setText("Not Booked");
                                                    seconddose.setTextColor(Color.RED);
                                                    userdetail.child(get_aadhar).child("vaccine1status").setValue("Done");
                                                    firstdose.setTextColor(Color.GREEN);
                                                    seconddose.setText("Not Booked");

                                                    String Date = Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString();
                                                    String vaccine = snapshot.child("vaccine").getValue().toString().toLowerCase();
                                                    String[] DatE = Date.split("/");
                                                    String user_aadhar = Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString();



                                                    DatabaseReference vbd = FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                                                    vbd.child(DatE[0]+DatE[1]+DatE[2]).child(state[0]).child(district[0]).child(hospital[0]).child(vaccine).child("Done").child(user_aadhar).setValue(user_aadhar);




                                                }


                                            }
                                            else if (!snapshot.child("vaccine1status").exists()){
                                                Toast.makeText(getApplicationContext(),"Vaccine Not Booked by " + get_aadhar,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),get_aadhar + " not registered on Covac", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });



                            }


                        });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }
}