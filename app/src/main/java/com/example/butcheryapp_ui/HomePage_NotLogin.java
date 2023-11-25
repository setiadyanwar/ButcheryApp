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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class HomePage_NotLogin extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_notlogin);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");

        if (isLoggedIn) {
            Intent intent = new Intent(HomePage_NotLogin.this, Homepage_MainLogin.class);
            startActivity(intent);
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

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3,null, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

        ImageButton btnMasuk = findViewById(R.id.btn_masuk);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage_NotLogin.this, LoginPage.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(HomePage_NotLogin.this, Homepage_MainLogin.class));
                    return true;
                case R.id.wishlist:
                    startActivity(new Intent(HomePage_NotLogin.this, WishlistPage.class));
                    return true;
                case R.id.kategori:
                    startActivity(new Intent(HomePage_NotLogin.this, CategoriesPage.class));
                    return true;
                case R.id.profil:
                    startActivity(new Intent(HomePage_NotLogin.this, ProfilePage.class));
                    return true;
            }
            return false;
        });


        getDataAllProduk();
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

                        produkList.add(produk);
                    }

                    produkAdapter.setProdukList(produkList);
                    produkAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomePage_NotLogin.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(HomePage_NotLogin.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
