package com.hss.xservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.hss.xservices.R;
import com.hss.xservices.activities.ServiceDescriptionActivity;
import com.hss.xservices.activities.ServicesActivity;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.CategoryViewholder> {

    Context mContext;
//    OnCardClickListner onCardClickListner;
    public ServicesAdapter(Context context) {
        this.mContext=context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_services,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onCardClickListner.OnCardClicked(v, position);
                mContext.startActivity(new Intent(mContext, ServiceDescriptionActivity.class));
            }
        });
        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ServiceDescriptionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;
        Button btn_buy;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            btn_buy = itemView.findViewById(R.id.btn_buy);
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
