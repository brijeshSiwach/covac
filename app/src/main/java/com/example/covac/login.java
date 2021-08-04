package com.example.covac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Display;
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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class login extends AppCompatActivity {
    TextInputEditText email_edittext, pass_edittext;
    Button login_btn;
    TextView signup_txtview, forgotpassword;
    SharedPreferences preferences;
    FirebaseAuth fauth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        email_edittext = findViewById(R.id.username);
        pass_edittext = findViewById((R.id.password));
        login_btn = findViewById((R.id.buttonLogin));
        signup_txtview = findViewById((R.id.signUpText));
        forgotpassword = findViewById(R.id.textView46);
        fauth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), userhomescreen.class);
            startActivity(mainActivity);
            finish();
        }
        preferences = getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        final String[] saved_email = new String[1];
        final String[] saved_pass = new String[1];

        final String[] email = new String[1];
        final String[] password = new String[1];

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email[0] = email_edittext.getText().toString().trim();
                password[0] = pass_edittext.getText().toString().trim();
                saved_email[0] = preferences.getString("email","");
                saved_pass[0] = preferences.getString("password","");
                if(!email[0].equals("") && !password[0].equals("")){
//                    if(email[0].equals(saved_email[0]) && password[0].equals(saved_pass[0])){
//                        Intent intent = new Intent(login.this, userhomescreen.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Toast.makeText(login.this,"not valid2",Toast.LENGTH_SHORT).show();
//                    }
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
                        }
                    }
                    if(c1 != -1 && c2 != -1 && c1 < c2) {
                        String[] str = email[0].split("@");
                        String[] str1 = str[1].split("\\.");
                        String user_emailcheck = str[0] + str1[0] + str1[1];

                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
//                        DatabaseReference userdetail = dref.child("userdetails");
                        DatabaseReference aadharemaiL = dref.child("aadharemail");
//                        DatabaseReference cur_user= userdetail.child(use);
                        aadharemaiL.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(!snapshot.child(user_emailcheck).exists()){
                                    Toast.makeText(getApplicationContext(),"email not registered",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String get_aadhar = Objects.requireNonNull(snapshot.child(user_emailcheck).getValue()).toString();
                                    DatabaseReference userdetail = dref.child("userdetails");
                                    progressBar.setVisibility(View.VISIBLE);
                                    fauth.signInWithEmailAndPassword(email[0], password[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                if(Objects.requireNonNull(fauth.getCurrentUser()).isEmailVerified()){
                                                    savePrefsData();
                                                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("userdetails");
                                                    DatabaseReference cur_user = dref.child(get_aadhar);
                                                    cur_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            SharedPreferences preferences = getSharedPreferences("CovacPreferences",MODE_PRIVATE);
                                                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                                                            editor.putString("user_fullname", Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                                            editor.putString("user_aadharnumber", Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                                            editor.putString("user_phonenumber", Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                                            editor.putString("user_email", Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                                                            editor.apply();
                                                            Toast.makeText(getApplicationContext(), "logged in Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), userhomescreen.class));
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                                else {
//                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),"verify your email first",Toast.LENGTH_SHORT).show();
                                                }


                                            } else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });


                    }
                }
                else {
                    Toast.makeText(login.this,"not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,Signup.class);
                startActivity(intent);
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1,password1;
                email1 = String.valueOf(email_edittext.getText());

                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                if (!email1.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    int c1 = -1, c2 = -1, count1 = 0;
                    for (int i = 0; i < email1.length(); i++) {
                        if (email1.charAt(i) == '@') {
                            c1 = i;
                            count1++;
                        }
                        if (email1.charAt(i) == '.') {
                            c2 = i;
                        }
                    }
                    if (c1 != -1 && c2 != -1 && c1 < c2) {
                        String[] str = email1.split("@");
                        String[] str1 = str[1].split("\\.");
                        String user_emailcheck = str[0] + str1[0] + str1[1];


                        FirebaseDatabase busdb = FirebaseDatabase.getInstance();

                        final DatabaseReference node = busdb.getReference("userdetails");
                        final DatabaseReference emailcheck = node.child(user_emailcheck);
                        emailcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (email1.equals((String) snapshot.child("email").getValue())) {
                                        fauth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(login.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(login.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                                }

                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(login.this, "Admin not found, Please Signup! ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(login.this, "Admin not found, Please Signup! ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Enter valid Email",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(login.this,"email cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("alreadyloggedin", true);
        editor.apply();
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        return pref.getBoolean("alreadyloggedin", false);
    }

}