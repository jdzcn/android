package com.example.sqliteimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class productAdapter extends BaseAdapter {
    private List<product> mList;
    private Context mContext;

    public productAdapter(List<product> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.product_item, viewGroup, false);
        if (view != null) {
            ImageView iv = (ImageView) view.findViewById(R.id.pimage);
            TextView tv= (TextView) view.findViewById(R.id.editName);
            byte[] d = mList.get(i).img;

            //String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d;
            Bitmap b1 = BitmapFactory.decodeByteArray(d, 0, d.length);
            iv.setImageBitmap(b1);

            tv.setText(mList.get(i).name);
        }
        return view;
    }
}