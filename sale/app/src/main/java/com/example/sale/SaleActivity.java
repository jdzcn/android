package com.example.sale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity {
    public saleHelper db;
    private boolean viewGroupIsVisible = true;
    public SQLiteDatabase database;
    private List<Product> pList = new ArrayList<Product>();
    int SELECT_PICTURE = 200;
    ImageView img;
    private View mViewGroup;
    EditText ednum,edprice,edamount,edcost;
    ProductAdapter adapter;
    ListView listView;
    int sel=0;
    TextView textname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        db=new saleHelper(this);
        database=db.getWritableDatabase();
        mViewGroup = findViewById(R.id.sale);
        textname=(TextView)findViewById(R.id.textname);
        img=(ImageView)findViewById(R.id.image_product);
        ednum=(EditText)findViewById(R.id.ednum);
        edprice=(EditText)findViewById(R.id.edprice);
        edamount=(EditText)findViewById(R.id.edamount);
        edcost=(EditText)findViewById(R.id.edcost);

        Cursor cursor=database.rawQuery("select * from product", null);
        //database.execSQL("insert into product (name,images,cid) values("+"'jiuju','image1.jpg'"+",1)");

        if (cursor.moveToFirst()) {
            do {
                Product p=new Product(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getBlob(4));
                pList.add(p);
                p=null;
            } while (cursor.moveToNext());
        }
        cursor.close();


        adapter = new ProductAdapter(SaleActivity.this, R.layout.product_item, pList);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Product fruit = pList.get(position);
                Toast.makeText(SaleActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewGroupIsVisible) {
                    mViewGroup.setVisibility(View.GONE);
                } else {
                    mViewGroup.setVisibility(View.VISIBLE);
                }

                viewGroupIsVisible = !viewGroupIsVisible;

            }
        });
    }


    public void  save(View view) {
        byte[] imgdata=getByteArrayFromImageView(img);
        ContentValues cv = new ContentValues();

        cv.put("img", imgdata);
        //cv.put("name", editname.getText().toString());
        //cv.put("price",String.valueOf(editprice.getText()));
        //cv.put("cost",String.valueOf(editcost.getText()));
        database.insert("product", null, cv);
        Toast.makeText(SaleActivity.this, "Save sucessful.", Toast.LENGTH_SHORT).show();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void  loadimage(View view) {

        Intent intent = new Intent(SaleActivity.this, ProductActivity.class);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    sel=data.getIntExtra("data_return",0);
                    String sql="select name,price,cost,img from product where pid="+sel;
                    textname.setText(sql);

                    Cursor c=database.rawQuery(sql,null);
                    if(c.moveToFirst()) {
                        textname.setText(c.getString(0)+"(id:"+sel+")");

                        ednum.setText(c.getCount() + "");
                        edprice.setText(c.getInt(1) + "");
                        edamount.setText(c.getInt(1)+"");
                        edcost.setText(c.getInt(2)+"");
                        byte[] d = c.getBlob(3);
                        Bitmap b1 = BitmapFactory.decodeByteArray(d, 0, d.length);
                        img.setImageBitmap(b1);
                    }
                }
                break;
            default:
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