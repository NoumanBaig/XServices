package com.hss.xservices.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss.xservices.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    @BindView(R.id.img_camera)
    ImageView img_camera;
    @BindView(R.id.layout_addPhoto)
    LinearLayout layout_addPhoto;
    @BindView(R.id.recyclerPhotos)
    RecyclerView recyclerView;
    int PICK_IMAGE_MULTIPLE = 1;
    List<String> imagesEncodedList;
    String imageEncoded;
    List<Uri> uriArrayList = new ArrayList<>();
    ArrayList<Uri> mArrayUriGallery = new ArrayList<Uri>();

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

    @OnClick({R.id.btn_continue, R.id.layout_addPhoto, R.id.img_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                startActivity(new Intent(this, OrderSummaryActivity.class)
                        .putExtra("date", str_day)
                        .putExtra("time", str_time));
                break;
            case R.id.layout_addPhoto:
                requestPermission();
                break;
            case R.id.img_camera:
               // requestCameraPermission();
                break;
            default:
                break;

        }
    }


    private void requestCameraPermission() {
        Dexter.withActivity(ScheduleActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void requestPermission() {
        Dexter.withActivity(ScheduleActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        Log.e("Permission_grant", "" + response);
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Log.e("Permission_deni", "" + response);
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data
            Log.e("offers", "multiple_images_selected");
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();
            if (data.getData() != null) {

                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded = cursor.getString(columnIndex);
                cursor.close();

//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUriGallery.add(mImageUri);
                Log.e("offers", "mArrayUri" + mArrayUriGallery);
//                    int arr_size=0;
//                    arr_size=uriArrayList.size();
//                    if (arr_size <= 4){
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(ScheduleActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager2);
                GalleryAdapter galleryAdapter = new GalleryAdapter();
                recyclerView.setAdapter(galleryAdapter);
//                    }else {
//                        Toast.makeText(this, "Cannot upload more than 5 images ", Toast.LENGTH_SHORT).show();
//                    }
//                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
//                    gvGallery.setAdapter(galleryAdapter);
//                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
//                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
//                            .getLayoutParams();
//                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUriGallery.add(uri);
                        Log.e("offers.....", "mArrayUri" + mArrayUriGallery);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();

//                            int arr_size=0;
//                            arr_size=uriArrayList.size();


//                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
//                            gvGallery.setAdapter(galleryAdapter);
//                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
//                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
//                                    .getLayoutParams();
//                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    }
                    Log.e("LOG_TAG", "Selected Images" + mArrayUriGallery.size());
//                        if (imagesEncodedList.size() <= 4 && uriArrayList.size() <= 4) {
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(ScheduleActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager2);
                    GalleryAdapter galleryAdapter = new GalleryAdapter();
                    recyclerView.setAdapter(galleryAdapter);
//                        } else {
//                            Toast.makeText(this, "Please select 4 images", Toast.LENGTH_SHORT).show();
//                        }
                }
            }
        }
    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

        String containsString;

//        public GalleryAdapter(String containsString) {
//            this.containsString=containsString;
//        }

        @NonNull
        @Override
        public GalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.cell_gallery, viewGroup, false);
            GalleryAdapter.MyViewHolder myViewHolder = new GalleryAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryAdapter.MyViewHolder myViewHolder, int i) {

//            if (containsString != null){
//                Picasso.with(PicturePreviewActivity.this).load(images_arr.get(i)).resize(160,160).centerCrop().into(myViewHolder.imageView);
//                myViewHolder.imageView_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        images_arr.remove(i);
//                        notifyDataSetChanged();
//                        Log.e("images_arr",""+images_arr);
//                    }
//                });
//            }else {
//                Picasso.with(PicturePreviewActivity.this).load(mArrayUriGallery.get(i)).resize(160,160).centerCrop().into(myViewHolder.imageView);
//                myViewHolder.imageView_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mArrayUriGallery.remove(i);
//                        notifyDataSetChanged();
//                        Log.e("mArrayUriGallery",""+mArrayUriGallery);
//                    }
//                });
//            }

            Picasso.get().load(mArrayUriGallery.get(i)).resize(160, 160).centerCrop().into(myViewHolder.imageView);
//            myViewHolder.imageView_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mArrayUriGallery.remove(i);
//                    notifyDataSetChanged();
//                    Log.e("mArrayUriGallery", "" + mArrayUriGallery);
//                }
//            });


        }

        @Override
        public int getItemCount() {
//            if (containsString != null){
//                return images_arr.size();
//            }else {
//                return mArrayUriGallery.size();
//            }
            return mArrayUriGallery.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            //        TextView textView;
            ImageView imageView, imageView_delete;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//            textView = itemView.findViewById(R.id.txt);
                imageView = itemView.findViewById(R.id.img);
//                imageView_delete = itemView.findViewById(R.id.img_delete);
            }
        }
    }

}
