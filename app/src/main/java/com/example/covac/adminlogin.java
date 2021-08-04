package com.example.covac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

public class adminlogin extends AppCompatActivity {
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
        setContentView(R.layout.activity_adminlogin);
        email_edittext = findViewById(R.id.username);
        pass_edittext = findViewById((R.id.password));
        login_btn = findViewById((R.id.buttonLogin));
        signup_txtview = findViewById((R.id.signUpText));
        forgotpassword=findViewById(R.id.textView46);
        fauth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), adminhomescreen.class);
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
                email[0] = Objects.requireNonNull(email_edittext.getText()).toString().trim();
                password[0] = Objects.requireNonNull(pass_edittext.getText()).toString().trim();


//                saved_email[0] = preferences.getString("email","");
//                saved_pass[0] = preferences.getString("password","");

                if(!email[0].equals("") && !password[0].equals("")){
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
                        String admin_emailcheck = str[0] + str1[0]+str1[1];

                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference admindetail = dref.child("admindetails");
                        DatabaseReference cur_admin_emailcheck = admindetail.child(admin_emailcheck);

                        progressBar.setVisibility(View.VISIBLE);
                        cur_admin_emailcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    fauth.signInWithEmailAndPassword(email[0], password[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                savePrefsData();
                                                progressBar.setVisibility(View.INVISIBLE);

                                                Toast.makeText(getApplicationContext(),"check your email inbox", Toast.LENGTH_SHORT).show();

                                                if(Objects.requireNonNull(fauth.getCurrentUser()).isEmailVerified()){
                                                    Toast.makeText(getApplicationContext(), "logged in Successfully", Toast.LENGTH_SHORT).show();
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("adminemail",admin_emailcheck);
                                                    editor.apply();
                                                    startActivity(new Intent(getApplicationContext(), adminhomescreen.class));
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"email not verified",Toast.LENGTH_SHORT).show();
                                                }


                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Admin not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Email format is not correct",Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(adminlogin.this,"not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminlogin.this,adminsignup.class);
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
                        String admin_emailcheck = str[0] + str1[0] + str1[1];


                        FirebaseDatabase busdb = FirebaseDatabase.getInstance();

                        final DatabaseReference node = busdb.getReference("admindetails");
                        final DatabaseReference emailcheck = node.child(admin_emailcheck);
                        emailcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if (email1.equals((String) snapshot.child("email").getValue())) {
                                        fauth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(adminlogin.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(adminlogin.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                                }

                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(adminlogin.this, "Admin not found, Please Signup! ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(adminlogin.this, "Admin not found, Please Signup! ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(adminlogin.this,"email cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("adminalreadyloggedin", true);
        editor.apply();
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        return pref.getBoolean("adminalreadyloggedin", false);
    }
}