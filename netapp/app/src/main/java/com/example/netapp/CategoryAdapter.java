package com.example.netapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private List<Category> cList;
    Context c;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sname;


        public MyViewHolder(View v){
            super(v);
            sname = v.findViewById(R.id.tv_sname);

        }

    }

    public CategoryAdapter(Context c,List<Category> pl){
        this.cList = pl;
        this.c=c;
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        Category p=cList.get(position);
        holder.sname.setText(p.sname);
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        CategoryAdapter.MyViewHolder holder=new CategoryAdapter.MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Category p=cList.get(position);
                //openWebPage("http://172.96.193.223/images/"+p.images);
                //Toast.makeText(view.getContext(),"you clicked view:"+p.sname+"(id:"+p.id+")",Toast.LENGTH_LONG).show();
                ((MainActivity)c).sendRequestWithHttpURLConnection("?cid="+p.id);
                ((MainActivity)c).mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        return holder;
    }
}
