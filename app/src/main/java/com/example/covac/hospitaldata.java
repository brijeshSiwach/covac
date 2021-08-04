package com.example.covac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.prefs.Preferences;

public class hospitaldata extends AppCompatActivity {

    Spinner state_spinner,district_spinner,hospital_spinner;
    DatabaseReference dref,dref2,dref3;

    Button adddata;
    String hospital;
    SharedPreferences preferences;

    ValueEventListener listener,listener2,listener3;
    ArrayList<String> list;
    ArrayList<String> list2,list9,list10;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2,adapter10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitaldata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        state_spinner=findViewById(R.id.spinner);
        district_spinner=findViewById(R.id.spinner2);
        hospital_spinner= findViewById(R.id.spinner3);
        adddata = findViewById(R.id.adddatabutton);
        dref= FirebaseDatabase.getInstance().getReference("state");
        dref2= FirebaseDatabase.getInstance().getReference("district");
        dref3 = FirebaseDatabase.getInstance().getReference("hospitalname");
        preferences = getSharedPreferences("CovacPreferences", MODE_PRIVATE);
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
        adapter10=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,list10);
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
                    adapter2=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,list2);
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
        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital = hospital_spinner.getSelectedItem().toString();
                String state = state_spinner.getSelectedItem().toString().trim();
                String district = district_spinner.getSelectedItem().toString().trim();
                String cur_adminemail = preferences.getString("adminemail", "");
                if(!hospital.equals("") && !state.equals("-Select State-") && !district.equals("-Select District-") && !state.equals("") && !district.equals("")){
                    DatabaseReference dref3 = FirebaseDatabase.getInstance().getReference("admindetails");
                    DatabaseReference cur_admin = dref3.child(cur_adminemail);

                    String hospital_key = "";
                    for(int i = 0; i < list9.size(); i++){
                        String s = list9.get(i);
                        if(!s.equals("-Select Hospital-")){
                            String[] str = s.split("#");
                            if(str[1].equals(hospital)){
                                hospital_key=str[0];
                                cur_admin.child("hospital_name").setValue(hospital);
                                cur_admin.child("hospital_key").setValue(hospital_key);
                                cur_admin.child("state").setValue(state);
                                cur_admin.child("district").setValue(district);
                                cur_admin.child("verified").setValue("Pending");


                            }
                        }
                    }

                }

            }
        });

    }
}