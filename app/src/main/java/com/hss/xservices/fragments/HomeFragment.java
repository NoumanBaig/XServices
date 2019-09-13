package com.hss.xservices.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hss.xservices.R;
import com.hss.xservices.activities.HomeActivity;
import com.hss.xservices.activities.MainActivity;
import com.hss.xservices.activities.SearchActivity;
import com.hss.xservices.adapters.BestSellersAdapter;
import com.hss.xservices.adapters.CategoryAdapter;
import com.hss.xservices.adapters.HomeAdapter;
import com.hss.xservices.adapters.SliderAdapter;
import com.hss.xservices.adapters.WeeklyAdapter;
import com.schibstedspain.leku.LocationPickerActivity;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    SliderView sliderView;
    double latitude, longitude;
    TextView txt_location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        sliderView = view.findViewById(R.id.imageSlider);
        recyclerView = view.findViewById(R.id.recyclerServices);
        txt_location = view.findViewById(R.id.txt_location);
        imageSlider();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HomeAdapter homeAdapter = new HomeAdapter(getActivity());
        recyclerView.setAdapter(homeAdapter);
        getLocation();

        return view;
    }

    private void imageSlider() {
        final SliderAdapter adapter = new SliderAdapter(getActivity());
        adapter.setCount(3);
        sliderView.setSliderAdapter(adapter);

//        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
//        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
    }

    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations getActivity() can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("latitude", "" + latitude);
                            Log.e("longitude", "" + longitude);
                            String address = getAddressFromLatLng(latitude, longitude);
                            if (address != null) {
                                txt_location.setText(address);
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.linear_location)
    public void onLocClick(View view) {
        startActivityForResult(new LocationPickerActivity.Builder()
                .withLocation(latitude, longitude).withSatelliteViewHidden().shouldReturnOkOnBackPressed()
                .withGooglePlacesEnabled()
                .build(getActivity()), 1);
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
                if (address != null) {
                    txt_location.setText(address);
                }
            }
        }
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
