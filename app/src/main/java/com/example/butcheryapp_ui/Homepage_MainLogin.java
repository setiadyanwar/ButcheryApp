package com.example.butcheryapp_ui;

import static com.android.volley.VolleyLog.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Homepage_MainLogin extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_main_login);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");

        if (!isLoggedIn) {
            Intent intent = new Intent(Homepage_MainLogin.this, LoginPage.class);
            startActivity(intent);
            finish();
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
                System.exit(1);
                finish();
            }
         });


        recyclerView = findViewById(R.id.cardproduk);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        produkAdapter = new ProdukAdapter(new ArrayList<>());
        recyclerView.setAdapter(produkAdapter);

        ImageView searchklik = findViewById(R.id.iconsearch);
        EditText keyword = findViewById(R.id.search_container);
        ImageButton sub_kategori = findViewById(R.id.premium);

        sub_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage_MainLogin.this,SearchPage.class);
                i.putExtra("id_kategori","daging-sapi");
                startActivity(i);
            }
        });
        searchklik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage_MainLogin.this,SearchPage.class);
                i.putExtra("search",keyword.getText().toString());
                startActivity(i);
            }
        });

//        SLIDER
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner2,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner3,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner4,null, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

//BottomNavigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        ImageButton btncart = findViewById(R.id.nav_cart);
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage_MainLogin.this,CartPage.class);
                startActivity(i);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(Homepage_MainLogin.this, Homepage_MainLogin.class));
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
                    startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        getDataAllProduk();
        getKonsumenByID(id_user);
    }

    private void getDataAllProduk() {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getAllRekomendasiProduk";

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<ProdukModel> produkList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProdukModel produk = new ProdukModel();

                        String getSupplierId = jsonObject.getString("supplier_id");
                        String getNamaToko = jsonObject.getString("nama_toko");
                        JSONObject getAlamatToko = jsonObject.getJSONObject("alamat_toko");
                        JSONObject getFotoProduk = jsonObject.getJSONObject("foto");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getIdKategori = jsonObject.getString("id_kategori");
                        String getDeskripsiProduk = jsonObject.getString("deskripsi");
                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");

                        //SET PRODUK KE DALAM PRODUK MODEL
                        produk.setSupplierID(getSupplierId);
                        produk.setId_produk(jsonObject.getString("_id"));

                        produk.setNamaProduk(getNamaProduk);

                        String getHargaProduk = getVarianProduk.getJSONObject(0).getString("harga1");
                        Currency customCurrency = Currency.getInstance("IDR");
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                        currencyFormat.setCurrency(customCurrency);
                        String formattedCurrency = currencyFormat.format(Integer.parseInt(getHargaProduk));
                        produk.setHargaProduk(formattedCurrency);

                        produk.setNamaToko(getNamaToko);

                        String getDetailedAlamatToko = getAlamatToko.getString("alamat");
                        produk.setAlamatToko(getDetailedAlamatToko);

                        produk.setGambarproduk1(getFotoProduk.getString("foto1"));
                        produk.setGambarproduk2(getFotoProduk.getString("foto2"));
                        produk.setGambarproduk3(getFotoProduk.getString("foto3"));

                        produkList.add(produk);
                    }

                    produkAdapter.setProdukList(Homepage_MainLogin.this,produkList);
                    produkAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Homepage_MainLogin.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Homepage_MainLogin.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
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
                    TextView welcome_message;
                    welcome_message = findViewById(R.id.welcome_message);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String getUsername = jsonObject.getString("username");
                        welcome_message.setText("Hi! Selamat Datang\n" + getUsername);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Homepage_MainLogin.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Homepage_MainLogin.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}