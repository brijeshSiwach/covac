package com.example.covac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class adminuserlogin extends AppCompatActivity {
    Button admin_login,user_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminuserlogin);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        admin_login = findViewById(R.id.button3);
        user_login = findViewById(R.id.button4);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(),userhomescreen.class);
            startActivity(mainActivity);
            finish();
        }

        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), adminlogin.class);
                startActivity(intent);
            }
        });
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Covacpreferences", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("alreadyloggedin", false);
        return isIntroActivityOpnendBefore;
    }
    public void onBackPressed()
    {
        finishAffinity();;
    }
}