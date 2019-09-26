package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.hss.xservices.R;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDescriptionActivity extends AppCompatActivity {

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
    @BindView(R.id.txt_serviceNotes)
    TextView txt_serviceNotes;
    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    String str_title,str_desc,str_price,str_image,str_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_description);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            str_id = getIntent().getStringExtra("id");
            str_image = getIntent().getStringExtra("image");
            str_title = getIntent().getStringExtra("title");
            str_desc = getIntent().getStringExtra("description");
            str_price = getIntent().getStringExtra("price");

            getDetails(str_id);

            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(img);
            txt_title.setText(str_title);
            txt_serviceName.setText(str_title);
            txt_serviceDesc.setText(str_desc);
            txt_servicePrice.setText("$"+str_price);
            txt_serviceNotes.setText("NIL");

            Prefs.with(ServiceDescriptionActivity.this).save("str_id",str_id);
            Prefs.with(ServiceDescriptionActivity.this).save("image",str_image);
            Prefs.with(ServiceDescriptionActivity.this).save("title",str_title);
            Prefs.with(ServiceDescriptionActivity.this).save("description",str_desc);
            Prefs.with(ServiceDescriptionActivity.this).save("price",str_price);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_proceed)
    public void onClick(View view){
        startActivity(new Intent(this,ProceedActivity.class)
        .putExtra("image",str_image)
        .putExtra("title",str_title));
    }

        private void getDetails(String id){

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.BASE_URL + "/service/details/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response",""+response);

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));

                } catch (JSONException e) {

                    Log.e("JSONException",""+e);
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
                headers.put("token", Prefs.with(ServiceDescriptionActivity.this).getString("token",""));
                return headers;
            }
        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "get_services");
    }
}
