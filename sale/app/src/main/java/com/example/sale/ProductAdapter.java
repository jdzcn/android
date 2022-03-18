package com.example.sale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            viewHolder.pImage = (ImageView) view.findViewById (R.id.image);
            viewHolder.pName = (TextView) view.findViewById (R.id.name);
            //viewHolder.pPrice = (TextView) view.findViewById (R.id.price);
            //viewHolder.pCost = (TextView) view.findViewById (R.id.cost);
            //viewHolder.pid = (TextView) view.findViewById (R.id.pid);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        //viewHolder.pImage.setImageResource(p.getImageId());
        viewHolder.pName.setText(p.getName()+"("+p.getPid()+")");
        //viewHolder.pid.setText("编号:"+p.getPid());
        //viewHolder.pPrice.setText("单价:"+p.getPrice());
        // viewHolder.pCost.setText("成本:"+p.getCost());

        byte[] d = p.getImage();
        Bitmap b1 = BitmapFactory.decodeByteArray(d, 0, d.length);
        viewHolder.pImage.setImageBitmap(b1);

        return view;
    }

    class ViewHolder {

        ImageView pImage;
        //TextView pid,pPrice,pCost;
        TextView pName;

    }

}
