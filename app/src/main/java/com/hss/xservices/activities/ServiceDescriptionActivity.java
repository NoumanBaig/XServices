package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hss.xservices.R;
import com.hss.xservices.utils.Prefs;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDescriptionActivity extends AppCompatActivity {

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
    @BindView(R.id.txt_serviceNotes)
    TextView txt_serviceNotes;
    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    String str_title,str_desc,str_price,str_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_description);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            str_image = getIntent().getStringExtra("image");
            str_title = getIntent().getStringExtra("title");
            str_desc = getIntent().getStringExtra("description");
            str_price = getIntent().getStringExtra("price");

            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            txt_serviceName.setText(str_title);
            txt_serviceDesc.setText(str_desc);
            txt_servicePrice.setText(str_price);
            txt_serviceNotes.setText("NIL");

            Prefs.with(ServiceDescriptionActivity.this).save("image",str_image);
            Prefs.with(ServiceDescriptionActivity.this).save("title",str_title);
            Prefs.with(ServiceDescriptionActivity.this).save("description",str_desc);
            Prefs.with(ServiceDescriptionActivity.this).save("price",str_price);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_proceed)
    public void onClick(View view){
        startActivity(new Intent(this,ProceedActivity.class)
        .putExtra("image",str_image)
        .putExtra("title",str_title));
    }
}
