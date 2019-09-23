package com.hss.xservices.activities;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.hss.xservices.R;
import com.hss.xservices.models.Addresses;
import com.hss.xservices.models.Profile;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,DatePickerDialog.OnCancelListener {

    @BindView(R.id.edt_firstName)
    EditText edt_firstName;
//    @BindView(R.id.edt_middleName)
//    EditText edt_middleName;
    @BindView(R.id.edt_lastName)
    EditText edt_lastName;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_address1)
    EditText edt_address1;
    @BindView(R.id.edt_address2)
    EditText edt_address2;
    @BindView(R.id.edt_city)
    EditText edt_city;
    @BindView(R.id.edt_province)
    EditText edt_province;
    @BindView(R.id.edt_country)
    EditText edt_country;
    @BindView(R.id.edt_pincode)
    EditText edt_pincode;
    @BindView(R.id.edt_landmark)
    EditText edt_landmark;
    @BindView(R.id.spinner)
    Spinner spinner;
    String[] gender = {"Male","Female"};
    @BindView(R.id.txt_dob)
    TextView txt_dob;
    String editOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getResources().getString(R.string.my_orders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getProfile();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_spinner_item,gender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.txt_dob)
    public void onDOB(View view){
        showDate(1980, 0, 1, R.style.DatePickerSpinner);
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit(View view){
        if (edt_firstName.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter First name", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_lastName.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Last name", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_email.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Email ID", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_address1.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Address 1", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_address2.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Address 2", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_city.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter City", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_province.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Province", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_country.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Country", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_pincode.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Pincode", Toast.LENGTH_SHORT).show();
        }
        else  if (edt_landmark.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Land mark", Toast.LENGTH_SHORT).show();
        }else {
            setProfile();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("onDateSet",""+year+monthOfYear+dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        txt_dob.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.e("onCancel",""+dialog);
    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(ProfileActivity.this)
                .callback(ProfileActivity.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    private void getProfile() {
        String token = Prefs.with(ProfileActivity.this).getString("token", "");
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
                    if (code.equalsIgnoreCase("OK")){
                        JSONObject data = jsonObject2.getJSONObject("data");
                        JSONObject profile_obj = data.getJSONObject("profile");
//                        Profile profile = new Profile();
//                        profile.setUserId(profile_obj.optInt("userId"));
//                        profile.setUserType(profile_obj.optInt("userType"));
//                        profile.setUserRole(profile_obj.optInt("userRole"));
//                        profile.setGender(profile_obj.optInt("gender"));
//                        profile.setUserCode(profile_obj.optString("userCode"));
//                        profile.setMobile(profile_obj.optString("mobile"));
//                        profile.setPassword(profile_obj.optString("password"));
//                        profile.setTitle(profile_obj.optString("title"));
//                        profile.setFirstName(profile_obj.optString("firstName"));
//                        profile.setMiddleName(profile_obj.optString("middleName"));
//                        profile.setLastName(profile_obj.optString("lastName"));
//                        profile.setNickName(profile_obj.optString("nickName"));
//                        profile.setDob(profile_obj.optString("dob"));
//                        profile.setPhoto(profile_obj.optString("photo"));
//                        profile.setRegisterDate(profile_obj.optString("registerDate"));
//                        profile.setUpdatedOn(profile_obj.optString("updatedOn"));
//                        profile.setAddOn(profile_obj.optString("addOn"));
//                        profile.setEditOn(profile_obj.optString("editOn"));
//
//                        JSONArray array = profile_obj.getJSONArray("addressess");
//                        for (int i=0; i<array.length(); i++){
//                            JSONObject object = array.getJSONObject(i);
//                            Addresses addresses = new Addresses();
//                            addresses.setAddressId(object.optInt("addressId"));
//                            addresses.setAddressType(object.optInt("addressType"));
//                            addresses.setDisplaySeq(object.optInt("displaySeq"));
//                            addresses.setCountry(object.optString("country"));
//                            addresses.setProvince(object.optString("province"));
//                            addresses.setCity(object.optString("city"));
//                            addresses.setPin(object.optString("pin"));
//                            addresses.setAddress1(object.optString("address1"));
//                            addresses.setAddress2(object.optString("address2"));
//                            addresses.setLandmark(object.optString("landmark"));
//                            addresses.setGeoLocLat(object.optString("geoLocLat"));
//                            addresses.setGeoLocLon(object.optString("geoLocLon"));
//                            addresses.setAddOn(object.optString("addOn"));
//                            addresses.setEditOn(object.optString("editOn"));
//                            addressesList.add(addresses);
//                        }

//                        profile.setAddressess(addressesList);
//                        profileList.add(profile);
                        String title = profile_obj.optString("title");
                        String firstName = profile_obj.optString("firstName");
                        String lastName = profile_obj.optString("lastName");
                        editOn = profile_obj.optString("editOn");
                        if (!firstName.equals("null")){
                            edt_firstName.setText(firstName);
                        }
                        if (!lastName.equals("null")){
                            edt_lastName.setText(firstName);
                        }

                    }else {
                        Log.e("not OK","-->");
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
            object.put("editOn",editOn);
            object.put("email",edt_email.getText().toString());
            object.put("firstName",edt_firstName.getText().toString());
            object.put("lastName",edt_lastName.getText().toString());

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
                object1.put("country",edt_country.getText().toString());
                object1.put("province",edt_province.getText().toString());
                object1.put("pin",edt_pincode.getText().toString());
                object1.put("address1",edt_address1.getText().toString());
                object1.put("address2",edt_address2.getText().toString());
                object1.put("landmark",edt_landmark.getText().toString());
                object1.put("geoLocLat","12.80");
                object1.put("geoLocLon","78.60");
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
                    if (code.equalsIgnoreCase("OK")){

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
                headers.put("token", Prefs.with(ProfileActivity.this).getString("token", ""));
                return headers;
            }

        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "set_profile");
    }

}
