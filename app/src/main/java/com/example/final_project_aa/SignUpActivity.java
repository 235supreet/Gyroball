package com.example.final_project_aa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText newUserName, newPass;
    Button signUp, login;
    TextView signUpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        newUserName = findViewById(R.id.newUserName);
        newPass = findViewById(R.id.newPass);
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.back_login);
        signUpStatus = findViewById(R.id.signUpStatus);
        dbHelper = new DBHelper(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPass.getText().toString().equals("") || newUserName.getText().toString().equals("")) {
                    signUpStatus.setTextColor(Color.parseColor("#ff0000"));
                    signUpStatus.setText("Enter a valid Name/Password");
                }
                else {
                    boolean res = dbHelper.insert(newUserName.getText().toString(), newPass.getText().toString());
                    if (res) {
                        signUpStatus.setTextColor(Color.parseColor("#00ff00"));
                        signUpStatus.setText("Account created successfully");
                        newUserName.setText("");
                        newPass.setText("");

                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            System.out.println("Task failed");
                        }
                    }
                    else {
                        signUpStatus.setTextColor(Color.parseColor("#ff0000"));
                        signUpStatus.setText("Account creation failed");
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
               startActivity(myIntent);
            }
        });
    }
}