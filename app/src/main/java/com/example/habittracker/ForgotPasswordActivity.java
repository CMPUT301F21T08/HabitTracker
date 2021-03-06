package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/*
    This activity is used to send a reset password link to the user.
    The resource used for this: https://www.youtube.com/watch?v=w-Uv-ydX_LY
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private Button resetButton;
    private EditText emailEditText;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetButton = (Button) findViewById(R.id.forgotPassword_reset_Button);
        emailEditText = (EditText) findViewById(R.id.forgotPassword_email_EditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    /**
     * Process the KEY_RETURN signal in forgot password activity
     * When back button is pressed, return to log-in activity
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(ForgotPasswordActivity.this, LogInActivity.class);
        startActivity(intent3);
        finish();
    }

    /**
     * This function makes sure that when returning to habit event list using the arrow in the tool bar, a mode string will be passed
     * so that the return can be successful
     * This is how we customize the return arrow in the toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intentReturn = new Intent(getApplicationContext(), LogInActivity.class); // Return to the log-in page
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        // if empty
        if (email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        // if not an email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter an email address");
            emailEditText.requestFocus();
            return;
        }

        // else send the email
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "The email is sent.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "There was some error.", Toast.LENGTH_LONG).show();
                    emailEditText.requestFocus();
                }


            }
        });

    }
}

