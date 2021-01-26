package com.amitthakare.camerascanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amitthakare.camerascanner.Adapter.ImageFileAdapter;
import com.amitthakare.camerascanner.Model.ImageFileData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.sql.CommonDataSource;

public class MainActivity2 extends AppCompatActivity {

    DrawerLayout drawerLayout2;
    Toolbar toolbar2;

    private String folderName, tempFilePath;
    private File folderPath, tempFile;

    private FloatingActionButton fabCamera, fabGalleryMultiple;

    //RecyclerView
    List<ImageFileData> listFiles;
    RecyclerView fileRecyclerView;
    ImageFileAdapter imageFileAdapter;
    String imageFile, imageName;

    //For PDf Making
    LinearLayout invisibleLayout;
    ImageView invisibleImageView;
    Bitmap bitmap,scaledBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ini();
        getFolderFiles();
    }

    private void ini() {

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folder_name");
        folderPath = new File(Var.IMAGE_DIR + "/" + folderName);

        //---------General Hooks--------//
        fabCamera = findViewById(R.id.fabCamera);
        fabGalleryMultiple = findViewById(R.id.fabGalleryMultiple);
        invisibleImageView = findViewById(R.id.invisibleImageView);
        invisibleLayout = findViewById(R.id.invisibleLayout);


        //--------Recycler View ----------//
        fileRecyclerView = findViewById(R.id.imageRecyclerView);
        fileRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

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
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    tempFile = null;
                    try {
                        tempFile = File.createTempFile("tempFile", ".jpg", new File(Var.TEMP_DIR));
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MainActivity2.this, "com.amitthakare.camerascanner.fileprovider", tempFile));
                            tempFilePath = Uri.fromFile(tempFile) + "";
                            Log.d("texts", "openc: " + tempFilePath);
                            startActivityForResult(cameraIntent, 200);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                    startActivityForResult(Intent.createChooser(selectIntent, "Select Picture"), 300);
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(fileRecyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback =new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
            | ItemTouchHelper.START | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            String fromFileName = listFiles.get(fromPosition).getImageName();
            String toFileName = listFiles.get(toPosition).getImageName();

            String newFromFileName = getNewFileName(toFileName);
            String newToFileName = getNewFileName(fromFileName);

            Log.e("FromFileName",fromFileName);
            Log.e("ToFileName",toFileName);
            Log.e("newFromFileName",newFromFileName);
            Log.e("newToFileName",newToFileName);

            File file = new File(Var.IMAGE_DIR+"/"+folderName+File.separator+fromFileName);
            File file1 = new File(Var.IMAGE_DIR+"/"+folderName+File.separator+newFromFileName);
            file.renameTo(file1);

            File file2 = new File(Var.IMAGE_DIR+"/"+folderName+File.separator+toFileName);
            File file3 = new File(Var.IMAGE_DIR+"/"+folderName+File.separator+newToFileName);
            file2.renameTo(file3);

            Toast.makeText(MainActivity2.this, "From : "+ fromFileName+" To : "+toFileName, Toast.LENGTH_SHORT).show();

            Collections.swap(listFiles,fromPosition,toPosition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition,toPosition);
            getFolderFiles();
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return Var.isMovable;
        }
    };


    private String getNewFileName(String original)
    {
        String FullOldName = original;
        Log.e("oldName",FullOldName);

        String OldName = FullOldName.substring(0, (FullOldName.length() - 4));
        Log.e("After .jpg",OldName);

        String Part1 = OldName.substring(0, 10);
        Log.e("Part1",Part1);

        String Part2 = OldName.substring(10);
        Log.e("Part2",Part2);

        int Part2Int = Integer.parseInt(Part2) - 2;
        Log.e("After Calculation Part2",""+Part2Int);

        String NewName = Part1 + Part2Int + ".jpg";
        Log.e("Final Name",NewName);

        return NewName;
    }

    private void getFolderFiles() {
        File file = new File(Var.IMAGE_DIR+ "/" +folderName);
        File directory[] = file.listFiles();
        listFiles = new ArrayList<>();

        for (File files : directory )
        {
            imageFile = files.getPath();
            imageName = files.getName();
            ImageFileData imageFileData = new ImageFileData(imageFile,imageName);
            listFiles.add(imageFileData);
        }

        Collections.sort(listFiles, new Comparator<ImageFileData>() {
            @Override
            public int compare(ImageFileData o1, ImageFileData o2) {
                return o1.getImageName().compareTo(o2.getImageName());
            }
        });

        imageFileAdapter = new ImageFileAdapter(getApplicationContext(),listFiles);
        fileRecyclerView.setAdapter(imageFileAdapter);

    }

    private void createPDF()
    {
        File currentFolder = new File(Var.IMAGE_DIR+"/"+folderName);
        File pdfLocation = new File(Var.PDF_DIR);

        File[] fileList =currentFolder.listFiles();
        PdfDocument pdfDocument = new PdfDocument();

        if (fileList.length!=0 | fileList.length>0)
        {

            for (int i=0;i< fileList.length;i++)
            {
                imageName =listFiles.get(i).getImageName();
                imageFile = listFiles.get(i).getImage();
                bitmap = BitmapFactory.decodeFile(imageFile);
                invisibleImageView.setImageBitmap(bitmap);

                Bitmap newBitmap =Bitmap.createBitmap(invisibleLayout.getWidth(),invisibleLayout.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(newBitmap);
                invisibleLayout.draw(canvas1);

                float heightBitmap = (float) (newBitmap.getHeight());
                float widthBitmap = (float) (newBitmap.getWidth());

                Log.e("heightBitmap", "" + heightBitmap);
                Log.e("widthBitmap", "" + widthBitmap);


                float pageWidth = 1240;
                float pageHeight = 1754;

                scaledBitmap = newBitmap;

                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(((int) pageWidth), ((int) pageHeight), i + 1).create();
                PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
                float x1, y1, x2, y2, x, y;

                x1 = myPageInfo.getPageWidth() / 2f;
                y1 = myPageInfo.getPageHeight() / 2f;
                x2 = scaledBitmap.getWidth() / 2f;
                y2 = scaledBitmap.getHeight() / 2f;

                x = x1 - x2;
                y = y1 - y2;


                //trying to add text
                Paint myPaint = new Paint();
                myPaint.setTextAlign(Paint.Align.RIGHT);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                myPaint.setTextSize(30);
                myPaint.setColor(getResources().getColor(R.color.black));

                float textXpoint = myPageInfo.getPageWidth() - 60;
                float textYpoint = myPageInfo.getPageHeight() - 40;

                Canvas canvas = page.getCanvas();

                page.getCanvas().drawBitmap(scaledBitmap, x, y, null);
                canvas.drawText("Made with CameraScanner (Indian)", textXpoint, textYpoint, myPaint);
                pdfDocument.finishPage(page);

            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Var.PDF_FILE_NAME = timeStamp + ".pdf";

            File myPDFFile = new File(pdfLocation.getPath(), timeStamp + ".pdf");
            Var.PDF_FILE_PATH = myPDFFile;


            if (myPDFFile.exists()) {
                Toast.makeText(this, "Alredy Present", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    pdfDocument.writeTo(new FileOutputStream(myPDFFile));
                    Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(MainActivity2.this, PDFViewer.class);
                    startActivity(intent);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pdfDocument.close();

        }else
        {
            Toast.makeText(this, "Add Some Images!", Toast.LENGTH_SHORT).show();
        }


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
            createPDF();
        } else if (id == R.id.settingPDF) {
            Snackbar.make(findViewById(R.id.drawerLayout2), "Setting PDF", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.moveImage)
        {
            if (item.isChecked())
            {
                item.setChecked(false);
                Var.isMovable=false;
                Toast.makeText(this, "Now you can delete the items!", Toast.LENGTH_SHORT).show();
            }else
            {
                item.setChecked(true);
                Var.isMovable=true;
                Toast.makeText(this, "Now you can swap the item position!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 101)
        {
            if (imageFileAdapter.RemoveItem(item.getGroupId(),folderName))
            {
                getFolderFiles();
                Snackbar.make(findViewById(R.id.drawerLayout2),"Deleted!",Snackbar.LENGTH_LONG).show();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
            File newFile = new File(folderPath, timeStamp + ".jpg");

            if (tempFile.renameTo(newFile)) {
                Snackbar.make(findViewById(R.id.drawerLayout2), "Created!", Snackbar.LENGTH_LONG).show();
                getFolderFiles();
            } else {
                Snackbar.make(findViewById(R.id.drawerLayout2), "Not", Snackbar.LENGTH_LONG).show();
            }

        }

        if (requestCode == 300 && resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();

            if (clipData != null) {
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

                    //Snackbar.make(findViewById(R.id.drawerLayout2),"Bitmap"+i,Snackbar.LENGTH_LONG).show();


                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(outStream.toByteArray());
                        fo.flush();
                        fo.close();
                        getFolderFiles();
                    } catch (IOException e) {
                        Log.w("TAG", "Error saving image file: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            } else {
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


                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(outStream.toByteArray());
                    fo.flush();
                    fo.close();
                    getFolderFiles();
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