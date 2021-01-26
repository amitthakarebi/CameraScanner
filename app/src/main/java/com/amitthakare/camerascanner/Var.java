package com.amitthakare.camerascanner;

import android.os.Environment;

import java.io.File;

public class Var {

    //public static String ROOT_DIR = Environment.getRootDirectory().getParent() + File.separator + "sdcard/CameraScanner";

    public static String ROOT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/CameraScanner";
    public static String IMAGE_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/CameraScanner/Images";
    public static String TEMP_DIR =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/CameraScanner/Temp";
    public static String PDF_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/CameraScanner/PDF";

    //----------------For Cheching the image movable or not--------------/
    public static boolean isMovable = false;

    //----------------For Pdf File show and share -----------------------/
    public static String PDF_FILE_NAME=null;
    public static File PDF_FILE_PATH=null;


}
