package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hss.xservices.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_mobile,R.id.img_facebook,R.id.img_google})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_mobile:
                startActivity(new Intent(this,MobileLoginActivity.class));
                break;
            case R.id.img_facebook:

                break;
            case R.id.img_google:

                break;
        }
    }
}
