package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        //CART
        ImageButton navcart = findViewById(R.id.nav_cart);

        navcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,CartPage.class);
                startActivity(i);
            }
        });

//        BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profil);

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
                    startActivity(new Intent(getApplicationContext(),CategoriesPage.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.profil:
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(ProfilePage.this, LoginPage.class);
            startActivity(intent);
        }

        TextView btn_tokosaya = findViewById(R.id.toko_saya);
        TextView btn_keluarakun = findViewById(R.id.keluarakun);

        btn_tokosaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, HomeTokoPage.class);
                startActivity(intent);
            }
        });

        TextView btn_pesanan = findViewById(R.id.lihat_pesanansaya);

        btn_pesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,PesananSayaPage.class);
                startActivity(i);
            }
        });




        btn_keluarakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, HomePage_NotLogin.class);

                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("login");
                editor.apply();

                startActivity(intent);
            }
        });
    }
}