package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.hss.xservices.R;
import com.hss.xservices.rest.AppControler;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.hss.xservices.utils.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtpActivity extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.edt1)
    EditText edt1;
    @BindView(R.id.edt2)
    EditText edt2;
    @BindView(R.id.edt3)
    EditText edt3;
    @BindView(R.id.edt4)
    EditText edt4;
    String otp,mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        edt1.setEnabled(true);
        edt1.requestFocus();

        edt1.addTextChangedListener(this);
        edt2.addTextChangedListener(this);
        edt3.addTextChangedListener(this);
        edt4.addTextChangedListener(this);

        if ( getIntent().getExtras() != null){
            otp = getIntent().getStringExtra("otp");
            mobile = getIntent().getStringExtra("mobile");
            Log.e("otp",""+otp);
            char arr[] = otp.toCharArray();
            edt1.setText(""+arr[0]);
            edt2.setText(""+arr[1]);
            edt3.setText(""+arr[2]);
            edt4.setText(""+arr[3]);
        }
    }

    @OnClick({R.id.btn_continue})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_continue:
                validateEditFields();
                break;
                default:
                    break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.hashCode() == edt1.getText().hashCode()) {
            setEditTextFocus(edt2);
        } else if (s.hashCode() == edt2.getText().hashCode()) {
            setEditTextFocus(edt3);
        } else if (s.hashCode() == edt3.getText().hashCode()) {
            setEditTextFocus(edt4);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setEditTextFocus(EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();
    }

    private void validateEditFields() {
        String str_otp1 = edt1.getText().toString();
        String str_otp2 = edt2.getText().toString();
        String str_otp3 = edt3.getText().toString();
        String str_otp4 = edt4.getText().toString();

        if (str_otp1.equalsIgnoreCase("") || str_otp2.equalsIgnoreCase("")
                || str_otp3.equalsIgnoreCase("") || str_otp4.equalsIgnoreCase("")) {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
        else {
            verifyOTP(otp,mobile);
        }
    }

    private void verifyOTP(String otp,String mobile){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("otp",otp);
            object.put("mobile",mobile);
            jsonParams.put("request",object);

            Log.e("jsonParams",""+jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "/customer/otp/auth", jsonParams, new Response.Listener<JSONObject>() {
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
                        Toast.makeText(OtpActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        JSONObject data = jsonObject2.getJSONObject("data");
                        String token = data.optString("token");

                        Prefs.with(OtpActivity.this).save("token",token);
                        Prefs.with(OtpActivity.this).save("login","true");
                        Intent i = new Intent(OtpActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

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
        AppControler.getInstance().addToRequestQueue(jsonObjReq, "verify_otp");
    }


}
