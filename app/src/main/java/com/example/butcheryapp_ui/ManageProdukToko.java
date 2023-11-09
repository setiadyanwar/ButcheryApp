package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ManageProdukToko extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private DaftarProdukSupplierAdapter DaftarProdukSupplierAdapter;

    String id_produk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_produk_toko);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");

        if (!isLoggedIn) {
            Intent intent = new Intent(ManageProdukToko.this, LoginPage.class);
            startActivity(intent);
        }

        recyclerView = findViewById(R.id.listproduk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DaftarProdukSupplierAdapter = new DaftarProdukSupplierAdapter(new ArrayList<>());
        recyclerView.setAdapter(DaftarProdukSupplierAdapter);

        ImageView btnaddproduk = findViewById(R.id.addproduk);

        btnaddproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProdukToko.this,AddProduk.class );
                startActivity(intent);
            }
        });

        getAllProduk();
    }
    private void getAllProduk() {
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

                        produk.setId_produk(jsonObject.getString("_id"));

                        //SET PRODUK KE DALAM PRODUK MODEL
                        produk.setNamaProduk(getNamaProduk);

                        String getvarianProduk = getVarianProduk.getJSONObject(0).getString("varian1");
                        produk.setVarianProduk(getvarianProduk);

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

                    DaftarProdukSupplierAdapter.setProdukList(produkList);
                    DaftarProdukSupplierAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageProdukToko.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ManageProdukToko.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}