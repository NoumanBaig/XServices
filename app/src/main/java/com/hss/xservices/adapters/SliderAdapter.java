package com.hss.xservices.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.hss.xservices.R;
import com.hss.xservices.activities.ServiceDescriptionActivity;
import com.hss.xservices.models.Services;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private int mCount;
    ArrayList<Services> services;
    ArrayList<String> arrayList;
    String string = "";

    public SliderAdapter(Context context,ArrayList<Services> services) {
        this.context = context;
        this.services=services;
    }
    public SliderAdapter(Context context,ArrayList<String> arrayList,String string) {
        this.context = context;
        this.arrayList=arrayList;
        this.string=string;
    }

//    public void setCount(int count) {
//        this.mCount = count;
//    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                if (string.equalsIgnoreCase("true")){
                    String image_url = "http://3.83.243.193:3000/files/"+arrayList.get(position);
                   showDialog(image_url);

                }else {
                    String image_url = "http://3.83.243.193:3000/files/"+services.get(position).getImage();
                    showDialog(image_url);
                }
            }
        });

        if (string.equalsIgnoreCase("true")){
            String image_url = "http://3.83.243.193:3000/files/"+arrayList.get(position);
            Picasso.get().load(image_url).error(R.drawable.service).into(viewHolder.imageViewBackground);

        }else {
            String image_url = "http://3.83.243.193:3000/files/"+services.get(position).getImage();
            Picasso.get().load(image_url).error(R.drawable.service).into(viewHolder.imageViewBackground);
        }


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        if (string.equalsIgnoreCase("true")){
            return arrayList.size();
        }else {
            return services.size();
        }

    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }

    private void showDialog(String image){
        final Dialog alertDialog=new Dialog(context,android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.view_dialog);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView = alertDialog.findViewById(R.id.img);

            Picasso.get().load(image).error(R.drawable.service).into(imageView);

        ImageView imageView_close = alertDialog.findViewById(R.id.img_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
