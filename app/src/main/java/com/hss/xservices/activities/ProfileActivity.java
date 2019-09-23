package com.hss.xservices.activities;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hss.xservices.R;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,DatePickerDialog.OnCancelListener {

    @BindView(R.id.edt_firstName)
    EditText edt_firstName;
    @BindView(R.id.edt_middleName)
    EditText edt_middleName;
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
    @BindView(R.id.edt_landmark)
    EditText edt_landmark;
    @BindView(R.id.spinner)
    Spinner spinner;
    String[] gender = {"Gender","Male","Female"};
    @BindView(R.id.txt_dob)
    TextView txt_dob;

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
}
