package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class location extends AppCompatActivity {
    Spinner state_spinner,district_spinner,gender_spinner,age_spinner,vaccine_spinner,hospital_spinner;
    DatabaseReference dref,dref2,dref3;
    TextInputEditText user_dob,slot_date;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    ValueEventListener listener,listener2,listener3;
    ArrayList<String> list,list2,list3,list4,list5,list9,list10;
    ArrayAdapter<String> adapter,adapter2,adapter3,adapter4,adapter5,adapter10;
    DatePickerDialog.OnDateSetListener setListener,setListener2;
    ProgressBar progressBar;
    Button book_slot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        user_dob=findViewById(R.id.fullname);
        state_spinner=findViewById(R.id.spinner);
        district_spinner=findViewById(R.id.spinner2);
        gender_spinner=findViewById(R.id.spinner3);
        age_spinner=findViewById(R.id.spinner4);
        vaccine_spinner=findViewById(R.id.spinner5);
        hospital_spinner=findViewById(R.id.spinner6);
        slot_date=findViewById(R.id.slotdate);
        book_slot=findViewById(R.id.button8);
        progressBar=findViewById(R.id.progress);
        dref= FirebaseDatabase.getInstance().getReference("state");
        dref2= FirebaseDatabase.getInstance().getReference("district");
        dref3=FirebaseDatabase.getInstance().getReference("hospitalname");


        final Calendar[] calendar = {Calendar.getInstance()};
        int year= calendar[0].get(Calendar.YEAR);
        int day= calendar[0].get(Calendar.DAY_OF_MONTH);
        int month= calendar[0].get(Calendar.MONTH);

        SharedPreferences preferences=getSharedPreferences("CovacPreferences",MODE_PRIVATE);
        String check_vaccine=preferences.getString("vaccinenumber","");
        list9=new ArrayList<String>();
        list=new ArrayList<String>();
        list.add(0,"-Select State-");
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
        state_spinner.setAdapter(adapter);
        listener=dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                list.add(0,"-Select State-");
                for(DataSnapshot mdata:snapshot.getChildren())
                {
                    list.add(Objects.requireNonNull(mdata.getValue()).toString());
                    Collections.sort(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        final String[] s = {""};
        final String[] s1={""};
        list10=new ArrayList<String>();
        list10.add(0,"-Select Hospital-");
        adapter10=new ArrayAdapter<String>(location.this, android.R.layout.simple_spinner_dropdown_item,list10);
        hospital_spinner.setAdapter(adapter10);


        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s[0] =state_spinner.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), s[0],Toast.LENGTH_SHORT).show();;
                if(!s.equals(""))
                {
                    list2=new ArrayList<String>();
                    list2.add(0,"-Select District-");
                    adapter2=new ArrayAdapter<String>(location.this, android.R.layout.simple_spinner_dropdown_item,list2);
                    district_spinner.setAdapter(adapter2);
                    list10.clear();
                    adapter10.notifyDataSetChanged();

                    listener2=dref2.child(s[0]).orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            list2.clear();
                            list2.add(0,"-Select District-");
                            for(DataSnapshot mdata2:snapshot.getChildren())
                            {
                                list2.add(Objects.requireNonNull(mdata2.getValue()).toString());
                                Collections.sort(list2);
                                adapter2.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });





                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s1[0]=district_spinner.getSelectedItem().toString();
                s[0] = state_spinner.getSelectedItem().toString();
                if(!s1[0].equals("") && !s[0].equals(""))
                {
                    list10.clear();

                    list10.add(0,"-Select Hospital-");
                    adapter10.notifyDataSetChanged();

                    listener3=dref3.child(s[0]).child(s1[0]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            list10.clear();
                            list9.clear();
                            list10.add(0,"-Select Hospital-");
                            for(DataSnapshot mdata3:snapshot.getChildren())
                            {
                                list9.add(Objects.requireNonNull(mdata3.getKey()).toString()+"#"+mdata3.getValue().toString());
                                list10.add(Objects.requireNonNull(mdata3.getValue()).toString());
                                Collections.sort(list10);
                                adapter10.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        list3=new ArrayList<String>();
        list3.add(0,"-Select Gender-");
        list3.add(1,"Male");
        list3.add(2,"Female");
        list3.add(3,"Others");
        adapter3=new ArrayAdapter<String>(location.this, android.R.layout.simple_spinner_dropdown_item,list3);
        gender_spinner.setAdapter(adapter3);

        list4=new ArrayList<String>();
        list4.add(0,"-Select Age Group");
        list4.add(1,"Below 18");
        list4.add(2,"Above 18 And below 45");
        list4.add(3,"Above 45");
        adapter4=new ArrayAdapter<String>(location.this, android.R.layout.simple_spinner_dropdown_item,list4);
        age_spinner.setAdapter(adapter4);


        list5=new ArrayList<String>();
        list5.add(0,"-Select Vaccine-");
        list5.add(1,"Covaxin");
        list5.add(2,"Covishield");
        list5.add(3,"Sputnik V");
        adapter5=new ArrayAdapter<String>(location.this, android.R.layout.simple_spinner_dropdown_item,list5);
        vaccine_spinner.setAdapter(adapter5);

//
        user_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(location.this,
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

                user_dob.setText(date);
            }
        };

        slot_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(location.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,setListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }

        });
        setListener2=new DatePickerDialog.OnDateSetListener() {
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
                slot_date.setText(date);
            }
        };
        if(check_vaccine.equals("vaccine2status"))
        {

            user_dob.setEnabled(false);
            vaccine_spinner.setEnabled(false);
            age_spinner.setEnabled(false);
            gender_spinner.setEnabled(false);
            ArrayList<String>list6=new ArrayList<String>();
            ArrayAdapter<String> adapter6=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list6);
            age_spinner.setAdapter(adapter6);

            ArrayList<String>list7=new ArrayList<String>();
            ArrayAdapter<String> adapter7=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list7);
            gender_spinner.setAdapter(adapter7);

            ArrayList<String>list8=new ArrayList<String>();
            ArrayAdapter<String> adapter8=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list8);
            vaccine_spinner.setAdapter(adapter8);
            DatabaseReference dref=FirebaseDatabase.getInstance().getReference("userdetails");
            String check_aadhar=preferences.getString("user_aadharnumber","");
            //Toast.makeText(location.this,check_aadhar,Toast.LENGTH_SHORT).show();
            dref.child(check_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    list6.add(snapshot.child("agegroup").getValue().toString());
                    adapter6.notifyDataSetChanged();
                    list7.add(snapshot.child("gender").getValue().toString());
                    adapter7.notifyDataSetChanged();
                    list8.add(snapshot.child("vaccine").getValue().toString());
                    adapter8.notifyDataSetChanged();
                    user_dob.setText(snapshot.child("dob").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }


        book_slot.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {



                progressBar.setVisibility(View.VISIBLE);
                String spinner1=state_spinner.getSelectedItem().toString();
                String spinner2=district_spinner.getSelectedItem().toString();
                String dob= Objects.requireNonNull(user_dob.getText()).toString().trim();
                String user_slot= Objects.requireNonNull(slot_date.getText()).toString().trim();
                String gender=gender_spinner.getSelectedItem().toString();
                String age_group=age_spinner.getSelectedItem().toString();
                String vaccine_name=vaccine_spinner.getSelectedItem().toString();
                String hospital_name=hospital_spinner.getSelectedItem().toString();
                String user_name=preferences.getString("user_fullname","");
                String user_aadhar=preferences.getString("user_aadharnumber","");
                String user_phone=preferences.getString("user_phonenumber","");
                String user_email=preferences.getString("user_email","");

                final String vaccine_status=preferences.getString("vaccinenumber","");
                String vaccine_date="";
                String vaccine_number="";
                if(vaccine_status.equals("vaccine1status"))
                {
                    vaccine_date="vaccine1date";
                    vaccine_number="1";
                }
                else if(vaccine_status.equals("vaccine2status"))
                {
                    vaccine_date="vaccine2date";
                    vaccine_number="2";
                }



                if(!spinner1.equals("-Select State-")&&!spinner2.equals("-Select District-")
                        &&!dob.equals("")&&!user_slot.equals("")&&!gender.equals("-Select Gender-")&&!age_group.equals("-Select Age Group")&&!vaccine_name.equals("-Select Vaccine-")&&!hospital_name.equals("-Select Hospital-"))
                {
                    String hospital_key="";
                    for(int i=0;i<list9.size();i++)
                    {
                        String s=list9.get(i);
                        if(!s.equals("-Select Hospital-"))
                        {
                            String[] str=s.split("#");
                            if(str[1].equals(hospital_name))
                            {
                                hospital_key=str[0];
                            }
                        }
                    }
                    DatabaseReference dref=FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
//                    DatabaseReference dref2=FirebaseDatabase.getInstance().getReference("userbookingdata");
                    DatabaseReference dref3=FirebaseDatabase.getInstance().getReference("userdetails");
                    //DatabaseReference user_booking_aadhar=dref2.child(user_aadhar);
                    DatabaseReference cur_user_vaccine=dref3.child(user_aadhar);
                    if(vaccine_status.equals("vaccine2status"))
                    {
                        calendar[0] =Calendar.getInstance();
                        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        String vaccine1date_done=preferences.getString("vaccine1date","");
                        long days=0;
                        long hours=0;
                        long minutes=0;
                        try {
                            Date d1=dateFormat.parse(user_slot);
                            Date d2=dateFormat.parse(vaccine1date_done);
                            assert d2 != null;
                            assert d1 != null;
                            long diff = d1.getTime() - d2.getTime();
                            long seconds = diff / 1000;
                            minutes = seconds / 60;
                            hours = minutes / 60;
                            days=hours/24;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(location.this,String.valueOf(days),Toast.LENGTH_LONG).show();
                        if(days>5)
                        {
                            String[] str=user_slot.split("/");
                            String slotdate_node=str[0]+str[1]+str[2];


                            DatabaseReference check_slotdate=dref.child(slotdate_node);


                            String finalVaccine_number = vaccine_number;
                            String finalHospital_key = hospital_key;
                            check_slotdate.child(spinner1).child(spinner2).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    long size1=snapshot.child(finalHospital_key).child("covaxin").getChildrenCount();
                                    long size2=snapshot.child(finalHospital_key).child("covishield").getChildrenCount();
                                    long size3=snapshot.child(finalHospital_key).child("sputnik").getChildrenCount();
                                    //Toast.makeText(location.this,String.valueOf(size2),Toast.LENGTH_SHORT).show();
                                    if(vaccine_name.equals("Covaxin"))
                                    {
                                        if(size1>=100)
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(location.this,"All slot for Covaxin on selected date are filled",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("covaxin").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                            cur_user_vaccine.child("fullname").setValue(user_name);
                                            cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                            cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                            cur_user_vaccine.child("vaccine2date").setValue(user_slot);
                                            cur_user_vaccine.child("vaccine2state").setValue(spinner1);
                                            cur_user_vaccine.child("vaccine2district").setValue(spinner2);
                                            cur_user_vaccine.child("vaccine2hospital").setValue(hospital_name);
                                            cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                            cur_user_vaccine.child("email").setValue(user_email);
                                            cur_user_vaccine.child("gender").setValue(gender);
                                            cur_user_vaccine.child("dob").setValue(dob);
                                            cur_user_vaccine.child("vaccine2status").setValue("Booked");
                                            cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                            cur_user_vaccine.child("agegroup").setValue(age_group);
                                            cur_user_vaccine.child("vaccine2key").setValue(finalHospital_key);


                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                    else if(vaccine_name.equals("Covishield"))
                                    {
                                        if(size2>=100)
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(location.this,"All slot for Covishield on selected date are filled",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("covishield").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                            cur_user_vaccine.child("fullname").setValue(user_name);
                                            cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                            cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                            cur_user_vaccine.child("vaccine2date").setValue(user_slot);
                                            cur_user_vaccine.child("vaccine2state").setValue(spinner1);
                                            cur_user_vaccine.child("vaccine2district").setValue(spinner2);
                                            cur_user_vaccine.child("vaccine2hospital").setValue(hospital_name);
                                            cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                            cur_user_vaccine.child("email").setValue(user_email);
                                            cur_user_vaccine.child("gender").setValue(gender);
                                            cur_user_vaccine.child("dob").setValue(dob);
                                            cur_user_vaccine.child("vaccine2status").setValue("Booked");
                                            cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                            cur_user_vaccine.child("agegroup").setValue(age_group);
                                            cur_user_vaccine.child("vaccine2key").setValue(finalHospital_key);

                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else if(vaccine_name.equals("Sputnik V"))
                                    {
                                        if(size3>=100)
                                        {
                                            progressBar.setVisibility(View.VISIBLE);
                                            Toast.makeText(location.this,"All slot for Sputnik on selected date are filled",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("sputnik").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                            cur_user_vaccine.child("fullname").setValue(user_name);
                                            cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                            cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                            cur_user_vaccine.child("vaccine2date").setValue(user_slot);
                                            cur_user_vaccine.child("vaccine2state").setValue(spinner1);
                                            cur_user_vaccine.child("vaccine2district").setValue(spinner2);
                                            cur_user_vaccine.child("vaccine2hospital").setValue(hospital_name);
                                            cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                            cur_user_vaccine.child("email").setValue(user_email);
                                            cur_user_vaccine.child("gender").setValue(gender);
                                            cur_user_vaccine.child("dob").setValue(dob);
                                            cur_user_vaccine.child("vaccine2status").setValue("Booked");
                                            cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                            cur_user_vaccine.child("agegroup").setValue(age_group);
                                            cur_user_vaccine.child("vaccine2key").setValue(finalHospital_key);

                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(location.this,"please select a date 50 dys ahead of"+vaccine1date_done,Toast.LENGTH_LONG).show();
                        }

                    }
                    else if(vaccine_status.equals("vaccine1status"))
                    {
                        String[] str=user_slot.split("/");
                        String slotdate_node=str[0]+str[1]+str[2];


                        DatabaseReference check_slotdate=dref.child(slotdate_node);


                        String finalVaccine_date = vaccine_date;
                        String finalVaccine_number = vaccine_number;
                        String finalHospital_key = hospital_key;
                        check_slotdate.child(spinner1).child(spinner2).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                long size1=snapshot.child(finalHospital_key).child("covaxin").getChildrenCount();
                                long size2=snapshot.child(finalHospital_key).child("covishield").getChildrenCount();
                                long size3=snapshot.child(finalHospital_key).child("sputnik").getChildrenCount();
                                //Toast.makeText(location.this,String.valueOf(size2),Toast.LENGTH_SHORT).show();
                                if(vaccine_name.equals("Covaxin"))
                                {
                                    if(size1>=100)
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(location.this,"All slot for Covaxin on selected date are filled",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("covaxin").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                        cur_user_vaccine.child("fullname").setValue(user_name);
                                        cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                        cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                        cur_user_vaccine.child("vaccine1date").setValue(user_slot);
                                        cur_user_vaccine.child("vaccine1state").setValue(spinner1);
                                        cur_user_vaccine.child("vaccine1district").setValue(spinner2);
                                        cur_user_vaccine.child("vaccine1hospital").setValue(hospital_name);
                                        cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                        cur_user_vaccine.child("email").setValue(user_email);
                                        cur_user_vaccine.child("gender").setValue(gender);
                                        cur_user_vaccine.child("dob").setValue(dob);
                                        cur_user_vaccine.child("vaccine1status").setValue("Booked");
                                        cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                        cur_user_vaccine.child("agegroup").setValue(age_group);
                                        cur_user_vaccine.child("vaccine1key").setValue(finalHospital_key);

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(vaccine_name.equals("Covishield"))
                                {
                                    if(size2>=100)
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(location.this,"All slot for Covishield on selected date are filled",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("covishield").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                        cur_user_vaccine.child("fullname").setValue(user_name);
                                        cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                        cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                        cur_user_vaccine.child("vaccine1date").setValue(user_slot);
                                        cur_user_vaccine.child("vaccine1state").setValue(spinner1);
                                        cur_user_vaccine.child("vaccine1district").setValue(spinner2);
                                        cur_user_vaccine.child("vaccine1hospital").setValue(hospital_name);
                                        cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                        cur_user_vaccine.child("email").setValue(user_email);
                                        cur_user_vaccine.child("gender").setValue(gender);
                                        cur_user_vaccine.child("dob").setValue(dob);
                                        cur_user_vaccine.child("vaccine1status").setValue("Booked");
                                        cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                        cur_user_vaccine.child("agegroup").setValue(age_group);
                                        cur_user_vaccine.child("vaccine1key").setValue(finalHospital_key);

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(vaccine_name.equals("Sputnik V"))
                                {
                                    if(size3>=100)
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(location.this,"All slot for Sputnik on selected date are filled",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        check_slotdate.child(spinner1).child(spinner2).child(finalHospital_key).child("sputnik").child("Booked").child(user_aadhar).setValue(user_aadhar);
                                        cur_user_vaccine.child("fullname").setValue(user_name);
                                        cur_user_vaccine.child("phonenumber").setValue(user_phone);
                                        cur_user_vaccine.child("vaccine").setValue(vaccine_name);
                                        cur_user_vaccine.child("vaccine1date").setValue(user_slot);
                                        cur_user_vaccine.child("vaccine1state").setValue(spinner1);
                                        cur_user_vaccine.child("vaccine1district").setValue(spinner2);
                                        cur_user_vaccine.child("vaccine1hospital").setValue(hospital_name);
                                        cur_user_vaccine.child("aadharnumber").setValue(user_aadhar);
                                        cur_user_vaccine.child("email").setValue(user_email);
                                        cur_user_vaccine.child("gender").setValue(gender);
                                        cur_user_vaccine.child("dob").setValue(dob);
                                        cur_user_vaccine.child("vaccine1status").setValue("Booked");
                                        cur_user_vaccine.child("vaccinenumber").setValue(finalVaccine_number);
                                        cur_user_vaccine.child("agegroup").setValue(age_group);
                                        cur_user_vaccine.child("vaccine1key").setValue(finalHospital_key);

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),"Booked", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }


                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(location.this,"All fields are Required!",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),userhomescreen.class);
        startActivity(intent);
    }
}