package com.capsulestudio.dailynote.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.CheckPermission;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.ImageNoteModel;
import com.capsulestudio.dailynote.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateImageNoteActivity extends AppCompatActivity {

    ImageNoteModel imageNoteModel;

    ImageView imageView, ivSelectGalCam;
    EditText etTitle, etDetails;
    Button btnUpdate;
    PhotoView photoView;
    FrameLayout selectFrame;

    // all value field
    private String id;
    private byte[] photo;
    private String title;
    private String details;
    private String date_time;
    private Bitmap bp;

    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String IMAGE_DIRECTORY = "DailyNote";
    private static final int PICK_CAMERA_IMAGE = 2;
    private static final int PICK_GALLERY_IMAGE = 1;

    private File file;
    private File sourceFile;
    private SimpleDateFormat dateFormatter;
    private Uri imageCaptureUri;
    private Uri resultUri;
    private Uri imageUri;
    private File finalImage;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image_note);

        // make directory for temp image
        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
        db = new DatabaseHandler(getApplicationContext());
        dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Note");

        imageNoteModel = getIntent().getParcelableExtra("imageNote"); // parcelable Object

        id = String.valueOf(imageNoteModel.getId());
        photo = imageNoteModel.getPoto();
        title = imageNoteModel.getTitle();
        details = imageNoteModel.getDetails();
        date_time = imageNoteModel.getDate_time();


        // casting all views
        imageView = (ImageView)findViewById(R.id.iv_old_image);
        ivSelectGalCam = (ImageView)findViewById(R.id.iv_select_gal_cam);
        photoView = (PhotoView) findViewById(R.id.photo_view);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etDetails = (EditText)findViewById(R.id.etDetails);
        selectFrame = (FrameLayout)findViewById(R.id.selectFrame);
        btnUpdate = (Button) findViewById(R.id.btnimageNoteUpdate);
        ScrollView myScrollView = findViewById(R.id.scrollview);
        myScrollView.setVerticalScrollBarEnabled(false);


        if (photo != null){
            bp = convertToBitmap(photo);
                imageView.setImageBitmap(bp);

        }
        etTitle.setText(title);
        etDetails.setText(details);
        getSupportActionBar().setSubtitle(date_time);


        ivSelectGalCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(UpdateImageNoteActivity.this);
                View cview = layoutInflater.inflate(R.layout.select_image_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateImageNoteActivity.this);
                alertDialogBuilder.setView(cview);

                TextView tvGallery = (TextView) cview.findViewById(R.id.tvgallery);
                TextView tvCamera = (TextView) cview.findViewById(R.id.tvcamera);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Take Photo From")
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                tvGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImageFromGallery();
                        alertDialog.dismiss();
                    }
                });

                tvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureImageFromCamera();
                        alertDialog.dismiss();
                    }
                });

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });

        }

    private void updateNote() {

        //getting the current Year, Month, Day and Day of Week
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy, KK:mm aa");
        date_time = df.format(calendar.getTime());  // Date and Time

        if (etTitle.getText().toString().isEmpty()){
            etTitle.setError("Title Must Not Be Empty");

        }else if(etDetails.getText().toString().isEmpty()){
            etDetails.setError("Details Must Not Be Empty");

        }else if(photo == null){
            Toast.makeText(getApplicationContext(), "Must Be Added A Picture", Toast.LENGTH_LONG).show();

        }else {
            title = etTitle.getText().toString().trim();
            details = etDetails.getText().toString().trim();

            int update = db.updateImageNote(new ImageNoteModel(photo, title, details, date_time, Email.getEmail()), id);

            if (update > 0){
                Toast.makeText(getApplicationContext(), "Note Update", Toast.LENGTH_SHORT).show();
                if (sourceFile != null){
                    sourceFile.delete();
                }
                Intent intent = new Intent(getApplicationContext(), ImageNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
    }


    // Image take from Gallery
    private void selectImageFromGallery() {
        CheckPermission checkPermission = new CheckPermission(UpdateImageNoteActivity.this);
        if (checkPermission.checkPermission()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_GALLERY_IMAGE);
        } else {
            checkPermission.requestPermission();
        }
    }

    // Image take from camera
    private void captureImageFromCamera() {
        CheckPermission checkPermission = new CheckPermission(UpdateImageNoteActivity.this);
        if (checkPermission.checkPermission()) {
            sourceFile = new File(file, "img_"
                    + dateFormatter.format(new Date()).toString() + ".jpeg");
            imageCaptureUri = Uri.fromFile(sourceFile);

            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
            startActivityForResult(intentCamera, PICK_CAMERA_IMAGE);
        } else {
            checkPermission.requestPermission();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gallery
        if (requestCode == PICK_GALLERY_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();

            bp = decodeUri(imageUri, 400);
            photo = profileImage(bp);   // main photo byte array, this array will upload to the database
            Bitmap bitmap = convertToBitmap(photo);

            photoView.setImageBitmap(bitmap);
            photoView.setVisibility(View.VISIBLE);
            selectFrame.setVisibility(View.GONE);

        }

        // Camera
        if (requestCode == PICK_CAMERA_IMAGE && resultCode == RESULT_OK) {
            //imageUri = data.getData();
            if (imageCaptureUri == null) {
                Toast.makeText(getApplicationContext(), "Uri empty", Toast.LENGTH_LONG).show();
            } else {

                bp = decodeUri(imageCaptureUri, 400);
                photo = profileImage(bp);   // main photo byte array, this array will upload to the database
                Bitmap bitmap = convertToBitmap(photo);

                photoView.setImageBitmap(bitmap);
                photoView.setVisibility(View.VISIBLE);
                selectFrame.setVisibility(View.GONE);
            }
        }


    }



    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        return bos.toByteArray();

    }

    //Convert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //get bitmap image from byte array
    private Bitmap convertToBitmap(byte[] b){
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
