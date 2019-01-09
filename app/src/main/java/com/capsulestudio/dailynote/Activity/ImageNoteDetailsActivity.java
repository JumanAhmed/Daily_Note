package com.capsulestudio.dailynote.Activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.widget.TextView;

import com.capsulestudio.dailynote.Model.ImageNoteModel;
import com.capsulestudio.dailynote.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageNoteDetailsActivity extends AppCompatActivity {

    ImageNoteModel imageNoteModel;
    PhotoView photoView;
    TextView tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_note_details);

        photoView = (PhotoView) findViewById(R.id.photo_view);
        tvDetails = (TextView)findViewById(R.id.tvDetails);

        CardView cardView = (CardView)findViewById(R.id.card);

        imageNoteModel = getIntent().getParcelableExtra("imageNote"); // Perceable Object

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(imageNoteModel.getTitle());
        getSupportActionBar().setSubtitle(imageNoteModel.getDate_time());

        Bitmap bitmap = convertToBitmap(imageNoteModel.getPoto());
        photoView.setImageBitmap(bitmap);
        tvDetails.setText(imageNoteModel.getDetails());

//        ScrollView myScrollView = findViewById(R.id.scrollview);
//        myScrollView.setVerticalScrollBarEnabled(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String textsize = preferences.getString(getString(R.string.key_text_size), "");
        String textColor = preferences.getString(getString(R.string.key_text_color), "");
        String backGcolor = preferences.getString(getString(R.string.key_background_color), "");


        if (!textsize.isEmpty()){
            Float tSize = Float.parseFloat(textsize);
            tvDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP, tSize);
        }

        if (!textColor.isEmpty()){
            final int tColor = Integer.parseInt(textColor);
            tvDetails.setTextColor(tColor);
        }

        if (!backGcolor.isEmpty()){
            final int bColor = Integer.parseInt(backGcolor);
            cardView.setCardBackgroundColor(bColor);
        }

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
