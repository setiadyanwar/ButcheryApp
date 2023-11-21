package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioButton;


public class AllTokoActivity extends AppCompatActivity {

    RadioButton viewToko, viewProduk, viewEtalase;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_toko);

        viewToko = findViewById(R.id.view_toko);
        viewProduk = findViewById(R.id.view_produk);
        viewEtalase = findViewById(R.id.view_etalase);

        Fragment tokoFragment = new TokoFragment();
        Fragment produkFragment = new ProdukFragment();
        Fragment etalaseFragment = new EtalaseFragment();

        addFragment(tokoFragment, "TokoFragment");
        addFragment(produkFragment, "ProdukFragment");
        addFragment(etalaseFragment, "EtalaseFragment");

        currentFragment = tokoFragment;

        showFragment(tokoFragment);

        viewToko.setOnClickListener(v -> showFragment(tokoFragment));
        viewProduk.setOnClickListener(v -> showFragment(produkFragment));
        viewEtalase.setOnClickListener(v -> showFragment(etalaseFragment));
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, fragment, tag);
        }

        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    private void showFragment(Fragment fragment) {
        if (fragment != currentFragment) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
            currentFragment = fragment;
        } else {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }
}