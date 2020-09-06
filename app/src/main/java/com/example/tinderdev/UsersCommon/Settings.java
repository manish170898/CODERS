package com.example.tinderdev.UsersCommon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tinderdev.R;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    private Button mSignout;private
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mSignout = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        mSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(Settings.this, Choose_Login_Registration.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
