package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisPageFinish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_page_finish);

        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btndaftarakun = findViewById(R.id.btn_masuk_regis);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisPageFinish.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        btndaftarakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisPageFinish.this,Homepage_MainLogin.class);
                startActivity(intent);
            }
        });
    }
}