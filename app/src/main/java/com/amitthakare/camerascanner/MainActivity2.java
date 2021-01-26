package com.amitthakare.camerascanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.sql.CommonDataSource;

public class MainActivity2 extends AppCompatActivity {

    DrawerLayout drawerLayout2;
    Toolbar toolbar2;

    private String folderName;
    private File folderPath;

    private FloatingActionButton fabCamera, fabGalleryMultiple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ini();
    }

    private void ini() {

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folder_name");
        folderPath = new File(Var.IMAGE_DIR + "/" + folderName);

        //---------General Hooks--------//
        fabCamera = findViewById(R.id.fabCamera);
        fabGalleryMultiple = findViewById(R.id.fabGalleryMultiple);

        //------------Hooks-------------//
        drawerLayout2 = findViewById(R.id.drawerLayout2);
        toolbar2 = findViewById(R.id.navigationToolbar2);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbar2);
        if (!folderName.equals("")) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(folderName);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("CameraScanner");
        }

        toolbar2.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbar2.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity2.this, drawerLayout2, toolbar2, R.string.open, R.string.close);
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


        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    //do here your work

                }
            }
        });

        fabGalleryMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    // Do your work here
                    Intent selectIntent = new Intent(Intent.ACTION_PICK);
                    selectIntent.setAction(Intent.ACTION_GET_CONTENT);
                    selectIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    selectIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(selectIntent,"Select Picture"),300);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_2, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.createPDF) {
            Snackbar.make(findViewById(R.id.drawerLayout2), "Creating Pdf", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.settingPDF) {
            Snackbar.make(findViewById(R.id.drawerLayout2), "Setting PDF", Snackbar.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();

            if (clipData!=null)
            {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();

                    Bitmap bitmap = getBitmapByUri(imageUri);

                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
                    File f = new File(folderPath, timeStamp + ".jpg");

                    ByteArrayOutputStream outStream;
                    double heightBitmap = bitmap.getHeight();
                    double widthBitmap = bitmap.getWidth();

                    Bitmap scaledBitMap = Bitmap.createScaledBitmap(bitmap, (int) widthBitmap, (int) heightBitmap, true);
                    bitmap = scaledBitMap;

                    outStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                    Snackbar.make(findViewById(R.id.drawerLayout2),"Bitmap"+i,Snackbar.LENGTH_LONG).show();


                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(outStream.toByteArray());
                        fo.flush();
                        fo.close();
                    } catch (IOException e) {
                        Log.w("TAG", "Error saving image file: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }else {
                Uri imageUri = data.getData();
                Bitmap bitmap = getBitmapByUri(imageUri);

                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
                File f = new File(folderPath, timeStamp + ".jpg");

                double heightBitmap = bitmap.getHeight();
                double widthBitmap = bitmap.getWidth();
                ByteArrayOutputStream outStream;

                Bitmap scaledBitMap = Bitmap.createScaledBitmap(bitmap, (int) widthBitmap, (int) heightBitmap, true);
                bitmap = scaledBitMap;

                outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                Snackbar.make(findViewById(R.id.drawerLayout2),"Bitmap only one",Snackbar.LENGTH_LONG).show();


                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(outStream.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    Log.w("TAG", "Error saving image file: " + e.getMessage());
                    e.printStackTrace();
                }

            }

        }
    }

    private Bitmap getBitmapByUri(Uri uri) {

        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 29) {
            ImageDecoder.Source source = ImageDecoder.createSource(getApplicationContext().getContentResolver(), uri);
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}