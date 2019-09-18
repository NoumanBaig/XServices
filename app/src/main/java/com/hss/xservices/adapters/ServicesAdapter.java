package com.hss.xservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.hss.xservices.R;
import com.hss.xservices.activities.ServiceDescriptionActivity;
import com.hss.xservices.activities.ServicesActivity;
import com.hss.xservices.models.ServicesCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.CategoryViewholder> {

    Context mContext;
    List<ServicesCategory> categoryList;
//    OnCardClickListner onCardClickListner;
    public ServicesAdapter(Context context,List<ServicesCategory> categoryList) {
        this.mContext=context;
        this.categoryList=categoryList;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_services,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

        holder.txt_name.setText(""+categoryList.get(position).getSvcTitle());
        String description = categoryList.get(position).getSvcDescription();
        if(!description.equals("null")){
            holder.txt_desc.setText(""+description);
        }

//        if (categoryList.get(position).getPhotos().get(position).getPhotoFileName())
        try {
            String image_url = "http://3.83.243.193:3000/files/"+categoryList.get(position).getPhotos().get(0).getPhotoFileName();
            Log.e("image_url",image_url);
            Picasso.get().load(image_url).error(R.drawable.service).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txt_price.setText("$"+categoryList.get(position).getHourRate());

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
        return categoryList.size();
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;
        Button btn_buy;
        ImageView imageView;
        TextView txt_name,txt_desc,txt_price;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            btn_buy = itemView.findViewById(R.id.btn_buy);
            imageView = itemView.findViewById(R.id.img);
            txt_name = itemView.findViewById(R.id.txt_heading);
            txt_desc = itemView.findViewById(R.id.txt_subHeading);
            txt_price = itemView.findViewById(R.id.txt_price);
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
