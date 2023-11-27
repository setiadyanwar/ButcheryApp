package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WishlistPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_page);

        //BACK
        ImageView btnback = findViewById(R.id.arrow);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WishlistPage.this, Homepage_MainLogin.class);
                startActivity(i);
            }
        });
        //CART
        ImageButton navcart = findViewById(R.id.nav_cart);

        navcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WishlistPage.this,CartPage.class);
                startActivity(i);
            }
        });

        //        BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.wishlist);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),HomePage_NotLogin.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.wishlist:
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.kategori:
                    startActivity(new Intent(getApplicationContext(),CategoriesPage.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.profil:
                    startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });
    }
}