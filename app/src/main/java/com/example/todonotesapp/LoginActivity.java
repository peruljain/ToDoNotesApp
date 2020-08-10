package com.example.todonotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextUserName;
    private Button login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        setSharedPreference();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString();
                String userName = editTextUserName.getText().toString();
                if(!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(userName)) {
                    Intent intent = new Intent(LoginActivity.this, MyNotesActivity.class);
                    intent.putExtra(AppConstants.FULL_NAME, fullName);
                    intent.putExtra(AppConstants.USER_NAME, userName);
                    saveLoginStatus();
                    saveFullName(fullName);
                    startActivity(intent);
                }

            }
        });
    }

    private void saveFullName(String fullName) {
        editor =  sharedPreferences.edit();
        editor.putString(PreferenceConstants.FULL_NAME, fullName);
        editor.apply();
    }

    private void saveLoginStatus() {
        editor =  sharedPreferences.edit();
        editor.putBoolean(PreferenceConstants.IS_LOGGED_IN, true);
        editor.apply();
    }

    private void setSharedPreference() {
        sharedPreferences = getSharedPreferences(PreferenceConstants.NAME, MODE_PRIVATE);
    }

    private void bindView() {
        login = findViewById(R.id.loginButton);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUserName = findViewById(R.id.editTextUserName);
    }
}
