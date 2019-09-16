package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hss.xservices.R;
import com.hss.xservices.adapters.AddressAdapter;
import com.hss.xservices.adapters.ServicesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProceedActivity extends AppCompatActivity {

    @BindView(R.id.recyclerAddress)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preceed);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter addressAdapter = new AddressAdapter(this);
        recyclerView.setAdapter(addressAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.btn_schedule, R.id.btn_addAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addAddress:
                break;
            case R.id.btn_schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            default:
                break;

        }
    }
}