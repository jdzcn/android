package com.example.sale;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class findAdapter extends ArrayAdapter<find> {

    private int resourceId;

    public findAdapter(Context context, int textViewResourceId,
                          List<find> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        find p = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.one = (TextView) view.findViewById (R.id.one);
            viewHolder.two = (TextView) view.findViewById (R.id.two);
            viewHolder.three = (TextView) view.findViewById (R.id.three);
            //viewHolder.pPrice = (TextView) view.findViewById (R.id.price);
            //viewHolder.pCost = (TextView) view.findViewById (R.id.cost);
            //viewHolder.pid = (TextView) view.findViewById (R.id.pid);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        //viewHolder.pImage.setImageResource(p.getImageId());
        viewHolder.one.setText(p.one);
        viewHolder.two.setText(p.two+"");
        viewHolder.three.setText(p.three+"");
        //viewHolder.pid.setText("编号:"+p.getPid());
        //viewHolder.pPrice.setText("单价:"+p.getPrice());
        // viewHolder.pCost.setText("成本:"+p.getCost());
        return view;
    }

    class ViewHolder {

        TextView one,two,three;

    }

}
