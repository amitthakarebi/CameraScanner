package com.amitthakare.camerascanner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class AboutUs extends AppCompatActivity {

    DrawerLayout drawerLayoutAboutUs;
    Toolbar toolbarAboutUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ini();
    }

    private void ini() {

        //------------Hooks-------------//
        drawerLayoutAboutUs = findViewById(R.id.drawerLayoutAbout);
        toolbarAboutUs = findViewById(R.id.navigationToolbarAboutUs);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarAboutUs);
        Objects.requireNonNull(getSupportActionBar()).setTitle("About Us");
        toolbarAboutUs.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarAboutUs.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AboutUs.this,drawerLayoutAboutUs,toolbarAboutUs,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //drawerLayoutStudent.addDrawerListener(toggle);
        toggle.syncState();

    }
}