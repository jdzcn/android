package com.example.product;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private proHelper db;
    private SQLiteDatabase database;
    private List<product> mlist=new ArrayList<>();
    RecyclerViewAdapter adapter;
    private RecyclerView rview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new proHelper(this);
        database=db.getReadableDatabase();



        Cursor cursor=database.rawQuery("select id,name,images from product limit 20", null);
       //database.execSQL("insert into product (name,images,cid) values("+"'jiuju','image1.jpg'"+",1)");

        if (cursor.moveToFirst()) {
            do {
                product p=new product(cursor.getString(1),cursor.getString(2));
                mlist.add(p);
                p=null;
            } while (cursor.moveToNext());
        }
        cursor.close();

        Toast.makeText(MainActivity.this, mlist.size()+"条记录", Toast.LENGTH_SHORT).show();

        adapter = new RecyclerViewAdapter(MainActivity.this,mlist);

        rview=(RecyclerView)findViewById(R.id.rview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(layoutManager.HORIZONTAL);
        rview.setLayoutManager(layoutManager);
        rview.addItemDecoration(new DividerItemDecoration(rview.getContext(), DividerItemDecoration.VERTICAL));
        rview.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playmp3();
                BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                dialog.setContentView(R.layout.bottom);
                dialog.show();
                playmp3();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("MainActivity","MainActicity_onStart");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            // Do work with full size photo saved at fullPhotoUri
            Log.d("Main",uri.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }
    }

    public void playmp3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer.create(MainActivity.this,R.raw.chen).start();
            }
        }).start();
    }
}
