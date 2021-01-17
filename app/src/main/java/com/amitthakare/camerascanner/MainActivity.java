package com.amitthakare.camerascanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.amitthakare.camerascanner.Adapter.FolderAdapter;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ini();
        //it request the permission and create the necessary folders.
        requestPermissions();

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA},101);
    }

    private void createFolders() {

        List<String> list = new ArrayList<>();
        list.add(Var.IMAGE_DIR);
        list.add(Var.PDF_DIR);
        list.add(Var.TEMP_DIR);

        Log.e("List",list.toString());

        for (int i = 0; i<list.size(); i++)
        {
            File file = new File(list.get(i));
            if (file.mkdirs())
            {
                Toast.makeText(this, "Created Folder : "+(i+1), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void ini() {

        //----------General Hooks--------------//
        folderRecyclerView = findViewById(R.id.folderRecyclerView);
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //----------Hooks--------------//
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.navigationToolbar);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Home");

        //---------Navigation Drawer Menu-----------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //---------To Set Menu Item Clickable---------//
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share!", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 101:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    createFolders();
                }else
                {
                    Toast.makeText(this, "Declined!", Toast.LENGTH_SHORT).show();
                }
        }

    }
}