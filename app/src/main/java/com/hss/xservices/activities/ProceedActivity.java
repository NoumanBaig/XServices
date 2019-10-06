package com.hss.xservices.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hss.xservices.R;
import com.hss.xservices.adapters.AddressAdapter;
import com.hss.xservices.adapters.SliderAdapter;
import com.hss.xservices.models.Addresses;
import com.hss.xservices.models.Profile;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.schibstedspain.leku.LocationPickerActivity;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    @BindView(R.id.cardview)
    CardView cardview;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_address)
    TextView txt_address;
    //    @BindView(R.id.recyclerAddress)
//    RecyclerView recyclerView;
    String str_title, str_image;
    ArrayList<String> arr_photos;
    double latitude, longitude;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    String str_address_type = "", str_check = "";
    int ads_id = 0;
    ArrayList<String> arr_adds_name, arr_name, arr_mobile, arr_address;
    String str_mobile, str_firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getLocation();
        if (getIntent().getExtras() != null) {
            arr_photos = new ArrayList<>();
            str_image = getIntent().getStringExtra("image");
            str_title = getIntent().getStringExtra("title");
            arr_photos = getIntent().getStringArrayListExtra("arr_photos");

//            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            imageSlider(arr_photos);
        }
        setAddress();
        getProfile();
//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    str_check=buttonView.getText().toString();
//                }
//            }
//        })

//
    }

    private void setAddress() {
        if (!Prefs.with(ProceedActivity.this).getString("adds_name", "").equalsIgnoreCase("")) {
            cardview.setVisibility(View.VISIBLE);
            txt_name.setText("" + Prefs.with(ProceedActivity.this).getString("adds_name", ""));
            txt.setText("" + Prefs.with(ProceedActivity.this).getString("adds_type", ""));
            txt_mobile.setText("" + Prefs.with(ProceedActivity.this).getString("adds_mobile", ""));
            txt_address.setText("" + Prefs.with(ProceedActivity.this).getString("adds_adds", ""));
        } else {
            cardview.setVisibility(View.GONE);
            Log.e("Else---->", "--->");
        }
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
                if (!checkbox.isChecked()) {
                    Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
                } else if (cardview.getVisibility() == View.GONE) {
                    Toast.makeText(this, "Please add address", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, ScheduleActivity.class)
                            .putStringArrayListExtra("arr_photos", arr_photos));
                }
                break;
            default:
                break;

        }
    }

    private void imageSlider(ArrayList<String> arrayList) {
        final SliderAdapter adapter = new SliderAdapter(ProceedActivity.this, arrayList, "true");
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
                           // String address = getAddressFromLatLng(latitude, longitude);
//                            if (address != null) {
//                                showDialog(address);
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
                getAddressFromLatLng(latitude,longitude);
                if (address != null) {
                    showDialog(address);
                }
            }
        }
    }

    public void getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ProceedActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String getAdminArea = addresses.get(0).getAdminArea();
            String getCountryCode = addresses.get(0).getCountryCode();
            String getCountryName = addresses.get(0).getCountryName();
            String getFeatureName = addresses.get(0).getFeatureName();
            String getLocality = addresses.get(0).getLocality();
            String getPhone = addresses.get(0).getPhone();
            String getPostalCode = addresses.get(0).getPostalCode();
            String getPremises = addresses.get(0).getPremises();
            String getSubAdminArea = addresses.get(0).getSubAdminArea();

            Log.e("getAdminArea",getAdminArea);
            Log.e("getCountryCode",getCountryCode);
            Log.e("getCountryName",getCountryName);
            Log.e("getFeatureName",getFeatureName);
            Log.e("getLocality",getLocality);
            Log.e("getPhone",getPhone);
            Log.e("getPostalCode",getPostalCode);
            Log.e("getPremises",getPremises);
            Log.e("getSubAdminArea",getSubAdminArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String address) {
        final Dialog alertDialog = new Dialog(ProceedActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.address_dialog);
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txt_address = (TextView) alertDialog.findViewById(R.id.txt_address);
        EditText edt_addsName = (EditText) alertDialog.findViewById(R.id.edt_addsName);
        EditText edt_addsMobile = (EditText) alertDialog.findViewById(R.id.edt_addsMobile);
        EditText edt_own = (EditText) alertDialog.findViewById(R.id.edt_own);
        RadioButton radio_home = (RadioButton) alertDialog.findViewById(R.id.radio_home);
        RadioButton radio_work = (RadioButton) alertDialog.findViewById(R.id.radio_work);
        RadioButton radio_other = (RadioButton) alertDialog.findViewById(R.id.radio_other);
        Button btn_save = (Button) alertDialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);

        txt_address.setText("" + address);

//        radio_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (radio_home.isChecked()) {
//                    str_address_type = "Home";
//                }
//            }
//        });
//        radio_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (radio_work.isChecked()) {
//                    str_address_type = "Work";
//                }
//            }
//        });
        radio_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radio_other.isChecked()) {
                    edt_own.setVisibility(View.VISIBLE);
                } else {
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
                if (radio_home.isChecked()){
                    str_address_type = "Home";
                }
                else if (radio_work.isChecked()){
                    str_address_type = "Work";
                }else {
                    str_address_type = edt_own.getText().toString();
                }

                String adds_name = edt_addsName.getText().toString();
                String adds_mob = edt_addsMobile.getText().toString();
                if (adds_name.equalsIgnoreCase("")) {
                    adds_name = str_firstname;
                } if (edt_addsMobile.getText().toString().equalsIgnoreCase("")) {
                    adds_mob = str_mobile;
                }
                if (radio_other.isChecked()) {
                    if (edt_own.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(ProceedActivity.this, "Please enter Address Name", Toast.LENGTH_SHORT).show();
                    }else {
                        alertDialog.dismiss();
                        cardview.setVisibility(View.VISIBLE);
//                    Toast.makeText(ProceedActivity.this, "Very good", Toast.LENGTH_SHORT).show();
                        Prefs.with(ProceedActivity.this).save("adds_name", adds_name);
                        Prefs.with(ProceedActivity.this).save("adds_mobile", adds_mob);
                        Prefs.with(ProceedActivity.this).save("adds_type", str_address_type);
                        Prefs.with(ProceedActivity.this).save("adds_adds", address);
                        setAddress();
                    }
                }
                alertDialog.dismiss();
                cardview.setVisibility(View.VISIBLE);
//                    Toast.makeText(ProceedActivity.this, "Very good", Toast.LENGTH_SHORT).show();
                Prefs.with(ProceedActivity.this).save("adds_name", adds_name);
                Prefs.with(ProceedActivity.this).save("adds_mobile", adds_mob);
                Prefs.with(ProceedActivity.this).save("adds_type", str_address_type);
                Prefs.with(ProceedActivity.this).save("adds_adds", address);
                setAddress();
            }
        });
        alertDialog.show();
    }

    private void getProfile() {

        String token = Prefs.with(ProceedActivity.this).getString("token", "");
        Log.e("token", "" + token);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.BASE_URL + "/customer/profile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getProfile", "" + response);
                List<Profile> profileList = new ArrayList<>();
                List<Addresses> addressesList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    String code = jsonObject2.optString("code");
                    if (code.equalsIgnoreCase("OK")) {
                        JSONObject data = jsonObject2.getJSONObject("data");
                        JSONObject profile_obj = data.getJSONObject("profile");

                        Profile profile = new Profile();
                        profile.setUserId(profile_obj.optInt("userId"));
                        profile.setMobile(profile_obj.optString("mobile"));
                        profile.setEmail(profile_obj.optString("email"));
                        profile.setFirstName(profile_obj.optString("firstName"));
                        profile.setLastName(profile_obj.optString("lastName"));
                        profile.setAddOn(profile_obj.optString("addOn"));
                        profile.setEditOn(profile_obj.optString("editOn"));

                        str_firstname = profile_obj.optString("firstName");
                        str_mobile = profile_obj.optString("mobile");

                        JSONArray array = profile_obj.getJSONArray("addressess");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Addresses addresses = new Addresses();
                            addresses.setAddressId(object.optInt("addressId"));
                            ads_id = object.optInt("addressId");
                            addresses.setCountry(object.optString("country"));
                            addresses.setProvince(object.optString("province"));
                            addresses.setCity(object.optString("city"));
                            addresses.setPin(object.optString("pin"));
                            addresses.setAddress1(object.optString("address1"));
                            addresses.setAddress2(object.optString("address2"));
                            addresses.setLandmark(object.optString("landmark"));
                            addresses.setGeoLocLat(object.optString("geoLocLat"));
                            addresses.setGeoLocLon(object.optString("geoLocLon"));
                            addresses.setAddOn(object.optString("addOn"));
                            addresses.setEditOn(object.optString("editOn"));
                            addressesList.add(addresses);
                            profile.setAddressess(addressesList);
                        }
                        profileList.add(profile);
                        Prefs.with(ProceedActivity.this).save("adds_id", ads_id);
//                        if (addressesList.size()>0){
//                            recyclerView.setLayoutManager(new LinearLayoutManager(ProceedActivity.this));
//                            AddressAdapter addressAdapter = new AddressAdapter(ProceedActivity.this,addressesList);
//                            recyclerView.setAdapter(addressAdapter);
//                        }else {
//                            Toast.makeText(ProceedActivity.this, "Please add address", Toast.LENGTH_SHORT).show();
//                        }


                    } else {
                        Log.e("not OK", "-->");
                    }
                } catch (JSONException e) {
                    Log.e("JSONException", "" + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("F_resAdsList", "" + error);
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Failed to connect to server";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect to server";
                    }
                }
                // Toast.makeText(getActivity(), ""+errorMessage, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", token);
                return headers;
            }
        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "get_profile");
    }

    private void setProfile(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
//            object.put("editOn",editOn);
//            object.put("email",edt_email.getText().toString());
//            object.put("firstName",edt_firstName.getText().toString());
//            object.put("lastName",edt_lastName.getText().toString());

            JSONArray array = new JSONArray();
//            if (profile.getAddressess().size()==0){
//                JSONObject object1 = new JSONObject();
//                object1.put("addressType","0");
//                object1.put("country","India");
//                object1.put("province","Karnataka");
//                object1.put("city","Bangalore");
//                array.put(object1);
//            }else {
            JSONObject object1 = new JSONObject();
//                object1.put("addressId","");
            object1.put("addressType","0");
            object1.put("country","");
            object1.put("province","");
            object1.put("city","");
            object1.put("pin","");
            object1.put("address1","");
            object1.put("address2","");
            object1.put("landmark","");
            object1.put("geoLocLat","");
            object1.put("geoLocLon","");
            array.put(object1);
//            }

            object.put("addressess",array);
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Constants.BASE_URL + "/customer/profile", jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("setProfile",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    String code = jsonObject2.optString("code");
                    String message = jsonObject2.optString("message");
                    Toast.makeText(ProceedActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (code.equalsIgnoreCase("OK")){
                        JSONObject object = jsonObject2.getJSONObject("data");
                    }else {
                        Log.e("not OK","-->");
                    }

                } catch (JSONException e) {
                    Log.e("JSONException",""+e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("F_res", "" + error);
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Failed to connect to server";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect to server";
                    }
                }
                //new ShowToast(LoginActivity.this,""+errorMessage).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", Prefs.with(ProceedActivity.this).getString("token", ""));
                return headers;
            }

        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "set_profile");
    }

}