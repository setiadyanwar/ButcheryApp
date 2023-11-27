package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePage extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;

    TextView nama_profile;

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

        //BOTTOM NAVIGASI
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profil);

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
        String id_user = sharedPref.getString("id_user","");
        String role = sharedPref.getString("role","");

        LinearLayout btn_daftarMitra = findViewById(R.id.btn_daftarMitra);

        if (!isLoggedIn) {
            Intent intent = new Intent(ProfilePage.this, LoginPage.class);
            startActivity(intent);
        }else{
            if(role.equals("konsumen")){
                TextView btn_tokosaya = findViewById(R.id.toko_saya);
                btn_tokosaya.setVisibility(View.GONE);
                btn_daftarMitra.setVisibility(View.VISIBLE);
                btn_daftarMitra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String redirectUrl = "http://146.190.89.250/register_mitra/" + id_user;  // Replace with your actual URL
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                        startActivity(browserIntent);
                    }
                });
            }else{
                TextView btn_tokosaya = findViewById(R.id.toko_saya);
                btn_tokosaya.setVisibility(View.VISIBLE);
                btn_daftarMitra.setVisibility(View.GONE);
            }
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

        getKonsumenByID(id_user);
    }
    private void getKonsumenByID(String id_user) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByID?id=" + id_user;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String getUsername = jsonObject.getString("username");
                        nama_profile = findViewById(R.id.nama_profile);

                        nama_profile.setText(getUsername);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfilePage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ProfilePage.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}