package com.example.netapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private List<Product> pList;
    Context c;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView imgView;

        public MyViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.product_name);
            imgView=v.findViewById(R.id.pimage);
        }

    }

    public RecyclerViewAdapter(Context c,List<Product> pl){
        this.pList = pl;
        this.c=c;
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Product p=pList.get(position);
        //String i=p.id;
        String t = p.name;
        String d = p.images;
        holder.name.setText(t);
        //String path=common.getDownloadDir()+"thumb/"+d;
        //holder.imgView.setImageBitmap(BitmapFactory.decodeFile(path));
        Picasso.get().load(MainActivity.SERVER+"thumbnail/"+d).placeholder(R.drawable.ic_launcher).into(holder.imgView);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDownload(c,p.images);
                Toast.makeText(view.getContext(),"save image to "+common.getDownloadDir() + p.images,Toast.LENGTH_LONG).show();
            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);

                Intent intent = new Intent(c, ViewActivity.class);
                intent.putExtra("product", p);
                ((MainActivity)c).startActivity(intent);
            }
        });

        //holder.images.setText(d);
        //File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d);
        //holder.imgView.setImageURI(Uri.fromFile(file));

        //holder.imgView.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        MyViewHolder holder=new MyViewHolder(itemView);
        /*
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);
                Intent intent = new Intent(c, ViewActivity.class);
                intent.putExtra("product", p);
                ((MainActivity)c).startActivity(intent);

            }
        });
        */
        /*
        holder.btnurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);
                openWebPage("http://172.96.193.223/images/"+p.images);
            }
        });
        holder.btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);
                //copyfile((Environment.getDataDirectory().getAbsolutePath() + "/data/" + this.getPackageName() + "/databases/sale.db"),(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/sale.db"),true);
                String f=common.getDownloadDir()+p.images;
                //String f=common.getDownloadDir() + "sale.db";
                File file=new File(f);
                Uri uri=Uri.fromFile(file);
                composeEmail(new String[]{"jdzcn@qq.com"},p.name,uri);
            }
        });
        holder.btnnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);
                File file=new File(common.getDownloadDir() + p.images);
                Uri uri=Uri.fromFile(file);
                send(new String[]{"jdzcn@qq.com"},p.name,uri);

            }
        });

         */
        return holder;
    }

    //save image
    public static void imageDownload(Context ctx, String url){
        Picasso.get().load(MainActivity.SERVER+"images/"+url)
                .into(getTarget(url));
    }

    //target to save
    private static Target getTarget(final String url){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(common.getDownloadDir() + url);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }



            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }
    /*
    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            c.startActivity(intent);
        }
    }


    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            Intent i=Intent.createChooser(intent,"Select");
            c.startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        //intent.setType("/*");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            c.startActivity(intent);
        }
    }
    public void send(String[] addresses, String subject, Uri attachment) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("/*");
        //intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,subject);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            c.startActivity(intent);
        }
    }
*/
}
