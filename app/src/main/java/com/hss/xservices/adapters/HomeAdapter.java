package com.hss.xservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CategoryViewholder> {

    Context mContext;
    public HomeAdapter(Context context) {
        this.mContext=context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_home,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
