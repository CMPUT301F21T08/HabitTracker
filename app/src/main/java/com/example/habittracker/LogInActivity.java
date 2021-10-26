package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button login_signIn_button = findViewById(R.id.login_signIn_button);
        Button login_signUp_button = findViewById(R.id.login_signUp_button);

        login_signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Main);
            }
        });
    }
}