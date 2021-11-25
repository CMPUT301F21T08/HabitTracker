package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author 'adhanani' and 'zwan1'
 * Allows user to signup for an account, providing a user email, name and enter password twice
 * Should enter the info and then click Creat Account.
 */
public class SignUpActivity extends AppCompatActivity {

    //setting the fields
    Button createButton;
    EditText userName;
    EditText userEmail;
    EditText passWord;
    EditText confirmPassWord;
    private FirebaseAuth authentication;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        //connecting to the layout file
        createButton = findViewById(R.id.signUp_createAccount_Button);
        userName = findViewById(R.id.signUp_userName_EditView);
        userEmail = findViewById(R.id.signUp_userEmail_EditView);
        passWord = findViewById(R.id.signUp_passWord_EditView);
        confirmPassWord = findViewById(R.id.signUp_confirm_passWord_EditView);
        authentication =  FirebaseAuth.getInstance();

        
        // onClickListener to give for different error message of different wrong inputs, create account if nothing is wrong
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUserName = userName.getText().toString().trim();
                String sUserEmail = userEmail.getText().toString().trim();
                String sPassWord = passWord.getText().toString().trim();
                String sConfirmPassword = confirmPassWord.getText().toString().trim();

                if (TextUtils.isEmpty(sUserEmail) || TextUtils.isEmpty(sUserName)  || TextUtils.isEmpty(sPassWord) || TextUtils.isEmpty(sConfirmPassword)){
                    Toast.makeText(SignUpActivity.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }
                else if (!(sConfirmPassword.equals(sPassWord))){
                    Toast.makeText(SignUpActivity.this, "Password needs to match with Confirm Password", Toast.LENGTH_SHORT).show();
                }
                else  if (sPassWord.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Password needs to be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(sUserEmail, sPassWord, sUserName);
                }
            }
        });
    }

/**
* This method takes the userEmail, password and userName to sign up for an account
* Will toast messages to the user for whether sign up was successful or not
* @param userEmail: the email entered
* @param passWord: the password entered
* @param userName: the user Name entered
*/
    private void signUp(String userEmail, String passWord, String userName) {
        authentication.createUserWithEmailAndPassword(userEmail, passWord).addOnCompleteListener(SignUpActivity.this,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Signup was successful", Toast.LENGTH_SHORT).show();

                    if (authentication.getCurrentUser() != null){
                        uid = authentication.getCurrentUser().getUid();
                        System.out.println(uid);
                    }
                    
                    // if successful, push the personal infomation of the user onto the firebase with uid as branch
                    // Push the Personal_info class directly to the firebase, with gender set to "null" and age set to 0
                    HashMap<String,Object> map = new HashMap<>();
                    Personal_info empty = new Personal_info();
                    Personal_info personal_info = new Personal_info(userName,userEmail,"null",0);
                    personal_info.setUid(uid);
                    map.put("Info",personal_info);
                    FirebaseDatabase.getInstance().getReference().child(uid).updateChildren(map);
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class ));
                    finish();
                 } else {
                    Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
