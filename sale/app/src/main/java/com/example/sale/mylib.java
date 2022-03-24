package com.example.sale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class mylib {

    public static  String getDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    }
    public static String getDatabaseDir() {
        return Environment.getDataDirectory().getAbsolutePath() + "/data/com.example.sale/databases/";
    }
    public static  byte[] getByteArrayFromImageView(ImageView imageView)
    {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap bitmap;
        if(bitmapDrawable==null){
            imageView.buildDrawingCache();
            bitmap = imageView.getDrawingCache();
            imageView.buildDrawingCache(false);
        }else
        {
            bitmap = bitmapDrawable .getBitmap();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

        //Bitmap bitmap;
        //bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

        //ImageView yourImageView = (ImageView) findViewById(R.id.yourImageView);
        //Bitmap bitmap = ((BitmapDrawable)yourImageView.getDrawable()).getBitmap();
    }
}
