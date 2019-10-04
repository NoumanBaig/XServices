package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hss.xservices.R;
import com.hss.xservices.adapters.MyOrdersAdapter;
import com.hss.xservices.adapters.ServicesAdapter;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.recyclerMyOrders)
    RecyclerView recyclerView;
    ArrayList<String> arr_orderNo,arr_status,arr_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_orders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getOrders();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getOrders(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("offset","0");
            object.put("limit","10");
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "/customer/orders", jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("onSuccess",""+response);
                arr_orderNo = new ArrayList<>();
                arr_date = new ArrayList<>();
                arr_status = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    String code = jsonObject2.optString("code");
                    if (code.equalsIgnoreCase("OK")){
                        JSONObject data = jsonObject2.getJSONObject("data");
                        JSONArray array = data.getJSONArray("orders");
                        for (int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            arr_orderNo.add(object.optString("orderCode"));
                            arr_status.add(object.optString("status"));
                            arr_date.add(object.optString("addOn"));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
                        MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this,arr_orderNo,arr_status,arr_date);
                        recyclerView.setAdapter(myOrdersAdapter);
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
                headers.put("token", Prefs.with(MyOrdersActivity.this).getString("token", ""));
                return headers;
            }
        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "my_orders");
    }

}
