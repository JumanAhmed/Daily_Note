package com.capsulestudio.dailynote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTextNoteActivity extends AppCompatActivity {

    EditText etTitle, etDetails;
    Button btnSave;
    private DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Note");

        initView(); // init view


        db = new DatabaseHandler(getApplicationContext());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteToDataBase();
            }
        });
    }

    private void saveNoteToDataBase() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy, KK:mm aa");
        String date_and_time = df.format(calendar.getTime());  // Date and Time

        if (etTitle.getText().toString().isEmpty()){
            etTitle.setError("Title Must Not Be Empty");
        }else if(etDetails.getText().toString().isEmpty()){
            etDetails.setError("Details Must Not Be Empty");
        }else {
            String title = etTitle.getText().toString().trim();
            String details = etDetails.getText().toString().trim();

            long result = db.addTextNote(new TextNoteModel(title, details, date_and_time, Email.getEmail()));

            if (result > 0){
                Toast.makeText(getApplicationContext(), "Note Save", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TextNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }

    }

    private void initView() {
        etTitle = findViewById(R.id.etTitle);
        etDetails = findViewById(R.id.etDetails);
        btnSave = findViewById(R.id.btnSave);

        ScrollView myScrollView = findViewById(R.id.scrollview);
        myScrollView.setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
