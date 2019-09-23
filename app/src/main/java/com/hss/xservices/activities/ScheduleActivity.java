package com.hss.xservices.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hss.xservices.R;
import com.hss.xservices.utils.Constants;
import com.hss.xservices.utils.Prefs;
import com.hss.xservices.utils.VolleyMultipartRequest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ArrayList<String> arr_fileName,arr_originalName;

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
                        .putExtra("time", str_time)
                .putStringArrayListExtra("arr_fileName",arr_fileName)
                        .putStringArrayListExtra("arr_originalName",arr_originalName));
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
                uploadPics();
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
                    uploadPics();
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

        @NonNull
        @Override
        public GalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.cell_gallery, viewGroup, false);
            GalleryAdapter.MyViewHolder myViewHolder = new GalleryAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryAdapter.MyViewHolder myViewHolder, int i) {
            Picasso.get().load(mArrayUriGallery.get(i)).resize(160, 160).centerCrop().into(myViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return mArrayUriGallery.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView, imageView_delete;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.img);
            }
        }
    }


    private void uploadPics() {
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.BASE_URL+"/file/upload",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        arr_fileName = new ArrayList<>();
                        arr_originalName = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new String(response.data));
                            Log.e("ResponceProductUpload", String.valueOf(jsonObject));
                            JSONObject res_obj = jsonObject.getJSONObject("response");
                            String code = res_obj.optString("code");
                            String message = res_obj.optString("message");
                            Toast.makeText(ScheduleActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (code.equalsIgnoreCase("OK")){
                                JSONObject data_obj = res_obj.getJSONObject("data");
                                JSONArray array = data_obj.getJSONArray("files");
                                for (int i=0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    arr_fileName.add(object.optString("originalname"));
                                    arr_originalName.add(object.optString("filename"));
                                }
                                Log.e("arr_fileName", ""+arr_fileName);
                                Log.e("arr_originalName", ""+arr_originalName);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Exeption", error.toString());
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", Prefs.with(ScheduleActivity.this).getString("token", ""));
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int wor = 0; wor < mArrayUriGallery.size(); wor++) {
                    //Bitmap bitmapImageUpload_multiple = BitmapFactory.decodeFile(imgFile.get(wor).getAbsolutePath());
                    try {
                        String imagename_m = "multi_" + System.currentTimeMillis();
                        Bitmap bitmaps = getThumbnail(mArrayUriGallery.get(wor));
                        // Bitmap resized = Bitmap.createScaledBitmap(bitmaps, 10, 10, true);
                        params.put("files[]", new DataPart(imagename_m + ".png", getFileDataFromDrawable(bitmaps)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //  params.put("image[0]", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                //  params.put("image[1]", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ScheduleActivity.this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > originalSize) ? (originalSize / originalSize) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = ScheduleActivity.this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }


}
