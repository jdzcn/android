package com.example.netapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private List<group_category> cList;
    Context c;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sname;
        LinearLayout row_linearlayout;

        public MyViewHolder(View v){
            super(v);
            sname = v.findViewById(R.id.tv_sname);
            row_linearlayout=(LinearLayout)itemView.findViewById(R.id.line);
        }

    }

    public CategoryAdapter(Context c,List<group_category> pl){
        this.cList = pl;
        this.c=c;
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        group_category p=cList.get(position);
        if(p.id.equals("0")) {
            holder.row_linearlayout.setBackgroundColor(Color.parseColor("#009688"));

            holder.sname.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.sname.setText(p.name);
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        CategoryAdapter.MyViewHolder holder=new CategoryAdapter.MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                group_category p=cList.get(position);
                //openWebPage("http://172.96.193.223/images/"+p.images);
                //Toast.makeText(view.getContext(),"you clicked view:"+p.sname+"(id:"+p.id+")",Toast.LENGTH_LONG).show();

                 if(p.id.equals("-1")) ((MainActivity) c).curview="";
                else if(!p.id.equals("0")) ((MainActivity) c).curview="?cid=" + p.id;
                ((MainActivity) c).sendRequestWithHttpURLConnection(((MainActivity) c).curview);
                ((MainActivity) c).mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        return holder;
    }
}
