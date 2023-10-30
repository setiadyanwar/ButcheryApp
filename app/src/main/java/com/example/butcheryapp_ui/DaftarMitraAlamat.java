package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class DaftarMitraAlamat extends AppCompatActivity {

    Button btn_next;
    ImageButton btn_back;
    Spinner spinner_provinsi, spinner_kota, spinner_kecamatan;
    Boolean registered = true; //Kalau data sudh masuk database, registered => true
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_mitra_alamat);

        btn_next = findViewById(R.id.button_daftar_mitra);
        btn_next.setOnClickListener(
                v -> {
                    Intent i = new Intent(DaftarMitraAlamat.this, LoginPage.class);
                    if (registered) {
                        startActivity(i);
                    }
                }
        );

        btn_back = findViewById(R.id.kembali_ke_detail);
        btn_back.setOnClickListener(
                v -> {
                    Intent i = new Intent(DaftarMitraAlamat.this, DaftarMitraDetail.class);
                    startActivity(i);
                }
        );

        spinner_provinsi = findViewById(R.id.spinner_provinsi);
        ArrayAdapter<CharSequence> adapter_provinsi = ArrayAdapter.createFromResource(
                this,
                R.array.provinsi,
                android.R.layout.simple_spinner_item
        );
        adapter_provinsi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_provinsi.setAdapter(adapter_provinsi);

        spinner_kota = findViewById(R.id.spinner_kota);
        ArrayAdapter<CharSequence> adapter_kota = ArrayAdapter.createFromResource(
                this,
                R.array.kota,
                android.R.layout.simple_spinner_item
        );
        adapter_kota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_kota.setAdapter(adapter_kota);

        spinner_kecamatan = findViewById(R.id.spinner_kecamatan);
        ArrayAdapter<CharSequence> adapter_kecamatan = ArrayAdapter.createFromResource(
                this,
                R.array.kecamatan,
                android.R.layout.simple_spinner_item
        );
        adapter_kecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_kecamatan.setAdapter(adapter_kecamatan);
    }
}