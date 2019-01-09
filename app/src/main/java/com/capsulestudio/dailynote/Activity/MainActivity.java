package com.capsulestudio.dailynote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView tvName, tvEmail;
    String name,email,photoUrl;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.txtName);
        tvEmail = findViewById(R.id.txtEmail);


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");


        Email.setEmail(email);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        tvName.setText(name);
        tvEmail.setText(email);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out:
                signOut();
                break;

                case R.id.settings:
                startActivity(new Intent(getApplicationContext(), SettingsPrefActivity.class));
                break;

                case R.id.change_pass:
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(), "Log Out Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), GLogIn.class));
                        finish();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void textNote(View view) {
        startActivity(new Intent(getApplicationContext(), TextNoteActivity.class));

    }

    public void imageNote(View view) {

        startActivity(new Intent(getApplicationContext(), ImageNoteActivity.class));
    }
}
