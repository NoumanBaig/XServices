package com.hss.xservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;
import com.hss.xservices.models.Addresses;
import com.hss.xservices.models.Profile;
import com.hss.xservices.utils.Prefs;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.CategoryViewholder> {

    Context mContext;
    List<Addresses> addressesArrayList;
//    OnCardClickListner onCardClickListner;
    public AddressAdapter(Context context,List<Addresses> addressesArrayList) {
        this.mContext=context;
        this.addressesArrayList=addressesArrayList;
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

        holder.txt_address.setText(""+addressesArrayList.get(position).getAddress1() + addressesArrayList.get(position).getAddress2());
        holder.txt_name.setText(Prefs.with(mContext).getString("adds_name",""));
        holder.txt_mobile.setText(Prefs.with(mContext).getString("adds_mobile",""));
        holder.textView.setText(Prefs.with(mContext).getString("adds_type",""));


    }

    @Override
    public int getItemCount() {
        return addressesArrayList.size();
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView,txt_name,txt_mobile,txt_address;
        CheckBox checkBox;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            textView = itemView.findViewById(R.id.txt);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            txt_address = itemView.findViewById(R.id.txt_address);
            checkBox = itemView.findViewById(R.id.checkbox);
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
