package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;

import com.example.butcheryapp_ui.databinding.ActivityAllTokoBinding;

public class AllTokoActivity extends AppCompatActivity {

    ActivityAllTokoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding = ActivityAllTokoBinding.inflate(getLayoutInflater());
        RadioButton tokoOption = findViewById(R.id.view_toko);
        RadioButton produkOption = findViewById(R.id.view_produk);
        RadioButton etalaseOption = findViewById(R.id.view_etalase);
    }
}