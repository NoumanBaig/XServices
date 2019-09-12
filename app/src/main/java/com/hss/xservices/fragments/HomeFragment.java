package com.hss.xservices.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;
import com.hss.xservices.activities.MainActivity;
import com.hss.xservices.activities.SearchActivity;
import com.hss.xservices.adapters.BestSellersAdapter;
import com.hss.xservices.adapters.CategoryAdapter;
import com.hss.xservices.adapters.WeeklyAdapter;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    @BindView(R.id.txt_search)
    TextView txt_search;
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerCategory;
    @BindView(R.id.recyclerWeekly)
    RecyclerView recyclerWeekly;
    @BindView(R.id.recyclerSellers)
    RecyclerView recyclerSellers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        ButterKnife.bind(this,view);

        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerCategory.setLayoutManager(layoutManager);
        recyclerWeekly.setLayoutManager(layoutManager2);
        recyclerSellers.setLayoutManager(layoutManager3);
        recyclerCategory.setAdapter(new CategoryAdapter(getActivity()));
        recyclerWeekly.setAdapter(new WeeklyAdapter(getActivity()));
        recyclerSellers.setAdapter(new BestSellersAdapter(getActivity()));
        return view;
    }

}
