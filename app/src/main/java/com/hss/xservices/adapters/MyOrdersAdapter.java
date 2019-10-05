package com.hss.xservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;
import com.hss.xservices.activities.ServiceDescriptionActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.CategoryViewholder> {

    Context mContext;
    ArrayList<String> arr_ordeNo,arr_status,arr_date;
//    OnCardClickListner onCardClickListner;
    public MyOrdersAdapter(Context context,ArrayList<String> arr_ordeNo,ArrayList<String> arr_status,ArrayList<String> arr_date) {
        this.mContext=context;
        this.arr_ordeNo=arr_ordeNo;
        this.arr_status=arr_status;
        this.arr_date=arr_date;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_my_orders,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        String timeDate = parseTime(arr_date.get(position));
        holder.txt_booked_on.setText(""+timeDate);
        holder.txt_order_no.setText(""+arr_ordeNo.get(position));
        if (arr_status.get(position).equalsIgnoreCase("1")){
            holder.txt_status.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            holder.txt_status.setText("Processing");
        }else {
            holder.txt_status.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.txt_status.setText("In Process");
        }


    }

    @Override
    public int getItemCount() {
        return arr_ordeNo.size();
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txt_order_no,txt_booked_on,txt_status;


        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            txt_order_no = itemView.findViewById(R.id.txt_order_no);
            txt_booked_on = itemView.findViewById(R.id.txt_booked_on);
            txt_status = itemView.findViewById(R.id.txt_status);

        }
    }

//    public interface OnCardClickListner {
//        void OnCardClicked(View view, int position);
//    }
//
//    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
//        this.onCardClickListner = onCardClickListner;
//    }

    public String parseTime(String time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return str;
    }

}
