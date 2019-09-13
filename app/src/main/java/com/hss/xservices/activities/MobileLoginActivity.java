package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hbb20.CountryCodePicker;
import com.hss.xservices.R;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobileLoginActivity extends AppCompatActivity {

    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    String str_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mobile_login);
        ButterKnife.bind(this);

        ccp.setCountryForPhoneCode(91);
        str_code = ccp.getSelectedCountryCode();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.e("onCountrySelected",""+ccp.getSelectedCountryCode());
                str_code = ccp.getSelectedCountryCode();
            }
        });
    }

    @OnClick(R.id.btn_proceed)
    public void onClick(View view){
        if (edt_mobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        }else if (str_code.equalsIgnoreCase("")){
            Toast.makeText(this, "Please select country", Toast.LENGTH_SHORT).show();
        }
        else {
            sendOTP(str_code,edt_mobile.getText().toString());
        }

    }

    private void sendOTP(String str_code,String mobile){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("mobile",str_code+mobile);
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "/customer/otp", jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("onSuccess",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    String code = jsonObject2.optString("code");
                    if (code.equalsIgnoreCase("OK")){
                        String message = jsonObject2.optString("message");
                        Toast.makeText(MobileLoginActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        JSONObject data = jsonObject2.getJSONObject("data");
                        String otp = data.optString("otp");
                        startActivity(new Intent(MobileLoginActivity.this,OtpActivity.class)
                                .putExtra("otp",otp).putExtra("mobile",str_code+mobile));
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
        };
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "send_otp");
    }

}
