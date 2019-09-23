package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hss.xservices.R;
import com.hss.xservices.adapters.HomeAdapter;
import com.hss.xservices.adapters.ServicesAdapter;
import com.hss.xservices.models.Photo;
import com.hss.xservices.models.Services;
import com.hss.xservices.models.ServicesCategory;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServicesActivity extends AppCompatActivity {

    @BindView(R.id.recyclerServices)
    RecyclerView recyclerView;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.txt_title)
    TextView txt_title;
    String str_image,str_code,str_codeText,str_title;
    List<Photo> photos_list;
    List<ServicesCategory> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            str_code = getIntent().getStringExtra("code");
            str_codeText = getIntent().getStringExtra("code_text");
            str_image = getIntent().getStringExtra("image");
            str_title = getIntent().getStringExtra("title");
            Log.e("str_code",str_code);
            Log.e("str_codeText",str_codeText);
            Log.e("str_image",str_image);
            Log.e("str_title",str_title);

            Prefs.with(ServicesActivity.this).save("main_title",str_title);
            Picasso.get().load("http://3.83.243.193:3000/files/"+str_image).error(R.drawable.service).into(imageView);
            txt_title.setText(str_title);

            getServices(str_codeText);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getServices(String str_code){
        ProgressDialog dialog = new ProgressDialog(ServicesActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("type","SERVICE");
            object.put("svcCatg",str_code);
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "/search", jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("getServices",""+response);
                categoryList = new ArrayList<>();
                photos_list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject res_obj = jsonObject.getJSONObject("response");
                    String status = res_obj.optString("code");
                    if (status.equalsIgnoreCase("OK")){
                        JSONObject data_obj = res_obj.getJSONObject("data");
                        JSONArray array = data_obj.getJSONArray("services");
                        for (int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            ServicesCategory services = new ServicesCategory();
                            services.setSvcId(object.optInt("svcId"));
                            services.setSvcCatg(object.optInt("svcCatg"));
                            services.setSvcType(object.optInt("svcType"));
                            services.setSvcTitle(object.optString("svcTitle"));
                            services.setSvcDescription(object.optString("svcDescription"));
                            services.setHourRate(object.optString("hourRate"));
                            services.setSvcSla(object.optString("svcSla"));


                            JSONArray jsonArray = object.getJSONArray("photos");
                            if (jsonArray.length()>0){
                                for (int j=0; j<jsonArray.length(); j++){
                                    JSONObject object1 = jsonArray.getJSONObject(j);
                                    Photo photo = new Photo();
                                    photo.setSvcPhotoId(object1.optInt("svcPhotoId"));
                                    photo.setPhotoFileName(object1.optString("photoFileName"));
                                    photo.setPhotoDispName(object1.optString("photoDispName"));
                                    photos_list.add(photo);
                                }
                            }
                            services.setPhotos(photos_list);
                            categoryList.add(services);
                        }
                        ServicesAdapter servicesAdapter = new ServicesAdapter(ServicesActivity.this,categoryList);
                        recyclerView.setAdapter(servicesAdapter);
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
                Toast.makeText(ServicesActivity.this, ""+errorMessage, Toast.LENGTH_SHORT).show();
                //new ShowToast(LoginActivity.this,""+errorMessage).show();
            }
        }) {
        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "get_services");
    }

}
