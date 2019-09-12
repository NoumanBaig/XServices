package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hbb20.CountryCodePicker;
import com.hss.xservices.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobileLoginActivity extends AppCompatActivity {

    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mobile_login);
        ButterKnife.bind(this);

        ccp.setCountryForPhoneCode(91);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.e("onCountrySelected",""+ccp.getSelectedCountryCode());
            }
        });
    }

    @OnClick(R.id.btn_proceed)
    public void onClick(View view){
        startActivity(new Intent(this,OtpActivity.class));
    }
}
