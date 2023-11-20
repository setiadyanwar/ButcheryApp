package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioButton;


public class AllTokoActivity extends AppCompatActivity {

    RadioButton viewToko, viewProduk, viewEtalase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_toko);

        viewToko = findViewById(R.id.view_toko);
        viewProduk = findViewById(R.id.view_produk);
        viewEtalase = findViewById(R.id.view_etalase);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.toko_views_fragment_container, new TokoFragment(), "TokoFragment");
        fragmentTransaction.commit();

        viewToko.setOnClickListener(v -> showToko());
        viewProduk.setOnClickListener(v -> showProduk());
        viewEtalase.setOnClickListener(v -> showEtalase());
    }

    private void showToko() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment tokoFragment = fragmentManager.findFragmentByTag("TokoFragment");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (tokoFragment == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, new TokoFragment(), "TokoFragment");
            fragmentTransaction.commit();
            return;
        }

        fragmentTransaction.show(tokoFragment);
        fragmentTransaction.commit();
    }
    private void showProduk() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment produkFragment = fragmentManager.findFragmentByTag("ProdukFragment");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (produkFragment == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, new ProdukFragment(), "ProdukFragment");
            fragmentTransaction.commit();
            return;
        }

        fragmentTransaction.show(produkFragment);
        fragmentTransaction.commit();
    }
    private void showEtalase() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment etalaseFragment = fragmentManager.findFragmentByTag("EtalaseFragment");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (etalaseFragment == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, new EtalaseFragment(), "EtalaseFragment");
            fragmentTransaction.commit();
            return;
        }

        fragmentTransaction.show(etalaseFragment);
        fragmentTransaction.commit();
    }
}