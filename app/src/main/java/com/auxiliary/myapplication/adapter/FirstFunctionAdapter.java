package com.auxiliary.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myapplication.R;

import com.auxiliary.myapplication.model.UserMenu;
import com.auxiliary.myapplication.util.OnRecyclerViewClickListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class FirstFunctionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<UserMenu> list;

    private OnRecyclerViewClickListener listener;


    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public void update(ArrayList<UserMenu> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public FirstFunctionAdapter(ArrayList<UserMenu> list) {
        this.list = list;
    }

    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

         View   view = LayoutInflater.from(mContext).inflate(R.layout.item_first_function, parent, false);
         if(listener!=null){
             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     listener.onItemClickListener(v);
                 }
             });
         }
        return new ViewHolder(view);


    }
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof  ViewHolder){
            ViewHolder ViewHolder1 = (ViewHolder) viewHolder;
            UserMenu menu = list.get(position);
            ViewHolder1.imageView.setBackgroundResource(menu.getaIcon());
            ViewHolder1.textView.setText(menu.getText());
        }


    }




    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;


        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_item_function);
            imageView = view.findViewById(R.id.iv_item_function);




        }

    }



}
