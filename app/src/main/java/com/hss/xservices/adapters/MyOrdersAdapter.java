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

import java.util.ArrayList;

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

        holder.txt_booked_on.setText(""+arr_date.get(position));
        holder.txt_order_no.setText(""+arr_ordeNo.get(position));
        holder.txt_status.setText(""+arr_status.get(position));

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
}
