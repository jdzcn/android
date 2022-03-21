package com.example.sale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    public saleHelper db;
    public SQLiteDatabase database;
    private static List<Product> pList = new ArrayList<Product>();
    int SELECT_PICTURE = 200;
    ImageView img;
    Button btn;
    EditText editid,editname,editprice,editcost;
    ProductAdapter adapter;
    GridView listView;
    //RecyclerViewAdapter adapter;
    //RecyclerView rview;
    static String select;
    private View mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent=getIntent();
        select=intent.getStringExtra("select");

        mViewGroup = findViewById(R.id.product);
        Log.d("product",select);
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_toolbar);
        if(select.equals("yes")) {
            myChildToolbar.setTitle("选择商品");
            mViewGroup.setVisibility(View.GONE);
        }



        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        db=new saleHelper(this);
        database=db.getWritableDatabase();

        btn=(Button)findViewById(R.id.button);
        img=(ImageView)findViewById(R.id.image_product);
        editid=(EditText)findViewById(R.id.edit_id);
        editname=(EditText)findViewById(R.id.edit_Name);
        editprice=(EditText)findViewById(R.id.edit_Price);
        editcost=(EditText)findViewById(R.id.edit_Cost);
        listView = (GridView)  findViewById(R.id.listview);
        //rview=(RecyclerView)findViewById(R.id.rview);
        refreshdata();


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Product p=pList.get(position);
                if(select.equals("yes")) {
                    Intent intent = new Intent();
                    intent.putExtra("data_return", p.getPid());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    editid.setText(p.getPid()+"");
                    editname.setText(p.getName());
                    editprice.setText(p.getPrice()+"");
                    editcost.setText(p.getCost()+"");
                    byte[] d = p.getImage();
                    Bitmap b1 = BitmapFactory.decodeByteArray(d, 0, d.length);
                    img.setImageBitmap(b1);

                }
            }
        });


    }



    public void refreshdata() {
        Cursor cursor=database.rawQuery("select * from product order by pid desc", null);
        adapter=null;pList.clear();
        if (cursor.moveToFirst()) {
            do {
                Product p=new Product(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getBlob(4));
                pList.add(p);
                p=null;
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new ProductAdapter(ProductActivity.this, R.layout.product_item, pList);
        listView.setAdapter(adapter);
    }

    public void  save(View view) {
        if(TextUtils.isEmpty(editname.getText())) return;
        byte[] imgdata=getByteArrayFromImageView(img);
        ContentValues cv = new ContentValues();

        cv.put("img", imgdata);
        cv.put("name", editname.getText().toString());
        cv.put("price",String.valueOf(editprice.getText()));
        cv.put("cost",String.valueOf(editcost.getText()));

        if(TextUtils.isEmpty(editid.getText())) database.insert("product", null, cv);
        else database.update("product", cv, "pid = ?", new String[]{editid.getText().toString()});

        Toast.makeText(ProductActivity.this, "Save sucessful.", Toast.LENGTH_SHORT).show();

        refreshdata();

        //final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
    }

    public void  loadimage(View view) {

        imageChooser();

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