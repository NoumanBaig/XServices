package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.hss.xservices.R;
import com.hss.xservices.utils.AlertDialogBox;
import com.hss.xservices.utils.NetworkInformation;
import com.hss.xservices.utils.Prefs;
import com.hss.xservices.utils.SharedPrefsUtils;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (NetworkInformation.isConnected(SplashActivity.this)) {
            runTimer();
        } else {
            AlertDialogBox.showAlert(SplashActivity.this, "No Internet Connection!");
        }

    }

    private void runTimer() {

        String string = "false";
        string = SharedPrefsUtils.getString(SplashActivity.this, "login");
        String finalString = string;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.with(SplashActivity.this).getString("login","").equalsIgnoreCase("true")) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginSignupActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
