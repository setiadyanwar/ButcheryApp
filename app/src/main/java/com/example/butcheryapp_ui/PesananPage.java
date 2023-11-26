package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class PesananPage extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private PesananSupplierAdapter PesananSupplierAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_page);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");

        if (!isLoggedIn) {
            Intent intent = new Intent(PesananPage.this, LoginPage.class);
            startActivity(intent);
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerView = findViewById(R.id.listproduk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PesananSupplierAdapter = new PesananSupplierAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(PesananSupplierAdapter);

        getItemsPesanan();
    }

    private void getItemsPesanan() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getItemsPesanan";

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Toast.makeText(PesananPage.this, "response : " +response, Toast.LENGTH_SHORT).show();
                    List<PesananModel> pesananList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PesananModel pesanan = new PesananModel();

                        String getId = jsonObject.getString("_id");
                        String getIdUser = jsonObject.getString("id_user");
                        String getIdSupplier = jsonObject.getString("id_supplier");
                        String getIdProduk = jsonObject.getString("id_produk");
                        String getFotoProduk = jsonObject.getString("foto");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getVarian = jsonObject.getString("varian");
                        String getHarga = jsonObject.getString("harga");
                        String getQty = jsonObject.getString("qty");
                        String getAlamatPengiriman = jsonObject.getString("alamat_pengiriman");
                        String getOpsiPengiriman = jsonObject.getString("opsi_pengiriman");
                        String getBiayaOngkir = jsonObject.getString("biaya_ongkir");
                        String getBiayaLayanan = jsonObject.getString("biaya_layanan");
                        String getBiayaAsuransi = jsonObject.getString("biaya_asuransi");
                        String getBiayaTambahan = jsonObject.getString("biaya_tambahan");
                        String getSubtotal = jsonObject.getString("subtotal");
                        String getTotalHarga = jsonObject.getString("total_harga");
                        String getNote = jsonObject.getString("note");
                        String getStatus = jsonObject.getString("status");


                        pesanan.setId(getId);
                        pesanan.setId_user(getIdUser);
                        pesanan.setId_supplier(getIdSupplier);
                        pesanan.setId_produk(getIdProduk);
                        pesanan.setFoto(getFotoProduk);
                        pesanan.setNama_produk(getNamaProduk);
                        pesanan.setVarian(getVarian);
                        pesanan.setHarga(getHarga);
                        pesanan.setQty(getQty);
                        pesanan.setAlamat_pengiriman(getAlamatPengiriman);
                        pesanan.setOpsi_pengiriman(getOpsiPengiriman);
                        pesanan.setBiaya_ongkir(getBiayaOngkir);
                        pesanan.setBiaya_layanan(getBiayaLayanan);
                        pesanan.setBiaya_asuransi(getBiayaAsuransi);
                        pesanan.setBiaya_tambahan(getBiayaTambahan);
                        pesanan.setSubtotal(getSubtotal);
                        pesanan.setTotal_harga(getTotalHarga);
                        pesanan.setNote(getNote);
                        pesanan.setStatus(getStatus);

                        pesananList.add(pesanan);
                    }

                    PesananSupplierAdapter.setPesananList(pesananList);
                    PesananSupplierAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PesananPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PesananPage.this, "Error: Gagal mengambil data pesanan", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}