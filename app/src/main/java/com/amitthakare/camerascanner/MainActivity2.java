package com.amitthakare.camerascanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    DrawerLayout drawerLayout2;
    Toolbar toolbar2;

    private String folderName;
    private File folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ini();
    }

    private void ini() {

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folder_name");
        folderPath = new File(Var.IMAGE_DIR+"/"+folderName);

        //------------Hooks-------------//
        drawerLayout2 = findViewById(R.id.drawerLayout2);
        toolbar2 = findViewById(R.id.navigationToolbar2);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbar2);
        if (!folderName.equals(""))
        {
            Objects.requireNonNull(getSupportActionBar()).setTitle(folderName);
        }else
        {
            Objects.requireNonNull(getSupportActionBar()).setTitle("CameraScanner");
        }

        toolbar2.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbar2.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity2.this,drawerLayout2,toolbar2,R.string.open,R.string.close);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_2,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.createPDF)
        {
            Snackbar.make(findViewById(R.id.drawerLayout2),"Creating Pdf",Snackbar.LENGTH_LONG).show();
        }else if (id==R.id.settingPDF)
        {
            Snackbar.make(findViewById(R.id.drawerLayout2),"Setting PDF",Snackbar.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }
}