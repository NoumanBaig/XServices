package com.hss.xservices.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hss.xservices.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    int day, month, year;
    String str_day, str_time;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_time)
    TextView txt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setDateAndTime();

    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private void setDateAndTime() {
        calendarView.setDateSelected(CalendarDay.today(), true);

        day = CalendarDay.today().getDay();
        month = CalendarDay.today().getMonth();
        year = CalendarDay.today().getYear();
        str_day = day + "-" + month + "-" + year;
        Log.e("str_day", "" + str_day);

        txt_date.setText(str_day);
        str_time = getCurrentTime();
        txt_time.setText(str_time);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                day = date.getDay();
                month = date.getMonth();
                year = date.getYear();
                str_day = day + "-" + month + "-" + year;
                Log.e("str_day", "" + str_day);
                txt_date.setText(str_day);
                str_time = getCurrentTime();
                txt_time.setText(str_time);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.btn_continue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                startActivity(new Intent(this, OrderSummaryActivity.class)
                .putExtra("date",str_day)
                .putExtra("time",str_time));
                break;
            default:
                break;

        }
    }

}
