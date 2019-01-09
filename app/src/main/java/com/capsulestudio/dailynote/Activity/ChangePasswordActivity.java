package com.capsulestudio.dailynote.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.User;
import com.capsulestudio.dailynote.R;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etPass;
    Button btnUpdatePass;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        db = new DatabaseHandler(getApplicationContext());

        etPass = (EditText) findViewById(R.id.etpass);
        btnUpdatePass = (Button) findViewById(R.id.btnUpdatePass);

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = etPass.getText().toString().trim();

                if (newPass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Must Type a Password Before Enter", Toast.LENGTH_LONG).show();
                }else{
                        int update = db.updatePassword(new User(Email.getEmail(), newPass), Email.getEmail());

                        if (update > 0){
                            Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                            etPass.setText("");
                        }else {
                            Toast.makeText(getApplicationContext(), "Something Wrong !", Toast.LENGTH_LONG).show();

                        }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
