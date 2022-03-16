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

public class SaleAdapter extends ArrayAdapter<Sale> {

    private int resourceId;

    public SaleAdapter(Context context, int textViewResourceId,
                        List<Sale> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sale p = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvdate = (TextView) view.findViewById (R.id.date);
            viewHolder.ivprod = (ImageView) view.findViewById (R.id.image);
            viewHolder.tvname = (TextView) view.findViewById (R.id.name);
            viewHolder.tvnum = (TextView) view.findViewById (R.id.number);
            viewHolder.tvprice = (TextView) view.findViewById (R.id.price);
            viewHolder.tvamount = (TextView) view.findViewById (R.id.amount);
            viewHolder.tvcost = (TextView) view.findViewById (R.id.cost);
            viewHolder.tvprofit = (TextView) view.findViewById (R.id.profit);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        //viewHolder.pImage.setImageResource(p.getImageId());
        viewHolder.tvdate.setText(p.date);
        viewHolder.tvname.setText(p.name);
        viewHolder.tvnum.setText(p.number+"");
        viewHolder.tvprice.setText("x"+p.price);
        viewHolder.tvamount.setText(""+p.amount);
        viewHolder.tvcost.setText(p.cost+"");
        viewHolder.tvprofit.setText((p.amount-p.cost)+"");
        byte[] d = p.image;
        Bitmap b1 = BitmapFactory.decodeByteArray(d, 0, d.length);
        viewHolder.ivprod.setImageBitmap(b1);

        return view;
    }

    class ViewHolder {

        ImageView ivprod;
        TextView tvid,tvdate,tvname,tvpid,tvnum,tvprice,tvamount,tvcost,tvprofit;


    }

}
