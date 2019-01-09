package com.capsulestudio.dailynote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Model.User;
import com.capsulestudio.dailynote.R;

import java.util.List;

public class PasswordActivity extends AppCompatActivity {

    TextView tvtitle;
    EditText etPass;
    Button btnSetPass, btnEnter;
    String name,email;
    private DatabaseHandler db;
    private List<User> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

//       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseHandler(getApplicationContext());

        tvtitle =(TextView) findViewById(R.id.tvpassword);
        etPass = (EditText) findViewById(R.id.etpass);
        btnSetPass = (Button) findViewById(R.id.btnSetPass);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        ScrollView myScrollView = findViewById(R.id.scrollView);
        myScrollView.setVerticalScrollBarEnabled(false);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        //etPass.setText(name+":"+email);

        getSupportActionBar().setTitle(email);

        user = db.getSingleUserInfoById(email);

        if (user.size() > 0){
            btnEnter.setVisibility(View.VISIBLE);
            btnSetPass.setVisibility(View.GONE);
            tvtitle.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(), "Have password", Toast.LENGTH_LONG).show();

            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterPassToNote();
                }
            });

        }else {
            btnSetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setApassword();
                }
            });
        }

    }

    private void enterPassToNote() {
        String enterPass = etPass.getText().toString().trim();

        if (enterPass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Your Password", Toast.LENGTH_LONG).show();
        }else {
            String mainPass = user.get(0).getPassword();
            if (mainPass.equals(enterPass)){

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Password is Incorrect", Toast.LENGTH_LONG).show();
                etPass.setText("");

            }
        }
    }

    private void setApassword() {
        String pass = etPass.getText().toString().trim();

        if (pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Must Be Set A Password !", Toast.LENGTH_LONG).show();
        }else {
            long  insert =  db.addUser(new User(email, pass));
            if (insert > 0){
                Toast.makeText(getApplicationContext(), "Password Set Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(getApplicationContext(), "Something Problem", Toast.LENGTH_LONG).show();
            }
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        Toast.makeText(getApplicationContext(), "back", Toast.LENGTH_LONG).show();
//        finish();
//        return true;
//    }
}
