package com.auxiliary.myapplication.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.auxiliary.myapplication.model.AuxiliryCaseDetail;
import com.example.myapplication.R;


import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AuxiliaryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private ArrayList<AuxiliryCaseDetail> list;
    public final int TYPE_EMPTY = 0;
    public final int TYPE_NORMAL = 1;



    public void update(ArrayList<AuxiliryCaseDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public AuxiliaryDetailAdapter(ArrayList<AuxiliryCaseDetail> list) {
        this.list = list;
    }

    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view;
        if(viewType == TYPE_EMPTY){
            view = LayoutInflater.from(mContext).inflate(R.layout.empty_flag_layout, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_auxiliry_detail_case, parent, false);
            return new ViewHolder(view);
        }

    }
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ViewHolder) {
            ViewHolder ViewHolder1 = (ViewHolder) viewHolder;
            AuxiliryCaseDetail case1 = list.get(position);
            ViewHolder1.tv_tag.setText(case1.getTag());
            ViewHolder1.tv_tag_detail.setText(case1.getTagDetail());
            ViewHolder1.imageView.setBackgroundResource(case1.getIcon());

        }


    }

    @Override
    public int getItemViewType(int position) {
        if(position == 5||position == 11){
            return TYPE_EMPTY;
        }
        return  TYPE_NORMAL;
    }


    public int getItemCount() {

        return  list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tag;
        TextView tv_tag_detail;

        ImageView imageView;


        ViewHolder(View view) {
            super(view);
            tv_tag = view.findViewById(R.id.tv_tag);
            tv_tag_detail = view.findViewById(R.id.tv_detail);

            imageView = view.findViewById(R.id.icon_detail);


        }

    }
}
