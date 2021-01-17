package com.amitthakare.camerascanner;

import android.os.Environment;

import java.io.File;

public class Var {

    //public static String ROOT_DIR = Environment.getRootDirectory().getParent() + File.separator + "sdcard/CameraScanner";

    public static String ROOT_DIR = Environment.getRootDirectory().getParent()+ "sdcard/CameraScanner";
    public static String IMAGE_DIR = Environment.getRootDirectory().getParent()+ "sdcard/CameraScanner/Images";
    public static String TEMP_DIR =  Environment.getRootDirectory().getParent()+ "sdcard/CameraScanner/Temp";
    public static String PDF_DIR = Environment.getRootDirectory().getParent() + "sdcard/CameraScanner/PDF";


}
