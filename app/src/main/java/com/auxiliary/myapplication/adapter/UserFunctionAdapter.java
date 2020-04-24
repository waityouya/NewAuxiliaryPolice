package com.auxiliary.myapplication.adapter;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.auxiliary.myapplication.model.UserMenu;

import java.util.List;


public class UserFunctionAdapter extends BaseAdapter {
    private List<UserMenu> mData;
    private Context mContext;
    public UserFunctionAdapter(List<UserMenu> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView  = LayoutInflater.from(mContext).inflate(R.layout.item_function_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView =  convertView.findViewById(R.id.icon);
            viewHolder.textView =  convertView.findViewById(R.id.list_text);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setBackgroundResource(mData.get(position).getaIcon());
        viewHolder.textView.setText(mData.get(position).getText());

        return convertView;
    }
    static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
