package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class PesananSayaPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_saya_page);


        //menampilkan dialog rating nanti tambahin event ketika klik btn nilai
        RateDialog rateDialog = new RateDialog(PesananSayaPage.this);
        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        rateDialog.setCancelable(false);
        rateDialog.show();

    }
}