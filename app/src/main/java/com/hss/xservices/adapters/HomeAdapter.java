package com.hss.xservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;
import com.hss.xservices.activities.ServicesActivity;
import com.hss.xservices.models.Services;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CategoryViewholder> {

    Context mContext;
    ArrayList<Services> services;
//    OnCardClickListner onCardClickListner;
    public HomeAdapter(Context context,ArrayList<Services> services) {
        this.mContext=context;
        this.services=services;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_home,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {

        holder.txt_heading.setText(""+services.get(position).getDispText());
        String description = services.get(position).getDescription();
        if(!description.equals("null")){
            holder.txt_subHeading.setText(""+description);
        }

        String image_url = "http://3.83.243.193:3000/files/"+services.get(position).getImage();
        Log.e("image_url",image_url);
            Picasso.get().load(image_url).error(R.drawable.service).into(holder.imageView);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onCardClickListner.OnCardClicked(v, position);
                String code = services.get(position).getCode().toString();
                String code_text = services.get(position).getCodeText();
                String image = services.get(position).getImage();
                String title = services.get(position).getDispText();
                mContext.startActivity(new Intent(mContext, ServicesActivity.class).putExtra("code",code)
                .putExtra("code_text",code_text).putExtra("image",image).putExtra("title",title));
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class CategoryViewholder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView txt_heading,txt_subHeading;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            imageView = itemView.findViewById(R.id.img);
            txt_heading = itemView.findViewById(R.id.txt_heading);
            txt_subHeading = itemView.findViewById(R.id.txt_subHeading);
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
