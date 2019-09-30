package com.hss.xservices.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    String str_address_type="";

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

    private void showDialog(String address){
        final Dialog alertDialog=new Dialog(ProceedActivity.this,android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txt_address=(TextView)alertDialog.findViewById(R.id.txt_address);
        EditText edt_addsName =(EditText)alertDialog.findViewById(R.id.edt_addsName);
        EditText edt_addsMobile =(EditText)alertDialog.findViewById(R.id.edt_addsMobile);
        EditText edt_own =(EditText)alertDialog.findViewById(R.id.edt_own);
        RadioButton radio_home =(RadioButton)alertDialog.findViewById(R.id.radio_home);
        RadioButton radio_work =(RadioButton)alertDialog.findViewById(R.id.radio_work);
        RadioButton radio_other =(RadioButton)alertDialog.findViewById(R.id.radio_other);
        Button btn_save=(Button)alertDialog.findViewById(R.id.btn_save);
        Button btn_cancel=(Button)alertDialog.findViewById(R.id.btn_cancel);

        txt_address.setText(address);

        radio_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    str_address_type = buttonView.getText().toString();
                }
            }
        });
        radio_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    str_address_type = buttonView.getText().toString();
                }
            }
        });
        radio_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edt_own.setVisibility(View.VISIBLE);
                }else {
                    edt_own.setVisibility(View.GONE);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_address_type = edt_own.getText().toString();
                if (edt_addsName.getText().toString().equalsIgnoreCase("")){

                }else if (edt_addsMobile.getText().toString().equalsIgnoreCase("")){

                }
            }
        });
        alertDialog.show();
    }

}