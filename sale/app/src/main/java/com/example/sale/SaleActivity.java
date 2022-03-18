package com.example.sale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    int sel=0,prodcost;
    TextView tvname,tvdate,tvmonth,tvtoday;
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
        tvmonth = (TextView) findViewById(R.id.tvMonth);
        tvtoday = (TextView) findViewById(R.id.tvToday);
        tvdate.setText(getdate());
        img = (ImageView) findViewById(R.id.image_product);
        ednum = (EditText) findViewById(R.id.ednum);
        edprice = (EditText) findViewById(R.id.edprice);
        edamount = (EditText) findViewById(R.id.edamount);
        edcost = (EditText) findViewById(R.id.edcost);
        listView = (ListView) findViewById(R.id.listview);
        refreshdata();
        ednum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(ednum.getText())||TextUtils.isEmpty(edprice.getText())) return;
                int q = Integer.parseInt(ednum.getText().toString());
                int p=Integer.parseInt(edprice.getText().toString());
                //int c=Integer.parseInt(ed_cost.getText().toString());
                edamount.setText(q*p+"");
                edcost.setText(q*prodcost+"");
            }
        });

        edprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(ednum.getText())||TextUtils.isEmpty(edprice.getText())) return;
                int q = Integer.parseInt(ednum.getText().toString());
                int p=Integer.parseInt(edprice.getText().toString());
                edamount.setText(q*p+"");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String str="DELETE FROM sale where id="+pList.get(i).id;
                //Toast.makeText(MainActivity.this,"你选择了"+tv.getText().toString(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SaleActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("警告");
                builder.setMessage("确定要删除这条记录吗?\n"+str);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定按钮之后的回调

                        database.execSQL(str);
                        Toast.makeText(SaleActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        refreshdata();
                    }
                });


                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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
        String sql="select sum(amount-cost) as profit from sale where date='"+getdate()+"'";
        Cursor s=database.rawQuery(sql,null);
        //Log.d("sale",s.getCount()+sql);
        //Toast.makeText(SaleActivity.this, s.getCount()+sql, Toast.LENGTH_SHORT).show();
        if(s.moveToFirst()) tvtoday.setText(s.getInt(0)+"");
        s.close();
        sql="select sum(amount-cost) as profit from sale where substr(date,1,7)='"+getdate().substring(0,7)+"'";
        s=database.rawQuery(sql,null);
        //Log.d("sale",s.getCount()+sql);
        //Toast.makeText(SaleActivity.this, s.getCount()+sql, Toast.LENGTH_SHORT).show();
        if(s.moveToFirst()) tvmonth.setText(s.getInt(0)+"");
        s.close();

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
            case R.id.m_sale:
                if (viewGroupIsVisible) {
                    mViewGroup.setVisibility(View.GONE);
                } else {
                    mViewGroup.setVisibility(View.VISIBLE);
                }

                viewGroupIsVisible = !viewGroupIsVisible;
                return true;
            case R.id.m_export:

                copyfile((Environment.getDataDirectory().getAbsolutePath() + "/data/" + this.getPackageName() + "/databases/sale.db"),(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/sale.db"),true);

                return true;
            case R.id.m_import:

                copyfile((Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/sale.db"),(Environment.getDataDirectory().getAbsolutePath() + "/data/" +getPackageName() + "/databases/sale.db"),false);


                return true;
            case R.id.m_item:
                Intent intent = new Intent(SaleActivity.this, ProductActivity.class);
                intent.putExtra("select","no");
                startActivityForResult(intent,1);
                return true;
            case R.id.m_share:
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
            //Toast.makeText(SaleActivity.this, "添加成功！.", Toast.LENGTH_SHORT).show();

            refreshdata();
        }
    }

    public void  loadimage(View view) {

        Intent intent = new Intent(SaleActivity.this, ProductActivity.class);
        intent.putExtra("select","yes");
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
                        prodcost=c.getInt(2);
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

    public void copyfile(final String inputstr,final String outputstr,final boolean issend)	{
        AlertDialog.Builder builder = new AlertDialog.Builder(SaleActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("详情");
        builder.setMessage("源文件:"+inputstr+"\n目标文件:"+outputstr+"\n确定执行操作吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确定按钮之后的回调
                File dbFile = new File(inputstr);
                if(!dbFile.exists())	{
                    Toast.makeText(SaleActivity.this, "文件不存在!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    //文件复制到sd卡中
                    fis = new FileInputStream(dbFile);

                    fos = new FileOutputStream(outputstr);
                    int len = 0;
                    byte[] buffer = new byte[2048];
                    while (-1 != (len = fis.read(buffer))) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();

                    Toast.makeText(SaleActivity.this, issend?"备份成功!":"恢复成功!", Toast.LENGTH_SHORT).show();
                    if (issend) {}
                    else {
                        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) fos.close();
                        if (fis != null) fis.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消按钮之后的回调
            }
        });

        AlertDialog dialog1 = builder.create();
        dialog1.show();

    }
}