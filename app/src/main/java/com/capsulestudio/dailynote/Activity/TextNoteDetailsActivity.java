package com.capsulestudio.dailynote.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.widget.TextView;

import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.R;

public class TextNoteDetailsActivity extends AppCompatActivity {
    TextView tvDetails;
    TextNoteModel textNoteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note_details);

        tvDetails = (TextView)findViewById(R.id.tvDetails);
        CardView cardView = (CardView)findViewById(R.id.card);
        textNoteModel = getIntent().getParcelableExtra("textNote"); // Perceable Object

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(textNoteModel.getTitle());
        getSupportActionBar().setSubtitle(textNoteModel.getDate_time());

        tvDetails.setText(textNoteModel.getDetails());


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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
