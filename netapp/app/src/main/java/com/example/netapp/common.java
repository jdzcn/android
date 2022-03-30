package com.example.netapp;

import android.os.Environment;

public class common {
    public static  String getDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/";
    }
}
