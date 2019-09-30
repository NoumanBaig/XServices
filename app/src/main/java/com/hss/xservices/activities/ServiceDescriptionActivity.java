package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hss.xservices.R;
import com.hss.xservices.adapters.SliderAdapter;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDescriptionActivity extends AppCompatActivity {

//    @BindView(R.id.img)
//    ImageView img;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.imageSlider)
    SliderView sliderView;
//    @BindView(R.id.txt_serviceName)
//    TextView txt_serviceName;
    @BindView(R.id.txt_serviceDesc)
    TextView txt_serviceDesc;
    @BindView(R.id.txt_servicePrice)
    TextView txt_servicePrice;
    @BindView(R.id.txt_serviceNotes)
    TextView txt_serviceNotes;
    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    String str_title,str_desc,str_price,str_image,str_id;
    ArrayList<String> arr_photos;

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
//            str_image = getIntent().getStringExtra("image");
//            str_title = getIntent().getStringExtra("title");
//            str_desc = getIntent().getStringExtra("description");
//            str_price = getIntent().getStringExtra("price");

            getDetails(str_id);

            Prefs.with(ServiceDescriptionActivity.this).save("str_id",str_id);

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
        .putExtra("title",str_title)
        .putStringArrayListExtra("arr_photos",arr_photos));
    }

        private void getDetails(String id){

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.BASE_URL + "/service/details/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response",""+response);
                arr_photos = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject res_obj = jsonObject.getJSONObject("response");
                    String status = res_obj.optString("code");
                    if (status.equalsIgnoreCase("OK")){
                        JSONObject data_obj = res_obj.getJSONObject("data");
                        JSONObject svcDetails = data_obj.getJSONObject("svcDetails");
                        String svcId = svcDetails.optString("svcId");
                        String svcTitle = svcDetails.optString("svcTitle");
                        String svcDescription = svcDetails.optString("svcDescription");
                        String hourRate = svcDetails.optString("hourRate");
                        String taxRate = svcDetails.optString("taxRate");
                        String operCommission = svcDetails.optString("operCommission");
                        JSONArray jsonArray = svcDetails.getJSONArray("photos");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            arr_photos.add(object.optString("photoFileName"));
                        }
                        txt_title.setText(svcTitle);
                        Spanned html_text = Html.fromHtml(svcDescription);
                        txt_serviceDesc.setText(html_text);
                        txt_servicePrice.setText("$"+hourRate);
                        txt_serviceNotes.setText("NIL");
                        imageSlider(arr_photos);
                        Prefs.with(ServiceDescriptionActivity.this).save("title",svcTitle);
                        Prefs.with(ServiceDescriptionActivity.this).save("description",svcDescription);
                        Prefs.with(ServiceDescriptionActivity.this).save("price",hourRate);
                    }else {
                        Log.e("Something","went wrong");
                    }

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

    private void imageSlider(ArrayList<String> arrayList) {
        final SliderAdapter adapter = new SliderAdapter(ServiceDescriptionActivity.this,arrayList,"true");
        sliderView.setSliderAdapter(adapter);
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
    }

}
