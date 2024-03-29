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
import com.hss.xservices.adapters.EarnAdapter;
import com.hss.xservices.adapters.HistoryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment {

    @BindView(R.id.recyclerHistory)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        ButterKnife.bind(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity());
        recyclerView.setAdapter(historyAdapter);
        return view;
    }
}
