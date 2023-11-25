package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoriesPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_page);

        //        BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.kategori);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),Homepage_MainLogin.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.wishlist:
                    startActivity(new Intent(getApplicationContext(),WishlistPage.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.kategori:
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.profil:
                    startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }
}