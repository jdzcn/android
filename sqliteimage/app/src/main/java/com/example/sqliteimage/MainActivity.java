package com.example.sqliteimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private saleHelper db;
    private SQLiteDatabase database;
    private List<product> mlist=new ArrayList<>();
    RecyclerViewAdapter adapter;
    private RecyclerView rview;
    int SELECT_PICTURE = 200;
    ImageView img;
    EditText editname;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new saleHelper(this);
        database=db.getWritableDatabase();
        img=(ImageView)findViewById(R.id.imageView);
        editname=(EditText) findViewById(R.id.editName);
        Cursor cursor=database.rawQuery("select name,img from product", null);
        //database.execSQL("insert into product (name,images,cid) values("+"'jiuju','image1.jpg'"+",1)");

        if (cursor.moveToFirst()) {
            do {
                product p=new product();
                p.name=cursor.getString(0);
                p.img=cursor.getBlob(1);
                mlist.add(p);
                p=null;
            } while (cursor.moveToNext());
        }
        cursor.close();

        Toast.makeText(MainActivity.this, mlist.size()+"条记录", Toast.LENGTH_SHORT).show();

        adapter = new RecyclerViewAdapter(mlist);

        rview=(RecyclerView)findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(layoutManager.HORIZONTAL);
        rview.setLayoutManager(layoutManager);
        rview.addItemDecoration(new DividerItemDecoration(rview.getContext(), DividerItemDecoration.VERTICAL));
        rview.setAdapter(adapter);
    }

    public void  save(View view) {
        byte[] imgdata=getByteArrayFromImageView(img);
        ContentValues cv = new ContentValues();

        cv.put("img", imgdata);
        cv.put("name", editname.getText().toString());

        database.insert("product", null, cv);
        Toast.makeText(MainActivity.this, "Save sucessful.", Toast.LENGTH_SHORT).show();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void  loadimage(View view) {
        imageChooser();
        //imgView.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/Download/img.jpg"));
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    img.setImageURI(selectedImageUri);
                }
            }
        }
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