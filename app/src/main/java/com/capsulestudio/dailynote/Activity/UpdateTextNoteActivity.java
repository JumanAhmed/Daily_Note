package com.capsulestudio.dailynote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateTextNoteActivity extends AppCompatActivity {

    TextNoteModel textNoteModel;

    EditText etTitle, etDetails;
    Button btnUpdate;

    // all value field
    private String id;
    private String title;
    private String details;
    private String date_time;

    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_text_note);


        db = new DatabaseHandler(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Note");

        textNoteModel = getIntent().getParcelableExtra("textNote"); // parcelable Object

        id = String.valueOf(textNoteModel.getId());
        title = textNoteModel.getTitle();
        details = textNoteModel.getDetails();
        date_time = textNoteModel.getDate_time();

        // casting all views
        etTitle = (EditText)findViewById(R.id.etTitle);
        etDetails = (EditText)findViewById(R.id.etDetails);
        btnUpdate = (Button) findViewById(R.id.btnimageNoteUpdate);


        etTitle.setText(title);
        etDetails.setText(details);
        getSupportActionBar().setSubtitle(date_time);


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

        }else {
            title = etTitle.getText().toString().trim();
            details = etDetails.getText().toString().trim();

            int update = db.updateTextNote(new TextNoteModel(title, details, date_time, Email.getEmail()), id);

            if (update > 0){
                Toast.makeText(getApplicationContext(), "Note Update", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), TextNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}