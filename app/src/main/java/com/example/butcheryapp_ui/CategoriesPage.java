package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoriesPage extends AppCompatActivity {
    ImageView btn_ikan,btn_ayam,btn_bebek,btn_kambing,btn_sapi,btn_kelinci,btn_domba,btn_puyuh,btn_udang,btn_kerang,btn_cumi;
    String id_kategori = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_page);

        //BOTTOM NAVIGASI

        //back
        ImageView btnback = findViewById(R.id.arrow);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoriesPage.this, Homepage_MainLogin.class);
                startActivity(i);
            }
        });
        //CART
        ImageButton navcart = findViewById(R.id.nav_cart);

        navcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoriesPage.this,CartPage.class);
                startActivity(i);
            }
        });

        //        BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.kategori);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),HomePage_NotLogin.class));
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

        btn_ikan = findViewById(R.id.kategori_ikan);
        btn_ayam = findViewById(R.id.kategori_ayam);
        btn_bebek = findViewById(R.id.kategori_bebek);
        btn_kambing = findViewById(R.id.kategori_kambing);
        btn_sapi = findViewById(R.id.kategori_sapi);
        btn_kelinci = findViewById(R.id.kategori_kelinci);
        btn_domba = findViewById(R.id.kategori_domba);
        btn_puyuh = findViewById(R.id.kategori_puyuh);
        btn_udang = findViewById(R.id.kategori_udang);
        btn_kerang = findViewById(R.id.kategori_kerang);
        btn_cumi = findViewById(R.id.kategori_cumi);

        btn_ikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-ikan";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_ayam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-ayam";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_bebek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-bebek";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_kambing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-kambing";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_sapi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-sapi";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_kelinci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-kelinci";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_domba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-domba";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_puyuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-puyuh";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_udang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-udang";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_kerang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "kerang-dan-tiram";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
        btn_cumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this,SearchPage.class);
                id_kategori = "daging-cumi";
                intent.putExtra("id_kategori",id_kategori);
                startActivity(intent);
            }
        });
    }
}