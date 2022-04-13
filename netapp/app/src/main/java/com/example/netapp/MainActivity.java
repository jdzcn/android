package com.example.netapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static String SERVER;
    public DrawerLayout mDrawerLayout;
    List<group_category> glist=new ArrayList<>();
    List<Category> clist;
    RecyclerViewAdapter adapter;
    RecyclerView rview,cview;
    CategoryAdapter cadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You click floatingbutton.", Toast.LENGTH_SHORT).show();
            }
        });

        rview=(RecyclerView)findViewById(R.id.rview);
        //registerForContextMenu(rview);
        cview=(RecyclerView)findViewById(R.id.cview);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"you click"+clist.get(i).id,Toast.LENGTH_SHORT).show();
                sendRequestWithHttpURLConnection("?cid="+clist.get(i).id);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        */
        LinearLayoutManager categorylayout=new LinearLayoutManager(this);
        cview.setLayoutManager(categorylayout);
        cview.addItemDecoration(new DividerItemDecoration(rview.getContext(), DividerItemDecoration.VERTICAL));

        GridLayoutManager layoutManager=new GridLayoutManager(this,2 );
        //layoutManager.setOrientation(layoutManager.HORIZONTAL);
        rview.setLayoutManager(layoutManager);

        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        SERVER=sp.getString("server","http://172.96.193.223/");

        get_category();
        sendRequestWithHttpURLConnection("");

    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.m_delete:

                return true;
            case R.id.m_open:
                return true;
            case R.id.m_download:
                return true;
                default:
                return super.onContextItemSelected(item);
        }
    }
*/


    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("data", 0).edit();
        editor.putString("server",SERVER);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.m_add:

                break;
            case R.id.m_search:
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(MainActivity.this);
                inputDialog.setTitle("输入关键字").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String t=editText.getText().toString();
                                if(t.length()>0) sendRequestWithHttpURLConnection("?key="+t);
                            }
                        }).show();
                break;
            case R.id.m_server:
                final EditText etServer = new EditText(MainActivity.this);
                etServer.setText(SERVER);
                AlertDialog.Builder sDialog =
                        new AlertDialog.Builder(MainActivity.this);
                sDialog.setTitle("服务器地址").setView(etServer);
                sDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SERVER=etServer.getText().toString();
                            }
                        }).show();
                break;
            default:
        }
        return true;
    }



    public void sendRequestWithHttpURLConnection(String str ) {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(SERVER+"product.php"+str);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                    //parseJSONWithGSON(response.toString());
                    //parseProduct(response.toString());
                    //parseXMLWithPull(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void get_category() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url(SERVER+"category.php")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //parseJSONWithGSON(responseData);
//                    parseJSONWithJSONObject(responseData);
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
                    show_category(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void show_category(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                Gson gson = new Gson();
                clist = gson.fromJson(response, new TypeToken<List<Category>>() {}.getType());

                int l=clist.size();
                String cname="";
                group_category a=new group_category();
                a.name="首页";
                a.id=-1;
                glist.add(a);
                for(int i=0;i<l;i++) {
                    Category c=clist.get(i);
                    group_category item=new group_category();
                    group_category group=new group_category();
                    if(!c.cname.equals(cname)) {
                        group.name=c.cname+"类";
                        group.id=0;
                        glist.add(group);
                    }
                    item.name=c.sname;
                    item.id=c.id;
                    glist.add(item);
                    cname=c.cname;
                }

                //创建ArrayAdapter
                //cadapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,strs);

                cadapter=new CategoryAdapter(MainActivity.this,glist);
                cview.setAdapter(cadapter);
            }
        });
    }
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                Gson gson = new Gson();
                List<Product> plist = gson.fromJson(response, new TypeToken<List<Product>>() {}.getType());
                adapter = new RecyclerViewAdapter(MainActivity.this,plist);


                rview.setAdapter(adapter);
            }
        });
    }

    /*save image
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }
*/


}