package com.hss.xservices.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hss.xservices.R;
import com.hss.xservices.adapters.AddressAdapter;
import com.hss.xservices.adapters.SliderAdapter;
import com.schibstedspain.leku.LocationPickerActivity;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class ProceedActivity extends AppCompatActivity {

    @BindView(R.id.imageSlider)
    SliderView sliderView;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.recyclerAddress)
    RecyclerView recyclerView;
    String str_title,str_image;
    ArrayList<String> arr_photos;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getLocation();
        if (getIntent().getExtras() != null){
            arr_photos = new ArrayList<>();
            str_image = getIntent().getStringExtra("image");
            str_title = getIntent().getStringExtra("title");
            arr_photos = getIntent().getStringArrayListExtra("arr_photos");

//            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            imageSlider(arr_photos);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter addressAdapter = new AddressAdapter(this);
        recyclerView.setAdapter(addressAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.btn_schedule, R.id.btn_addAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addAddress:
               // startActivity(new Intent(this, ProfileActivity.class));
                startActivityForResult(new LocationPickerActivity.Builder()
                        .withLocation(latitude, longitude).withSatelliteViewHidden().shouldReturnOkOnBackPressed()
                        .withGooglePlacesEnabled()
                        .build(ProceedActivity.this), 1);
                break;
            case R.id.btn_schedule:
                startActivity(new Intent(this, ScheduleActivity.class).putStringArrayListExtra("arr_photos",arr_photos));
                break;
            default:
                break;

        }
    }

    private void imageSlider(ArrayList<String> arrayList) {
        final SliderAdapter adapter = new SliderAdapter(ProceedActivity.this,arrayList,"true");
        sliderView.setSliderAdapter(adapter);
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
    }


    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(ProceedActivity.this);
        if (ActivityCompat.checkSelfPermission(ProceedActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProceedActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(ProceedActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations ProceedActivity.this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("latitude", "" + latitude);
                            Log.e("longitude", "" + longitude);
                            String address = getAddressFromLatLng(latitude, longitude);
//                            if (address != null) {
//                                txt_location.setText(address);
//                            }
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.e("RESULT****", "OK");
            if (requestCode == 1) {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.e("LATITUDE****", "" + latitude);
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.e("LONGITUDE****", "" + longitude);
                String address = data.getStringExtra(LOCATION_ADDRESS);
                Log.e("ADDRESS****", "" + address);
//                if (address != null) {
//                    txt_location.setText(address);
//                }
            }
        }
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ProceedActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}