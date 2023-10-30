package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DaftarMitraDetail extends AppCompatActivity {

    Button btn_next;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_mitra_detail);
        btn_next = findViewById(R.id.button_menuju_ke_alamat);
        btn_next.setOnClickListener(
                v -> {
                    Intent i = new Intent(DaftarMitraDetail.this, DaftarMitraAlamat.class);
                    startActivity(i);
                }
        );

        btn_back = findViewById(R.id.kembali_ke_nama);
        btn_back.setOnClickListener(
                v -> {
                    Intent i = new Intent(DaftarMitraDetail.this, DaftarMitra.class);
                    startActivity(i);
                }
        );
    }
}