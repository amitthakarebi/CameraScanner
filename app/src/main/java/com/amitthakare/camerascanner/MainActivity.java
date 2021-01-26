package com.amitthakare.camerascanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.amitthakare.camerascanner.Adapter.FolderAdapter;
import com.amitthakare.camerascanner.Model.FolderData;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//Step5:we have to implement the DocumentFolderDialog with its listener to get the text.
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DocumentFolderDialog.DocumentFolderDialogListener, RenameDialog.RenameDialogListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;

    String folderName, folderDate, folderTime, folderPages, folderImage;
    List<FolderData> folderList;

    //for deection of the item who is goinng to rename
    int renameItemId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ini();
        //checkBuildAndCreateFolder();
        requestPermissions();

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //---------To Set Menu Item Clickable---------//
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void getFolderDirectory() {

        File root = new File(Var.IMAGE_DIR);
        File directory[] = root.listFiles();
        folderList = new ArrayList<>();

        for (File Outer : directory) {
            if ((Outer.listFiles().length >= 1)) {
                File[] sortedFileName = Outer.listFiles();
                if (sortedFileName != null && sortedFileName.length > 1) {
                    Arrays.sort(sortedFileName, new Comparator<File>() {
                        @Override
                        public int compare(File object1, File object2) {
                            return object1.getName().compareTo(object2.getName());
                        }
                    });
                }

                if (sortedFileName != null && sortedFileName.length > 0) {
                    folderImage = sortedFileName[0].getPath();
                }
            } else {
                //set Drawable img
                Uri path = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/" + R.drawable.ic_baseline_android_24);
                folderImage = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/15ed55ac-6ea4-42ea-b506-9eecc02bb639/d609yh7-825deba0-857d-441a-a2d1-851710cecf57.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOiIsImlzcyI6InVybjphcHA6Iiwib2JqIjpbW3sicGF0aCI6IlwvZlwvMTVlZDU1YWMtNmVhNC00MmVhLWI1MDYtOWVlY2MwMmJiNjM5XC9kNjA5eWg3LTgyNWRlYmEwLTg1N2QtNDQxYS1hMmQxLTg1MTcxMGNlY2Y1Ny5wbmcifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6ZmlsZS5kb3dubG9hZCJdfQ.EDAaUS_mHCeOg2X8yzDpfaOUixnEk7o9Yu4f87l3CWk";
            }

            folderName = Outer.getName();

            Date lastModifiedDate = new Date(Outer.lastModified());
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            folderDate = formatterDate.format(lastModifiedDate);

            Date lastModifiedTime = new Date(Outer.lastModified());
            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            folderTime = formatterTime.format(lastModifiedTime);

            folderPages = Outer.listFiles().length + "";

            FolderData folderData = new FolderData(folderName, folderDate, folderTime, folderPages, folderImage);
            folderList.add(folderData);

        }

        folderAdapter = new FolderAdapter(getApplicationContext(), folderList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        folderRecyclerView.addItemDecoration(dividerItemDecoration);
        folderRecyclerView.setAdapter(folderAdapter);

        folderAdapter.setOnRecyclerClickListerner(new FolderAdapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerItemClick(int position) {
                //Toast.makeText(MainActivity.this, folderList.get(position).getFolderName(), Toast.LENGTH_SHORT).show();
                //Snackbar.make(findViewById(R.id.drawerLayout),folderList.get(position).getFolderName(),Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("folder_name",folderList.get(position).getFolderName());
                startActivity(intent);
            }
        });

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }

    private void createFolders() {

        List<String> list = new ArrayList<>();
        list.add(Var.IMAGE_DIR);
        list.add(Var.PDF_DIR);
        list.add(Var.TEMP_DIR);

        //Log.e("List", list.toString());

        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            if (file.mkdirs()) {
                Toast.makeText(this, "Created Folder : " + (i + 1), Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void openCreateFolderDialog() {
        DocumentFolderDialog documentFolderDialog = new DocumentFolderDialog();
        documentFolderDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.nav_home)
        {

        }else if (id==R.id.nav_share)
        {
            Toast.makeText(this, "share!", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.nav_about)
        {
            Intent intent = new Intent(MainActivity.this,AboutUs.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFolders();
                    getFolderDirectory();
                } else {
                    Toast.makeText(this, "Declined!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    //it is used to show the menus on the toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        //for searching text and passing to the filter method which is present in the adapterclass
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                folderAdapter.getFilter().filter(s);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //it is use to handle the click on the menus which is shown on the toolbar

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.createDocument)
        {
            openCreateFolderDialog();
        }else if (id == R.id.refresh)
        {
            getFolderDirectory();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createDocument(String documentName) {
        File file = new File(Var.IMAGE_DIR+File.separator+documentName);
        if (!file.exists())
        {
            if (file.mkdirs())
            {
                Toast.makeText(this, "Created!", Toast.LENGTH_SHORT).show();
                getFolderDirectory();
            }else
            {
                Toast.makeText(this, "Not Created!", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(this, "Document Already Exist!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 101)
        {
            if (folderAdapter.RemoveItem(item.getGroupId()))
            {
                Snackbar.make(findViewById(R.id.drawerLayout),"Deleted",Snackbar.LENGTH_LONG).show();
            }
        }else if (item.getItemId() == 102)
        {
            RenameDialog renameDialog = new RenameDialog();
            renameDialog.show(getSupportFragmentManager(),"example dialog");
            renameItemId = item.getGroupId();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void renameDocument(String documentName) {
        String OldName = folderList.get(renameItemId).getFolderName();
        File oldFile = new File(Var.IMAGE_DIR+"/"+OldName);
        File newFile = new File(Var.IMAGE_DIR+"/"+documentName);

        if (oldFile.renameTo(newFile))
        {
            Snackbar.make(findViewById(R.id.drawerLayout),"Renamed!",Snackbar.LENGTH_LONG).show();
            folderAdapter.notifyDataSetChanged();
            getFolderDirectory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFolderDirectory();
    }
}


// Recycler Filter Video                        = https://www.youtube.com/watch?v=sJ-Z9G0SDhc
// Recycler Item Delete Context Menu Video      = https://www.youtube.com/watch?v=y7gNVZ0JGOg
// Recycler On Item Click Listener              = https://www.youtube.com/watch?v=bhhs4bwYyhc