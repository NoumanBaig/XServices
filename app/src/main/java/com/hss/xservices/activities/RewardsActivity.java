package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hss.xservices.R;
import com.hss.xservices.fragments.EarnFragment;
import com.hss.xservices.fragments.HistoryFragment;
import com.hss.xservices.fragments.HomeFragment;
import com.hss.xservices.fragments.RedeemFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RewardsActivity extends AppCompatActivity {

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.btn_redeem)
    Button btn_redeem;
    @BindView(R.id.btn_earn)
    Button btn_earn;
    @BindView(R.id.btn_history)
    Button btn_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setTitle(getResources().getString(R.string.rewards));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_title.setText(getResources().getString(R.string.rewards));

        btn_redeem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_redeem.setTextColor(getResources().getColor(R.color.white));

        btn_earn.setBackgroundColor(getResources().getColor(R.color.gray));
        btn_earn.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_history.setBackgroundColor(getResources().getColor(R.color.gray));
        btn_history.setTextColor(getResources().getColor(R.color.colorPrimary));

        RedeemFragment redeemFragment = new RedeemFragment();
        loadFragment(redeemFragment);
    }

    @OnClick({R.id.btn_redeem,R.id.btn_earn,R.id.btn_history})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_redeem:
                btn_redeem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_redeem.setTextColor(getResources().getColor(R.color.white));

                btn_earn.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_earn.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_history.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_history.setTextColor(getResources().getColor(R.color.colorPrimary));

                RedeemFragment redeemFragment = new RedeemFragment();
                loadFragment(redeemFragment);

                break;
            case R.id.btn_earn:
                btn_earn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_earn.setTextColor(getResources().getColor(R.color.white));

                btn_redeem.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_redeem.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_history.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_history.setTextColor(getResources().getColor(R.color.colorPrimary));

                EarnFragment earnFragment = new EarnFragment();
                loadFragment(earnFragment);
                break;
            case R.id.btn_history:
                btn_history.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_history.setTextColor(getResources().getColor(R.color.white));

                btn_redeem.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_redeem.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn_earn.setBackgroundColor(getResources().getColor(R.color.gray));
                btn_earn.setTextColor(getResources().getColor(R.color.colorPrimary));

                HistoryFragment historyFragment = new HistoryFragment();
                loadFragment(historyFragment);
                break;

        }
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragment);
       // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
