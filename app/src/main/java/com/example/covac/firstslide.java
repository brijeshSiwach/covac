package com.example.covac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class firstslide extends AppCompatActivity {

    private ViewPager screenPager;
    firstslideadapter firstslideadapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // make the activity on full screen

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();



        setContentView(R.layout.activity_firstslide);

        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);




        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), adminuserlogin.class);
            startActivity(mainActivity);
            finish();
        }


        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        boolean fresh_food = mList.add(new ScreenItem("Fresh Fruits", "Sweets and pie will make you cry, Fruits and Vegetables give you the edge", R.drawable.vaccination));
        mList.add(new ScreenItem("Fresh Vegetables", "Have fruits & vegetables if you want to lead a fruitful life", R.drawable.modi));
        mList.add(new ScreenItem("Fast Delivery", "Extraordinary Service.", R.drawable.covidvaccine));


        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        firstslideadapter = new firstslideadapter(this, mList);
        screenPager.setAdapter(firstslideadapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(),adminuserlogin.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();

            }
        });



        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();
                }
            }
        });


        // skip button click listener
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });



        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }


    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CovacPreferences", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }



}