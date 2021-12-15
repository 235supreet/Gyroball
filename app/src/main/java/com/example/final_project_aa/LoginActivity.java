package com.example.final_project_aa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView loginStatus;
    EditText userName, password;
    Button login, signUp;
    String name;
    String passwordStr;
    private SharedPreference sharedPreference;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.singup_login);
        loginStatus = findViewById(R.id.loginStatus);
        dbHelper = new DBHelper(this);
        sharedPreference = new SharedPreference();
        sharedPreference.clearSharedPreference(context);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Cursor result = dbHelper.getAll();
                    String record ="";

                    if (!password.getText().toString().equals("") || !userName.getText().toString().equals("")) {
                        while (result.moveToNext()) {
                            name = result.getString(0);
                            passwordStr = result.getString(1);
                            record += "NAME: " + name + ", PASSWORD: " + password + "\n";

                            if (password.getText().toString().equals(passwordStr) && userName.getText().toString().equals(name)) {
                                loginStatus.setText("");
                                sharedPreference.save(context, userName.getText().toString());
                                //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }

                            else if (!password.getText().toString().equals(passwordStr) && !userName.getText().toString().equals(name)){
                                loginStatus.setText("Invalid credentials");
                                loginStatus.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loginStatus.setText("");
                                    }
                                }, 2000);
                            }
                        }
                    }
                    else {
                        loginStatus.setText("Enter a valid Name/Password");
                        loginStatus.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loginStatus.setText("");
                            }
                        }, 2000);
                    }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(myIntent);
            }
        });
    }
}