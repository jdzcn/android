package com.example.netapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private List<Product> pList;
    Context c;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button btnurl,btnmail,btnnote;
        ImageView imgView;

        public MyViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.product_name);
            btnurl=v.findViewById(R.id.url);
            imgView=v.findViewById(R.id.pimage);
            btnmail=v.findViewById(R.id.mail);
            btnnote=v.findViewById(R.id.note);
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
        String path=common.getDownloadDir()+"thumb/"+d;
        //holder.imgView.setImageBitmap(BitmapFactory.decodeFile(path));
        Picasso.get().load(MainActivity.SERVER+"thumbnail/"+d).placeholder(R.drawable.ic_launcher).into(holder.imgView);
        //holder.images.setText(d);
        //File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d);
        //holder.imgView.setImageURI(Uri.fromFile(file));

        //holder.imgView.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        MyViewHolder holder=new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Product p=pList.get(position);
                //openWebPage("http://172.96.193.223/images/"+p.images);
                Toast.makeText(view.getContext(),"you clicked view:"+p.name+"(id:"+p.id+")",Toast.LENGTH_LONG).show();
            }
        });
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
        return holder;
    }
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
        //intent.setType("*/*");
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
        intent.setType("*/*");
        //intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,subject);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            c.startActivity(intent);
        }
    }

}
