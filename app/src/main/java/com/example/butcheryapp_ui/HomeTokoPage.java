package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeTokoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_toko_page);

        ImageButton btnproduktoko = findViewById(R.id.btn_produktoko);

        btnproduktoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTokoPage.this,ManageProdukToko.class);
                startActivity(intent);
            }
        });




    }
}