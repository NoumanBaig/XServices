package com.hss.xservices.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hss.xservices.R;
import com.hss.xservices.activities.ServicesActivity;
import com.hss.xservices.adapters.EarnAdapter;
import com.hss.xservices.adapters.ServicesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarnFragment extends Fragment {

    @BindView(R.id.recyclerEarn)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.earn_fragment,container,false);
        ButterKnife.bind(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EarnAdapter earnAdapter = new EarnAdapter(getActivity());
        recyclerView.setAdapter(earnAdapter);

        return view;
    }
}
