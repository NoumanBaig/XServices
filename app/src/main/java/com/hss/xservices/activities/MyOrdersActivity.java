package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hss.xservices.R;
import com.hss.xservices.adapters.MyOrdersAdapter;
import com.hss.xservices.adapters.ServicesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.recyclerMyOrders)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_orders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(this);
        recyclerView.setAdapter(myOrdersAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
