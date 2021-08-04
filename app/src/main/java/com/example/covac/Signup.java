package com.example.covac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    TextView login_txtview;
    TextInputEditText email_txtedit, pass_txtedit,aadhar_txtedit,phone_textedit,user_txtedit;
    Button signup_btn;
    SharedPreferences preferences;
    FirebaseAuth fauth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        email_txtedit = findViewById(R.id.email);
        pass_txtedit = findViewById(R.id.password);
        aadhar_txtedit=findViewById(R.id.AadharNumber);
        phone_textedit=findViewById(R.id.phonenumber);
        user_txtedit=findViewById(R.id.fullname);
        signup_btn=findViewById(R.id.buttonSignUp);
        login_txtview = findViewById(R.id.loginText);
        fauth=FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);

        preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);

        final String[] fullname = new String[1];
        final String[] email = new String[1];
        final String[] phonenumber = new String[1];
        final String[] password = new String[1];
        final String[] aadharnumber = new String[1];

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname[0] =user_txtedit.getText().toString().trim();
                email[0] = email_txtedit.getText().toString().trim();
                aadharnumber[0] = Objects.requireNonNull(aadhar_txtedit.getText()).toString().trim();
                phonenumber[0] =phone_textedit.getText().toString().trim();
                password[0] = Objects.requireNonNull(pass_txtedit.getText()).toString().trim();

                if (!fullname[0].equals("") && !phonenumber[0].equals("") && !password[0].equals("") && !email[0].equals("") && !aadharnumber[0].equals("")){
                    if(aadharnumber[0].length() != 12){
                        Toast.makeText(Signup.this,"aadhar number must be of 12 number",Toast.LENGTH_SHORT).show();
                    }
                    else if (phonenumber[0].length() != 10){
                        Toast.makeText(Signup.this,"phone number is not correct",Toast.LENGTH_SHORT).show();
                    }
                    else if(password[0].length() < 6){
                        Toast.makeText(Signup.this,"password length must be greater than 5",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int c1=-1,c2=-1,count1=0;
                        for(int i=0;i<email[0].length();i++)
                        {
                            if(email[0].charAt(i)=='@')
                            {
                                c1=i;
                                count1++;
                            }
                            if(email[0].charAt(i)=='.')
                            {
                                c2=i;
                                break;
                            }
                        }
                        if(c1 != -1 && c2 != -1 && c1 < c2) {
                            String[] str = email[0].split("@");
                            String[] str1 = str[1].split("\\.");
                            String user_emailcheck = str[0] + str1[0] + str1[1];

//                        String[] str = email[0].split("@");
                            final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                            DatabaseReference aadharemaiL = database.child("aadharemail");
                            aadharemaiL.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                    if(snapshot.child(user_emailcheck).exists()){
                                        Toast.makeText(getApplicationContext(),"already exist", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        aadharemaiL.child(user_emailcheck).setValue(aadharnumber[0]);
                                        DatabaseReference userdetail = database.child("userdetails");
                                        userdetail.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                if (snapshot.child(aadharnumber[0]).exists()) {
                                                    Toast.makeText(getApplicationContext(), "Email Already registered", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    fauth.createUserWithEmailAndPassword(email[0], password[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                progressBar.setVisibility(View.INVISIBLE);

                                                                Objects.requireNonNull(fauth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            usersignupdataholder obj = new usersignupdataholder(fullname[0], phonenumber[0], email[0], aadharnumber[0], password[0]);
                                                                            FirebaseDatabase busdb = FirebaseDatabase.getInstance();
                                                                            DatabaseReference node = busdb.getReference("userdetails");
                                                                            node.child(aadharnumber[0]).setValue(obj);


                                                                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(), login.class));

                                                                            finish();
                                                                        }
                                                                        else {
                                                                            Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });


                                                            } else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                }
                            });
//                            DatabaseReference cur_useremailcheck = userdetail.child(user_emailcheck);
                            //DatabaseReference user = userdetail.child(str[0]);


                        }


//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("fullname", fullname[0]);
//                        editor.putString("password", password[0]);
//                        editor.putString("email", email[0]);
//                        editor.putString("phonenumber", phonenumber[0]);
//                        editor.putString("aadharnumber", aadharnumber[0]);
//                        editor.apply();
//                        Intent intent = new Intent(Signup.this,login.class);
//                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(Signup.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,login.class);
                startActivity(intent);
            }
        });

    }
}