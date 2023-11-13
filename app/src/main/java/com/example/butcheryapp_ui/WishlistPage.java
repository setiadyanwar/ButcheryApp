package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WishlistPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_page);

        //        BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.wishlist);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),Homepage_MainLogin.class));
                    finish();
                    return true;
                case R.id.wishlist:
                    return true;
                case R.id.kategori:
                    startActivity(new Intent(getApplicationContext(),CategoriesPage.class));
                    finish();
                    return true;
                case R.id.profil:
                    startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                    finish();
                    return true;
            }
            return false;
        });
    }
}