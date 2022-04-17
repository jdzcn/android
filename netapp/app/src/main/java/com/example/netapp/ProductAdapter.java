package com.example.netapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private int resourceId;

    public ProductAdapter(Context context, int textViewResourceId,
                          List<Product> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product p = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.pImage = (ImageView) view.findViewById (R.id.pimage);
            viewHolder.pName = (TextView) view.findViewById (R.id.product_name);
            //viewHolder.pPrice = (TextView) view.findViewById (R.id.price);
            //viewHolder.pCost = (TextView) view.findViewById (R.id.cost);
            //viewHolder.pid = (TextView) view.findViewById (R.id.pid);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        //viewHolder.pImage.setImageResource(p.getImageId());
        viewHolder.pName.setText(p.name);
        //viewHolder.pid.setText("编号:"+p.getPid());
        //viewHolder.pPrice.setText("单价:"+p.getPrice());
        // viewHolder.pCost.setText("成本:"+p.getCost());

        Picasso.get().load(MainActivity.SERVER+"thumbnail/"+p.images).placeholder(R.drawable.ic_launcher).into(viewHolder.pImage);

        return view;
    }

    class ViewHolder {

        ImageView pImage;
        //TextView pid,pPrice,pCost;
        TextView pName;

    }

}
