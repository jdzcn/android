package com.example.product;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private List<product> pList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,images;
        ImageView imgView;

        public MyViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.product_name);
            images = v.findViewById(R.id.product_images);
            imgView=v.findViewById(R.id.pimage);

        }

    }

    public RecyclerViewAdapter(List<product> pl){
        this.pList = pl;
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        product p=pList.get(position);

        String t = p.name;
        String d = p.images;
        holder.name.setText(t);
        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d;

        holder.imgView.setImageBitmap(BitmapFactory.decodeFile("/mnt/sdcard/product/thumb/"+d));
        holder.images.setText(d);
        //File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d);
        //holder.imgView.setImageURI(Uri.fromFile(file));

        //holder.imgView.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new MyViewHolder(itemView);
    }
}
