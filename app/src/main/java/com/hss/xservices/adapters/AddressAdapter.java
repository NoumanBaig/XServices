package com.hss.xservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.CategoryViewholder> {

    Context mContext;
//    OnCardClickListner onCardClickListner;
    public AddressAdapter(Context context) {
        this.mContext=context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_address,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onCardClickListner.OnCardClicked(v, position);
              //  mContext.startActivity(new Intent(mContext, ServicesActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
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
