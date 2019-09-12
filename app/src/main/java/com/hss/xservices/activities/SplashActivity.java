package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hss.xservices.R;
import com.hss.xservices.utils.AlertDialogBox;
import com.hss.xservices.utils.NetworkInformation;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (NetworkInformation.isConnected(SplashActivity.this))
        {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(SplashActivity.this, LoginSignupActivity.class));
                        finish();
                    }
                }
            };
            timer.start();
        }else {
            AlertDialogBox.showAlert(SplashActivity.this,"No Internet Connection!");
        }

    }
}
