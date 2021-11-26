package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 'adhanani' and 'zwan1'
 * Allows user to login by providing their password and email as long as they have signed up before.
 * Should enter the info and then click signIn.
 * Clicking signup will take the user to the signup activity.
 */
public class LogInActivity extends AppCompatActivity {

    //setting the fields
    private Button login_signIn_button;
    private Button login_signUp_button;
    private Button login_forgotPassword_button;
    private EditText userEmail;
    private EditText passWord;
    private CheckBox rememberMeBox;
    public static boolean rememberUser = false;
    private FirebaseAuth authentication;


    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //connecting to the layout file
        login_signIn_button = findViewById(R.id.login_signIn_button);
        login_signUp_button = findViewById(R.id.login_signUp_button);
        userEmail = findViewById(R.id.login_useremail_editText);
        passWord = findViewById(R.id.login_password_editText);
        rememberMeBox = (CheckBox) findViewById(R.id.login_rememberMe);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        rememberUser = loginPreferences.getBoolean("saveLogin", false);
        if (rememberUser == true){
            userEmail.setText(loginPreferences.getString("userEmail", ""));
            passWord.setText(loginPreferences.getString("password", ""));
            rememberMeBox.setChecked(true);
        }



        login_forgotPassword_button = (Button) findViewById(R.id.login_forgot_button);
        login_forgotPassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
            }
        });



        // getting FireBase reference
        authentication =  FirebaseAuth.getInstance();

        // onClickListener to take try logging the user in.
        login_signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUserEmail = userEmail.getText().toString().trim();
                String sPassWord = passWord.getText().toString().trim();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userEmail.getWindowToken(), 0);
                if (rememberMeBox.isChecked()){
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("userEmail", sUserEmail);
                    loginPrefsEditor.putString("password", sPassWord);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                userLogin(sUserEmail, sPassWord);
            }
        });

        // onClickListener to take user to signUp activity
        login_signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Main = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(Main);
            }
        });
    }

    /**
     * This method takes the email address and password and checks with FireBase if the information
     * is correct. If successful go to main page. ELse, post a toast telling the user it failed.
     * @param userEmail: the email entered
     * @param passWord: the password entered
     */
    private void userLogin(@NonNull String userEmail,@NonNull String passWord) {
        authentication.signInWithEmailAndPassword(userEmail, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



















