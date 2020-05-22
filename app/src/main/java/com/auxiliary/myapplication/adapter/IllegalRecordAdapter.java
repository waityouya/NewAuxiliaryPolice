package com.auxiliary.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.auxiliary.myapplication.model.Case;
import com.auxiliary.myapplication.ui.activitys.IllegalRecordDetailActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class IllegalRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;

    private ArrayList<Case> list;




    public void update(ArrayList<Case> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public IllegalRecordAdapter(ArrayList<Case> list) {
        this.list = list;
    }

    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);


    }
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof  ViewHolder){
            ViewHolder ViewHolder1 = (ViewHolder) viewHolder;
            final Case aCase = list.get(position);
            ViewHolder1.tv_name.setText(aCase.getOffName());
            ViewHolder1.car_number.setText( aCase.getOffPlateNumber());
            if(aCase.getAuditType() == 0){
                ViewHolder1.tv_record_aditu.setText( "未审核");
            }else if(aCase.getAuditType() == 1){
                ViewHolder1.tv_record_aditu.setText( "审核通过");
            }else {
                ViewHolder1.tv_record_aditu.setText( "审核未通过");
            }
            ViewHolder1.tv_record_time.setText( aCase.getOffTime());
            ViewHolder1.imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, IllegalRecordDetailActivity.class);
                    intent.putExtra("auxiliaryCaseId",aCase.getAuxiliaryCaseId());
                    mContext.startActivity(intent);
                }
            });
        }


    }


    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMore;
        TextView car_number;
        TextView tv_name;
        TextView tv_record_aditu;
        TextView tv_record_time;


        ViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_record_name_item);
            car_number = view.findViewById(R.id.tv_record_car_number_item);
            tv_record_aditu = view.findViewById(R.id.aduit);
            tv_record_time = view.findViewById(R.id.tv_record_time_item);
            imageViewMore = view.findViewById(R.id.iv_more);


        }

    }
}
