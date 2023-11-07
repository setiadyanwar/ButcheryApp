package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btnnextdaftar = findViewById(R.id.btn_next_registrasi);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
        btnnextdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText identifier, password, confirm_password;

                identifier = findViewById(R.id.identifier);
                password = findViewById(R.id.password);
                confirm_password = findViewById(R.id.password_confirm);

                String identifierValue = identifier.getText().toString();
                String passwordValue = password.getText().toString();
                String confirmPasswordValue = confirm_password.getText().toString();

                if(TextUtils.isEmpty(identifierValue) || TextUtils.isEmpty(passwordValue) || TextUtils.isEmpty(confirmPasswordValue)){
                    Toast.makeText(RegisterPage.this, "Isi field terlebih dahulu", Toast.LENGTH_LONG).show();
                }

                String dataIdentifier = "";

                if(isEmailValid(identifierValue)){
                    dataIdentifier = identifierValue;
                }else if(isPhoneNumber(identifierValue)){
                    dataIdentifier = identifierValue;
                }else{
                    Intent intent = new Intent(RegisterPage.this, RegisterPage.class);
                    Toast.makeText(RegisterPage.this, "Tolong masukkan email atau nomor handphone", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

                if(!passwordValue.equals(confirmPasswordValue)){
                    Intent intent = new Intent(RegisterPage.this, RegisterPage.class);
                    Toast.makeText(RegisterPage.this, "password tidak cocok", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(RegisterPage.this, RegisPageFinish.class);

                    intent.putExtra("identifier", dataIdentifier);
                    intent.putExtra("password", passwordValue);

                    startActivity(intent);
                }
            }
        });
    }
    public boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isPhoneNumber(String phoneNumber) {
        // Define a regular expression pattern for a valid phone number
        String regex = "^(\\+[0-9]+)?[0-9-]+$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the phone number against the pattern
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return true if it's a valid phone number, otherwise false
        return matcher.matches();
    }

}