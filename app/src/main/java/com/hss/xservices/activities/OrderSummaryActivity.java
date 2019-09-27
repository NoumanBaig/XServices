package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.hss.xservices.R;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_serviceName)
    TextView txt_serviceName;
    @BindView(R.id.txt_serviceDesc)
    TextView txt_serviceDesc;
    @BindView(R.id.txt_servicePrice)
    TextView txt_servicePrice;
    @BindView(R.id.txt_serviceDate)
    TextView txt_serviceDate;
    double price = 0;
    int id=0;
    long dateOrgin;
    String str_title,str_desc,str_price,str_image,str_date,str_time,str_id,sending_dateTime;
    ArrayList<String> arr_fileName,arr_originalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            arr_fileName = new ArrayList<>();
            arr_originalName = new ArrayList<>();
            sending_dateTime = getIntent().getStringExtra("sending_dateTime");
            str_date = getIntent().getStringExtra("date");
            str_time = getIntent().getStringExtra("time");

            arr_fileName = getIntent().getStringArrayListExtra("arr_fileName");
            arr_originalName = getIntent().getStringArrayListExtra("arr_originalName");

            Log.e("arr_fileName",""+arr_fileName);
            Log.e("arr_originalName",""+arr_originalName);
            str_id = Prefs.with(OrderSummaryActivity.this).getString("str_id","");
            str_image = Prefs.with(OrderSummaryActivity.this).getString("image","");
            str_title = Prefs.with(OrderSummaryActivity.this).getString("title","");
            str_desc = Prefs.with(OrderSummaryActivity.this).getString("description","");
            str_price = Prefs.with(OrderSummaryActivity.this).getString("price","");
            price = Double.parseDouble(str_price);
            id = Integer.parseInt(str_id);

            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            txt_serviceName.setText(str_title);
            txt_serviceDesc.setText(str_desc);
            txt_servicePrice.setText(str_price);
            txt_serviceDate.setText(str_date+" "+str_time);

            String expiryDateString = "2018-10-15T17:52:00Z";
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = formatter.parse(expiryDateString);
                Log.e("date--->",""+date);
                expiryDateString=formatter.format(date);
                Log.e("expiryDateString",""+expiryDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_confirmOrder)
    public void onConfirmClick(View view){
        try {
            if (arr_fileName != null || arr_fileName.size() <= 0){
                Toast.makeText(this, "Please upload picture", Toast.LENGTH_SHORT).show();
            }else {
                orderRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // startActivity(new Intent(this,MyOrdersActivity.class));
    }

    private void orderRequest(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("svcId",id);
            object.put("addressId",36);
            object.put("svcCost",price);
            object.put("svcTax",0.00);
            object.put("startDateTime",sending_dateTime);
            object.put("endDateTime",sending_dateTime);

            JSONArray array = new JSONArray();
            JSONObject object1 = new JSONObject();
            for (int i=0; i<arr_fileName.size(); i++){
                object1.put("photoFileName",arr_fileName.get(i));
                object1.put("photoDispName",arr_originalName.get(i));
                array.put(object1);
            }

            object.put("photos",array);
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "/order/schedule", jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("orderRequest",""+response);
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
                headers.put("token", Prefs.with(OrderSummaryActivity.this).getString("token", ""));
                return headers;
            }

        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "order");
    }

}