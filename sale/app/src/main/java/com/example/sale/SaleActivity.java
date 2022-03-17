package com.example.sale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleActivity extends AppCompatActivity {
    public saleHelper db;
    private boolean viewGroupIsVisible = true;
    public SQLiteDatabase database;
    private List<Sale> pList = new ArrayList<Sale>();
    int SELECT_PICTURE = 200;
    ImageView img;
    private View mViewGroup;
    EditText ednum,edprice,edamount,edcost;
    SaleAdapter adapter;
    ListView listView;
    int sel=0;
    TextView tvname,tvdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = new saleHelper(this);
        database = db.getWritableDatabase();
        mViewGroup = findViewById(R.id.sale);
        tvname = (TextView) findViewById(R.id.textname);
        tvdate = (TextView) findViewById(R.id.textdate);
        tvdate.setText(getdate());
        img = (ImageView) findViewById(R.id.image_product);
        ednum = (EditText) findViewById(R.id.ednum);
        edprice = (EditText) findViewById(R.id.edprice);
        edamount = (EditText) findViewById(R.id.edamount);
        edcost = (EditText) findViewById(R.id.edcost);
        listView = (ListView) findViewById(R.id.listview);
        refreshdata();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Sale fruit = pList.get(position);
                Toast.makeText(SaleActivity.this, fruit.id + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshdata() {
        Cursor c = database.rawQuery("select id,date,name,number,sale.price,amount,sale.cost,img from sale,product where sale.pid=product.pid order by id desc limit 20", null);
        //database.execSQL("insert into product (name,images,cid) values("+"'jiuju','image1.jpg'"+",1)");
        pList.clear();
        if (c.moveToFirst()) {
            do {
                Sale p = new Sale();
                p.id = c.getInt(0);
                p.date = c.getString(1);
                p.name = c.getString(2);
                p.number = c.getInt(3);
                p.price = c.getInt(4);
                p.amount = c.getInt(5);
                p.cost = c.getInt(6);
                p.image = c.getBlob(7);
                pList.add(p);
                p = null;
            } while (c.moveToNext());
        }
        c.close();


        adapter = new SaleAdapter(SaleActivity.this, R.layout.saleitem, pList);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide:
                if (viewGroupIsVisible) {
                    mViewGroup.setVisibility(View.GONE);
                } else {
                    mViewGroup.setVisibility(View.VISIBLE);
                }

                viewGroupIsVisible = !viewGroupIsVisible;
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    public String getdate() {
        SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
        return d.format(new Date());
    }

    public void  save(View view) {
        if (sel>0) {
            ContentValues cv = new ContentValues();
            cv.put("date", getdate());
            cv.put("pid", sel);
            cv.put("number", String.valueOf(ednum.getText()));
            cv.put("price", String.valueOf(edprice.getText()));
            cv.put("amount", String.valueOf(edamount.getText()));
            cv.put("cost", String.valueOf(edcost.getText()));
            database.insert("sale", null, cv);
            Toast.makeText(SaleActivity.this, "添加成功！.", Toast.LENGTH_SHORT).show();

            refreshdata();
        }
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


                    Cursor c=database.rawQuery(sql,null);
                    if(c.moveToFirst()) {
                        tvname.setText(c.getString(0)+"(id:"+sel+")");

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