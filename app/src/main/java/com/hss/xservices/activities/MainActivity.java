package com.hss.xservices.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hss.xservices.R;
import com.hss.xservices.fragments.HomeFragment;
import com.hss.xservices.fragments.OrdersFragment;
import com.hss.xservices.fragments.ProfileFragment;
import com.hss.xservices.fragments.RewardsFragment;
import com.hss.xservices.utils.BottomNavigationBehavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
       // mTextMessage = findViewById(R.id.message);
        loadFragmentHome(new HomeFragment());
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadFragmentHome(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    // mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_orders:
                    fragment = new OrdersFragment();
                    loadFragment(fragment);
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_rewards:
                    fragment = new RewardsFragment();
                    loadFragment(fragment);
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };


}
