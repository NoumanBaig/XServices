package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.hss.xservices.R;
import com.hss.xservices.adapters.HomeAdapter;
import com.hss.xservices.adapters.ServicesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ServicesActivity extends AppCompatActivity {

    @BindView(R.id.recyclerServices)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ServicesAdapter servicesAdapter = new ServicesAdapter(this);
        recyclerView.setAdapter(servicesAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
