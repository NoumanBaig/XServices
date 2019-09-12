package com.hss.xservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hss.xservices.R;

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
            Intent i = new Intent(OtpActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
           // callRegister(str_mobile,str_otp1+str_otp2+str_otp3+str_otp4+str_otp5+str_otp6);
        }
    }

}
