package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.hss.xservices.adapters.MyOrdersAdapter;
import com.hss.xservices.models.Addresses;
import com.hss.xservices.models.Profile;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsActivity extends AppCompatActivity {

    String order_id,order_date,service_cost,service_tax,mobile,name,adds1,adds2,landmark,city,province,country,pin,service_name;
    @BindView(R.id.txt_serviceName)
    TextView txt_serviceName;
    @BindView(R.id.txt_orderNo)
    TextView txt_orderNo;
    @BindView(R.id.txt_schedule)
    TextView txt_schedule;
    @BindView(R.id.txt_serviceCost)
    TextView txt_serviceCost;
    @BindView(R.id.txt_taxes)
    TextView txt_taxes;
    @BindView(R.id.txt_totalAmount)
    TextView txt_totalAmount;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_address)
    TextView txt_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.order_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
             order_id = getIntent().getStringExtra("order_id");
             order_date = getIntent().getStringExtra("order_date");
            getOrderDetails(order_id);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getOrderDetails(String id) {

        ProgressDialog dialog = new ProgressDialog(OrderDetailsActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        String token = Prefs.with(OrderDetailsActivity.this).getString("token", "");
        Log.e("token", "" + token);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.BASE_URL + "/order/details/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getOrderDetails", "" + response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    String code = jsonObject2.optString("code");
                    if (code.equalsIgnoreCase("OK")){
                        JSONObject data = jsonObject2.getJSONObject("data");
                        JSONObject orderDetails = data.getJSONObject("orderDetails");
                        service_cost = orderDetails.optString("svcCost");
                        service_tax = orderDetails.optString("svcTax");
                        JSONObject userId = orderDetails.getJSONObject("userId");
                        mobile = userId.optString("mobile");
                        name = userId.optString("firstName");
                        JSONObject address = orderDetails.getJSONObject("address");
                        country = address.optString("country");
                        province = address.optString("province");
                        city = address.optString("city");
                        pin = address.optString("pin");
                        adds1 = address.optString("address1");
                        adds2 = address.optString("address2");
                        landmark = address.optString("landmark");
                        JSONObject service = orderDetails.getJSONObject("service");
                        service_name = service.optString("svcTitle");

                        txt_serviceName.setText(service_name);
                        txt_orderNo.setText("Order No: "+order_id);
                        txt_schedule.setText("Scheduled On: "+order_date);
                        txt_serviceCost.setText(service_cost);
                        txt_taxes.setText(service_tax);
                        txt_totalAmount.setText(service_cost);
                        txt_name.setText("Name: "+name);
                        txt_mobile.setText("Mobile: "+mobile);
                        txt_address.setText("Address: "+adds1+" "+adds2+" "+landmark+" "+city+" "+province+" "+country+" "+pin);
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
                dialog.dismiss();
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
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "get_orderDetails");
    }

}
