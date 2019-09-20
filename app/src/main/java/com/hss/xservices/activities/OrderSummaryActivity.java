package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hss.xservices.R;
import com.hss.xservices.utils.Prefs;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_serviceName)
    TextView txt_serviceName;
    @BindView(R.id.txt_serviceDesc)
    TextView txt_serviceDesc;
    @BindView(R.id.txt_servicePrice)
    TextView txt_servicePrice;
    @BindView(R.id.txt_serviceDate)
    TextView txt_serviceDate;
    String str_title,str_desc,str_price,str_image,str_date,str_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            str_date = getIntent().getStringExtra("date");
            str_time = getIntent().getStringExtra("time");

            str_image = Prefs.with(OrderSummaryActivity.this).getString("image","");
            str_title = Prefs.with(OrderSummaryActivity.this).getString("title","");
            str_desc = Prefs.with(OrderSummaryActivity.this).getString("description","");
            str_price = Prefs.with(OrderSummaryActivity.this).getString("price","");

            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            txt_serviceName.setText(str_title);
            txt_serviceDesc.setText(str_desc);
            txt_servicePrice.setText(str_price);
            txt_serviceDate.setText(str_date+" "+str_time);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_confirmOrder)
    public void onConfirmClick(View view){
        startActivity(new Intent(this,MyOrdersActivity.class));
    }
}